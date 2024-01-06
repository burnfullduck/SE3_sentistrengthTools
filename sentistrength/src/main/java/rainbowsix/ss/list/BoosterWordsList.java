// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   BoosterWordsList.java

package rainbowsix.ss.list;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import rainbowsix.ss.ClassificationOptions;
import rainbowsix.utilities.FileOps;
import rainbowsix.utilities.Sort;

// Referenced classes of package uk.ac.wlv.ss:
//            ClassificationOptions

/**
 * 增强词列表类.
 * <br>从文件中读取并保存增强词及其强度。提供新增增强词，重新排序，以及查询增强词的强度的方法。
 * <br>UC-4 Booster Word Rule
 *
 * @author 注释编写 徐晨 胡才轩 朱甲豪
 */
public class BoosterWordsList {
    /**
     * 增强词列表.
     */
    private String[] sgBoosterWords;
    /**
     * 增强词强度列表.
     */
    private int[] igBoosterWordStrength;
    /**
     * 增强词数量.
     */
    private int igBoosterWordsCount;

    /**
     * 构造方法.
     * <br>增强词数为0。
     * <br>UC-4 Booster Word Rule
     */
    public BoosterWordsList() {
        igBoosterWordsCount = 0;
    }

    /**
     * 初始化增强词列表类.
     * <br>从文件中读取增强词及其强度，可能需要进行重新编码，然后保存在对象中。根据给定的数量在列表中创造对应的空白条目以对列表进行扩展。
     * <br>UC-4 Booster Word Rule
     *
     * @param sFilename                        文件路径
     * @param options                          分类器选项
     * @param iExtraBlankArrayEntriesToInclude 需要扩展的列表项的数目
     * @return 是否初始化成功
     */
    public boolean initialise(final String sFilename,
                              final ClassificationOptions options,
                              final int iExtraBlankArrayEntriesToInclude) {
        int iLinesInFile;
        int iWordStrength;
        if (Objects.equals(sFilename, "")) {
            System.out.println("No booster words file specified");
            return false;
        }
        File f = new File(sFilename);
        if (!f.exists()) {
            System.out.println(
                    "Could not find booster words file: " + sFilename);
            return false;
        }
        iLinesInFile = FileOps.iCountLinesInTextFile(sFilename);
        if (iLinesInFile < 1) {
            System.out.println("No booster words specified");
            return false;
        }
        sgBoosterWords = new String[iLinesInFile + 1
                + iExtraBlankArrayEntriesToInclude];
        igBoosterWordStrength = new int[iLinesInFile + 1
                + iExtraBlankArrayEntriesToInclude];
        igBoosterWordsCount = 0;
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
                                iWordStrength = Integer.parseInt(
                                        sLine.substring(iFirstTabLocation + 1,
                                                iSecondTabLocation));
                            } else {
                                iWordStrength = Integer.parseInt(
                                        sLine.substring(iFirstTabLocation + 1)
                                                .trim());
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(
                                    "Failed to identify integer weight for "
                                            + "booster word! Assuming it is "
                                            + "zero");
                            System.out.println("Line: " + sLine);
                            iWordStrength = 0;
                        }
                        sLine = sLine.substring(0, iFirstTabLocation);
                        if (sLine.contains(" ")) {
                            sLine = sLine.trim();
                        }
                        if (!sLine.equals("")) {
                            igBoosterWordsCount++;
                            sgBoosterWords[igBoosterWordsCount] = sLine;
                            igBoosterWordStrength[igBoosterWordsCount]
                                    = iWordStrength;
                        }
                    }
                }
            }
            Sort.quickSortStringsWithInt(sgBoosterWords, igBoosterWordStrength,
                    1, igBoosterWordsCount);
            rReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(
                    "Could not find booster words file: " + sFilename);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(
                    "Found booster words file but could not read from it: "
                            + sFilename);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 添加额外的增强词及强度.
     * <br>给定新的增强词及其强度，将其添加到增强词列表的末尾。可以根据传入的参数决定是否要重新排序。
     * <br>UC-4 Booster Word Rule
     *
     * @param sText                           新增增强词
     * @param iWordStrength                   增强词强度
     * @param bSortBoosterListAfterAddingTerm 添加后是否重新排序增强词列表
     * @return 是否添加成功
     */
    public boolean addExtraTerm(final String sText, final int iWordStrength,
                                final boolean bSortBoosterListAfterAddingTerm) {
        try {
            igBoosterWordsCount++;
            sgBoosterWords[igBoosterWordsCount] = sText;
            igBoosterWordStrength[igBoosterWordsCount] = iWordStrength;
            if (bSortBoosterListAfterAddingTerm) {
                Sort.quickSortStringsWithInt(sgBoosterWords,
                        igBoosterWordStrength, 1, igBoosterWordsCount);
            }
        } catch (Exception e) {
            System.out.println("Could not add extra booster word: " + sText);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 使列表按增强词字符串的字典序从小到大排序.
     * <br>UC-4 Booster Word Rule
     */
    public void sortBoosterWordList() {
        Sort.quickSortStringsWithInt(sgBoosterWords, igBoosterWordStrength, 1,
                igBoosterWordsCount);
    }

    /**
     * 获取给定增强词的强度.
     * <br>若单词在列表中，则返回单词强度；否则返回0。
     * <br>UC-4 Booster Word Rule
     *
     * @param sWord 需要获取强度的增强词
     * @return 给定增强词的强度
     */
    public int getBoosterStrength(final String sWord) {
        int iWordID = Sort.findStringPositionInSortedArray(sWord.toLowerCase(),
                sgBoosterWords, 1, igBoosterWordsCount);
        return iWordID >= 0 ? igBoosterWordStrength[iWordID] : 0;
    }
}
