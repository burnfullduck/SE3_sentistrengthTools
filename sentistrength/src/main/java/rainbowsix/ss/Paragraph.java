package rainbowsix.ss;

import java.util.Random;

import rainbowsix.utilities.Sort;
import rainbowsix.utilities.StringIndex;

/**
 * 段落段.
 * <br>
 * 初始化段落，计算段落情绪得分。<br> UC-11 Classify a single text<br> UC-12 Classify all lines
 * of text in a file for sentiment [includes accuracy evaluations]<br> UC-21
 * Classify positive (1 to 5) and negative (-1 to -5) sentiment strength
 * separately<br> UC-22 Use trinary classification
 * (positive-negative-neutral)<br> UC-23 Use binary classification
 * (positive-negative)<br> UC-24 Use a single positive-negative scale
 * classification<br>
 *
 * @author 詹美瑛
 */
public class Paragraph {
    /**
     * 由句子计算段落情感强度的调整因子.
     */
    private static final double ADJUST_FACTOR = 0.5D;
    /**
     * 位置变化的因子.
     */
    private static final int POSITION_FACTOR = 3;
    /**
     * 结果分类的阈值.
     */
    private static final double THRESHOLD = 0.5D;
    /**
     * 随机数产生.
     */
    private final Random generator = new Random();
    /**
     * 段落中的句子.
     */
    private Sentence[] sentence;
    /**
     * 段落中句子的数目.
     */
    private int igSentenceCount = 0;
    /**
     * 段落中情绪词的索引id列表.
     */
    private int[] igSentimentIDList;
    /**
     * 段落中情绪词的索引列表的大小.
     */
    private int igSentimentIDListCount = 0;
    /**
     * 段落中情绪词的索引列表是否已建立.
     */
    private boolean bSentimentIDListMade = false;
    /**
     * 积极情感强度.
     */
    private int igPositiveSentiment = 0;
    /**
     * 消极情感强度.
     */
    private int igNegativeSentiment = 0;
    /**
     * 三元检测结果.
     */
    private int igTrinarySentiment = 0;
    /**
     * Scale检测结果.
     */
    private int igScaleSentiment = 0;
    /**
     * 文本分类器选项.
     */
    private ClassificationOptions options;
    /**
     * 分类的理由.
     */
    private String sgClassificationRationale = "";

    /**
     * 将段落加入积极和消极值的索引列表.
     *
     * @param unusedTermsClassificationIndex 索引列表
     * @param iCorrectPosClass               实际的积极强度
     * @param iEstPosClass                   估计的积极强度
     * @param iCorrectNegClass               实际的消极强度
     * @param iEstNegClass                   估计的消极强度
     */
    public void addParagraphToIndexWithPosNegValues(
            final UnusedTermsClassificationIndex unusedTermsClassificationIndex,
            final int iCorrectPosClass, final int iEstPosClass,
            final int iCorrectNegClass, final int iEstNegClass) {
        for (int i = 1; i <= this.igSentenceCount; ++i) {
            this.sentence[i].addSentenceToIndex(unusedTermsClassificationIndex);
        }
        unusedTermsClassificationIndex.addNewIndexToMainIndexWithPosNegValues(
                iCorrectPosClass, iEstPosClass, iCorrectNegClass, iEstNegClass);
    }

    /**
     * 将段落加入单指标值的索引列表.
     *
     * @param unusedTermsClassificationIndex 索引列表
     * @param iCorrectScaleClass             正确的规模值
     * @param iEstScaleClass                 估计的规模值
     */
    public void addParagraphToIndexWithScaleValues(
            final UnusedTermsClassificationIndex unusedTermsClassificationIndex,
            final int iCorrectScaleClass, final int iEstScaleClass) {
        for (int i = 1; i <= this.igSentenceCount; ++i) {
            this.sentence[i].addSentenceToIndex(unusedTermsClassificationIndex);
        }

        unusedTermsClassificationIndex.addNewIndexToMainIndexWithScaleValues(
                iCorrectScaleClass, iEstScaleClass);
    }

