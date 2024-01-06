// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   Corpus.java

package rainbowsix.ss;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


import rainbowsix.utilities.FileOps;
import rainbowsix.utilities.Sort;

// Referenced classes of package uk.ac.wlv.ss:
// ClassificationOptions, ClassificationResources,
// UnusedTermsClassificationIndex, Paragraph,
// ClassificationStatistics, SentimentWords

/**
 * 语料库类.
 * <br>UC-11 Classify a single text
 * <br>UC-12 Classify all lines of text in a file for sentiment
 * [includes accuracy evaluations]
 * <br>UC-13 Classify texts in a column within a file or folder
 * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5)
 * sentiment strength separately
 * <br>UC-22 Use trinary classification (positive-negative-neutral)
 * <br>UC-23 Use binary classification (positive-negative)
 * <br>UC-24 Use a single positive-negative scale classification
 * <br>UC-27 Optimise sentiment strengths of existing sentiment terms
 * <br>UC-29 Machine learning evaluations
 *
 * @author 注释编写 徐晨 朱甲豪
 */
public class Corpus {

    /**
     * 分类器选项.
     */
    private ClassificationOptions options;
    /**
     * 分类器资源.
     */
    private ClassificationResources resources;
    /**
     * 要处理的文本分割成的各个段落.
     */
    private Paragraph[] paragraph;
    /**
     * 段落总数.
     */
    private int igParagraphCount;
    /**
     * 积极情绪实际值.
     */
    private int[] igPosCorrect;
    /**
     * 消极情绪实际值.
     */
    private int[] igNegCorrect;
    /**
     * 三元分类实际值.
     */
    private int[] igTrinaryCorrect;
    /**
     * 单一正负比例分类实际值.
     */
    private int[] igScaleCorrect;
    /**
     * 积极情绪预测值.
     */
    private int[] igPosClass;
    /**
     * 消极情绪预测值.
     */
    private int[] igNegClass;
    /**
     * 三元分类预测值.
     */
    private int[] igTrinaryClass;
    /**
     * 单一正负比例分类预测值.
     */
    private int[] igScaleClass;
    /**
     * 语料库是否进行过分类.
     */
    private boolean bgCorpusClassified;
    /**
     * 情绪ID列表.
     */
    private int[] igSentimentIDList;
    /**
     * 情绪ID列表元素总数.
     */
    private int igSentimentIDListCount;
    /**
     * 各个段落的情绪ID元素个数.
     */
    private int[] igSentimentIDParagraphCount;
    /**
     * 是否建立了情绪ID列表.
     */
    private boolean bSentimentIDListMade;
    /**
     * 未使用术语的分类器索引类.
     */
    private UnusedTermsClassificationIndex unusedTermsClassificationIndex;
    /**
     * 段落是否是子语料库成员的boolean数组.
     */
    private boolean[] bgSupcorpusMember;
    /**
     * 子语料库成员总数.
     */
    private int igSupcorpusMemberCount;

    /**
     * scale 分类结果的上限.
     */
    private static final int MAX_SCALE = 4;
    /**
     * scale 分类结果的下限.
     */
    private static final int MIN_SCALE = -4;

    /**
     * 积极结果的上限.
     */
    private static final int MAX_POS = 5;
    /**
     * 积极结果的下限.
     */
    private static final int MIN_POS = 1;
    /**
     * 消极结果的上限.
     */
    private static final int MAX_NEG = 5;
    /**
     * 消极结果的下限.
     */
    private static final int MIN_NEG = 1;

    /**
     * 线程睡眠时间.
     */
    private static final Long SLEEP_TIME = 10L;

    /**
     * 无效的分类正确个数.
     */
    private static final int INVALID_VALUE = -1;

    /**
     * 无效的比例.
     */
    private static final double INVALID_PROPORTION = -1D;

    /**
     * 无效的相关系数.
     */
    private static final double INVALID_CORR = 9999D;

    /**
     * 无效的MPE.
     */
    private static final double INVALID_MPE = 9999D;
    /**
     * 混淆矩阵大小.
     */
    private static final int CONFUSION_SIZE = 3;

    /**
     * 无效的分类结果.
     */
    private static final int INVALID_CORRECT = 999;

    /**
     * 无效的三元分类结果.
     */
    private static final int INVALID_TRINARY = -3;

    /**
     * 无效的文件三元分类结果.
     */
    private static final int INVALID_FILE_TRINARY = -2;

    /**
     * 无效的scale分类.
     */
    private static final int INVALID_SCALE = -10;

    /**
     * 无效的scale的文件分类结果.
     */
    private static final int INVALID_FILE_SCALE = -9;

    /**
     * 计算最大相关系数的最大数据量.
     */
    private static final int MAX_CLASSIFY_FOR_CORRELATION = 20000;

    /**
     * 计算百分比.
     */
    private static final float PERCENTAGE = 100F;

    /**
     * 100.
     */
    private static final int ONE_HUNDRED = 100;

    /**
     * 十折交叉验证.
     */
    private static final int TEN_FOLD_CROSS_VALIDATION = 10;

    /**
     * 除数因子.
     */
    private static final float FACTOR = 10;

    /**
     * 权重优化因子.
     */
    private static final double OPTIMIZE_FACTOR = 0.5D;

    /**
     * 无效的十折交叉验证结果.
     */
    private static final float INVALID_TEN_CROSS = 9999999F;

    /**
     * 十折交叉行结果行数.
     */
    private static final int ROW = 28;

    /**
     * 十折交叉结果列数.
     */
    private static final int COL = 24;
    /**
     * 模式.
     */
    private static Mode mode = new NegPosMode();


    /**
     * 构造方法.
     */
    public Corpus() {
        setOptions(new ClassificationOptions());
        setResources(new ClassificationResources());
        igParagraphCount = 0;
        bgCorpusClassified = false;
        igSentimentIDListCount = 0;
        bSentimentIDListMade = false;
        unusedTermsClassificationIndex = null;
    }

