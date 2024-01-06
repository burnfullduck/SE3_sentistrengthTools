// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name: Lemmatiser.java

package rainbowsix.ss.list;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


import rainbowsix.utilities.FileOps;
import rainbowsix.utilities.Sort;

/**
 * 词根提取类.
 * <br>
 * 工具类.<br>
 * 在对象创建时,需要从文本文件中读取词根表并保存在对象中,随后提供将输入的词提取出词根并返回的功能。<br>
 *
 * @author 注释编写 徐晨 胡才轩
 */
public class Lemmatiser {
    /**
     * 单词列表.
     */
    private String[] sgWord;
    /**
     * 词根列表.
     */
    private String[] sgLemma;
    /**
     * 最后一个单词的位置.
     */
    private int igWordLast;

    /**
     * 构造方法.
     */
    public Lemmatiser() {
        igWordLast = -1;
    }

    /**
     * 初始化词根提取类.
     * <br>从文件中读入词根字典,并根据参数决定是否要转换为UTF8,并保存.如果初始化成功返回true,否则false。<br>
     *
     * @param sFileName  词根字典文件路径
     * @param bForceUTF8 是否要UTF8编码
     * @return 初始化是否成功
     */
    public boolean initialise(final String sFileName,
                              final boolean bForceUTF8) {
        int iLinesInFile;
        if (sFileName.equals("")) {
            System.out.println("No lemma file specified!");
            return false;
        }
        File f = new File(sFileName);
        if (!f.exists()) {
            System.out.println("Could not find lemma file: " + sFileName);
            return false;
        }
        iLinesInFile = FileOps.iCountLinesInTextFile(sFileName);
        if (iLinesInFile < 2) {
            System.out.println(
                    "Less than 2 lines in sentiment file: " + sFileName);
            return false;
        }
        sgWord = new String[iLinesInFile + 1];
        sgLemma = new String[iLinesInFile + 1];
        igWordLast = -1;
        try {
            BufferedReader rReader;
            if (bForceUTF8) {
                rReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(sFileName),
                                StandardCharsets.UTF_8));
            } else {
                rReader = new BufferedReader(new FileReader(sFileName));
            }
            String sLine;
            while ((sLine = rReader.readLine()) != null) {
                if (!sLine.equals("")) {
                    int iFirstTabLocation = sLine.indexOf("\t");
                    if (iFirstTabLocation >= 0) {
                        int iSecondTabLocation = sLine.indexOf("\t",
                                iFirstTabLocation + 1);
                        sgWord[++igWordLast] = sLine.substring(0,
                                iFirstTabLocation);
                        if (iSecondTabLocation > 0) {
                            sgLemma[igWordLast] = sLine.substring(
                                    iFirstTabLocation + 1, iSecondTabLocation);
                        } else {
                            sgLemma[igWordLast] = sLine.substring(
                                    iFirstTabLocation + 1);
                        }
                        if (sgWord[igWordLast].contains(" ")) {
                            sgWord[igWordLast] = sgWord[igWordLast].trim();
                        }
                        if (sgLemma[igWordLast].contains(" ")) {
                            sgLemma[igWordLast] = sgLemma[igWordLast].trim();
                        }
                    }
                }
            }
            rReader.close();
            Sort.quickSortStringsWithStrings(sgWord, sgLemma, 0, igWordLast);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find lemma file: " + sFileName);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(
                    "Found lemma file but couldn't read from it: " + sFileName);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 对单词提取词根.
     *
     * @param sWord 单词
     * @return 提取出的词根
     */
    public String lemmatise(final String sWord) {
        int iLemmaID = Sort.findStringPositionInSortedArray(sWord, sgWord, 0,
                igWordLast);
        if (iLemmaID >= 0) {
            return sgLemma[iLemmaID];
        } else {
            return sWord;
        }
    }
}