    /**
     * 将段落加入二元值的索引列表.
     * <br>将段落按句子加入索引列表中，段落的预测结果加入二元值索引列表中。
     *
     * @param unusedTermsClassificationIndex 索引列表
     * @param iCorrectBinaryClass            实际的二元分类值
     * @param iEstBinaryClass                估计的二元分类值
     */
    public void addParagraphToIndexWithBinaryValues(
            final UnusedTermsClassificationIndex unusedTermsClassificationIndex,
            final int iCorrectBinaryClass, final int iEstBinaryClass) {
        for (int i = 1; i <= this.igSentenceCount; ++i) {
            this.sentence[i].addSentenceToIndex(unusedTermsClassificationIndex);
        }


        unusedTermsClassificationIndex.addNewIndexToMainIndexWithBinaryValues(
                iCorrectBinaryClass, iEstBinaryClass);
    }

    /**
     * 将段落加入字符串索引列表.
     * <br>将整个段落以句子为单位加入索引列表中。
     *
     * @param stringIndex        字符串索引列表
     * @param textParsingOptions 分本分析的可选设置
     * @param bRecordCount       加入后是否增加个数
     * @param bArffIndex         是否是Arff结构
     * @return 加入索引中的段落中术语的总数
     */
    public int addToStringIndex(final StringIndex stringIndex,
                                final TextParsingOptions textParsingOptions,
                                final boolean bRecordCount,
                                final boolean bArffIndex) {
        int iTermsChecked = 0;

        for (int i = 1; i <= this.igSentenceCount; ++i) {
            iTermsChecked += this.sentence[i].addToStringIndex(stringIndex,
                    textParsingOptions, bRecordCount, bArffIndex);
        }

        return iTermsChecked;
    }

    /**
     * 将段落加入三元值索引列表中.
     * <br>将段落按句子加入索引列表中，段落的预测结果加入三元值索引列表中。
     *
     * @param unusedTermsClassificationIndex 索引列表
     * @param iCorrectTrinaryClass           实际的三元分类结果
     * @param iEstTrinaryClass               估计的三元分类结果
     */
    public void addParagraphToIndexWithTrinaryValues(
            final UnusedTermsClassificationIndex unusedTermsClassificationIndex,
            final int iCorrectTrinaryClass, final int iEstTrinaryClass) {
        for (int i = 1; i <= this.igSentenceCount; ++i) {
            this.sentence[i].addSentenceToIndex(unusedTermsClassificationIndex);
        }
        unusedTermsClassificationIndex.addNewIndexToMainIndexWithTrinaryValues(
                iCorrectTrinaryClass, iEstTrinaryClass);
    }


    /**
     * 设置段落，进行段落的初始化.
     * <br>读取整个段落的字符串，对其进行解析，初始化段落的内容以及其他成员。
     *
     * @param sParagraph               段落的字符串
     * @param classResources           分类所需资源
     * @param newClassificationOptions 可选设置
     */
    public void setParagraph(final String sParagraph,
                             final ClassificationResources classResources,
                             final ClassificationOptions newClassificationOptions) {
        String paragraph = sParagraph;
        this.options = newClassificationOptions;
        if (paragraph.contains("\"")) {
            paragraph = paragraph.replace("\"", "'");
        }

        int iSentenceEnds = 2;
        int iPos = 0;

        while (iPos >= 0 && iPos < paragraph.length()) {
            iPos = paragraph.indexOf("<br>", iPos);
            if (iPos >= 0) {
                iPos += POSITION_FACTOR;
                ++iSentenceEnds;
            }
        }

        iPos = 0;

        while (iPos >= 0 && iPos < paragraph.length()) {
            iPos = paragraph.indexOf(".", iPos);
            if (iPos >= 0) {
                ++iPos;
                ++iSentenceEnds;
            }
        }

        iPos = 0;

        while (iPos >= 0 && iPos < paragraph.length()) {
            iPos = paragraph.indexOf("!", iPos);
            if (iPos >= 0) {
                ++iPos;
                ++iSentenceEnds;
            }
        }

        iPos = 0;

        while (iPos >= 0 && iPos < paragraph.length()) {
            iPos = paragraph.indexOf("?", iPos);
            if (iPos >= 0) {
                ++iPos;
                ++iSentenceEnds;
            }
        }

        this.sentence = new Sentence[iSentenceEnds];
        this.igSentenceCount = 0;
        int iLastSentenceEnd = -1;
        boolean bPunctuationIndicatesSentenceEnd = false;
        int iNextBr = paragraph.indexOf("<br>");
        String sNextSentence = "";

        for (iPos = 0; iPos < paragraph.length(); ++iPos) {
            String sNextChar = paragraph.substring(iPos, iPos + 1);
            if (iPos == paragraph.length() - 1) {
                sNextSentence = paragraph.substring(iLastSentenceEnd + 1);
            } else if (iPos == iNextBr) {
                sNextSentence = paragraph.substring(iLastSentenceEnd + 1, iPos);
                iLastSentenceEnd = iPos + POSITION_FACTOR;
                iNextBr = paragraph.indexOf("<br>", iNextBr + 2);
            } else if (this.bIsSentenceEndPunctuation(sNextChar)) {
                bPunctuationIndicatesSentenceEnd = true;
            } else if (sNextChar.compareTo(" ") == 0) {
                if (bPunctuationIndicatesSentenceEnd) {
                    sNextSentence = paragraph.substring(iLastSentenceEnd + 1,
                            iPos);
                    iLastSentenceEnd = iPos;
                }
            } else if (this.bIsAlphanumeric(sNextChar)
                    && bPunctuationIndicatesSentenceEnd) {
                sNextSentence = paragraph.substring(iLastSentenceEnd + 1, iPos);
                iLastSentenceEnd = iPos - 1;
            }

            if (!sNextSentence.equals("")) {
                ++this.igSentenceCount;
                this.sentence[this.igSentenceCount] = new Sentence();
                this.sentence[this.igSentenceCount].setSentence(sNextSentence,
                        classResources, this.options);
                sNextSentence = "";
                bPunctuationIndicatesSentenceEnd = false;
            }
        }

    }

