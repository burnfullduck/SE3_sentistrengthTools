// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   UnusedTermsClassificationIndex.java

package rainbowsix.ss;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

import rainbowsix.utilities.Trie;

import static java.lang.System.out;


/**
 * 未使用术语的分类器索引类.
 * <br>工具类.
 *
 * @author 注释编写 徐晨 胡才轩
 */
public class UnusedTermsClassificationIndex {

    /**
     * 术语列表.
     */
    private String[] sgTermList;
    /**
     * 术语数量.
     */
    private int igTermListCount;
    /**
     * 术语列表的最大值.
     */
    private int igTermListMax;
    /**
     * 术语列表左子树指针.
     */
    private int[] igTermListLessPtr;
    /**
     * 术语列表右子树指针.
     */
    private int[] igTermListMorePtr;
    /**
     * 术语的出现频率列表.
     */
    private int[] igTermListFreq;
    /**
     * 术语的出现频率临时列表.
     */
    private int[] igTermListFreqTemp;
    /**
     * 术语的积极分类差值列表.
     */
    private int[] igTermListPosClassDiff;
    /**
     * 术语添加ID用到的暂时列表.
     */
    private int[] iTermsAddedIDTemp;
    /**
     * 术语的消极分类差值列表.
     */
    private int[] igTermListNegClassDiff;
    /**
     * 术语的单个Scale分类法的差值列表.
     */
    private int[] igTermListScaleClassDiff;
    /**
     * 术语的二元分类法的差值列表.
     */
    private int[] igTermListBinaryClassDiff;
    /**
     * 术语的三元分类法的差值列表.
     */
    private int[] igTermListTrinaryClassDiff;
    /**
     * 添加了ID的术语数量的临时变量.
     */
    private int iTermsAddedIDTempCount;
    /**
     * 术语的真实积极值列表.
     */
    private int[][] igTermListPosCorrectClass;
    /**
     * 术语的真实消极值列表.
     */
    private int[][] igTermListNegCorrectClass;
    /**
     * 术语的单一指标分类法的真实值列表.
     */
    private int[][] igTermListScaleCorrectClass;
    /**
     * 术语的二元分类法的真实值列表.
     */
    private int[][] igTermListBinaryCorrectClass;
    /**
     * 术语的三元分类法的真实值列表.
     */
    private int[][] igTermListTrinaryCorrectClass;
    /**
     * 最大术语数量.
     */
    private static final int MAX_TERM_LIST_NUMBER = 50000;
    /**
     * 单一指标分类的间隔.
     */
    private static final int CLASS_GAP = 4;
    /**
     * 实际积极得分存放位置.
     */
    private static final int POSITIVE_CORRECT_INDEX = 5;
    /**
     * 实际消极得分存放位置.
     */
    private static final int NEGATIVE_CORRECT_INDEX = 5;
    /**
     * 实际单一指标分类结果存放位置.
     */
    private static final int SINGLE_SCALE_CORRECT_INDEX = 9;
    /**
     * 实际二元分类结果存放位置.
     */
    private static final int SINGLE_BINARY_CORRECT_INDEX = 2;
    /**
     * 实际三元分类结果存放位置.
     */
    private static final int SINGLE_TRINARY_CORRECT_INDEX = 3;
    /**
     * 最大得分.
     */
    private static final int MAX_SCORE = 5;
    /**
     * 非满最高积极得分.
     */
    private static final int HIGH_SCORE = 4;
    /**
     * 非满最高消极得分.
     */
    private static final int LOW_SCORE = -4;

    /**
     * 类的构造函数.
     * <br>默认最大值为50000.
     */
    public UnusedTermsClassificationIndex() {
        sgTermList = null;
        igTermListCount = 0;
        igTermListMax = MAX_TERM_LIST_NUMBER;
    }

    /**
     * main函数入口.
     *
     * @param args1 main函数实参
     */
    public static void main(final String[] args1) {
    }

