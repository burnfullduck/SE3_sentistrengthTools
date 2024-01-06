// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name: QuestionWords.java

package rainbowsix.ss.list;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import rainbowsix.ss.ClassificationOptions;
import rainbowsix.utilities.FileOps;
import rainbowsix.utilities.Sort;

// Referenced classes of package uk.ac.wlv.ss:
//            ClassificationOptions

/**
 * 疑问词类.
 * <br>
 * 对象被创建后，需要从文件中读取疑问词表进行初始化，以计算并保存其中的疑问词，并提供一个判断输入词语是否为疑问词的方法。<br>
 * UC-10 Negative Sentiment Ignored in Questions<br>
 *
 * @author 注释编写 徐晨
 */
public class QuestionWords {
    /**
     * 疑问词列表.
     */
    private String[] sgQuestionWord;
    /**
     * 疑问词计数.
     */
    private int igQuestionWordCount;
    /**
     * 最大的疑问词数量.
     */
    private int igQuestionWordMax;

    /**
     * 构造方法.
     */
    public QuestionWords() {
        igQuestionWordCount = 0;
        igQuestionWordMax = 0;
    }

    /**
     * 初始化疑问词类.
     * <br>
     * 传入词典的文本文件以及分类器的偏好选择，如果已经初始化直接返回true，如果没有初始化，则将文本文件里面的疑问词作为疑问词词典存在对象中,
     * 然后返回true,如果初始化错误返回false。<br>
     * UC-10 Negative Sentiment Ignored in Questions<br>
     *
     * @param sFilename 疑问词词典文件路径
     * @param options   分类器选项
     * @return 是否初始化成功
     */
    public boolean initialise(final String sFilename,
                              final ClassificationOptions options) {
        if (igQuestionWordMax > 0) {
            return true;
        }
        File f = new File(sFilename);
        if (!f.exists()) {
            System.out.println(
                    "Could not find the question word file: " + sFilename);
            return false;
        }
        igQuestionWordMax = FileOps.iCountLinesInTextFile(sFilename) + 2;
        sgQuestionWord = new String[igQuestionWordMax];
        igQuestionWordCount = 0;
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
                    igQuestionWordCount++;
                    sgQuestionWord[igQuestionWordCount] = sLine;
                }
            }
            rReader.close();
            Sort.quickSortStrings(sgQuestionWord, 1, igQuestionWordCount);
        } catch (FileNotFoundException e) {
            System.out.println(
                    "Could not find the question word file: " + sFilename);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(
                    "Found question word file but could not read from it: "
                            + sFilename);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断一个词是否在疑问词词典中.
     * <br>
     * UC-10 Negative Sentiment Ignored in Questions<br>
     *
     * @param sWord 需要判断的词
     * @return 一个词是否在疑问词词典中
     */
    public boolean questionWord(final String sWord) {
        return Sort.findStringPositionInSortedArray(sWord, sgQuestionWord, 1,
                igQuestionWordCount) >= 0;
    }
}
