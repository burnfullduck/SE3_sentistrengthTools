// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   IdiomList.java

package rainbowsix.ss.list;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


import rainbowsix.ss.ClassificationOptions;
import rainbowsix.utilities.FileOps;

// Referenced classes of package:
//            ClassificationOptions

/**
 * 习语列表类.
 * <br>初始化习语列表，增加习语，通过习语id获取习语.
 * <br>UC-2 Assigning Sentiment Scores for Phrases<br>
 *
 * @author 注释编写 徐晨 詹美瑛
 */
public class IdiomList {
    /**
     * 习语数组.
     */
    private String[] sgIdioms;
    /**
     * 习语强度数组.
     */
    private int[] igIdiomStrength;
    /**
     * 列表内习语数.
     */
    private int igIdiomCount;
    /**
     * 习语单词二维数组.
     */
    private String[][] sgIdiomWords;
    /**
     * 习语中的单词个数列表.
     */
    private int[] igIdiomWordCount;

    /**
     * 习语可以包含的最大单词数.
     */
    private static final int MAX_WORD_NUM = 9;

    /**
     * 无效强度.
     */
    private static final int INVALID_STRENGTH = 999;

    /**
     * 构造方法.
     */
    public IdiomList() {
        setIgIdiomCount(0);
    }

    /**
     * 初始化习语列表.
     * <br>从指定文件得到习语列表，选择是否转换为UTF8，若没有识别习语权重，打印所在行.
     * <br>UC-2 Assigning Sentiment Scores for Phrases<br>
     *
     * @param sFilename                        文件路径
     * @param options                          分类器选项
     * @param iExtraBlankArrayEntriesToInclude 需需要额外创建的空白项目的数量
     * @return 是否初始化成功
     */
    public boolean initialise(final String sFilename,
                              final ClassificationOptions options,
                              final int iExtraBlankArrayEntriesToInclude) {
        int iLinesInFile;
        int iIdiomStrength;
        if (Objects.equals(sFilename, "")) {
            return false;
        }
        File f = new File(sFilename);
        if (!f.exists()) {
            System.out.println("Could not find idiom list file: " + sFilename);
            return false;
        }
        iLinesInFile = FileOps.iCountLinesInTextFile(sFilename);
        setSgIdioms(new String[iLinesInFile + 2
                + iExtraBlankArrayEntriesToInclude]);
        setIgIdiomStrength(
                new int[iLinesInFile + 2 + iExtraBlankArrayEntriesToInclude]);
        setIgIdiomCount(0);
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
                    int iFirstTabLocation = sLine.indexOf("\t");
                    if (iFirstTabLocation >= 0) {
                        int iSecondTabLocation = sLine.indexOf("\t",
                                iFirstTabLocation + 1);
                        try {
                            if (iSecondTabLocation > 0) {
                                iIdiomStrength = Integer.parseInt(
                                        sLine.substring(iFirstTabLocation + 1,
                                                iSecondTabLocation).trim());
                            } else {
                                iIdiomStrength = Integer.parseInt(
                                        sLine.substring(iFirstTabLocation + 1)
                                                .trim());
                            }
                            if (iIdiomStrength > 0) {
                                iIdiomStrength--;
                            } else if (iIdiomStrength < 0) {
                                iIdiomStrength++;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(
                                    "Failed to identify integer weight for "
                                            + "idiom! Ignoring idiom");
                            System.out.println("Line: " + sLine);
                            iIdiomStrength = 0;
                        }
                        sLine = sLine.substring(0, iFirstTabLocation);
                        if (sLine.contains(" ")) {
                            sLine = sLine.trim();
                        }
                        if (sLine.indexOf("  ") > 0) {
                            sLine = sLine.replace("  ", " ");
                        }
                        if (sLine.indexOf("  ") > 0) {
                            sLine = sLine.replace("  ", " ");
                        }
                        if (!sLine.equals("")) {
                            setIgIdiomCount(getIgIdiomCount() + 1);
                            getSgIdioms()[getIgIdiomCount()] = sLine;
                            getIgIdiomStrength()[getIgIdiomCount()]
                                    = iIdiomStrength;
                        }
                    }
                }
            }
            rReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find idiom list file: " + sFilename);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(
                    "Found idiom list file but could not read from it: "
                            + sFilename);
            e.printStackTrace();
            return false;
        }
        convertIdiomStringsToWordLists();
        return true;
    }

    /**
     * 添加额外习语.
     * <br>UC-2 Assigning Sentiment Scores for Phrases
     *
     * @param sIdiom                                          添加的习语
     * @param iIdiomStrength                                  添加习语的长度
     * @param bConvertIdiomStringsToWordListsAfterAddingIdiom
     * 是否在添加习语后将其字符串转化为单词列表
     * @return 是否添加成功
     */
    public boolean addExtraIdiom(final String sIdiom, final int iIdiomStrength,
                                 final boolean bConvertIdiomStringsToWordListsAfterAddingIdiom) {
        try {
            int idiomStrength = iIdiomStrength;
            setIgIdiomCount(getIgIdiomCount() + 1);
            getSgIdioms()[getIgIdiomCount()] = sIdiom;
            if (idiomStrength > 0) {
                idiomStrength--;
            } else if (idiomStrength < 0) {
                idiomStrength++;
            }
            getIgIdiomStrength()[getIgIdiomCount()] = idiomStrength;
            if (bConvertIdiomStringsToWordListsAfterAddingIdiom) {
                convertIdiomStringsToWordLists();
            }
        } catch (Exception e) {
            System.out.println("Could not add extra idiom: " + sIdiom);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 将习语字符串转化为单词列表.
     * <br>习语过长忽略之。
     */
    public void convertIdiomStringsToWordLists() {
        setSgIdiomWords(new String[getIgIdiomCount() + 1][MAX_WORD_NUM + 1]);
        igIdiomWordCount = new int[getIgIdiomCount() + 1];
        for (int iIdiom = 1; iIdiom <= getIgIdiomCount(); iIdiom++) {
            String[] sWordList = getSgIdioms()[iIdiom].split(" ");
            if (sWordList.length >= MAX_WORD_NUM) {
                System.out.println(
                        "Ignoring idiom! Too many words in it! (>9): "
                                + getSgIdioms()[iIdiom]);
            } else {

                igIdiomWordCount[iIdiom] = sWordList.length;
                System.arraycopy(sWordList, 0, getSgIdiomWords()[iIdiom], 0,
                        sWordList.length);

            }
        }

    }

    /**
     * 获取习语中的单词个数列表.
     * @return 习语中的单词个数列表
     */
    public int[] getIdiomWordCount() {
        return igIdiomWordCount;
    }

    /**
     * 获取习语强度.
     * <br>UC-2 Assigning Sentiment Scores for Phrases<br>
     *
     * @param sPhrase 习语
     * @return 习语强度
     * @deprecated
     */
    public int getIdiomStrengthOldNotUseful(final String sPhrase) {
        String sPhrase1 = sPhrase.toLowerCase();
        for (int i = 1; i <= getIgIdiomCount(); i++) {
            if (sPhrase1.contains(getSgIdioms()[i])) {
                return getIgIdiomStrength()[i];
            }
        }
        return INVALID_STRENGTH;
    }

    /**
     * 根据习语ID返回习语.
     *
     * @param iIdiomID 习语ID（索引）
     * @return 若习语存在，则返回习语字符串；否则返回空字符串
     */
    public String getIdiom(final int iIdiomID) {
        return (iIdiomID > 0 && iIdiomID < getIgIdiomCount())
                ? getSgIdioms()[iIdiomID] : "";
    }

    /**
     * 获取习语数组.
     *
     * @return 习语数组.
     */
    public String[] getSgIdioms() {
        return sgIdioms;
    }

    /**
     * 设置习语数组.
     *
     * @param strings 习语数组.
     */
    public void setSgIdioms(final String[] strings) {
        this.sgIdioms = strings;
    }

    /**
     * 获取习语强度数组.
     *
     * @return 习语强度数组.
     */
    public int[] getIgIdiomStrength() {
        return igIdiomStrength;
    }

    /**
     * 设置习语强度数组.
     *
     * @param ints 习语强度数组.
     */
    public void setIgIdiomStrength(final int[] ints) {
        this.igIdiomStrength = ints;
    }

    /**
     * 获取列表内习语数.
     *
     * @return 列表内习语数.
     */
    public int getIgIdiomCount() {
        return igIdiomCount;
    }

    /**
     * 设置列表内习语数.
     *
     * @param i 列表内习语数.
     */
    public void setIgIdiomCount(final int i) {
        this.igIdiomCount = i;
    }

    /**
     * 获取习语单词二维数组.
     *
     * @return 习语单词二维数组.
     */
    public String[][] getSgIdiomWords() {
        return sgIdiomWords;
    }

    /**
     * 设置习语单词二维数组.
     *
     * @param strings 习语单词二维数组.
     */
    public void setSgIdiomWords(final String[][] strings) {
        this.sgIdiomWords = strings;
    }
}