    /**
     * 得到段落的情绪词id列表.
     *
     * @return 句子的id列表
     */
    public int[] getSentimentIDList() {
        if (!this.bSentimentIDListMade) {
            this.makeSentimentIDList();
        }

        return this.igSentimentIDList;
    }

    /**
     * 得到分类的理由.
     *
     * @return 分类的理由
     */
    public String getClassificationRationale() {
        return this.sgClassificationRationale;
    }

    /**
     * 生成段落的情绪词的id列表.
     * <br>解析段落中每个句子的情绪词id列表生成整个段落的情绪词id列表。
     */
    public void makeSentimentIDList() {
        boolean bIsDuplicate;
        this.igSentimentIDListCount = 0;

        int i;
        for (i = 1; i <= this.igSentenceCount; ++i) {
            if (this.sentence[i].getSentimentIDList() != null) {
                this.igSentimentIDListCount
                        += this.sentence[i].getSentimentIDList().length;
            }
        }

        if (this.igSentimentIDListCount > 0) {
            this.igSentimentIDList = new int[this.igSentimentIDListCount + 1];
            this.igSentimentIDListCount = 0;

            for (i = 1; i <= this.igSentenceCount; ++i) {
                int[] sentenceIDList = this.sentence[i].getSentimentIDList();
                if (sentenceIDList != null) {
                    for (int j = 1; j < sentenceIDList.length; ++j) {
                        if (sentenceIDList[j] != 0) {
                            bIsDuplicate = false;

                            for (int k = 1; k <= this.igSentimentIDListCount;
                                 ++k) {
                                if (sentenceIDList[j]
                                        == this.igSentimentIDList[k]) {
                                    bIsDuplicate = true;
                                    break;
                                }
                            }

                            if (!bIsDuplicate) {
                                this.igSentimentIDList[++this.igSentimentIDListCount]
                                        = sentenceIDList[j];
                            }
                        }
                    }
                }
            }

            Sort.quickSortInt(this.igSentimentIDList, 1,
                    this.igSentimentIDListCount);
        }

        this.bSentimentIDListMade = true;
    }

    /**
     * 得到打标签的段落内容.
     * <br>将段落中的每个句子打上标签，从而得到打上标签的段落。
     *
     * @return 被打标签的段落的字符串
     */
    public String getTaggedParagraph() {
        StringBuilder sTagged = new StringBuilder();

        for (int i = 1; i <= this.igSentenceCount; ++i) {
            sTagged.append(this.sentence[i].getTaggedSentence());
        }

        return sTagged.toString();
    }

    /**
     * 得到翻译的段落.
     * <br>翻译段落中的每一个句子，从而得到翻译的段落
     *
     * @return 翻译的段落的字符串
     */
    public String getTranslatedParagraph() {
        StringBuilder sTranslated = new StringBuilder();

        for (int i = 1; i <= this.igSentenceCount; ++i) {
            sTranslated.append(this.sentence[i].getTranslatedSentence());
        }

        return sTranslated.toString();
    }

