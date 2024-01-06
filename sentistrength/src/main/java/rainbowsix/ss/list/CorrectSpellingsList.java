// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   CorrectSpellingsList.java

package rainbowsix.ss.list;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import rainbowsix.ss.ClassificationOptions;
import rainbowsix.utilities.FileOps;
import rainbowsix.utilities.Sort;

// Referenced classes of package uk.ac.wlv.ss:
//            ClassificationOptions

/**
 * 正确拼写词类.
 * <br>从文件中读取并保存正确单词的词典，提供判断单词是否拼写正确的方法。
 * <br>UC-3 Spelling Correction
 *
 * @author 注释编写 徐晨 胡才轩
 */
public class CorrectSpellingsList {

    /**
     * 正确单词词典.
     */
    private String[] sgCorrectWord;
    /**
     * 正确单词个数.
     */
    private int igCorrectWordCount;
    /**
     * 词典容量.
     */
    private int igCorrectWordMax;

    /**
     * 构造方法.
     */
    public CorrectSpellingsList() {
        igCorrectWordCount = 0;
        igCorrectWordMax = 0;
    }

    /**
     * 初始化正确单词的词典.
     * <br>从文件中读取正确单词词典，并根据分类器设置进行处理后保存词典。options主要关注其bgForceUTF8属性，用于更改字符编码。
     * <br>UC-3 Spelling Correction
     *
     * @param sFilename 正确单词词典的文件路径
     * @param options   分类器设置
     * @return 是否初始化成功
     */
    public boolean initialise(final String sFilename,
                              final ClassificationOptions options) {
        if (igCorrectWordMax > 0) {
            return true;
        }
        if (!options.isBgCorrectSpellingsUsingDictionary()) {
            return true;
        }
        igCorrectWordMax = FileOps.iCountLinesInTextFile(sFilename) + 2;
        sgCorrectWord = new String[igCorrectWordMax];
        igCorrectWordCount = 0;
        File f = new File(sFilename);
        if (!f.exists()) {
            System.out.println(
                    "Could not find the spellings file: " + sFilename);
            return false;
        }
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
                    igCorrectWordCount++;
                    sgCorrectWord[igCorrectWordCount] = sLine;
                }
            }
            rReader.close();
            Sort.quickSortStrings(sgCorrectWord, 1, igCorrectWordCount);
        } catch (FileNotFoundException e) {
            System.out.println(
                    "Could not find the spellings file: " + sFilename);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(
                    "Found spellings file but could not read from it: "
                            + sFilename);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断输入法单词是否拼写正确.
     * <br>通过查找正确单词表，若未找到则认为此单词不正确。
     * <br>UC-3 Spelling Correction
     *
     * @param sWord 需要判断拼写是否正确的单词
     * @return 单词拼写是否正确
     */
    public boolean correctSpelling(final String sWord) {
        return Sort.findStringPositionInSortedArray(sWord, sgCorrectWord, 1,
                igCorrectWordCount) >= 0;
    }
}
