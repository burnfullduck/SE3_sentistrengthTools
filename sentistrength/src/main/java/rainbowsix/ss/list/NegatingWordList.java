// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   NegatingWordList.java

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

// Referenced classes of package:
//            ClassificationOptions

/**
 * 否定词列表类.
 * <br>包含反转后续情感词的单词（包括任何前面的助词）<br>
 * UC-5 Negating Word Rule<br>
 *
 * @author 注释编写 徐晨
 */
public class NegatingWordList {

    /**
     * 否定词列表.
     */
    private String[] sgNegatingWord;
    /**
     * 列表中否定词的个数.
     */
    private int igNegatingWordCount;
    /**
     * 列表中否定词的最大个数.
     */
    private int igNegatingWordMax;

    /**
     * 构造方法.
     */
    public NegatingWordList() {
        igNegatingWordCount = 0;
        igNegatingWordMax = 0;
    }

    /**
     * 初始化否定词列表类.
     * <br>从指定文件获取否定词，得到否定词列表.
     *
     * @param sFilename 文件路径
     * @param options   分类器选项
     * @return 是否初始化成功
     */
    public boolean initialise(final String sFilename,
                              final ClassificationOptions options) {
        if (igNegatingWordMax > 0) {
            return true;
        }
        File f = new File(sFilename);
        if (!f.exists()) {
            System.out.println(
                    "Could not find the negating words file: " + sFilename);
            return false;
        }
        igNegatingWordMax = FileOps.iCountLinesInTextFile(sFilename) + 2;
        sgNegatingWord = new String[igNegatingWordMax];
        igNegatingWordCount = 0;
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
                    igNegatingWordCount++;
                    sgNegatingWord[igNegatingWordCount] = sLine;
                }
            }
            rReader.close();
            Sort.quickSortStrings(sgNegatingWord, 1, igNegatingWordCount);
        } catch (FileNotFoundException e) {
            System.out.println(
                    "Could not find negating words file: " + sFilename);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(
                    "Found negating words file but could not read from it: "
                            + sFilename);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断是否是否定词.
     * <br> UC-5 Negating Word Rule<br>
     *
     * @param sWord 单词
     * @return 是否是否定词
     */
    public boolean negatingWord(final String sWord) {
        return Sort.findStringPositionInSortedArray(sWord, sgNegatingWord, 1,
                igNegatingWordCount) >= 0;
    }
}