    /**
     * 重新计算段落的分数.
     */
    public void recalculateParagraphSentimentScores() {
        for (int iSentence = 1; iSentence <= this.igSentenceCount;
             ++iSentence) {
            this.sentence[iSentence].recalculateSentenceSentimentScore();
        }

        this.calculateParagraphSentimentScores();
    }


    /**
     * 强度改变时重新分类已分类的段落.
     * <br>强度改变时重新计算段落的分数。
     *
     * @param iSentimentWordID 强度改变的术语的id
     */
    public void reClassifyClassifiedParagraphForSentimentChange(
            final int iSentimentWordID) {
        if (this.igNegativeSentiment == 0) {
            this.calculateParagraphSentimentScores();
        } else {
            if (!this.bSentimentIDListMade) {
                this.makeSentimentIDList();
            }

            if (this.igSentimentIDListCount != 0) {
                if (Sort.findIntPositionInSortedArray(iSentimentWordID,
                        this.igSentimentIDList, 1, this.igSentimentIDListCount)
                        >= 0) {
                    for (int iSentence = 1; iSentence <= this.igSentenceCount;
                         ++iSentence) {
                        this.sentence[iSentence].reClassifyClassifiedSentenceForSentimentChange(
                                iSentimentWordID);
                    }

                    this.calculateParagraphSentimentScores();
                }

            }
        }
    }

    /**
     * 获得段落的积极情感强度.
     *
     * @return 段落的积极情感强度
     */
    public int getParagraphPositiveSentiment() {
        if (this.igPositiveSentiment == 0) {
            this.calculateParagraphSentimentScores();
        }

        return this.igPositiveSentiment;
    }

    /**
     * 得到段落的消极情感强度.
     *
     * @return 段落的消极情感强度
     */
    public int getParagraphNegativeSentiment() {
        if (this.igNegativeSentiment == 0) {
            this.calculateParagraphSentimentScores();
        }

        return this.igNegativeSentiment;
    }

    /**
     * 得到段落的三元分类结果.
     *
     * @return 段落的三元分类结果
     */
    public int getParagraphTrinarySentiment() {
        if (this.igNegativeSentiment == 0) {
            this.calculateParagraphSentimentScores();
        }

        return this.igTrinarySentiment;
    }

    /**
     * 得到段落的单指标方法结果.
     *
     * @return 段落的单指标方法结果
     */
    public int getParagraphScaleSentiment() {
        if (this.igNegativeSentiment == 0) {
            this.calculateParagraphSentimentScores();
        }

        return this.igScaleSentiment;
    }

    /**
     * 判断句子是否以标点结尾.
     *
     * @param sChar 句子的结尾符号
     * @return 是否以标点结尾
     */
    private boolean bIsSentenceEndPunctuation(final String sChar) {
        return sChar.compareTo(".") == 0 || sChar.compareTo("!") == 0
                || sChar.compareTo("?") == 0;
    }

    /**
     * 判断字符是否为数字或字母.
     *
     * @param sChar 要判断的字符
     * @return 是否为数字或字母
     */
    private boolean bIsAlphanumeric(final String sChar) {
        return sChar.compareToIgnoreCase("a") >= 0
                && sChar.compareToIgnoreCase("z") <= 0
                || sChar.compareTo("0") >= 0 && sChar.compareTo("9") <= 0
                || sChar.compareTo("$") == 0 || sChar.compareTo("£") == 0
                || sChar.compareTo("'") == 0;
    }


