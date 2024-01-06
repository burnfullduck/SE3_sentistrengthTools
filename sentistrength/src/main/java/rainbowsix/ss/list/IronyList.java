// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   IronyList.java

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
 * 讽刺语列表.
 * <br>初始化讽刺语列表，判断术语是否是讽刺语。
 *
 * @author 注释编写 徐晨
 */
public class IronyList {
    /**
     * 讽刺术语列表.
     */
    private String[] sgIronyTerm;
    /**
     * 列表中讽刺术语的个数.
     */
    private int igIronyTermCount;
    /**
     * 列表中讽刺术语的最大个数.
     */
    private int igIronyTermMax;

    /**
     * 构造方法.
     */
    public IronyList() {
        igIronyTermCount = 0;
        igIronyTermMax = 0;
    }

    /**
     * 术语是否是语讽刺.
     *
     * @param term 术语
     * @return 术语是否是讽刺语
     */
    public boolean termIsIronic(final String term) {
        int iIronyTermCount = Sort.findStringPositionInSortedArray(term,
                sgIronyTerm, 1, igIronyTermCount);
        return iIronyTermCount >= 0;
    }

    /**
     * 初始化讽刺语类.
     * <br>
     * 从指定文件得到讽刺语.
     *
     * @param sSourceFile 源文件路径
     * @param options     分类器选项
     * @return 是否初始化成功
     */
    public boolean initialise(final String sSourceFile,
                              final ClassificationOptions options) {
        if (igIronyTermCount > 0) {
            return true;
        }
        File f = new File(sSourceFile);
        if (!f.exists()) {
            return true;
        }
        try {
            igIronyTermMax = FileOps.iCountLinesInTextFile(sSourceFile) + 2;
            igIronyTermCount = 0;
            sgIronyTerm = new String[igIronyTermMax];
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
                    if (sData.length > 0) {
                        sgIronyTerm[++igIronyTermCount] = sData[0];
                    }
                }
            }
            rReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find IronyTerm file: " + sSourceFile);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(
                    "Found IronyTerm file but could not read from it: "
                            + sSourceFile);
            e.printStackTrace();
            return false;
        }
        Sort.quickSortStrings(sgIronyTerm, 1, igIronyTermCount);
        return true;
    }
}
