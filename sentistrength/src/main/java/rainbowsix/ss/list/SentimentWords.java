// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name: SentimentWords.java

package rainbowsix.ss.list;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


import rainbowsix.ss.ClassificationOptions;
import rainbowsix.ss.Corpus;
import rainbowsix.utilities.FileOps;
import rainbowsix.utilities.Sort;

// Referenced classes of package uk.ac.wlv.ss:
//            Corpus, ClassificationOptions

/**
 * 情绪词类.
 * <br>
 * 初始化,更新,存储情绪词及其对应强度，*开头的情绪词及其强度会被单独存储。提供将情绪词列表和情绪强度单行输出，将情绪词词典保存在本地文件的方法。<br>
 * UC-1 Assigning Sentiment Scores for Words<br>
 * UC-28 Suggest new sentiment terms (from terms in misclassified texts)<br>
 *
 * @author 注释编写 徐晨 胡才轩
 */
public class SentimentWords {
    /**
     * 情绪词列表.
     */
    private String[] sgSentimentWords;
    /**
     * 情绪词强度列表1.
     */
    private int[] igSentimentWordsStrengthTake1;
    /**
     * 情绪词数量.
     */
    private int igSentimentWordsCount;
    /**
     * 以*开头的情绪词.
     */
    private String[] sgSentimentWordsWithStarAtStart;
    /**
     * 以*开头的情绪词强度.
     */
    private int[] igSentimentWordsWithStarAtStartStrengthTake1;
    /**
     * 以*开头的情绪词数量.
     */
    private int igSentimentWordsWithStarAtStartCount;
    /**
     * 以*开头以*结束的情绪词列表.
     */
    private boolean[] bgSentimentWordsWithStarAtStartHasStarAtEnd;
    /**
     * 默认的情绪强度;通常为错误.
     */
    private static final int DEFAULT_SENTIMENT_STRENGTH = 999;

    /**
     * 构造方法.
     */
    public SentimentWords() {
        igSentimentWordsCount = 0;
        igSentimentWordsWithStarAtStartCount = 0;
    }

    /**
     * 通过情绪词ID得到情绪词.
     * <br>
     * 若不存在，返回空字符串。
     *
     * @param iWordID 情绪词ID
     * @return ID对应的情绪词
     */
    public String getSentimentWord(final int iWordID) {
        if (iWordID > 0) {
            if (iWordID <= igSentimentWordsCount) {
                return sgSentimentWords[iWordID];
            }
            if (iWordID <= igSentimentWordsCount
                    + igSentimentWordsWithStarAtStartCount) {
                return sgSentimentWordsWithStarAtStart[iWordID
                        - igSentimentWordsCount];
            }
        }
        return "";
    }

    /**
     * 通过情绪词获取对应的情绪强度.
     * <br>
     * 若不存在，则返回999。<br>
     * UC-1 Assigning Sentiment Scores for Words<br>
     *
     * @param sWord 情绪词
     * @return 对应的情绪强度
     */
    public int getSentiment(final String sWord) {
        int iWordID = Sort.findStringPositionInSortedArrayWithWildcardsInArray(
                sWord.toLowerCase(), sgSentimentWords, 1,
                igSentimentWordsCount);
        if (iWordID >= 0) {
            return igSentimentWordsStrengthTake1[iWordID];
        }
        int iStarWordID = getMatchingStarAtStartRawWordID(sWord);
        if (iStarWordID >= 0) {
            return igSentimentWordsWithStarAtStartStrengthTake1[iStarWordID];
        } else {
            return DEFAULT_SENTIMENT_STRENGTH;
        }
    }