    /**
     * 给语料库建立分类索引.
     * <br>根据options中的选项, 选择建立对应类型的术语分类器索引,
     * 然后将所有段落也按照对应的类型添加到索引中.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately.
     * <br>UC-22 Use trinary classification (positive-negative-neutral)
     * <br>UC-23 Use binary classification (positive-negative)
     * <br>UC-24 Use a single positive-negative scale classification
     */
    public void indexClassifiedCorpus() {
        unusedTermsClassificationIndex = new UnusedTermsClassificationIndex();

        if (getOptions().isBgScaleMode()) {
            unusedTermsClassificationIndex.initialise(true, false, false,
                    false);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithScaleValues(
                        unusedTermsClassificationIndex, igScaleCorrect[i],
                        igScaleClass[i]);
            }
        } else if (getOptions().isBgTrinaryMode()
                && getOptions().isBgBinaryVersionOfTrinaryMode()) {
            unusedTermsClassificationIndex.initialise(false, false, true,
                    false);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithBinaryValues(
                        unusedTermsClassificationIndex, igTrinaryCorrect[i],
                        igTrinaryClass[i]);
            }
        } else if (getOptions().isBgTrinaryMode()) {
            unusedTermsClassificationIndex.initialise(false, false, false,
                    true);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithTrinaryValues(
                        unusedTermsClassificationIndex, igTrinaryCorrect[i],
                        igTrinaryClass[i]);
            }
        } else {
            unusedTermsClassificationIndex.initialise(false, true, false,
                    false);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithPosNegValues(
                        unusedTermsClassificationIndex, igPosCorrect[i],
                        igPosClass[i], igNegCorrect[i], igNegClass[i]);
            }

        }
    }

    /**
     * 打印语料库未使用的术语分类索引.
     * <br>确保语料库已经进行过分类、且已经完成索引的初始化后，根据选项的不同模式
     * 分别将对应的分类索引按照一定的频率筛选后打印到指定的输出文件中.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     * <br>UC-22 Use trinary classification (positive-negative-neutral)
     * <br>UC-23 Use binary classification (positive-negative)
     * <br>UC-24 Use a single positive-negative scale classification
     *
     * @param saveFile 保存文本路径
     * @param iMinFreq 最小频率
     */
    public void printCorpusUnusedTermsClassificationIndex(final String saveFile,
                                                          final int iMinFreq) {
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        if (unusedTermsClassificationIndex == null) {
            indexClassifiedCorpus();
        }
        if (getOptions().isBgScaleMode()) {
            unusedTermsClassificationIndex.printIndexWithScaleValues(saveFile,
                    iMinFreq);
        } else if (getOptions().isBgTrinaryMode()
                && getOptions().isBgBinaryVersionOfTrinaryMode()) {
            unusedTermsClassificationIndex.printIndexWithBinaryValues(saveFile,
                    iMinFreq);
        } else if (getOptions().isBgTrinaryMode()) {
            unusedTermsClassificationIndex.printIndexWithTrinaryValues(saveFile,
                    iMinFreq);
        } else {
            unusedTermsClassificationIndex.printIndexWithPosNegValues(saveFile,
                    iMinFreq);
        }
        System.out.println("Term weights saved to " + saveFile);
    }

    /**
     * 设置子语料库.
     * <br>根据子语料库成员更改全局子语料库成员数组、子语料库成员数量.
     *
     * @param bSubcorpusMember 是否是子语料库成员的boolean数组
     */
    public void setSubcorpus(final boolean[] bSubcorpusMember) {
        igSupcorpusMemberCount = 0;
        for (int i = 0; i <= igParagraphCount; i++) {
            if (bSubcorpusMember[i]) {
                bgSupcorpusMember[i] = true;
                igSupcorpusMemberCount++;
            } else {
                bgSupcorpusMember[i] = false;
            }
        }

    }

    /**
     * 采用整个语料库而不是子语料库.
     */
    public void useWholeCorpusNotSubcorpus() {
        for (int i = 0; i <= igParagraphCount; i++) {
            bgSupcorpusMember[i] = true;
        }

        igSupcorpusMemberCount = igParagraphCount;
    }

    /**
     * 获取语料库量级.
     *
     * @return 语料库段落数
     */
    public int getCorpusSize() {
        return igParagraphCount;
    }

    /**
     * 设置单文本文件作为语料库.
     * <br>UC-11 Classify a single text
     *
     * @param sText       文本
     * @param iPosCorrect 积极实际值
     * @param iNegCorrect 消极实际值
     * @return 是否设置成功
     */
    public boolean setSingleTextAsCorpus(final String sText,
                                         final int iPosCorrect,
                                         final int iNegCorrect) {
        int negCorrect = iNegCorrect;
        if (getResources() == null || !getResources().initialise(
                getOptions())) {
            return false;
        }
        igParagraphCount = 2;
        paragraph = new Paragraph[igParagraphCount];
        igPosCorrect = new int[igParagraphCount];
        igNegCorrect = new int[igParagraphCount];
        igTrinaryCorrect = new int[igParagraphCount];
        igScaleCorrect = new int[igParagraphCount];
        bgSupcorpusMember = new boolean[igParagraphCount];
        igParagraphCount = 1;
        paragraph[igParagraphCount] = new Paragraph();
        paragraph[igParagraphCount].setParagraph(sText, getResources(),
                getOptions());
        igPosCorrect[igParagraphCount] = iPosCorrect;
        if (negCorrect < 0) {
            negCorrect *= -1;
        }
        igNegCorrect[igParagraphCount] = negCorrect;
        useWholeCorpusNotSubcorpus();
        return true;
    }

    /**
     * 设置语料库.
     * <br>根据输入文件设置语料库
     *
     * @param sInFilenameAndPath 文件名和路径
     * @return 是否设置成功
     */
    public boolean setCorpus(final String sInFilenameAndPath) {
        if (getResources() == null || !getResources().initialise(
                getOptions())) {
            return false;
        }
        igParagraphCount = FileOps.iCountLinesInTextFile(sInFilenameAndPath)
                + 1;
        if (igParagraphCount <= 2) {
            igParagraphCount = 0;
            return false;
        }
        paragraph = new Paragraph[igParagraphCount];
        igPosCorrect = new int[igParagraphCount];
        igNegCorrect = new int[igParagraphCount];
        igTrinaryCorrect = new int[igParagraphCount];
        igScaleCorrect = new int[igParagraphCount];
        bgSupcorpusMember = new boolean[igParagraphCount];
        igParagraphCount = 0;
        try {
            BufferedReader rReader = new BufferedReader(
                    new FileReader(sInFilenameAndPath));
            String sLine;
            if (rReader.ready()) {
                rReader.readLine();
            }
            while ((sLine = rReader.readLine()) != null) {
                if (!sLine.equals("")) {
                    paragraph[++igParagraphCount] = new Paragraph();
                    int iLastTabPos = sLine.lastIndexOf("\t");
                    int iFirstTabPos = sLine.indexOf("\t");
                    if (iFirstTabPos < iLastTabPos || iFirstTabPos > 0 && (
                            getOptions().isBgTrinaryMode()
                                    || getOptions().isBgScaleMode())) {
                        paragraph[igParagraphCount].setParagraph(
                                sLine.substring(iLastTabPos + 1),
                                getResources(), getOptions());
                        if (getOptions().isBgTrinaryMode()) {
                            try {
                                igTrinaryCorrect[igParagraphCount]
                                        = Integer.parseInt(
                                        sLine.substring(0, iFirstTabPos)
                                                .trim());
                            } catch (Exception e) {
                                System.out.println(
                                        "Trinary classification could not be "
                                                + "read and will be ignored!: "
                                                + sLine);
                                igTrinaryCorrect[igParagraphCount]
                                        = INVALID_CORRECT;
                            }
                            if (igTrinaryCorrect[igParagraphCount] > 1
                                    || igTrinaryCorrect[igParagraphCount]
                                    < -1) {
                                System.out.println(
                                        "Trinary classification out of bounds"
                                                + " and will be ignored!: "
                                                + sLine);
                                igParagraphCount--;
                            } else if (
                                    getOptions().isBgBinaryVersionOfTrinaryMode()
                                            &&
                                            igTrinaryCorrect[igParagraphCount]
                                                    == 0) {
                                System.out.println(
                                        "Warning, unexpected 0 in binary "
                                                + "classification!: " + sLine);
                            }
                        } else if (getOptions().isBgScaleMode()) {
                            try {
                                igScaleCorrect[igParagraphCount]
                                        = Integer.parseInt(
                                        sLine.substring(0, iFirstTabPos)
                                                .trim());
                            } catch (Exception e) {
                                System.out.println(
                                        "Scale classification could not be "
                                                + "read and will be ignored!: "
                                                + sLine);
                                igScaleCorrect[igParagraphCount]
                                        = INVALID_CORRECT;
                            }
                            if (igScaleCorrect[igParagraphCount] > MAX_SCALE
                                    || igTrinaryCorrect[igParagraphCount]
                                    < MIN_SCALE) {
                                System.out.println(
                                        "Scale classification out of bounds "
                                                + "(-4 to +4) and will be "
                                                + "ignored!: " + sLine);
                                igParagraphCount--;
                            }
                        } else {
                            try {
                                igPosCorrect[igParagraphCount]
                                        = Integer.parseInt(
                                        sLine.substring(0, iFirstTabPos)
                                                .trim());
                                igNegCorrect[igParagraphCount]
                                        = Integer.parseInt(
                                        sLine.substring(iFirstTabPos + 1,
                                                iLastTabPos).trim());
                                if (igNegCorrect[igParagraphCount] < 0) {
                                    igNegCorrect[igParagraphCount]
                                            = -igNegCorrect[igParagraphCount];
                                }
                            } catch (Exception e) {
                                System.out.println(
                                        "Positive or negative classification "
                                                + "could not be read and will"
                                                + " be ignored!: " + sLine);
                                igPosCorrect[igParagraphCount] = 0;
                            }
                            if (igPosCorrect[igParagraphCount] > MAX_POS
                                    || igPosCorrect[igParagraphCount]
                                    < MIN_POS) {
                                System.out.println(
                                        "Warning, positive classification out"
                                                + "of bounds and line will be"
                                                + "ignored!: " + sLine);
                                igParagraphCount--;
                            } else if (igNegCorrect[igParagraphCount] > MAX_NEG
                                    || igNegCorrect[igParagraphCount]
                                    < MIN_NEG) {
                                System.out.println(
                                        "Warning, negative classification out"
                                                + "of bounds (must be 1,2,3,4,"
                                                + "or 5, with or without -) and"
                                                + "line will be ignored!: "
                                                + sLine);
                                igParagraphCount--;
                            }
                        }
                    } else {
                        if (iFirstTabPos >= 0) {
                            if (getOptions().isBgTrinaryMode()) {
                                igTrinaryCorrect[igParagraphCount]
                                        = Integer.parseInt(
                                        sLine.substring(0, iFirstTabPos)
                                                .trim());
                            }
                            sLine = sLine.substring(iFirstTabPos + 1);
                        } else if (getOptions().isBgTrinaryMode()) {
                            igTrinaryCorrect[igParagraphCount] = 0;
                        }
                        paragraph[igParagraphCount].setParagraph(sLine,
                                getResources(), getOptions());
                        igPosCorrect[igParagraphCount] = 0;
                        igNegCorrect[igParagraphCount] = 0;
                    }
                }
            }
            rReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        useWholeCorpusNotSubcorpus();
        System.out.println("Number of texts in corpus: " + igParagraphCount);
        return true;
    }

    /**
     * 初始化语料库.
     *
     * @return 是否初始化成功
     */
    public boolean initialise() {
        return getResources().initialise(getOptions());
    }

    /**
     * 重新计算语料库情绪分数.
     */
    public void reCalculateCorpusSentimentScores() {
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                paragraph[i].recalculateParagraphSentimentScores();
            }
        }

        calculateCorpusSentimentScores();
    }

    /**
     * 获取语料库成员积极情绪分数.<br> 若未找到则返回0
     *
     * @param i 段落索引
     * @return 语料库成员积极情绪分数
     */
    public int getCorpusMemberPositiveSentimentScore(final int i) {
        if (i < 1 || i > igParagraphCount) {
            return 0;
        } else {
            return paragraph[i].getParagraphPositiveSentiment();
        }
    }

    /**
     * 获取语料库成员消极情绪分数.<br> 若未找到则返回0
     *
     * @param i 段落索引
     * @return 语料库成员消极情绪分数
     */
    public int getCorpusMemberNegativeSentimentScore(final int i) {
        if (i < 1 || i > igParagraphCount) {
            return 0;
        } else {
            return paragraph[i].getParagraphNegativeSentiment();
        }
    }

    /**
     * 计算语料库情绪分数.
     */
    public void calculateCorpusSentimentScores() {
        if (igParagraphCount == 0) {
            return;
        }
        if (igPosClass == null || igPosClass.length < igPosCorrect.length) {
            igPosClass = new int[igParagraphCount + 1];
            igNegClass = new int[igParagraphCount + 1];
            igTrinaryClass = new int[igParagraphCount + 1];
            igScaleClass = new int[igParagraphCount + 1];
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                igPosClass[i] = paragraph[i].getParagraphPositiveSentiment();
                igNegClass[i] = paragraph[i].getParagraphNegativeSentiment();
                if (getOptions().isBgTrinaryMode()) {
                    igTrinaryClass[i]
                            = paragraph[i].getParagraphTrinarySentiment();
                }
                if (getOptions().isBgScaleMode()) {
                    igScaleClass[i] = paragraph[i].getParagraphScaleSentiment();
                }
            }
        }

        bgCorpusClassified = true;
    }

    /**
     * 对出现情绪变化的已经分类的语料库进行重分类.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     * <br>UC-22 Use trinary classification (positive-negative-neutral)
     * <br>UC-23 Use binary classification (positive-negative)
     * <br>UC-24 Use a single positive-negative scale classification
     *
     * @param iSentimentWordID       情绪词ID
     * @param iMinParasToContainWord 容纳单词的最小段落数
     */
    public void reClassifyClassifiedCorpusForSentimentChange(
            final int iSentimentWordID, final int iMinParasToContainWord) {
        if (igParagraphCount == 0) {
            return;
        }
        if (!bSentimentIDListMade) {
            makeSentimentIDListForCompleteCorpusIgnoringSubcorpus();
        }
        int iSentimentWordIDArrayPos = Sort.findIntPositionInSortedArray(
                iSentimentWordID, igSentimentIDList, 1, igSentimentIDListCount);
        if (iSentimentWordIDArrayPos == -1
                || igSentimentIDParagraphCount[iSentimentWordIDArrayPos]
                < iMinParasToContainWord) {
            return;
        }
        igPosClass = new int[igParagraphCount + 1];
        igNegClass = new int[igParagraphCount + 1];
        if (getOptions().isBgTrinaryMode()) {
            igTrinaryClass = new int[igParagraphCount + 1];
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                paragraph[i].reClassifyClassifiedParagraphForSentimentChange(
                        iSentimentWordID);
                igPosClass[i] = paragraph[i].getParagraphPositiveSentiment();
                igNegClass[i] = paragraph[i].getParagraphNegativeSentiment();
                if (getOptions().isBgTrinaryMode()) {

                    igTrinaryClass[i]
                            = paragraph[i].getParagraphTrinarySentiment();
                }
                if (getOptions().isBgScaleMode()) {

                    igScaleClass[i] = paragraph[i].getParagraphScaleSentiment();
                }
            }
        }

        bgCorpusClassified = true;
    }

    /**
     * 打印语料库情绪分数.<br> 计算并打印整个语料库的情绪分数（包括实际值和预测值）.
     *
     * @param sOutFilenameAndPath 输出文件路径
     * @return 是否打印成功
     */
    public boolean printCorpusSentimentScores(
            final String sOutFilenameAndPath) {
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new FileWriter(sOutFilenameAndPath));
            wWriter.write("Correct+\tCorrect-\tPredict+\tPredict-\tText\n");
            for (int i = 1; i <= igParagraphCount; i++) {
                if (bgSupcorpusMember[i]) {
                    wWriter.write(
                            igPosCorrect[i] + "\t" + igNegCorrect[i] + "\t"
                                    + igPosClass[i] + "\t" + igNegClass[i]
                                    + "\t" + paragraph[i].getTaggedParagraph()
                                    + "\n");
                }
            }

            wWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取分类中积极部分的精准比例.<br> 若没有子语料库成员，返回0
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5)
     * sentiment strength separately
     *
     * @return 分类中积极部分的精准比例
     */
    public float getClassificationPositiveAccuracyProportion() {
        if (igSupcorpusMemberCount == 0) {
            return 0.0F;
        } else {
            return (float) getClassificationPositiveNumberCorrect()
                    / (float) igSupcorpusMemberCount;
        }
    }

    /**
     * 获取分类中消极部分的精准比例.<br> 若没有子语料库成员，返回0
     * <br>UC-21 Classify positive (1 to 5) and negative
     * (-1 to -5) sentiment strength separately
     *
     * @return 分类中消极部分的精准比例
     */
    public float getClassificationNegativeAccuracyProportion() {
        if (igSupcorpusMemberCount == 0) {
            return 0.0F;
        } else {
            return (float) getClassificationNegativeNumberCorrect()
                    / (float) igSupcorpusMemberCount;
        }
    }

    /**
     * 获取消极部分基线精准的比例.
     * <br>
     * 若没有段落，返回0
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5)
     * sentiment strength separately
     *
     * @return 消极部分基线精准的比例
     */
    public double getBaselineNegativeAccuracyProportion() {
        if (igParagraphCount == 0) {
            return 0.0D;
        } else {
            return ClassificationStatistics.baselineAccuracyMajorityClassProportion(
                    igNegCorrect, igParagraphCount);
        }
    }

    /**
     * 获取积极部分基线精准的比例.<br> 若没有段落，返回0
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     *
     * @return 积极部分基线精准的比例
     */
    public double getBaselinePositiveAccuracyProportion() {
        if (igParagraphCount == 0) {
            return 0.0D;
        } else {
            return ClassificationStatistics.baselineAccuracyMajorityClassProportion(
                    igPosCorrect, igParagraphCount);
        }
    }

    /**
     * 获取分类中消极部分的实际值.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     *
     * @return 分类中消极部分的实际值
     */
    public int getClassificationNegativeNumberCorrect() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iMatches = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i] && igNegCorrect[i] == -igNegClass[i]) {
                iMatches++;
            }
        }

        return iMatches;
    }

    /**
     * 获取分类中积极部分的实际值.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     *
     * @return 分类中积极部分的实际值
     */
    public int getClassificationPositiveNumberCorrect() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iMatches = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i] && igPosCorrect[i] == igPosClass[i]) {
                iMatches++;
            }
        }

        return iMatches;
    }

    /**
     * 获取分类中积极部分的平均差.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     *
     * @return 分类中积极部分的平均差
     */
    public double getClassificationPositiveMeanDifference() {
        if (igParagraphCount == 0) {
            return 0.0D;
        }
        double fTotalDiff = 0.0D;
        int iTotal = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                fTotalDiff += Math.abs(igPosCorrect[i] - igPosClass[i]);
                iTotal++;
            }
        }

        if (iTotal > 0) {
            return fTotalDiff / (double) iTotal;
        } else {
            return 0.0D;
        }
    }

    /**
     * 获取分类中积极部分的全差分.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     *
     * @return 分类中积极部分的全差分
     */
    public int getClassificationPositiveTotalDifference() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iTotalDiff = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                iTotalDiff += Math.abs(igPosCorrect[i] - igPosClass[i]);
            }
        }

        return iTotalDiff;
    }

    /**
     * 获取三元分类方法的结果实际值.
     * <br>UC-22 Use trinary classification (positive-negative-neutral)
     *
     * @return 三元分类方法的结果实际值
     */
    public int getClassificationTrinaryNumberCorrect() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iTrinaryCorrect = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]
                    && igTrinaryCorrect[i] == igTrinaryClass[i]) {
                iTrinaryCorrect++;
            }
        }

        return iTrinaryCorrect;
    }

    /**
     * 获取整个语料库分类中scale方法的相关系数.<br> 没有段落则返回0
     * <br>UC-24 Use a single positive-negative scale classification
     *
     * @return 整个语料库分类中scale方法的相关量
     */
    public float getClassificationScaleCorrelationWholeCorpus() {
        if (igParagraphCount == 0) {
            return 0.0F;
        } else {
            return (float) ClassificationStatistics.correlation(igScaleCorrect,
                    igScaleClass, igParagraphCount);
        }
    }

    /**
     * 获取分类中的scale分类方法的精确比例.<br> 若未找到子语料库成员，则返回0
     * <br>UC-24 Use a single positive-negative scale classification
     *
     * @return 分类中的scale分类方法的精确比例
     */
    public float getClassificationScaleAccuracyProportion() {
        if (igSupcorpusMemberCount == 0) {
            return 0.0F;
        } else {
            return (float) getClassificationScaleNumberCorrect()
                    / (float) igSupcorpusMemberCount;
        }
    }

    /**
     * 获取整个语料库分类中积极部分方法的相关系数.<br> 没有段落则返回0.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     *
     * @return 整个语料库分类中积极部分的相关系数
     */
    public float getClassificationPosCorrelationWholeCorpus() {
        if (igParagraphCount == 0) {
            return 0.0F;
        } else {
            return (float) ClassificationStatistics.correlationAbs(igPosCorrect,
                    igPosClass, igParagraphCount);
        }
    }

    /**
     * 获取整个语料库分类中消极部分方法的相关系数.<br> 没有段落则返回0.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     *
     * @return 整个语料库分类中消极部分的相关系数
     */
    public float getClassificationNegCorrelationWholeCorpus() {
        if (igParagraphCount == 0) {
            return 0.0F;
        } else {
            return (float) ClassificationStatistics.correlationAbs(igNegCorrect,
                    igNegClass, igParagraphCount);
        }
    }

    /**
     * 获取分类中scale方法的的实际值.<br> 没有段落则返回0.
     * <br>UC-24 Use a single positive-negative scale classification
     *
     * @return 分类中scale方法的的实际值
     */
    public int getClassificationScaleNumberCorrect() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iScaleCorrect = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i] && igScaleCorrect[i] == igScaleClass[i]) {
                iScaleCorrect++;
            }
        }

        return iScaleCorrect;
    }

    /**
     * 获取分类中消极部分的全差分.
     * <br>
     * 没有段落则返回0.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     *
     * @return 分类中消极部分的全差分.
     */
    public int getClassificationNegativeTotalDifference() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iTotalDiff = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                iTotalDiff += Math.abs(igNegCorrect[i] + igNegClass[i]);
            }
        }

        return iTotalDiff;
    }

    /**
     * 获取分类中消极部分的平均差.
     * <br>
     * 没有段落则返回0.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     *
     * @return 分类中消极部分的平均差.
     */
    public double getClassificationNegativeMeanDifference() {
        if (igParagraphCount == 0) {
            return 0.0D;
        }
        double fTotalDiff = 0.0D;
        int iTotal = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                fTotalDiff += Math.abs(igNegCorrect[i] + igNegClass[i]);
                iTotal++;
            }
        }

        if (iTotal > 0) {
            return fTotalDiff / (double) iTotal;
        } else {
            return 0.0D;
        }
    }

    /**
     * 打印分类中的结果总结.<br>
     *
     * @param sOutFilenameAndPath 输出文件路径
     * @return 是否打印成功
     * @deprecated
     */
    public boolean printClassificationResultsSummaryNOTDONE(
            final String sOutFilenameAndPath) {

        if (!bgCorpusClassified) {

            calculateCorpusSentimentScores();
        }
        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new FileWriter(sOutFilenameAndPath));
            for (int i = 1; i <= igParagraphCount; i++) {
                boolean tmp = bgSupcorpusMember[i];
            }

            wWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 为完整语料库制作情绪ID列表，忽略子语料库.
     */
    @SuppressWarnings("checkstyle:NeedBraces")
    public void makeSentimentIDListForCompleteCorpusIgnoringSubcorpus() {
        igSentimentIDListCount = 0;
        for (int i = 1; i <= igParagraphCount; i++) {
            paragraph[i].makeSentimentIDList();
            if (paragraph[i].getSentimentIDList() != null) {
                igSentimentIDListCount
                        += paragraph[i].getSentimentIDList().length;
            }
        }

        if (igSentimentIDListCount > 0) {
            igSentimentIDList = new int[igSentimentIDListCount + 1];
            igSentimentIDParagraphCount = new int[igSentimentIDListCount + 1];
            igSentimentIDListCount = 0;
            for (int i = 1; i <= igParagraphCount; i++) {
                int[] sentenceIDList = paragraph[i].getSentimentIDList();
                if (sentenceIDList != null) {
                    for (int k : sentenceIDList) {
                        if (k != 0) {
                            igSentimentIDList[++igSentimentIDListCount] = k;
                        }
                    }

                }
            }

            Sort.quickSortInt(igSentimentIDList, 1, igSentimentIDListCount);
            for (int i = 1; i <= igParagraphCount; i++) {
                int[] sentenceIDList = paragraph[i].getSentimentIDList();
                if (sentenceIDList != null) {
                    for (int k : sentenceIDList) {
                        if (k != 0) {
                            igSentimentIDParagraphCount[Sort.findIntPositionInSortedArray(
                                    k, igSentimentIDList, 1,
                                    igSentimentIDListCount)]++;
                        }
                    }

                }
            }

        }
        bSentimentIDListMade = true;
    }

    //<br>UC-29 Machine learning evaluations
    private void run10FoldCrossValidationMultipleTimes(
            final int iMinImprovement, final boolean bUseTotalDifference,
            final int iReplications, final int iMultiOptimisations,
            final BufferedWriter sWriter,
            final BufferedWriter wTermStrengthWriter) {
        for (int i = 1; i <= iReplications; i++) {
            run10FoldCrossValidationOnce(iMinImprovement, bUseTotalDifference,
                    iMultiOptimisations, sWriter, wTermStrengthWriter);
        }

        System.out.println("Set of " + iReplications
                + " 10-fold cross validations finished");
    }

    /**
     * 多次进行十折的交叉验证.
     * <br>
     * 根据输入的参数多次运行十折的交叉验证, 并将结果输出到指定文件中.
     * <br>UC-29 Machine learning evaluations
     *
     * @param iMinImprovement     最小提升
     * @param bUseTotalDifference 是否要将差异累加
     * @param iReplications       重复次数
     * @param iMultiOptimisations 权重优化的总次数
     * @param sOutFileName        输出的文件路径
     */
    public void run10FoldCrossValidationMultipleTimes(final int iMinImprovement,
                                                      final boolean bUseTotalDifference,
                                                      final int iReplications,
                                                      final int iMultiOptimisations,
                                                      final String sOutFileName) {
        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new FileWriter(sOutFileName));
            BufferedWriter wTermStrengthWriter = new BufferedWriter(
                    new FileWriter(FileOps.sChopFileNameExtension(sOutFileName)
                            + "_termStrVars.txt"));
            getOptions().printClassificationOptionsHeadings(wWriter);
            writeClassificationStatsHeadings(wWriter);
            getOptions().printClassificationOptionsHeadings(
                    wTermStrengthWriter);
            getResources().getSentimentWords()
                    .printSentimentTermsInSingleHeaderRow(wTermStrengthWriter);
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wWriter, wTermStrengthWriter);
            wWriter.close();
            wTermStrengthWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分析输入文件中的所有行并连ID列一起记录到输出文件.<br> 将相关系数、平均差、三元方法、二元方法、scale方法等值打印到输出文件.
     * <br>UC-12 Classify all lines of text in a file for sentiment [includes
     * accuracy evaluations]
     * <br>UC-13 Classify texts in a column within a file or folder
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     * <br>UC-22 Use trinary classification (positive-negative-neutral)
     * <br>UC-23 Use binary classification (positive-negative)
     * <br>UC-24 Use a single positive-negative scale classification
     *
     * @param sInputFile  输入文件路径
     * @param iTextCol    文本列
     * @param iIDCol      ID列
     * @param sOutputFile 输出文本路径
     */
    public void classifyAllLinesAndRecordWithID(final String sInputFile,
                                                final int iTextCol,
                                                final int iIDCol,
                                                final String sOutputFile) {
        int iPos;
        int iNeg;
        int iTrinary;
        int iScale;
        int iCount1 = 0;
        String sLine = "";
        try {
            BufferedReader rReader = new BufferedReader(
                    new FileReader(sInputFile));
            BufferedWriter wWriter = new BufferedWriter(
                    new FileWriter(sOutputFile));
            while (rReader.ready()) {
                sLine = rReader.readLine();
                iCount1++;
                if (!Objects.equals(sLine, "")) {
                    String[] sData = sLine.split("\t");
                    if (sData.length > iTextCol && sData.length > iIDCol) {
                        Paragraph newParagraph = new Paragraph();
                        newParagraph.setParagraph(sData[iTextCol],
                                getResources(), getOptions());
                        if (getOptions().isBgTrinaryMode()) {
                            iTrinary
                                    =
                                    newParagraph.getParagraphTrinarySentiment();
                            wWriter.write(
                                    sData[iIDCol] + "\t" + iTrinary + "\n");
                        } else if (getOptions().isBgScaleMode()) {
                            iScale = newParagraph.getParagraphScaleSentiment();
                            wWriter.write(sData[iIDCol] + "\t" + iScale + "\n");
                        } else {
                            iPos = newParagraph.getParagraphPositiveSentiment();
                            iNeg = newParagraph.getParagraphNegativeSentiment();
                            wWriter.write(
                                    sData[iIDCol] + "\t" + iPos + "\t" + iNeg
                                            + "\n");
                        }
                    }
                }
            }
            Thread.sleep(SLEEP_TIME);
            if (rReader.ready()) {
                System.out.println("Reader ready again after pause!");
            }
            int character = rReader.read();
            if (character != -1) {
                System.out.println(
                        "Reader returns char after reader.read() false! "
                                + character);
            }
            rReader.close();
            wWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find input file: " + sInputFile);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(
                    "Error reading or writing from file: " + sInputFile);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(
                    "Error reading from or writing to file: " + sInputFile);
            e.printStackTrace();
        }
        System.out.println(
                "Processed " + iCount1 + " lines from file: " + sInputFile
                        + ". Last line was:\n" + sLine);
    }

    /**
     * 注释输入文件中的所有行的某一列.<br>
     *
     * @param sInputFile 输入文件路径
     * @param iTextCol   文本列
     */
    public void annotateAllLinesInInputFile(final String sInputFile,
                                            final int iTextCol) {
        int iPos;
        int iNeg;
        int iTrinary;
        int iScale;
        String sTempFile = sInputFile + "_temp";
        try {
            BufferedReader rReader = new BufferedReader(
                    new FileReader(sInputFile));
            BufferedWriter wWriter = new BufferedWriter(
                    new FileWriter(sTempFile));
            while (rReader.ready()) {
                String sLine = rReader.readLine();
                if (!Objects.equals(sLine, "")) {
                    String[] sData = sLine.split("\t");
                    if (sData.length > iTextCol) {
                        Paragraph newParagraph = new Paragraph();
                        newParagraph.setParagraph(sData[iTextCol],
                                getResources(), getOptions());
                        if (getOptions().isBgTrinaryMode()) {
                            iTrinary
                                    =
                                    newParagraph.getParagraphTrinarySentiment();
                            wWriter.write(sLine + "\t" + iTrinary + "\n");
                        } else if (getOptions().isBgScaleMode()) {
                            iScale = newParagraph.getParagraphScaleSentiment();
                            wWriter.write(sLine + "\t" + iScale + "\n");
                        } else {
                            iPos = newParagraph.getParagraphPositiveSentiment();
                            iNeg = newParagraph.getParagraphNegativeSentiment();
                            wWriter.write(
                                    sLine + "\t" + iPos + "\t" + iNeg + "\n");
                        }
                    } else {
                        wWriter.write(sLine + "\n");
                    }
                }
            }
            rReader.close();
            wWriter.close();
            File original = new File(sInputFile);
            original.delete();
            File newFile = new File(sTempFile);
            newFile.renameTo(original);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find input file: " + sInputFile);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(
                    "Error reading or writing from file: " + sInputFile);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(
                    "Error reading from or writing to file: " + sInputFile);
            e.printStackTrace();
        }
    }

    /**
     * 分析输入文件中的所有行到输出文件.<br> 将相关系数、平均差、三元方法、二元方法、scale方法等值打印到输出文件；检测文件中数据的合理性.
     * <br>UC-12 Classify all lines of text in a file for sentiment [includes
     * accuracy evaluations]
     * <br>UC-13 Classify texts in a column within a file or folder
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     * <br>UC-22 Use trinary classification (positive-negative-neutral)
     * <br>UC-23 Use binary classification (positive-negative)
     * <br>UC-24 Use a single positive-negative scale classification
     *
     * @param sInputFile  输入文件路径
     * @param iTextCol    文本列
     * @param sOutputFile 输出文本路径
     */
    public void classifyAllLinesInInputFile(final String sInputFile,
                                            final int iTextCol,
                                            final String sOutputFile) {
        int iPos = 0;
        int iNeg = 0;
        int iTrinary = INVALID_TRINARY;
        int iScale = INVALID_SCALE;
        int iFileTrinary = INVALID_FILE_TRINARY;
        int iFileScale = INVALID_FILE_SCALE;
        int iClassified = 0;
        int iCorrectPosCount = 0;
        int iCorrectNegCount = 0;
        int iCorrectTrinaryCount = 0;
        int iCorrectScaleCount = 0;
        int iPosAbsDiff = 0;
        int iNegAbsDiff = 0;
        int[][] confusion = {new int[CONFUSION_SIZE], new int[CONFUSION_SIZE],
                new int[CONFUSION_SIZE]};
        int maxClassifyForCorrelation = MAX_CLASSIFY_FOR_CORRELATION;
        int[] iPosClassCorr = new int[maxClassifyForCorrelation];
        int[] iNegClassCorr = new int[maxClassifyForCorrelation];
        int[] iPosClassPred = new int[maxClassifyForCorrelation];
        int[] iNegClassPred = new int[maxClassifyForCorrelation];
        int[] iScaleClassCorr = new int[maxClassifyForCorrelation];
        int[] iScaleClassPred = new int[maxClassifyForCorrelation];
        String sRationale = "";
        String sOutput;
        try {
            BufferedReader rReader;
            BufferedWriter wWriter;
            if (getOptions().isBgForceUTF8()) {
                wWriter = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(sOutputFile),
                        StandardCharsets.UTF_8));
                rReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(sInputFile),
                                StandardCharsets.UTF_8));
            } else {
                wWriter = new BufferedWriter(new FileWriter(sOutputFile));
                rReader = new BufferedReader(new FileReader(sInputFile));
            }
            if (getOptions().isBgTrinaryMode()
                    || getOptions().isBgScaleMode()) {
                wWriter.write("Overall\tText");
            } else if (getOptions().isBgTensiStrength()) {
                wWriter.write("Relax\tStress\tText");
            } else {
                wWriter.write("Positive\tNegative\tText");
            }
            if (getOptions().isBgExplainClassification()) {
                wWriter.write("\tExplanation\n");
            } else {
                wWriter.write("\n");
            }
            while (rReader.ready()) {
                String sLine = rReader.readLine();
                if (!Objects.equals(sLine, "")) {
                    int iTabPos = sLine.lastIndexOf("\t");
                    int iFilePos = 0;
                    int iFileNeg = 0;
                    if (iTabPos >= 0) {
                        String[] sData = sLine.split("\t");
                        if (sData.length > 1) {
                            if (iTextCol > -1) {
                                wWriter.write(sLine + "\t");
                                if (iTextCol < sData.length) {
                                    sLine = sData[iTextCol];
                                }
                            } else if (getOptions().isBgTrinaryMode()) {
                                try {
                                    iFileTrinary = Integer.parseInt(
                                            sData[0].trim());
                                    if (iFileTrinary > 1 || iFileTrinary < -1) {
                                        System.out.println(
                                                "Invalid trinary sentiment "
                                                        + iFileTrinary
                                                        + " (expected -1,0,1)"
                                                        + " at line: " + sLine);
                                        iFileTrinary = 0;
                                    }
                                } catch (
                                        NumberFormatException numberformatexception) {
                                    throw new RuntimeException(
                                            numberformatexception);
                                }
                            } else if (getOptions().isBgScaleMode()) {
                                try {
                                    iFileScale = Integer.parseInt(
                                            sData[0].trim());
                                    if (iFileScale > MAX_SCALE
                                            || iFileScale < MIN_SCALE) {
                                        System.out.println(
                                                "Invalid overall sentiment "
                                                        + iFileScale
                                                        + " (expected -4 to "
                                                        + "+4) at line: "
                                                        + sLine);
                                        iFileScale = 0;
                                    }
                                } catch (
                                        NumberFormatException numberformatexception1) {
                                    throw new RuntimeException(
                                            numberformatexception1);
                                }
                            } else {
                                try {
                                    iFilePos = Integer.parseInt(
                                            sData[0].trim());
                                    iFileNeg = Integer.parseInt(
                                            sData[1].trim());
                                    if (iFileNeg < 0) {
                                        iFileNeg = -iFileNeg;
                                    }
                                } catch (
                                        NumberFormatException numberformatexception2) {
                                    throw new RuntimeException(
                                            numberformatexception2);
                                }
                            }
                        }
                        sLine = sLine.substring(iTabPos + 1);
                    }
                    Paragraph newParagraph = new Paragraph();
                    newParagraph.setParagraph(sLine, getResources(),
                            getOptions());
                    if (getOptions().isBgTrinaryMode()) {
                        iTrinary = newParagraph.getParagraphTrinarySentiment();
                        if (getOptions().isBgExplainClassification()) {
                            sRationale = "\t"
                                    + newParagraph.getClassificationRationale();
                        }
                        sOutput = iTrinary + "\t" + sLine + sRationale + "\n";
                    } else if (getOptions().isBgScaleMode()) {
                        iScale = newParagraph.getParagraphScaleSentiment();
                        if (getOptions().isBgExplainClassification()) {
                            sRationale = "\t"
                                    + newParagraph.getClassificationRationale();
                        }
                        sOutput = iScale + "\t" + sLine + sRationale + "\n";
                    } else {
                        iPos = newParagraph.getParagraphPositiveSentiment();
                        iNeg = newParagraph.getParagraphNegativeSentiment();
                        if (getOptions().isBgExplainClassification()) {
                            sRationale = "\t"
                                    + newParagraph.getClassificationRationale();
                        }
                        sOutput = iPos + "\t" + iNeg + "\t" + sLine + sRationale
                                + "\n";
                    }
                    wWriter.write(sOutput);
                    if (getOptions().isBgTrinaryMode()) {
                        if (iFileTrinary >= -1 && iTrinary >= -1
                                && iTrinary <= 1) {
                            iClassified++;
                            if (iFileTrinary == iTrinary) {
                                iCorrectTrinaryCount++;
                            }
                            confusion[iTrinary + 1][iFileTrinary + 1]++;
                        }
                    } else if (getOptions().isBgScaleMode()) {
                        if (iFileScale > INVALID_FILE_SCALE) {
                            iClassified++;
                            if (iFileScale == iScale) {
                                iCorrectScaleCount++;
                            }
                            if (iClassified < maxClassifyForCorrelation) {
                                iScaleClassCorr[iClassified] = iFileScale;
                            }
                            iScaleClassPred[iClassified] = iScale;
                        }
                    } else if (iFileNeg != 0) {
                        iClassified++;
                        if (iPos == iFilePos) {
                            iCorrectPosCount++;
                        }
                        iPosAbsDiff += Math.abs(iPos - iFilePos);
                        if (iClassified < maxClassifyForCorrelation) {
                            iPosClassCorr[iClassified] = iFilePos;
                        }
                        iPosClassPred[iClassified] = iPos;
                        if (iNeg == -iFileNeg) {
                            iCorrectNegCount++;
                        }
                        iNegAbsDiff += Math.abs(iNeg + iFileNeg);
                        iNegClassCorr[iClassified] = iFileNeg;
                        iNegClassPred[iClassified] = iNeg;
                    }
                }
            }
            rReader.close();
            wWriter.close();
            if (iClassified > 0) {
                if (getOptions().isBgTrinaryMode()) {
                    System.out.println(
                            "Trinary correct: " + iCorrectTrinaryCount + " ("
                                    + ((float) iCorrectTrinaryCount
                                            / (float) iClassified) * PERCENTAGE
                                    + "%)."); // 这是计算百分比用的，不应该在外部声明.
                    System.out.println("Correct -> -1   0   1");
                    System.out.println("Est = -1   " + confusion[0][0] + " "
                            + confusion[0][1] + " " + confusion[0][2]);
                    System.out.println("Est =  0   " + confusion[1][0] + " "
                            + confusion[1][1] + " " + confusion[1][2]);
                    System.out.println("Est =  1   " + confusion[2][0] + " "
                            + confusion[2][1] + " " + confusion[2][2]);
                } else if (getOptions().isBgScaleMode()) {
                    System.out.println(
                            "Scale correct: " + iCorrectScaleCount + " ("
                                    + ((float) iCorrectScaleCount
                                            / (float) iClassified) * PERCENTAGE
                                    + "%) out of " + iClassified);
                    System.out.println("  Correlation: "
                            + ClassificationStatistics.correlation(
                            iScaleClassCorr, iScaleClassPred, iClassified));
                } else {
                    System.out.print(
                            getOptions().getSgProgramPos() + " correct: "
                                    + iCorrectPosCount + " ("
                                    + ((float) iCorrectPosCount
                                            / (float) iClassified) * PERCENTAGE
                                    + "%).");
                    System.out.println(" Mean abs diff: "
                            + (float) iPosAbsDiff / (float) iClassified);
                    if (iClassified < maxClassifyForCorrelation) {
                        System.out.println(" Correlation: "
                                + ClassificationStatistics.correlationAbs(
                                iPosClassCorr, iPosClassPred, iClassified));
                        int corrWithin1
                                = ClassificationStatistics.accuracyWithin1(
                                iPosClassCorr, iPosClassPred, iClassified,
                                false);
                        System.out.println(
                                " Correct +/- 1: " + corrWithin1 + " ("
                                        + (float) (ONE_HUNDRED * corrWithin1)
                                        / (float) iClassified + "%)");
                    }
                    System.out.print(
                            getOptions().getSgProgramNeg() + " correct: "
                                    + iCorrectNegCount + " ("
                                    + ((float) iCorrectNegCount
                                            / (float) iClassified) * PERCENTAGE
                                    + "%).");
                    System.out.println(" Mean abs diff: "
                            + (float) iNegAbsDiff / (float) iClassified);
                    if (iClassified < maxClassifyForCorrelation) {
                        System.out.println(" Correlation: "
                                + ClassificationStatistics.correlationAbs(
                                iNegClassCorr, iNegClassPred, iClassified));
                        int corrWithin1
                                = ClassificationStatistics.accuracyWithin1(
                                iNegClassCorr, iNegClassPred, iClassified,
                                true);
                        System.out.println(
                                " Correct +/- 1: " + corrWithin1 + " ("
                                        + (float) (ONE_HUNDRED * corrWithin1)
                                        / (float) iClassified + "%)");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find input file: " + sInputFile);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading from input file: " + sInputFile
                    + " or writing to output file " + sOutputFile);
            e.printStackTrace();
        }
    }

    private void writeClassificationStatsHeadings(final BufferedWriter w)
            throws IOException {
        String sPosOrScale;
        if (getOptions().isBgScaleMode()) {
            sPosOrScale = "ScaleCorrel";
        } else {
            sPosOrScale = "PosCorrel";
        }
        w.write("\tPosCorrect\tiPosCorrect/Total\tNegCorrect\tNegCorrect"
                + "/Total\tPosWithin1\tPosWithin1/Total\t"
                + "NegWithin1\tNegWithin1/Total\t" + sPosOrScale + "\tNegCorrel"
                + "\tPosMPE\tNegMPE\tPosMPEnoDiv\tNegMPEnoDiv"
                + "\tTrinaryOrScaleCorrect\tTrinaryOrScaleCorrect"
                + "/TotalClassified" + "\tTrinaryOrScaleCorrectWithin1"
                + "\tTrinaryOrScaleCorrectWithin1/TotalClassified"
                + "\test-1corr-1\test-1corr0\test-1corr1"
                + "\test0corr-1\test0corr0\test0corr1"
                + "\test1corr-1\test1corr0\test1corr1" + "\tTotalClassified\n");
    }


    private boolean printClassificationResultsRow(final int[] iPosClassAll,
                                                  final int[] iNegClassAll,
                                                  final int[] iTrinaryOrScaleClassAll,
                                                  final BufferedWriter wWriter) {
        // 一下这些均表示结果的无效值。
        int iPosCorrect = INVALID_VALUE;
        int iNegCorrect = INVALID_VALUE;
        int iPosWithin1 = INVALID_VALUE;
        int iNegWithin1 = INVALID_VALUE;
        int iTrinaryCorrect = INVALID_VALUE;
        int iTrinaryCorrectWithin1 = INVALID_VALUE;
        double fPosCorrectPoportion = INVALID_PROPORTION;
        double fNegCorrectPoportion = INVALID_PROPORTION;
        double fPosWithin1Poportion = INVALID_PROPORTION;
        double fNegWithin1Poportion = INVALID_PROPORTION;
        double fTrinaryCorrectPoportion = INVALID_PROPORTION;
        double fTrinaryCorrectWithin1Poportion = INVALID_PROPORTION;
        double fPosOrScaleCorr = INVALID_CORR;
        double fNegCorr = INVALID_CORR;
        double fPosMPE = INVALID_MPE;
        double fNegMPE = INVALID_MPE;
        double fPosMPEnoDiv = INVALID_MPE;
        double fNegMPEnoDiv = INVALID_MPE;
        int[][] estCorr = {// 这个和数据结构有关。
                new int[CONFUSION_SIZE], new int[CONFUSION_SIZE],
                new int[CONFUSION_SIZE]};
        try {
            if (getOptions().isBgTrinaryMode()) {
                iTrinaryCorrect = ClassificationStatistics.accuracy(
                        igTrinaryCorrect, iTrinaryOrScaleClassAll,
                        igParagraphCount, false);
                iTrinaryCorrectWithin1
                        = ClassificationStatistics.accuracyWithin1(
                        igTrinaryCorrect, iTrinaryOrScaleClassAll,
                        igParagraphCount, false);
                fTrinaryCorrectPoportion = (float) iTrinaryCorrect
                        / (float) igParagraphCount;
                fTrinaryCorrectWithin1Poportion = (float) iTrinaryCorrectWithin1
                        / (float) igParagraphCount;
                ClassificationStatistics.trinaryOrBinaryConfusionTable(
                        iTrinaryOrScaleClassAll, igTrinaryCorrect,
                        igParagraphCount, estCorr);
            } else if (getOptions().isBgScaleMode()) {
                iTrinaryCorrect = ClassificationStatistics.accuracy(
                        igScaleCorrect, iTrinaryOrScaleClassAll,
                        igParagraphCount, false);
                iTrinaryCorrectWithin1
                        = ClassificationStatistics.accuracyWithin1(
                        igScaleCorrect, iTrinaryOrScaleClassAll,
                        igParagraphCount, false);
                fTrinaryCorrectPoportion = (float) iTrinaryCorrect
                        / (float) igParagraphCount;
                fTrinaryCorrectWithin1Poportion = (float) iTrinaryCorrectWithin1
                        / (float) igParagraphCount;
                fPosOrScaleCorr = ClassificationStatistics.correlation(
                        igScaleCorrect, iTrinaryOrScaleClassAll,
                        igParagraphCount);
            } else {
                iPosCorrect = ClassificationStatistics.accuracy(igPosCorrect,
                        iPosClassAll, igParagraphCount, false);
                iNegCorrect = ClassificationStatistics.accuracy(igNegCorrect,
                        iNegClassAll, igParagraphCount, true);
                iPosWithin1 = ClassificationStatistics.accuracyWithin1(
                        igPosCorrect, iPosClassAll, igParagraphCount, false);
                iNegWithin1 = ClassificationStatistics.accuracyWithin1(
                        igNegCorrect, iNegClassAll, igParagraphCount, true);
                fPosOrScaleCorr = ClassificationStatistics.correlationAbs(
                        igPosCorrect, iPosClassAll, igParagraphCount);
                fNegCorr = ClassificationStatistics.correlationAbs(igNegCorrect,
                        iNegClassAll, igParagraphCount);
                fPosMPE = ClassificationStatistics.absoluteMeanPercentageError(
                        igPosCorrect, iPosClassAll, igParagraphCount, false);
                fNegMPE = ClassificationStatistics.absoluteMeanPercentageError(
                        igNegCorrect, iNegClassAll, igParagraphCount, true);
                fPosMPEnoDiv
                        =
                        ClassificationStatistics.absoluteMeanPercentageErrorNoDivision(
                        igPosCorrect, iPosClassAll, igParagraphCount, false);
                fNegMPEnoDiv
                        =
                        ClassificationStatistics.absoluteMeanPercentageErrorNoDivision(
                        igNegCorrect, iNegClassAll, igParagraphCount, true);
                fPosCorrectPoportion = (float) iPosCorrect
                        / (float) igParagraphCount;
                fNegCorrectPoportion = (float) iNegCorrect
                        / (float) igParagraphCount;
                fPosWithin1Poportion = (float) iPosWithin1
                        / (float) igParagraphCount;
                fNegWithin1Poportion = (float) iNegWithin1
                        / (float) igParagraphCount;
            }
            wWriter.write(
                    "\t" + iPosCorrect + "\t" + fPosCorrectPoportion + "\t"
                            + iNegCorrect + "\t" + fNegCorrectPoportion + "\t"
                            + iPosWithin1 + "\t" + fPosWithin1Poportion + "\t"
                            + iNegWithin1 + "\t" + fNegWithin1Poportion + "\t"
                            + fPosOrScaleCorr + "\t" + fNegCorr + "\t" + fPosMPE
                            + "\t" + fNegMPE + "\t" + fPosMPEnoDiv + "\t"
                            + fNegMPEnoDiv + "\t" + iTrinaryCorrect + "\t"
                            + fTrinaryCorrectPoportion + "\t"
                            + iTrinaryCorrectWithin1 + "\t"
                            + fTrinaryCorrectWithin1Poportion + "\t"
                            + estCorr[0][0] + "\t" + estCorr[0][1] + "\t"
                            + estCorr[0][2] + "\t" + estCorr[1][0] + "\t"
                            + estCorr[1][1] + "\t" + estCorr[1][2] + "\t"
                            + estCorr[2][0] + "\t" + estCorr[2][1] + "\t"
                            + estCorr[2][2] + "\t" + igParagraphCount + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void selectDecileAsSubcorpus(final int[] iParagraphRand,
                                         final int iDecile,
                                         final boolean bInvert) {
        if (igParagraphCount == 0) {
            return;
        }
        int iMin = (int) (((float) igParagraphCount / FACTOR) * (float) (iDecile
                - 1)) + 1;
        int iMax = (int) (((float) igParagraphCount / FACTOR)
                * (float) iDecile);
        if (iDecile == (int) FACTOR) {
            iMax = igParagraphCount;
        }
        if (iDecile == 0) {
            iMin = 0;
        }
        igSupcorpusMemberCount = 0;
        for (int i = 1; i <= igParagraphCount; i++) {
            if (i >= iMin && i <= iMax) {
                bgSupcorpusMember[iParagraphRand[i]] = !bInvert;
                if (!bInvert) {
                    igSupcorpusMemberCount++;
                }
            } else {
                bgSupcorpusMember[iParagraphRand[i]] = bInvert;
                if (bInvert) {
                    igSupcorpusMemberCount++;
                }
            }
        }

    }

    /**
     * 多次优化语料库所有的字典权重.
     * <br>UC-27 Optimise sentiment strengths of existing sentiment terms
     *
     * @param iMinImprovement     最小提升
     * @param bUseTotalDifference 是否使用全差分
     * @param iOptimisationTotal  优化次数
     */
    public void optimiseDictionaryWeightingsForCorpusMultipleTimes(
            final int iMinImprovement, final boolean bUseTotalDifference,
            final int iOptimisationTotal) {
        if (iOptimisationTotal < 1) {
            return;
        }
        if (iOptimisationTotal == 1) {
            optimiseDictionaryWeightingsForCorpus(iMinImprovement,
                    bUseTotalDifference);
            return;
        }
        int iTotalSentimentWords = getResources().getSentimentWords()
                .getSentimentWordCount();
        int[] iOriginalSentimentStrengths = new int[iTotalSentimentWords + 1];
        for (int j = 1; j <= iTotalSentimentWords; j++) {
            iOriginalSentimentStrengths[j] = getResources().getSentimentWords()
                    .getSentiment(j);
        }

        int[] iTotalWeight = new int[iTotalSentimentWords + 1];
        for (int j = 1; j <= iTotalSentimentWords; j++) {
            iTotalWeight[j] = 0;
        }

        for (int i = 0; i < iOptimisationTotal; i++) {
            optimiseDictionaryWeightingsForCorpus(iMinImprovement,
                    bUseTotalDifference);
            for (int j = 1; j <= iTotalSentimentWords; j++) {
                iTotalWeight[j] += getResources().getSentimentWords()
                        .getSentiment(j);
            }

            for (int j = 1; j <= iTotalSentimentWords; j++) {
                getResources().getSentimentWords()
                        .setSentiment(j, iOriginalSentimentStrengths[j]);
            }

        }

        for (int j = 1; j <= iTotalSentimentWords; j++) {
            getResources().getSentimentWords().setSentiment(j,
                    (int) ((double) ((float) iTotalWeight[j]
                            / (float) iOptimisationTotal) + OPTIMIZE_FACTOR));
        }

        optimiseDictionaryWeightingsForCorpus(iMinImprovement,
                bUseTotalDifference);
    }

    /**
     * 优化语料库所有的字典权重.
     * <br>UC-27 Optimise sentiment strengths of existing sentiment terms
     *
     * @param iMinImprovement     最小提升
     * @param bUseTotalDifference 是否使用全差分
     */
    public void optimiseDictionaryWeightingsForCorpus(final int iMinImprovement,
                                                      final boolean bUseTotalDifference) {
        mode.optDictWeig(iMinImprovement, bUseTotalDifference);
//          if (getOptions().isBgTrinaryMode()) {
//               optimiseDictionaryWeightingsForCorpusTrinaryOrBinary
//               (iMinImprovement);
//          } else if (getOptions().isBgScaleMode()) {
//               optimiseDictionaryWeightingsForCorpusScale(iMinImprovement);
//          } else {
//               optimiseDictionaryWeightingsForCorpusPosNeg(iMinImprovement,
//               bUseTotalDifference);
//          }
    }

    /**
     * 使用所有的可能的不同选项进行十折的交叉验证. <br> 根据options中的相关属性, 分别决定是否要使用三元分类法, scale分类法,
     * 二元分类法 , 以及习语, 重复强调, 增强词等多种影响情绪得分的因素 , 进行十折的交叉验证, 并把结果保存在指定文件中.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     * <br>UC-22 Use trinary classification (positive-negative-neutral)
     * <br>UC-23 Use binary classification (positive-negative)
     * <br>UC-24 Use a single positive-negative scale classification
     * <br>UC-29 Machine learning evaluations
     *
     * @param iMinImprovement     最小提升
     * @param bUseTotalDifference 是否要累加所有差异
     * @param iReplications       重复次数
     * @param iMultiOptimisations 权重优化次数
     * @param sOutFileName        输出文件路径
     */
    public void run10FoldCrossValidationForAllOptionVariations(
            final int iMinImprovement, final boolean bUseTotalDifference,
            final int iReplications, final int iMultiOptimisations,
            final String sOutFileName) {
        try {
            BufferedWriter wResultsWriter = new BufferedWriter(
                    new FileWriter(sOutFileName));
            BufferedWriter wTermStrengthWriter = new BufferedWriter(
                    new FileWriter(FileOps.sChopFileNameExtension(sOutFileName)
                            + "_termStrVars.txt"));
            if (igPosClass == null || igPosClass.length < igPosCorrect.length) {
                igPosClass = new int[igParagraphCount + 1];
                igNegClass = new int[igParagraphCount + 1];
                igTrinaryClass = new int[igParagraphCount + 1];
            }
            getOptions().printClassificationOptionsHeadings(wResultsWriter);
            writeClassificationStatsHeadings(wResultsWriter);
            getOptions().printClassificationOptionsHeadings(
                    wTermStrengthWriter);
            getResources().getSentimentWords()
                    .printSentimentTermsInSingleHeaderRow(wTermStrengthWriter);
            System.out.println(
                    "About to start classifications for 20 different option "
                            + "variations");
            if (getOptions().isBgTrinaryMode()) {
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(
                        igTrinaryCorrect, igTrinaryClass, igParagraphCount,
                        false);
            } else if (getOptions().isBgScaleMode()) {
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(
                        igScaleCorrect, igScaleClass, igParagraphCount, false);
            } else {
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(
                        igPosCorrect, igPosClass, igParagraphCount, false);
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(
                        igNegCorrect, igNegClass, igParagraphCount, true);
            }
            getOptions().printBlankClassificationOptions(wResultsWriter);
            if (getOptions().isBgTrinaryMode()) {
                printClassificationResultsRow(igPosClass, igNegClass,
                        igTrinaryClass, wResultsWriter);
            } else {
                printClassificationResultsRow(igPosClass, igNegClass,
                        igScaleClass, wResultsWriter);
            }
            getOptions().printClassificationOptions(wResultsWriter,
                    igParagraphCount, bUseTotalDifference, iMultiOptimisations);
            calculateCorpusSentimentScores();
            if (getOptions().isBgTrinaryMode()) {
                printClassificationResultsRow(igPosClass, igNegClass,
                        igTrinaryClass, wResultsWriter);
            } else {
                printClassificationResultsRow(igPosClass, igNegClass,
                        igScaleClass, wResultsWriter);
            }
            getOptions().printBlankClassificationOptions(wTermStrengthWriter);
            getResources().getSentimentWords()
                    .printSentimentValuesInSingleRow(wTermStrengthWriter);
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setIgEmotionParagraphCombineMethod(
                    1 - getOptions().getIgEmotionParagraphCombineMethod());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setIgEmotionParagraphCombineMethod(
                    1 - getOptions().getIgEmotionParagraphCombineMethod());
            getOptions().setIgEmotionSentenceCombineMethod(
                    1 - getOptions().getIgEmotionSentenceCombineMethod());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setIgEmotionSentenceCombineMethod(
                    1 - getOptions().getIgEmotionSentenceCombineMethod());
            getOptions().setBgReduceNegativeEmotionInQuestionSentences(
                    !getOptions().isBgReduceNegativeEmotionInQuestionSentences());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgReduceNegativeEmotionInQuestionSentences(
                    !getOptions().isBgReduceNegativeEmotionInQuestionSentences());
            getOptions().setBgMissCountsAsPlus2(
                    !getOptions().isBgMissCountsAsPlus2());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgMissCountsAsPlus2(
                    !getOptions().isBgMissCountsAsPlus2());
            getOptions().setBgYouOrYourIsPlus2UnlessSentenceNegative(
                    !getOptions().isBgYouOrYourIsPlus2UnlessSentenceNegative());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgYouOrYourIsPlus2UnlessSentenceNegative(
                    !getOptions().isBgYouOrYourIsPlus2UnlessSentenceNegative());
            getOptions().setBgExclamationInNeutralSentenceCountsAsPlus2(
                    !getOptions().isBgExclamationInNeutralSentenceCountsAsPlus2());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgExclamationInNeutralSentenceCountsAsPlus2(
                    !getOptions().isBgExclamationInNeutralSentenceCountsAsPlus2());
            getOptions().setBgUseIdiomLookupTable(
                    !getOptions().isBgUseIdiomLookupTable());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgUseIdiomLookupTable(
                    !getOptions().isBgUseIdiomLookupTable());
            int iTemp = getOptions().getIgMoodToInterpretNeutralEmphasis();
            getOptions().setIgMoodToInterpretNeutralEmphasis(
                    -getOptions().getIgMoodToInterpretNeutralEmphasis());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setIgMoodToInterpretNeutralEmphasis(0);
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setIgMoodToInterpretNeutralEmphasis(iTemp);
            System.out.println(
                    "About to start 10th option variation classification");
            getOptions().setBgAllowMultiplePositiveWordsToIncreasePositiveEmotion(
                    !getOptions().isBgAllowMultiplePositiveWordsToIncreasePositiveEmotion());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgAllowMultiplePositiveWordsToIncreasePositiveEmotion(
                    !getOptions().isBgAllowMultiplePositiveWordsToIncreasePositiveEmotion());
            getOptions().setBgAllowMultipleNegativeWordsToIncreaseNegativeEmotion(
                    !getOptions().isBgAllowMultipleNegativeWordsToIncreaseNegativeEmotion());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgAllowMultipleNegativeWordsToIncreaseNegativeEmotion(
                    !getOptions().isBgAllowMultipleNegativeWordsToIncreaseNegativeEmotion());
            getOptions().setBgIgnoreBoosterWordsAfterNegatives(
                    !getOptions().isBgIgnoreBoosterWordsAfterNegatives());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgIgnoreBoosterWordsAfterNegatives(
                    !getOptions().isBgIgnoreBoosterWordsAfterNegatives());
            getOptions().setBgMultipleLettersBoostSentiment(
                    !getOptions().isBgMultipleLettersBoostSentiment());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgMultipleLettersBoostSentiment(
                    !getOptions().isBgMultipleLettersBoostSentiment());
            getOptions().setBgBoosterWordsChangeEmotion(
                    !getOptions().isBgBoosterWordsChangeEmotion());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgBoosterWordsChangeEmotion(
                    !getOptions().isBgBoosterWordsChangeEmotion());
            if (getOptions().isBgNegatingWordsFlipEmotion()) {
                getOptions().setBgNegatingWordsFlipEmotion(false);
                run10FoldCrossValidationMultipleTimes(iMinImprovement,
                        bUseTotalDifference, iReplications, iMultiOptimisations,
                        wResultsWriter, wTermStrengthWriter);
                getOptions().setBgNegatingWordsFlipEmotion(
                        !getOptions().isBgNegatingWordsFlipEmotion());
            } else {
                getOptions().setBgNegatingPositiveFlipsEmotion(
                        !getOptions().isBgNegatingPositiveFlipsEmotion());
                run10FoldCrossValidationMultipleTimes(iMinImprovement,
                        bUseTotalDifference, iReplications, iMultiOptimisations,
                        wResultsWriter, wTermStrengthWriter);
                getOptions().setBgNegatingPositiveFlipsEmotion(
                        !getOptions().isBgNegatingPositiveFlipsEmotion());
                getOptions().setBgNegatingNegativeNeutralisesEmotion(
                        !getOptions().isBgNegatingNegativeNeutralisesEmotion());
                run10FoldCrossValidationMultipleTimes(iMinImprovement,
                        bUseTotalDifference, iReplications, iMultiOptimisations,
                        wResultsWriter, wTermStrengthWriter);
                getOptions().setBgNegatingNegativeNeutralisesEmotion(
                        !getOptions().isBgNegatingNegativeNeutralisesEmotion());
            }
            getOptions().setBgCorrectSpellingsWithRepeatedLetter(
                    !getOptions().isBgCorrectSpellingsWithRepeatedLetter());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgCorrectSpellingsWithRepeatedLetter(
                    !getOptions().isBgCorrectSpellingsWithRepeatedLetter());
            getOptions().setBgUseEmoticons(!getOptions().isBgUseEmoticons());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgUseEmoticons(!getOptions().isBgUseEmoticons());
            getOptions().setBgCapitalsBoostTermSentiment(
                    !getOptions().isBgCapitalsBoostTermSentiment());
            run10FoldCrossValidationMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            getOptions().setBgCapitalsBoostTermSentiment(
                    !getOptions().isBgCapitalsBoostTermSentiment());
            if (iMinImprovement > 1) {
                run10FoldCrossValidationMultipleTimes(iMinImprovement - 1,
                        bUseTotalDifference, iReplications, iMultiOptimisations,
                        wResultsWriter, wTermStrengthWriter);
            }
            run10FoldCrossValidationMultipleTimes(iMinImprovement + 1,
                    bUseTotalDifference, iReplications, iMultiOptimisations,
                    wResultsWriter, wTermStrengthWriter);
            wResultsWriter.close();
            wTermStrengthWriter.close();
            summariseMultiple10FoldValidations(sOutFileName,
                    sOutFileName + "_sum.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //UC-29 Machine learning evaluations
    private void run10FoldCrossValidationOnce(final int iMinImprovement,
                                              final boolean bUseTotalDifference,
                                              final int iMultiOptimisations,
                                              final BufferedWriter wWriter,
                                              final BufferedWriter wTermStrengthWriter) {
        int iTotalSentimentWords = getResources().getSentimentWords()
                .getSentimentWordCount();
        int[] iParagraphRand = new int[igParagraphCount + 1];
        int[] iPosClassAll = new int[igParagraphCount + 1];
        int[] iNegClassAll = new int[igParagraphCount + 1];
        int[] iTrinaryOrScaleClassAll = new int[igParagraphCount + 1];
        Sort.makeRandomOrderList(iParagraphRand);
        int[] iOriginalSentimentStrengths = new int[iTotalSentimentWords + 1];
        for (int i = 1; i < iTotalSentimentWords; i++) {
            iOriginalSentimentStrengths[i] = getResources().getSentimentWords()
                    .getSentiment(i);
        }

        for (int iFold = 1; iFold <= TEN_FOLD_CROSS_VALIDATION;
             iFold++) { // 这个就是10FoldCrossValidation，不应该在外部声明。
            selectDecileAsSubcorpus(iParagraphRand, iFold, true);
            reCalculateCorpusSentimentScores();
            optimiseDictionaryWeightingsForCorpusMultipleTimes(iMinImprovement,
                    bUseTotalDifference, iMultiOptimisations);
            getOptions().printClassificationOptions(wTermStrengthWriter,
                    iMinImprovement, bUseTotalDifference, iMultiOptimisations);
            getResources().getSentimentWords()
                    .printSentimentValuesInSingleRow(wTermStrengthWriter);
            selectDecileAsSubcorpus(iParagraphRand, iFold, false);
            reCalculateCorpusSentimentScores();
            for (int i = 1; i <= igParagraphCount; i++) {
                if (bgSupcorpusMember[i]) {
                    iPosClassAll[i] = igPosClass[i];
                    iNegClassAll[i] = igNegClass[i];
                    if (getOptions().isBgTrinaryMode()) {
                        iTrinaryOrScaleClassAll[i] = igTrinaryClass[i];
                    } else {
                        iTrinaryOrScaleClassAll[i] = igScaleClass[i];
                    }
                }
            }

            for (int i = 1; i < iTotalSentimentWords; i++) {
                getResources().getSentimentWords()
                        .setSentiment(i, iOriginalSentimentStrengths[i]);
            }

        }

        useWholeCorpusNotSubcorpus();
        getOptions().printClassificationOptions(wWriter, iMinImprovement,
                bUseTotalDifference, iMultiOptimisations);
        printClassificationResultsRow(iPosClassAll, iNegClassAll,
                iTrinaryOrScaleClassAll, wWriter);
    }

    /**
     * 根据单指标方法的性能变化进行字典权重优化.<br>
     * 根据单指标方法的结果(以差值大小表示)是否达到了规定的最小提升,来对现存的字典进行权重优化,如果不满足优化要求则不保存权重更改.
     * <br>UC-27 Optimise sentiment strengths of existing sentiment terms
     * <br>UC-24 Use a single positive-negative scale classification
     *
     * @param iMinImprovement 最小提升
     */
    public void optimiseDictionaryWeightingsForCorpusScale(
            final int iMinImprovement) {
        boolean bFullListChanges = true;
        int iLastScaleNumberCorrect = getClassificationScaleNumberCorrect();
        int iNewScaleNumberCorrect;
        int iTotalSentimentWords = getResources().getSentimentWords()
                .getSentimentWordCount();
        int[] iWordRand = new int[iTotalSentimentWords + 1];
        while (bFullListChanges) {
            Sort.makeRandomOrderList(iWordRand);
            bFullListChanges = false;
            for (int i = 1; i <= iTotalSentimentWords; i++) {
                int iOldTermSentimentStrength
                        = getResources().getSentimentWords()
                        .getSentiment(iWordRand[i]);
                boolean bCurrentIDChange = false;
                int iAddOneImprovement;
                int iSubtractOneImprovement;
                if (iOldTermSentimentStrength < MAX_POS - 1) {
                    getResources().getSentimentWords()
                            .setSentiment(iWordRand[i],
                                    iOldTermSentimentStrength + 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i],
                            1);
                    iNewScaleNumberCorrect
                            = getClassificationScaleNumberCorrect();
                    iAddOneImprovement = iNewScaleNumberCorrect
                            - iLastScaleNumberCorrect;
                    if (iAddOneImprovement >= iMinImprovement) {
                        bCurrentIDChange = true;
                        iLastScaleNumberCorrect += iAddOneImprovement;
                    }
                }
                if (iOldTermSentimentStrength > -MAX_NEG + 1
                        && !bCurrentIDChange) {
                    getResources().getSentimentWords()
                            .setSentiment(iWordRand[i],
                                    iOldTermSentimentStrength - 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i],
                            1);
                    iNewScaleNumberCorrect
                            = getClassificationScaleNumberCorrect();
                    iSubtractOneImprovement = iNewScaleNumberCorrect
                            - iLastScaleNumberCorrect;
                    if (iSubtractOneImprovement >= iMinImprovement) {
                        bCurrentIDChange = true;
                        iLastScaleNumberCorrect += iSubtractOneImprovement;
                    }
                }
                if (bCurrentIDChange) {
                    bFullListChanges = true;
                } else {
                    getResources().getSentimentWords()
                            .setSentiment(iWordRand[i],
                                    iOldTermSentimentStrength);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i],
                            1);
                }
            }

        }
    }

    /**
     * 根据三元分类法或二元分类法性能变化进行字典权重优化.
     * <br>根据三元分类法或二元分类法的结果(以差值大小表示)是否达到了规定的最小提升,来对现存的字典进行权重优化,
     * 如果不满足优化要求则不保存权重更改.
     * <br>UC-22 Use trinary classification (positive-negative-neutral)
     * <br>UC-23 Use binary classification (positive-negative)
     * <br>UC-27 Optimise sentiment strengths of existing sentiment terms
     *
     * @param iMinImprovement 最小提升
     */
    public void optimiseDictionaryWeightingsForCorpusTrinaryOrBinary(
            final int iMinImprovement) {
        boolean bFullListChanges = true;
        int iLastTrinaryCorrect = getClassificationTrinaryNumberCorrect();
        int iNewTrinary;
        int iTotalSentimentWords = getResources().getSentimentWords()
                .getSentimentWordCount();
        int[] iWordRand = new int[iTotalSentimentWords + 1];
        while (bFullListChanges) {
            Sort.makeRandomOrderList(iWordRand);
            bFullListChanges = false;
            for (int i = 1; i <= iTotalSentimentWords; i++) {
                int iOldSentimentStrength = getResources().getSentimentWords()
                        .getSentiment(iWordRand[i]);
                boolean bCurrentIDChange = false;
                int iAddOneImprovement;
                int iSubtractOneImprovement;
                if (iOldSentimentStrength < MAX_POS - 1) {
                    getResources().getSentimentWords()
                            .setSentiment(iWordRand[i],
                                    iOldSentimentStrength + 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i],
                            1);
                    iNewTrinary = getClassificationTrinaryNumberCorrect();
                    iAddOneImprovement = iNewTrinary - iLastTrinaryCorrect;
                    if (iAddOneImprovement >= iMinImprovement) {
                        bCurrentIDChange = true;
                        iLastTrinaryCorrect += iAddOneImprovement;
                    }
                }
                if (iOldSentimentStrength > -MAX_NEG + 1 && !bCurrentIDChange) {
                    getResources().getSentimentWords()
                            .setSentiment(iWordRand[i],
                                    iOldSentimentStrength - 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i],
                            1);
                    iNewTrinary = getClassificationTrinaryNumberCorrect();
                    iSubtractOneImprovement = iNewTrinary - iLastTrinaryCorrect;
                    if (iSubtractOneImprovement >= iMinImprovement) {
                        bCurrentIDChange = true;
                        iLastTrinaryCorrect += iSubtractOneImprovement;
                    }
                }
                if (bCurrentIDChange) {
                    bFullListChanges = true;
                } else {
                    getResources().getSentimentWords()
                            .setSentiment(iWordRand[i], iOldSentimentStrength);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i],
                            1);
                }
            }

        }
    }

    /**
     * 根据积极/消极情绪评分进行字典权重优化.
     * <br>根据积极/消极评分的结果(以差值大小表示)是否达到了规定的最小提升,来对现存的字典进行权重优化,如果不满足优化要求则不保存权重更改.
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     * <br>UC-27 Optimise sentiment strengths of existi ng sentiment terms
     *
     * @param iMinImprovement     最小提升
     * @param bUseTotalDifference 是否要累加差值
     */
    public void optimiseDictionaryWeightingsForCorpusPosNeg(
            final int iMinImprovement, final boolean bUseTotalDifference) {
        boolean bFullListChanges = true;
        int iLastPos = 0;
        int iLastNeg = 0;
        int iLastPosTotalDiff = 0;
        int iLastNegTotalDiff = 0;
        if (bUseTotalDifference) {
            iLastPosTotalDiff = getClassificationPositiveTotalDifference();
            iLastNegTotalDiff = getClassificationNegativeTotalDifference();
        } else {
            iLastPos = getClassificationPositiveNumberCorrect();
            iLastNeg = getClassificationNegativeNumberCorrect();
        }
        int iNewPos = 0;
        int iNewNeg = 0;
        int iNewPosTotalDiff = 0;
        int iNewNegTotalDiff = 0;
        int iTotalSentimentWords = getResources().getSentimentWords()
                .getSentimentWordCount();
        int[] iWordRand = new int[iTotalSentimentWords + 1];
        while (bFullListChanges) {
            Sort.makeRandomOrderList(iWordRand);
            bFullListChanges = false;
            for (int i = 1; i <= iTotalSentimentWords; i++) {
                int iOldSentimentStrength = getResources().getSentimentWords()
                        .getSentiment(iWordRand[i]);
                boolean bCurrentIDChange = false;
                if (iOldSentimentStrength < MAX_POS - 1) {
                    getResources().getSentimentWords()
                            .setSentiment(iWordRand[i],
                                    iOldSentimentStrength + 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i],
                            1);
                    if (bUseTotalDifference) {
                        iNewPosTotalDiff
                                = getClassificationPositiveTotalDifference();
                        iNewNegTotalDiff
                                = getClassificationNegativeTotalDifference();
                        if (((iNewPosTotalDiff - iLastPosTotalDiff)
                                + iNewNegTotalDiff) - iLastNegTotalDiff
                                <= -iMinImprovement) {
                            bCurrentIDChange = true;
                        }
                    } else {
                        iNewPos = getClassificationPositiveNumberCorrect();
                        iNewNeg = getClassificationNegativeNumberCorrect();
                        if (((iNewPos - iLastPos) + iNewNeg) - iLastNeg
                                >= iMinImprovement) {
                            bCurrentIDChange = true;
                        }
                    }
                }
                if (iOldSentimentStrength > -MAX_NEG + 1 && !bCurrentIDChange) {
                    getResources().getSentimentWords()
                            .setSentiment(iWordRand[i],
                                    iOldSentimentStrength - 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i],
                            1);
                    if (bUseTotalDifference) {
                        iNewPosTotalDiff
                                = getClassificationPositiveTotalDifference();
                        iNewNegTotalDiff
                                = getClassificationNegativeTotalDifference();
                        if (((iNewPosTotalDiff - iLastPosTotalDiff)
                                + iNewNegTotalDiff) - iLastNegTotalDiff
                                <= -iMinImprovement) {
                            bCurrentIDChange = true;
                        }
                    } else {
                        iNewPos = getClassificationPositiveNumberCorrect();
                        iNewNeg = getClassificationNegativeNumberCorrect();
                        if (((iNewPos - iLastPos) + iNewNeg) - iLastNeg
                                >= iMinImprovement) {
                            bCurrentIDChange = true;
                        }
                    }
                }
                if (bCurrentIDChange) {
                    if (bUseTotalDifference) {
                        iLastNegTotalDiff = iNewNegTotalDiff;
                        iLastPosTotalDiff = iNewPosTotalDiff;
                    } else {
                        iLastNeg = iNewNeg;
                        iLastPos = iNewPos;
                    }
                    bFullListChanges = true;
                } else {
                    getResources().getSentimentWords()
                            .setSentiment(iWordRand[i], iOldSentimentStrength);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i],
                            1);
                }
            }

        }
    }

    /**
     * 总结十折交叉验证的结果. 从输入文件中读取十折交叉验证的结果, 通过对比实际结果和预测结果后将对比结果写入输出文件中.
     * <br>UC-29 Machine learning evaluations
     *
     * @param sInputFile  输入文件
     * @param sOutputFile 输出文件
     */
    public void summariseMultiple10FoldValidations(final String sInputFile,
                                                   final String sOutputFile) {
        int iDataRows = ROW;
        int iLastOptionCol = COL;
        BufferedReader rResults;
        BufferedWriter wSummary;
        String sLine = null;
        String[] sPrevData = null;
        String[] sData;
        float[] total = new float[iDataRows];
        int iRows = 0;
        int i = 0;
        try {
            rResults = new BufferedReader(new FileReader(sInputFile));
            wSummary = new BufferedWriter(new FileWriter(sOutputFile));
            sLine = rResults.readLine();
            wSummary.write(sLine + "\tNumber\n");
            while (rResults.ready()) {
                sLine = rResults.readLine();
                sData = sLine.split("\t");
                boolean bMatching = true;
                if (sPrevData != null) {
                    for (i = 0; i < iLastOptionCol; i++) {
                        if (!sData[i].equals(sPrevData[i])) {
                            bMatching = false;
                        }
                    }
                }

                if (!bMatching) {
                    for (i = 0; i < iLastOptionCol; i++) {
                        wSummary.write(sPrevData[i] + "\t");
                    }

                    for (i = 0; i < iDataRows; i++) {
                        wSummary.write(total[i] / (float) iRows + "\t");
                    }

                    wSummary.write(iRows + "\n");
                    for (i = 0; i < iDataRows; i++) {
                        total[i] = 0.0F;
                    }

                    iRows = 0;
                }
                for (i = iLastOptionCol; i < iLastOptionCol + iDataRows; i++) {
                    try {
                        total[i - iLastOptionCol] += Float.parseFloat(sData[i]);
                    } catch (Exception e) {
                        total[i - iLastOptionCol] += INVALID_TEN_CROSS;
                    }
                }

                iRows++;
                sPrevData = sLine.split("\t");
            }
            for (i = 0; i < iLastOptionCol; i++) {
                wSummary.write(sPrevData[i] + "\t");
            }

            for (i = 0; i < iDataRows; i++) {
                wSummary.write(total[i] / (float) iRows + "\t");
            }

            wSummary.write(iRows + "\n");
            wSummary.close();
            rResults.close();
        } catch (IOException e) {
            System.out.println(
                    "SummariseMultiple10FoldValidations: File I/O error: "
                            + sInputFile);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(
                    "SummariseMultiple10FoldValidations: Error at line: "
                            + sLine);
            System.out.println("Value of i: " + i);
            e.printStackTrace();
        }
    }

    /**
     * 获取分类器选项..
     *
     * @return 分类器选项.
     */
    public ClassificationOptions getOptions() {
        return options;
    }

    /**
     * 设置分类器选项.
     *
     * @param classificationOptions 分类器选项.
     */
    public void setOptions(final ClassificationOptions classificationOptions) {
        this.options = classificationOptions;
    }

    /**
     * 获取分类器资源.
     *
     * @return 分类器资源.
     */
    public ClassificationResources getResources() {
        return resources;
    }

    /**
     * 设置分类器资源.
     *
     * @param classificationResources 分类器资源.
     */
    public void setResources(
            final ClassificationResources classificationResources) {
        this.resources = classificationResources;
    }
}
