// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   EmoticonsList.java

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
 * 表情符号列表类.
 * <br>
 * 初始化表情符号列表，查找表情符号的强度。<br>
 * UC-7 Emoji Rule<br>
 *
 * @author 注释编写 徐晨 詹美瑛
 */
public class EmoticonsList {
    /**
     * 表情符字符串表.
     */
    private String[] sgEmoticon;
    /**
     * 表情符强度表.
     */
    private int[] igEmoticonStrength;
    /**
     * 表情符表中的表情符个数.
     */
    private int igEmoticonCount;
    /**
     * 表情符表中的最大表情符个数.
     */
    private int igEmoticonMax;

    /**
     * 无效的情感强度.
     */
    private static final int INVALID_STRENGTH = 999;

    /**
     * 构造函数.
     */
    public EmoticonsList() {
        igEmoticonCount = 0;
        igEmoticonMax = 0;
    }

    /**
     * 获得指定表情符号的情绪强度.
     * <br>若表情符号不存在，返回999.
     * <br>UC-7 Emoji Rule
     *
     * @param emoticon 表情符号
     * @return 情绪强度
     */
    public int getEmoticon(final String emoticon) {
        int iEmoticon = Sort.findStringPositionInSortedArray(emoticon,
                sgEmoticon, 1, igEmoticonCount);
        if (iEmoticon >= 0) {
            return igEmoticonStrength[iEmoticon];
        } else {
            return INVALID_STRENGTH;
        }
    }

    /**
     * 初始化表情符号列表类.
     * <br>
     * 从指定文件中读取表情符号，得到每一个表情符号和其对应强度。<br>
     * UC-7 Emoji Rule<br>
     *
     * @param sSourceFile 表情符文件路径
     * @param options     分类器选项
     * @return 是否初始化成功
     */
    public boolean initialise(final String sSourceFile,
                              final ClassificationOptions options) {
        if (igEmoticonCount > 0) {
            return true;
        }
        File f = new File(sSourceFile);
        if (!f.exists()) {
            System.out.println("Could not find file: " + sSourceFile);
            return false;
        }
        try {
            igEmoticonMax = FileOps.iCountLinesInTextFile(sSourceFile) + 2;
            igEmoticonCount = 0;
            sgEmoticon = new String[igEmoticonMax];
            igEmoticonStrength = new int[igEmoticonMax];
            BufferedReader rReader;
            if (options.isBgForceUTF8()) {
                rReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(sSourceFile),
                                StandardCharsets.UTF_8));
            } else {
                rReader = new BufferedReader(new FileReader(sSourceFile));
            }
            String sLine;
            while ((sLine = rReader.readLine()) != null) {
                if (!sLine.equals("")) {
                    String[] sData = sLine.split("\t");
                    if (sData.length > 1) {
                        igEmoticonCount++;
                        sgEmoticon[igEmoticonCount] = sData[0];
                        try {
                            igEmoticonStrength[igEmoticonCount]
                                    = Integer.parseInt(sData[1].trim());
                        } catch (NumberFormatException e) {
                            System.out.println(
                                    "Failed to identify integer weight for "
                                            + "emoticon! Ignoring emoticon");
                            System.out.println("Line: " + sLine);
                            igEmoticonCount--;
                        }
                    }
                }
            }
            rReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find emoticon file: " + sSourceFile);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(
                    "Found emoticon file but could not read from it: "
                            + sSourceFile);
            e.printStackTrace();

            return false;
        }
        if (igEmoticonCount > 1) {
            Sort.quickSortStringsWithInt(sgEmoticon, igEmoticonStrength, 1,
                    igEmoticonCount);
        }
        return true;
    }
}