    /**
     * 给术语添加新的术语索引. <br>
     * 从给定文本中提取出新术语,并给术语添加新的术语索引. <br>
     *
     * @param sTerm 需要提取的新术语.
     */
    public void addTermToNewTermIndex(final String sTerm) {
        if (sgTermList == null) {
            initialise(true, true, true, true);
        }
        if (Objects.equals(sTerm, "")) {
            return;
        }
        boolean bDontAddMoreElements = igTermListCount == igTermListMax;
        int iTermID = Trie.getTriePositionForString(sTerm, sgTermList,
                igTermListLessPtr, igTermListMorePtr, 1, igTermListCount,
                bDontAddMoreElements);
        if (iTermID > 0) {
            iTermsAddedIDTemp[++iTermsAddedIDTempCount] = iTermID;
            igTermListFreqTemp[iTermID]++;
            if (iTermID > igTermListCount) {
                igTermListCount = iTermID;
            }
        }
    }

    /**
     * 将新的索引按照积极/消极的值添加到主索引中. <br>
     * 将iTermsAddedIDTemp中的所有术语的积极/消极的正确值添加到总的术语列表积极/消极分类中,并保存实际值与估计值之间的差值. <br>
     *
     * @param iCorrectPosClass 正确的积极分类
     * @param iEstPosClass     估计的积极分类
     * @param iCorrectNegClass 正确的消极分类
     * @param iEstNegClass     估计的消极分类
     */
    public void addNewIndexToMainIndexWithPosNegValues(
            final int iCorrectPosClass, final int iEstPosClass,
            final int iCorrectNegClass, final int iEstNegClass) {
        if (iCorrectNegClass > 0 && iCorrectPosClass > 0) {
            for (int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++) {
                int iTermID = iTermsAddedIDTemp[iTerm];
                if (igTermListFreqTemp[iTermID] != 0) {
                    try {
                        igTermListNegCorrectClass[iTermID][iCorrectNegClass
                                - 1]++;
                        igTermListPosCorrectClass[iTermID][iCorrectPosClass
                                - 1]++;
                        igTermListPosClassDiff[iTermID] += iCorrectPosClass
                                - iEstPosClass;
                        igTermListNegClassDiff[iTermID] += iCorrectNegClass
                                + iEstNegClass;
                        igTermListFreq[iTermID]++;
                        iTermsAddedIDTemp[iTerm] = 0;
                    } catch (Exception e) {
                        out.println(
                                "[UnusedTermsClassificationIndex] Error "
                                        + "trying to add Pos + Neg to index. "
                                        + e.getMessage());
                    }
                }
            }

        }
        iTermsAddedIDTempCount = 0;
    }

    /**
     * 通过Scale值将新索引添加到主索引中. <br>
     * 将iTermsAddedIDTemp中的所有术语的Scale值添加到总的术语列表刻度值分类中,并保存预测和实际之间的差值. <br>
     *
     * @param iCorrectScaleClass 正确的(实际的)刻度值分类
     * @param iEstScaleClass     预测的刻度值分类
     */
    public void addNewIndexToMainIndexWithScaleValues(
            final int iCorrectScaleClass, final int iEstScaleClass) {
        for (int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++) {
            int iTermID = iTermsAddedIDTemp[iTerm];
            if (igTermListFreqTemp[iTermID] != 0) {
                try {
                    igTermListScaleCorrectClass[iTermID][iCorrectScaleClass
                            + CLASS_GAP]++;
                    igTermListScaleClassDiff[iTermID] += iCorrectScaleClass
                            - iEstScaleClass;
                    igTermListFreq[iTermID]++;
                    iTermsAddedIDTemp[iTerm] = 0;
                } catch (Exception e) {
                    out.println("Error trying to add scale values to index. "
                            + e.getMessage());
                }
            }
        }

        iTermsAddedIDTempCount = 0;
    }