    /**
     * 计算段落的情感分数.
     * <br>根据分类器选项，计算不同模式下段落的分数。<br>
     * UC-11 Classify a single text<br> UC-12 Classify all lines of text in a
     * file for sentiment [includes accuracy evaluations]<br> UC-21 Classify
     * positive (1 to 5) and negative (-1 to -5) sentiment strength
     * separately<br> UC-22 Use trinary classification
     * (positive-negative-neutral)<br> UC-23 Use binary classification
     * (positive-negative)<br> UC-24 Use a single positive-negative scale
     * classification<br>
     */
    private void calculateParagraphSentimentScores() {
        this.igPositiveSentiment = 1;
        this.igNegativeSentiment = -1;
        this.igTrinarySentiment = 0;
        if (this.options.isBgExplainClassification()
                && this.sgClassificationRationale.length() > 0) {
            this.sgClassificationRationale = "";
        }

        int iPosTotal = 0;
        int iPosMax = 0;
        int iNegTotal = 0;
        int iNegMax = 0;
        int iPosTemp;
        int iNegTemp;
        int iSentencesUsed = 0;
        if (this.igSentenceCount != 0) {
            int iNegTot;
            for (iNegTot = 1; iNegTot <= this.igSentenceCount; ++iNegTot) {
                iNegTemp
                        = this.sentence[iNegTot].getSentenceNegativeSentiment();
                iPosTemp
                        = this.sentence[iNegTot].getSentencePositiveSentiment();
                if (iNegTemp != 0 || iPosTemp != 0) {
                    iNegTotal += iNegTemp;
                    ++iSentencesUsed;
                    if (iNegMax > iNegTemp) {
                        iNegMax = iNegTemp;
                    }

                    iPosTotal += iPosTemp;
                    if (iPosMax < iPosTemp) {
                        iPosMax = iPosTemp;
                    }
                }

                if (this.options.isBgExplainClassification()) {
                    this.sgClassificationRationale =
                            this.sgClassificationRationale
                                    + this.sentence[iNegTot].getClassificationRationale()
                                    + " ";
                }
            }

            int var10000;
            if (iNegTotal == 0) {
                var10000 = this.options.getIgEmotionParagraphCombineMethod();
                this.options.getClass();
                if (var10000 != 2) {
                    this.igPositiveSentiment = 0;
                    this.igNegativeSentiment = 0;
                    this.igTrinarySentiment = this.binarySelectionTieBreaker();
                    return;
                }
            }

            var10000 = this.options.getIgEmotionParagraphCombineMethod();
            this.options.getClass();
            if (var10000 == 1) {
                this.igPositiveSentiment = (int) (
                        (double) ((float) iPosTotal / (float) iSentencesUsed)
                                + ADJUST_FACTOR);
                this.igNegativeSentiment = (int) (
                        (double) ((float) iNegTotal / (float) iSentencesUsed)
                                - ADJUST_FACTOR);

                if (this.options.isBgExplainClassification()) {
                    this.sgClassificationRationale =
                            this.sgClassificationRationale
                                    + "[result = average (" + iPosTotal
                                    + " and " + iNegTotal + ") of "
                                    + iSentencesUsed + " sentences]";
                }
            } else {
                this.options.getClass();
                if (var10000 == 2) {
                    this.igPositiveSentiment = iPosTotal;
                    this.igNegativeSentiment = iNegTotal;
                    if (this.options.isBgExplainClassification()) {
                        this.sgClassificationRationale =
                                this.sgClassificationRationale
                                        + "[result: total positive; total "
                                        + "negative]";
                    }
                } else {
                    this.igPositiveSentiment = iPosMax;
                    this.igNegativeSentiment = iNegMax;
                    if (this.options.isBgExplainClassification()) {
                        this.sgClassificationRationale =
                                this.sgClassificationRationale
                                        + "[result: max + and - of any "
                                        + "sentence]";
                    }
                }
            }

            this.options.getClass();
            if (var10000 != 2) {
                if (this.igPositiveSentiment == 0) {
                    this.igPositiveSentiment = 1;
                }

                if (this.igNegativeSentiment == 0) {
                    this.igNegativeSentiment = -1;
                }
            }

            if (this.options.isBgScaleMode()) {
                this.igScaleSentiment = this.igPositiveSentiment
                        + this.igNegativeSentiment;
                if (this.options.isBgExplainClassification()) {
                    this.sgClassificationRationale =
                            this.sgClassificationRationale
                                    + "[scale result = sum of pos and neg "
                                    + "scores]";
                }

            } else {
                this.options.getClass();
                if (var10000 == 2) {
                    if (this.igPositiveSentiment == 0
                            && this.igNegativeSentiment == 0) {
                        if (this.options.isBgBinaryVersionOfTrinaryMode()) {
                            this.igTrinarySentiment
                                    =
                                    this.options.getIgDefaultBinaryClassification();
                            if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + "[binary result set to "
                                                + "default value]";
                            }
                        } else {
                            this.igTrinarySentiment = 0;
                            if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + "[trinary result 0 as pos=1, "
                                                + "neg=-1]";
                            }
                        }
                    } else {
                        if ((float) this.igPositiveSentiment
                                > this.options.getFgNegativeSentimentMultiplier()
                                        * (float) (-this.igNegativeSentiment)) {
                            this.igTrinarySentiment = 1;
                            if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + "[overall result 1 as pos > "
                                                + "-neg * "
                                                + this.options.getFgNegativeSentimentMultiplier()
                                                + "]";
                            }

                            return;
                        }

                        if ((float) this.igPositiveSentiment
                                < this.options.getFgNegativeSentimentMultiplier()
                                        * (float) (-this.igNegativeSentiment)) {
                            this.igTrinarySentiment = -1;
                            if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + "[overall result -1 as pos < "
                                                + "-neg * "
                                                + this.options.getFgNegativeSentimentMultiplier()
                                                + "]";
                            }

                            return;
                        }

                        if (this.options.isBgBinaryVersionOfTrinaryMode()) {
                            this.igTrinarySentiment
                                    =
                                    this.options.getIgDefaultBinaryClassification();
                            if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + "[binary result = default "
                                                + "value as pos = -neg * "
                                                + this.options.getFgNegativeSentimentMultiplier()
                                                + "]";
                            }
                        } else {
                            this.igTrinarySentiment = 0;
                            if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + "[trinary result = 0 as pos ="
                                                + " -neg * "
                                                + this.options.getFgNegativeSentimentMultiplier()
                                                + "]";
                            }
                        }
                    }
                } else {
                    if (this.igPositiveSentiment == 1
                            && this.igNegativeSentiment == -1) {
                        if (this.options.isBgBinaryVersionOfTrinaryMode()) {
                            this.igTrinarySentiment
                                    = this.binarySelectionTieBreaker();
                            if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + "[binary result = default "
                                                + "value as pos=1 neg=-1]";
                            }
                        } else {
                            this.igTrinarySentiment = 0;
                            if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + "[trinary result = 0 as pos=1"
                                                + " neg=-1]";
                            }
                        }

                        return;
                    }

                    if (this.igPositiveSentiment > -this.igNegativeSentiment) {
                        this.igTrinarySentiment = 1;
                        if (this.options.isBgExplainClassification()) {
                            this.sgClassificationRationale =
                                    this.sgClassificationRationale
                                            + "[overall result = 1 as "
                                            + "pos>-neg]";
                        }

                        return;
                    }

                    if (this.igPositiveSentiment < -this.igNegativeSentiment) {
                        this.igTrinarySentiment = -1;
                        if (this.options.isBgExplainClassification()) {
                            this.sgClassificationRationale =
                                    this.sgClassificationRationale
                                            + "[overall result = -1 as "
                                            + "pos<-neg]";
                        }

                        return;
                    }

                    iNegTot = 0;
                    int iPosTot = 0;

                    for (int iSentence = 1; iSentence <= this.igSentenceCount;
                         ++iSentence) {
                        iNegTot +=
                                this.sentence[iSentence].
                                        getSentenceNegativeSentiment();
                        iPosTot +=
                                this.sentence[iSentence].
                                        getSentencePositiveSentiment();
                    }

                    if (this.options.isBgBinaryVersionOfTrinaryMode()
                            && iPosTot == -iNegTot) {
                        this.igTrinarySentiment
                                = this.binarySelectionTieBreaker();
                        if (this.options.isBgExplainClassification()) {
                            this.sgClassificationRationale =
                                    this.sgClassificationRationale
                                            + "[binary result = default as "
                                            + "posSentenceTotal"
                                            + ">-negSentenceTotal]";
                        }
                    } else {
                        if (this.options.isBgExplainClassification()) {
                            this.sgClassificationRationale =
                                    this.sgClassificationRationale
                                            + "[overall result = largest of "
                                            + "posSentenceTotal, "
                                            + "negSentenceTotal]";
                        }

                        if (iPosTot > -iNegTot) {
                            this.igTrinarySentiment = 1;
                        } else {
                            this.igTrinarySentiment = -1;
                        }
                    }
                }

            }
        }
    }

    /**
     * 根据二元结果的默认值获得三元结果的默认值.
     *
     * @return 三元的分类结果
     */
    private int binarySelectionTieBreaker() {
        if (this.options.getIgDefaultBinaryClassification() != 1
                && this.options.getIgDefaultBinaryClassification() != -1) {
            return this.generator.nextDouble() > THRESHOLD ? 1 : -1;
        } else {
            return this.options.getIgDefaultBinaryClassification();
        }
    }
}