    /**
     * 给情绪词设置情绪强度.
     * <br>
     * 若情绪词带*，根据设置的情绪强度正负进行对应的强度加减。<br>
     * UC-1 Assigning Sentiment Scores for Words<br>
     *
     * @param sWord         需要设置强度的情绪词
     * @param iNewSentiment 设置的情绪强度
     * @return 是否设置成功
     */
    public boolean setSentiment(final String sWord, final int iNewSentiment) {
        String word = sWord;
        int iWordID = Sort.findStringPositionInSortedArrayWithWildcardsInArray(
                word.toLowerCase(), sgSentimentWords, 1, igSentimentWordsCount);
        if (iWordID >= 0) {
            if (iNewSentiment > 0) {
                setSentiment(iWordID, iNewSentiment - 1);
            } else {
                setSentiment(iWordID, iNewSentiment + 1);
            }
            return true;
        }
        if (word.indexOf("*") == 0) {
            word = word.substring(1);
            if (word.indexOf("*") > 0) {
                word.substring(0, word.length() - 1);
            }
        }
        if (igSentimentWordsWithStarAtStartCount > 0) {
            for (int i = 1; i <= igSentimentWordsWithStarAtStartCount; i++) {
                if (word.equals(sgSentimentWordsWithStarAtStart[i])) {
                    if (iNewSentiment > 0) {
                        setSentiment(igSentimentWordsCount + i,
                                iNewSentiment - 1);
                    } else {
                        setSentiment(igSentimentWordsCount + i,
                                iNewSentiment + 1);
                    }
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 将情绪词和对应强度输出到文件.
     * <br>
     * 将现有的情绪词和对应强度,以及*开头的情绪词和对应强度,在进行可能的编码转换后保存到输入的文件中。语料库主要关注options
     * .bgForceUTF8,以判断编码是否要转化为UTF8。<br>
     *
     * @param sFilename 输出文件路径
     * @param c         语料库
     * @return 是否保存成功
     */
    public boolean saveSentimentList(final String sFilename, final Corpus c) {
        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new FileWriter(sFilename));
            for (int i = 1; i <= igSentimentWordsCount; i++) {
                int iSentimentStrength = igSentimentWordsStrengthTake1[i];
                if (iSentimentStrength < 0) {
                    iSentimentStrength--;
                } else {
                    iSentimentStrength++;
                }
                String sOutput = sgSentimentWords[i] + "\t" + iSentimentStrength
                        + "\n";
                if (c.getOptions().isBgForceUTF8()) {
                    sOutput = new String(
                            sOutput.getBytes(StandardCharsets.UTF_8),
                            StandardCharsets.UTF_8);
                }
                wWriter.write(sOutput);
            }

            for (int i = 1; i <= igSentimentWordsWithStarAtStartCount; i++) {
                int iSentimentStrength
                        = igSentimentWordsWithStarAtStartStrengthTake1[i];
                if (iSentimentStrength < 0) {
                    iSentimentStrength--;
                } else {
                    iSentimentStrength++;
                }
                String sOutput = "*" + sgSentimentWordsWithStarAtStart[i];
                if (bgSentimentWordsWithStarAtStartHasStarAtEnd[i]) {
                    sOutput = sOutput + "*";
                }
                sOutput = sOutput + "\t" + iSentimentStrength + "\n";
                if (c.getOptions().isBgForceUTF8()) {
                    sOutput = new String(
                            sOutput.getBytes(StandardCharsets.UTF_8),
                            StandardCharsets.UTF_8);
                }
                wWriter.write(sOutput);
            }

            wWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 单行写入所有情绪词强度.
     * <br>
     * 在给定的BufferedWriter中以单行的形式输出现有的所有情绪词强度、*开头的情绪词强度。<br>
     *
     * @param wWriter 需要写入的BufferedWriter
     * @return 是否写入成功
     */
    public boolean printSentimentValuesInSingleRow(
            final BufferedWriter wWriter) {
        try {
            for (int i = 1; i <= igSentimentWordsCount; i++) {
                int iSentimentStrength = igSentimentWordsStrengthTake1[i];
                wWriter.write("\t" + iSentimentStrength);
            }

            for (int i = 1; i <= igSentimentWordsWithStarAtStartCount; i++) {
                int iSentimentStrength
                        = igSentimentWordsWithStarAtStartStrengthTake1[i];
                wWriter.write("\t" + iSentimentStrength);
            }

            wWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 单行写入所有情绪词.
     * <br>
     * 在给定的BufferedWriter中以单行的形式输出现有的所有情绪词、*开头的情绪词。<br>
     *
     * @param wWriter 需要写入的BufferedWriter
     * @return 是否写入成功
     */
    public boolean printSentimentTermsInSingleHeaderRow(
            final BufferedWriter wWriter) {
        try {
            for (int i = 1; i <= igSentimentWordsCount; i++) {
                wWriter.write("\t" + sgSentimentWords[i]);
            }

            for (int i = 1; i <= igSentimentWordsWithStarAtStartCount; i++) {
                wWriter.write("\t*" + sgSentimentWordsWithStarAtStart[i]);
                if (bgSentimentWordsWithStarAtStartHasStarAtEnd[i]) {
                    wWriter.write("*");
                }
            }

            wWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 通过情绪词ID获取对应的情绪词强度.
     * <br>
     * 输入情绪词ID,返回对应的情绪词强度；如果ID不存在, 则返回999<br>
     * UC-1 Assigning Sentiment Scores for Words<br>
     *
     * @param iWordID 需要获取强度的情绪词ID
     * @return 情绪词的强度
     */
    public int getSentiment(final int iWordID) {
        if (iWordID > 0) {
            if (iWordID <= igSentimentWordsCount) {
                return igSentimentWordsStrengthTake1[iWordID];
            } else {
                return igSentimentWordsWithStarAtStartStrengthTake1[iWordID
                        - igSentimentWordsCount];
            }
        } else {
            return DEFAULT_SENTIMENT_STRENGTH;
        }
    }

    /**
     * 设置情绪强度.
     * <br>
     * 给定情绪词ID和强度, 将该情绪词的强度设置为给定强度；根据是否带*判断。<br>
     * UC-1 Assigning Sentiment Scores for Words<br>
     *
     * @param iWordID       需要设置强度的情绪词ID
     * @param iNewSentiment 需要设置的强度
     */
    public void setSentiment(final int iWordID, final int iNewSentiment) {
        if (iWordID <= igSentimentWordsCount) {
            igSentimentWordsStrengthTake1[iWordID] = iNewSentiment;
        } else {
            igSentimentWordsWithStarAtStartStrengthTake1[iWordID
                    - igSentimentWordsCount] = iNewSentiment;
        }
    }

    /**
     * 根据情绪词获得对应的ID.
     * <br>
     * 如果不存在则返回-1。
     *
     * @param sWord 输入的情绪词
     * @return 情绪词对应的ID
     */
    public int getSentimentID(final String sWord) {
        int iWordID = Sort.findStringPositionInSortedArrayWithWildcardsInArray(
                sWord.toLowerCase(), sgSentimentWords, 1,
                igSentimentWordsCount);
        if (iWordID >= 0) {
            return iWordID;
        }
        iWordID = getMatchingStarAtStartRawWordID(sWord);
        if (iWordID >= 0) {
            return iWordID + igSentimentWordsCount;
        } else {
            return -1;
        }
    }

    /**
     * 根据*开头的情绪词,获得其原始的ID.
     * <br>
     * 无法找到则返回-1.
     *
     * @param sWord *开头的情绪词
     * @return 原始ID
     */
    private int getMatchingStarAtStartRawWordID(final String sWord) {
        int iSubStringPos;
        if (igSentimentWordsWithStarAtStartCount > 0) {
            for (int i = 1; i <= igSentimentWordsWithStarAtStartCount; i++) {
                iSubStringPos = sWord.indexOf(
                        sgSentimentWordsWithStarAtStart[i]);
                if (iSubStringPos >= 0 && (
                        bgSentimentWordsWithStarAtStartHasStarAtEnd[i]
                                || iSubStringPos
                                        + sgSentimentWordsWithStarAtStart[i].length()
                                        == sWord.length())) {
                    return i;
                }
            }

        }
        return -1;
    }

    /**
     * 获取情绪词的数量.
     *
     * @return 情绪词的数量.
     */
    public int getSentimentWordCount() {
        return igSentimentWordsCount;
    }

    /**
     * 从文本文件中读取情绪词并初始化,并添加规定数量的空白的项目.
     * <br>
     * 从给定的文件中读取情绪词及其强度,并更新情绪词计数;根据选项可能会进行重新编码;
     * 同时根据输入的参数给情绪词列表创建对应数量的空项；如果有*开头的情绪词,
     * 则执行*开头的情绪词的初始化并返回其初始化结果；分类器选项主要关注bgForceUTF8属性,以判断是否要转化为UTF8编码。<br>
     * UC-1 Assigning Sentiment Scores for Words<br>
     *
     * @param sFilename                        输入文件的文件路径
     * @param options                          分类器选项
     * @param iExtraBlankArrayEntriesToInclude 需要创建的额外空白项数目
     * @return 是否初始化成功
     */
    public boolean initialise(final String sFilename,
                              final ClassificationOptions options,
                              final int iExtraBlankArrayEntriesToInclude) {
        int iWordStrength;
        int iWordsWithStarAtStart = 0;
        if (Objects.equals(sFilename, "")) {
            System.out.println("No sentiment file specified");
            return false;
        }
        File f = new File(sFilename);
        if (!f.exists()) {
            System.out.println("Could not find sentiment file: " + sFilename);
            return false;
        }
        int iLinesInFile = FileOps.iCountLinesInTextFile(sFilename);
        if (iLinesInFile < 2) {
            System.out.println(
                    "Less than 2 lines in sentiment file: " + sFilename);
            return false;
        }
        igSentimentWordsStrengthTake1 = new int[iLinesInFile + 1
                + iExtraBlankArrayEntriesToInclude];
        sgSentimentWords = new String[iLinesInFile + 1
                + iExtraBlankArrayEntriesToInclude];
        igSentimentWordsCount = 0;
        try {
            BufferedReader rReader;
            if (options.isBgForceUTF8()) {
                rReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(sFilename),
                                StandardCharsets.UTF_8));
            } else {
                rReader = new BufferedReader(new FileReader(sFilename));
            }
            String sLine;
            while ((sLine = rReader.readLine()) != null) {
                if (!sLine.equals("")) {
                    if (sLine.indexOf("*") == 0) {
                        iWordsWithStarAtStart++;
                    } else {
                        int iFirstTabLocation = sLine.indexOf("\t");
                        if (iFirstTabLocation >= 0) {
                            int iSecondTabLocation = sLine.indexOf("\t",
                                    iFirstTabLocation + 1);
                            try {
                                if (iSecondTabLocation > 0) {
                                    iWordStrength = Integer.parseInt(
                                            sLine.substring(
                                                    iFirstTabLocation + 1,
                                                    iSecondTabLocation).trim());
                                } else {
                                    iWordStrength = Integer.parseInt(
                                            sLine.substring(
                                                            iFirstTabLocation + 1)
                                                    .trim());
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(
                                        "Failed to identify integer weight "
                                                + "for sentiment word! "
                                                + "Ignoring word\nLine: "
                                                + sLine);
                                iWordStrength = 0;
                            }
                            sLine = sLine.substring(0, iFirstTabLocation);
                            if (sLine.contains(" ")) {
                                sLine = sLine.trim();
                            }
                            if (!sLine.equals("")) {
                                sgSentimentWords[++igSentimentWordsCount]
                                        = sLine;
                                if (iWordStrength > 0) {
                                    iWordStrength--;
                                } else if (iWordStrength < 0) {
                                    iWordStrength++;
                                }
                                igSentimentWordsStrengthTake1[igSentimentWordsCount]
                                        = iWordStrength;
                            }
                        }
                    }
                }
            }
            rReader.close();
            Sort.quickSortStringsWithInt(sgSentimentWords,
                    igSentimentWordsStrengthTake1, 1, igSentimentWordsCount);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find sentiment file: " + sFilename);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(
                    "Found sentiment file but couldn't read from it: "
                            + sFilename);
            e.printStackTrace();
            return false;
        }
        if (iWordsWithStarAtStart > 0) {
            return initialiseWordsWithStarAtStart(sFilename, options,
                    iWordsWithStarAtStart, iExtraBlankArrayEntriesToInclude);
        } else {
            return true;
        }
    }

    /**
     * 从文件中读取*开头的情绪词以实现初始化,并添加规定数量的空白项.
     * <br>
     * 从给定路径中读取*开头的情绪词,并更新对应列表,同时更新*开头情绪词计数;根据选项可能会进行重新编码;
     * 按照给定的数量在列表中增加对应数目的空白项；分类器选项主要关注bgForceUTF8属性,以判断是否需要重新编码。<br>
     * UC-1 Assigning Sentiment Scores for Words<br>
     *
     * @param sFilename                        需要读取的文件路径
     * @param options                          分类器选项
     * @param iWordsWithStarAtStart            *开头的情绪词的数量
     * @param iExtraBlankArrayEntriesToInclude 额外空白项的数量
     * @return 是否初始化成功
     */
    public boolean initialiseWordsWithStarAtStart(final String sFilename,
                                                  final ClassificationOptions options,
                                                  final int iWordsWithStarAtStart,
                                                  final int iExtraBlankArrayEntriesToInclude) {
        int iWordStrength;
        File f = new File(sFilename);
        if (!f.exists()) {
            System.out.println("Could not find sentiment file: " + sFilename);
            return false;
        }
        igSentimentWordsWithStarAtStartStrengthTake1 = new int[
                iWordsWithStarAtStart + 1 + iExtraBlankArrayEntriesToInclude];
        sgSentimentWordsWithStarAtStart = new String[iWordsWithStarAtStart + 1
                + iExtraBlankArrayEntriesToInclude];
        bgSentimentWordsWithStarAtStartHasStarAtEnd = new boolean[
                iWordsWithStarAtStart + 1 + iExtraBlankArrayEntriesToInclude];
        igSentimentWordsWithStarAtStartCount = 0;
        try {
            BufferedReader rReader;
            if (options.isBgForceUTF8()) {
                rReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(sFilename),
                                StandardCharsets.UTF_8));
            } else {
                rReader = new BufferedReader(new FileReader(sFilename));
            }
            while (rReader.ready()) {
                String sLine = rReader.readLine();
                if (!Objects.equals(sLine, "") && sLine.indexOf("*") == 0) {
                    int iFirstTabLocation = sLine.indexOf("\t");
                    if (iFirstTabLocation >= 0) {
                        int iSecondTabLocation = sLine.indexOf("\t",
                                iFirstTabLocation + 1);
                        try {
                            if (iSecondTabLocation > 0) {
                                iWordStrength = Integer.parseInt(
                                        sLine.substring(iFirstTabLocation + 1,
                                                iSecondTabLocation));
                            } else {
                                iWordStrength = Integer.parseInt(
                                        sLine.substring(iFirstTabLocation + 1));
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(
                                    "Failed to identify integer weight for "
                                            + "*sentiment* word! Ignoring "
                                            + "word\nLine: " + sLine);
                            iWordStrength = 0;
                        }
                        sLine = sLine.substring(1, iFirstTabLocation);
                        if (sLine.indexOf("*") > 0) {
                            sLine = sLine.substring(0, sLine.indexOf("*"));
                            bgSentimentWordsWithStarAtStartHasStarAtEnd[++igSentimentWordsWithStarAtStartCount]
                                    = true;
                        } else {
                            bgSentimentWordsWithStarAtStartHasStarAtEnd[++igSentimentWordsWithStarAtStartCount]
                                    = false;
                        }
                        if (sLine.contains(" ")) {
                            sLine = sLine.trim();
                        }
                        if (!sLine.equals("")) {
                            sgSentimentWordsWithStarAtStart[igSentimentWordsWithStarAtStartCount]
                                    = sLine;
                            if (iWordStrength > 0) {
                                iWordStrength--;
                            } else if (iWordStrength < 0) {
                                iWordStrength++;
                            }
                            igSentimentWordsWithStarAtStartStrengthTake1[igSentimentWordsWithStarAtStartCount]
                                    = iWordStrength;
                        } else {
                            igSentimentWordsWithStarAtStartCount--;
                        }
                    }
                }
            }
            rReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find *sentiment file*: " + sFilename);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(
                    "Found *sentiment file* but couldn't read from it: "
                            + sFilename);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 增加或修改情绪词条目.
     * <br>
     * 给定一个情绪词和的强度,如果该情绪词不存在,则加入该情绪词和对应强度,若该情绪词存在,则将其强度修改为给定强度；根据输入的参数,
     * 可能会进行情绪词的重新排序。<br>
     * UC-28 Suggest new sentiment terms (from terms in misclassified texts)<br>
     *
     * @param sTerm                             需要增加或修改的情绪词
     * @param iTermStrength                     情绪词对应的情绪强度
     * @param bSortSentimentListAfterAddingTerm 是否要重新排序
     * @return 是否添加/更新成功
     */
    public boolean addOrModifySentimentTerm(final String sTerm,
                                            final int iTermStrength,
                                            final boolean bSortSentimentListAfterAddingTerm) {
        int termStrength = iTermStrength;
        int iTermPosition = getSentimentID(sTerm);
        if (iTermPosition > 0) {
            if (termStrength > 0) {
                termStrength--;
            } else if (termStrength < 0) {
                termStrength++;
            }
            igSentimentWordsStrengthTake1[iTermPosition] = termStrength;
        } else {
            try {
                sgSentimentWords[++igSentimentWordsCount] = sTerm;
                if (termStrength > 0) {
                    termStrength--;
                } else if (termStrength < 0) {
                    termStrength++;
                }
                igSentimentWordsStrengthTake1[igSentimentWordsCount]
                        = termStrength;
                if (bSortSentimentListAfterAddingTerm) {
                    Sort.quickSortStringsWithInt(sgSentimentWords,
                            igSentimentWordsStrengthTake1, 1,
                            igSentimentWordsCount);
                }
            } catch (Exception e) {
                System.out.println(
                        "Could not add extra sentiment term: " + sTerm);
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 重新按照情绪值强度排序情绪词.<br>
     */
    public void sortSentimentList() {
        Sort.quickSortStringsWithInt(sgSentimentWords,
                igSentimentWordsStrengthTake1, 1, igSentimentWordsCount);
    }
}