    /**
     * 通过三元分类将新索引添加到主要索引中. <br>
     * 将iTermsAddedIDTemp中的所有术语的三元分类结果添加到总的术语列表三元分类中,并保存预测和实际之间的差值.
     *
     * @param iCorrectTrinaryClass 正确的三元分类
     * @param iEstTrinaryClass     预测的三元分类
     */
    public void addNewIndexToMainIndexWithTrinaryValues(
            final int iCorrectTrinaryClass, final int iEstTrinaryClass) {
        for (int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++) {
            int iTermID = iTermsAddedIDTemp[iTerm];
            if (igTermListFreqTemp[iTermID] != 0) {
                try {
                    igTermListTrinaryCorrectClass[iTermID][iCorrectTrinaryClass
                            + 1]++;
                    igTermListTrinaryClassDiff[iTermID] += iCorrectTrinaryClass
                            - iEstTrinaryClass;
                    igTermListFreq[iTermID]++;
                    iTermsAddedIDTemp[iTerm] = 0;
                } catch (Exception e) {
                    out.println("Error trying to add trinary values to index. "
                            + e.getMessage());
                }
            }
        }

        iTermsAddedIDTempCount = 0;
    }

    /**
     * 通过二元分类结果将新索引添加到主要索引当中. <br>
     * 将iTermsAddedIDTemp中的所有术语的二元分类结果添加到总的术语列表二元分类中,并保存预测和实际之间的差值.
     *
     * @param iCorrectBinaryClass 正确的二元分类
     * @param iEstBinaryClass     预测的二元分类
     */
    public void addNewIndexToMainIndexWithBinaryValues(
            final int iCorrectBinaryClass, final int iEstBinaryClass) {
        int correctBinaryClass = iCorrectBinaryClass;
        for (int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++) {
            int iTermID = iTermsAddedIDTemp[iTerm];
            if (igTermListFreqTemp[iTermID] != 0) {
                try {
                    igTermListBinaryClassDiff[iTermID] += correctBinaryClass
                            - iEstBinaryClass;
                    if (correctBinaryClass == -1) {
                        correctBinaryClass = 0;
                    }
                    igTermListBinaryCorrectClass[iTermID][correctBinaryClass]++;
                    igTermListFreq[iTermID]++;
                    iTermsAddedIDTemp[iTerm] = 0;
                } catch (Exception e) {
                    out.println("Error trying to add scale values to index. "
                            + e.getMessage());
                }
            }
        }

        iTermsAddedIDTempCount = 0;
    }

    /**
     * 未使用的术语分类索引初始化. <br>
     * 根据输入的参数分别判断并进行积极/消极分数索引,Scale方法索引,二元分类索引和三元分类索引的初始化.<br>
     *
     * @param bInitialiseScale   是否要初始化Scale方法索引
     * @param bInitialisePosNeg  是否要初始化积极/消极分数索引
     * @param bInitialiseBinary  是否要初始化二元分类索引
     * @param bInitialiseTrinary 是否要初始化三元分类索引
     */
    public void initialise(final boolean bInitialiseScale,
                           final boolean bInitialisePosNeg,
                           final boolean bInitialiseBinary,
                           final boolean bInitialiseTrinary) {
        igTermListCount = 0;
        igTermListMax = MAX_TERM_LIST_NUMBER;
        iTermsAddedIDTempCount = 0;
        sgTermList = new String[igTermListMax];
        igTermListLessPtr = new int[igTermListMax + 1];
        igTermListMorePtr = new int[igTermListMax + 1];
        igTermListFreq = new int[igTermListMax + 1];
        igTermListFreqTemp = new int[igTermListMax + 1];
        iTermsAddedIDTemp = new int[igTermListMax + 1];
        if (bInitialisePosNeg) {
            igTermListNegCorrectClass = new int[igTermListMax
                    + 1][POSITIVE_CORRECT_INDEX];
            igTermListPosCorrectClass = new int[igTermListMax
                    + 1][NEGATIVE_CORRECT_INDEX];
            igTermListNegClassDiff = new int[igTermListMax + 1];
            igTermListPosClassDiff = new int[igTermListMax + 1];
        }
        if (bInitialiseScale) {
            igTermListScaleCorrectClass = new int[igTermListMax
                    + 1][SINGLE_SCALE_CORRECT_INDEX];
            igTermListScaleClassDiff = new int[igTermListMax + 1];
        }
        if (bInitialiseBinary) {
            igTermListBinaryCorrectClass = new int[igTermListMax
                    + 1][SINGLE_BINARY_CORRECT_INDEX];
            igTermListBinaryClassDiff = new int[igTermListMax + 1];
        }
        if (bInitialiseTrinary) {
            igTermListTrinaryCorrectClass = new int[igTermListMax
                    + 1][SINGLE_TRINARY_CORRECT_INDEX];
            igTermListTrinaryClassDiff = new int[igTermListMax + 1];
        }
    }

    /**
     * 打印积极/消极分数索引. <br>
     * 根据输入的最小频率,将筛选后的术语按照积极/消极分数索引,打印到指定的输出文件中.<br>
     *
     * @param sOutputFile 输出文件路径
     * @param iMinFreq    最小的输出频率
     */
    public void printIndexWithPosNegValues(final String sOutputFile,
                                           final int iMinFreq) {
        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new FileWriter(sOutputFile));
            wWriter.write("Term\tTermFreq >= " + iMinFreq + "\t"
                    + "PosClassDiff (correct-estimate)\t" + "NegClassDiff\t"
                    + "PosClassAvDiff\t" + "NegClassAvDiff\t");
            for (int i = 1; i <= MAX_SCORE; i++) {
                wWriter.write("CorrectClass" + i + "pos\t");
            }

            for (int i = 1; i <= MAX_SCORE; i++) {
                wWriter.write("CorrectClass" + i + "neg\t");
            }

            wWriter.write("\n");
            if (igTermListCount > 0) {
                for (int iTerm = 1; iTerm <= igTermListCount; iTerm++) {
                    if (igTermListFreq[iTerm] >= iMinFreq) {
                        wWriter.write(
                                sgTermList[iTerm] + "\t" + igTermListFreq[iTerm]
                                        + "\t" + igTermListPosClassDiff[iTerm]
                                        + "\t" + igTermListNegClassDiff[iTerm]
                                        + "\t"
                                        + (float) igTermListPosClassDiff[iTerm]
                                        / (float) igTermListFreq[iTerm] + "\t"
                                        + (float) igTermListNegClassDiff[iTerm]
                                        / (float) igTermListFreq[iTerm] + "\t");
                        for (int i = 0; i < MAX_SCORE; i++) {
                            wWriter.write(
                                    igTermListPosCorrectClass[iTerm][i] + "\t");
                        }

                        for (int i = 0; i < MAX_SCORE; i++) {
                            wWriter.write(
                                    igTermListNegCorrectClass[iTerm][i] + "\t");
                        }

                        wWriter.write("\n");
                    }
                }

            } else {
                wWriter.write("No terms found in corpus!\n");
            }
            wWriter.close();
        } catch (IOException e) {
            out.println("Error printing index to " + sOutputFile);
            e.printStackTrace();
        }
    }

    /**
     * 打印Scale方法的索引. <br>
     * 根据输入的最小频率,将筛选后的术语按照Scale方法值索引,打印到指定的输出文件中.<br>
     *
     * @param sOutputFile 输出文件路径
     * @param iMinFreq    最小的输出频率
     */
    public void printIndexWithScaleValues(final String sOutputFile,
                                          final int iMinFreq) {
        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new FileWriter(sOutputFile));
            wWriter.write(
                    "Term\tTermFreq\tScaleClassDiff (correct-estimate)"
                            + "\tScaleClassAvDiff\t");
            for (int i = LOW_SCORE; i <= HIGH_SCORE; i++) {
                wWriter.write("CorrectClass" + i + "\t");
            }

            wWriter.write("\n");
            for (int iTerm = 1; iTerm <= igTermListCount; iTerm++) {
                if (igTermListFreq[iTerm] > iMinFreq) {
                    wWriter.write(
                            sgTermList[iTerm] + "\t" + igTermListFreq[iTerm]
                                    + "\t" + igTermListScaleClassDiff[iTerm]
                                    + "\t"
                                    + (float) igTermListScaleClassDiff[iTerm]
                                    / (float) igTermListFreq[iTerm] + "\t");
                    for (int i = 0; i < SINGLE_SCALE_CORRECT_INDEX; i++) {
                        wWriter.write(
                                igTermListScaleCorrectClass[iTerm][i] + "\t");
                    }

                    wWriter.write("\n");
                }
            }

            wWriter.close();
        } catch (IOException e) {
            out.println("Error printing Scale index to " + sOutputFile);
            e.printStackTrace();
        }
    }

    /**
     * 打印三元分类值索引. <br>
     * 根据输入的最小频率,将筛选后的术语按照三元分类值索引,打印到指定的输出文件中.<br>
     *
     * @param sOutputFile 输出文件路径
     * @param iMinFreq    最小的输出频率
     */
    public void printIndexWithTrinaryValues(final String sOutputFile,
                                            final int iMinFreq) {
        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new FileWriter(sOutputFile));
            wWriter.write(
                    "Term\tTermFreq\tTrinaryClassDiff (correct-estimate)"
                            + "\tTrinaryClassAvDiff\t");
            for (int i = -1; i <= 1; i++) {
                wWriter.write("CorrectClass" + i + "\t");
            }

            wWriter.write("\n");
            for (int iTerm = 1; iTerm <= igTermListCount; iTerm++) {
                if (igTermListFreq[iTerm] > iMinFreq) {
                    wWriter.write(
                            sgTermList[iTerm] + "\t" + igTermListFreq[iTerm]
                                    + "\t" + igTermListTrinaryClassDiff[iTerm]
                                    + "\t"
                                    + (float) igTermListTrinaryClassDiff[iTerm]
                                    / (float) igTermListFreq[iTerm] + "\t");
                    for (int i = 0; i < HIGH_SCORE - 1; i++) {
                        wWriter.write(
                                igTermListTrinaryCorrectClass[iTerm][i] + "\t");
                    }

                    wWriter.write("\n");
                }
            }

            wWriter.close();
        } catch (IOException e) {
            out.println("Error printing Trinary index to " + sOutputFile);
            e.printStackTrace();
        }
    }

    /**
     * 打印二元分类值索引. <br>
     * 根据输入的最小频率,将筛选后的术语按照二元分类值索引,打印到指定的输出文件中.<br>
     *
     * @param sOutputFile 输出文件路径
     * @param iMinFreq    最小的输出频率
     */
    public void printIndexWithBinaryValues(final String sOutputFile,
                                           final int iMinFreq) {
        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new FileWriter(sOutputFile));
            wWriter.write(
                    "Term\tTermFreq\tBinaryClassDiff (correct-estimate)"
                            + "\tBinaryClassAvDiff\t");
            wWriter.write("CorrectClass-1\tCorrectClass1\t");
            wWriter.write("\n");
            for (int iTerm = 1; iTerm <= igTermListCount; iTerm++) {
                if (igTermListFreq[iTerm] > iMinFreq) {
                    wWriter.write(
                            sgTermList[iTerm] + "\t" + igTermListFreq[iTerm]
                                    + "\t" + igTermListBinaryClassDiff[iTerm]
                                    + "\t"
                                    + (float) igTermListBinaryClassDiff[iTerm]
                                    / (float) igTermListFreq[iTerm] + "\t");
                    for (int i = 0; i < 2; i++) {
                        wWriter.write(
                                igTermListBinaryCorrectClass[iTerm][i] + "\t");
                    }

                    wWriter.write("\n");
                }
            }

            wWriter.close();
        } catch (IOException e) {
            out.println("Error printing Binary index to " + sOutputFile);
            e.printStackTrace();
        }
    }
}
