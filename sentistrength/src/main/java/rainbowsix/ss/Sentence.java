//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package rainbowsix.ss;

import rainbowsix.utilities.Sort;
import rainbowsix.utilities.StringIndex;
import rainbowsix.wka.Arff;

/**
 * 句子类.
 * <br>
 * 以句子为单位保存并评估一系列术语的分数.并有各种评估和特殊判断的方法,同时记录评估的原因.<br>
 * UC-2 Assigning Sentiment Scores for Phrases<br>
 * UC-4 Booster Word Rule <br>
 * UC-5 Negating Word Rule<br>
 * UC-6 Repeated Letter Rule<br>
 * UC-8 Exclamation Mark Rule<br>
 * UC-9 Repeated Punctuation Rule<br>
 * UC-10 Negative Sentiment Ignored in Questions<br>
 * UC-25 Explain the classification<br>
 *
 * @author 注释添加: 胡才轩
 */
public class Sentence {
    /**
     * 句子包含的术语列表.
     */
    private Term[] term;
    /**
     * 术语后是否含有空格.
     */
    private boolean[] bgSpaceAfterTerm;
    /**
     * 术语的数量.
     */
    private int igTermCount = 0;

    /**
     * 获取术语数量.
     *
     * @return 术语数量
     */
    public int getIgTermCount() {
        return igTermCount;
    }

    /**
     * 获取情绪词数量.
     *
     * @return 情绪词数量
     */
    public int getIgSentiCount() {
        return igSentiCount;
    }

    /**
     * 情绪词数量.
     */
    private int igSentiCount = 0;
    /**
     * 积极情绪得分.
     */
    private int igPositiveSentiment = 0;
    /**
     * 消极情绪得分.
     */
    private int igNegativeSentiment = 0;
    /**
     * 没什么可分类了.
     */
    private boolean bgNothingToClassify = true;
    /**
     * 分类器资源.
     */
    private ClassificationResources resources;
    /**
     * 分类器选项.
     */
    private ClassificationOptions options;
    /**
     * 情绪词ID列表.
     */
    private int[] igSentimentIDList;
    /**
     * 情绪词ID列表计数.
     */
    private int igSentimentIDListCount = 0;
    /**
     * 情绪词ID列表是否已经创建.
     */
    private boolean bSentimentIDListMade = false;
    /**
     * 是否包含术语的布尔值列表.
     */
    private boolean[] bgIncludeTerm;
    /**
     * 是否使用习语规则.
     */
    private boolean bgIdiomsApplied = false;
    /**
     * 是否使用对象评估规则.
     */
    private boolean bgObjectEvaluationsApplied = false;
    /**
     * 分类理由.
     * <br>解释影响术语或句子得分的详细原因, 包括每次使用规则的说明.
     */
    private String sgClassificationRationale = "";
    /**
     * 术语开始有效的位置的默认值.
     */
    private static final int TERM_SINCE_VALID_INITIAL = 100000;
    /**
     * 最小文本长度.
     */
    private static final int MIN_TEXT_LENGTH = 3;
    /**
     * 增强词的影响.
     */
    private static final double BOOST_INFLUENCE = 0.6D;
    /**
     * 积极情绪得分修正.
     */
    private static final double POSITIVE_AMEND = 0.45D;
    /**
     * 消极情绪得分修正.
     */
    private static final double NEGATIVE_AMEND = 0.55D;
    /**
     * 积极情绪得分最大值.
     */
    private static final int POSITIVE_MAX = 5;
    /**
     * 消极情绪得分最大值.
     */
    private static final int NEGATIVE_MAX = -5;
    /**
     * 讽刺词位置.
     */
    private static final int IRONY_POSITION = 34;

    /**
     * 构造方法.
     */
    public Sentence() {
    }

    /**
     * 给句子添加索引.
     * <br>
     * 将句子放在给定的未使用的术语分类索引中.
     *
     * @param unusedTermClassificationIndex 空闲的术语分类索引
     */
    public void addSentenceToIndex(
            final UnusedTermsClassificationIndex unusedTermClassificationIndex) {
        for (int i = 1; i <= this.igTermCount; ++i) {
            unusedTermClassificationIndex.addTermToNewTermIndex(
                    this.term[i].getText());
        }

    }

    /**
     * 将句子中的术语添加到字符串索引中.
     * <br>
     * 根据分词选项,将该句子中的所有术语进行一定筛选后分别添加到字符串索引中,
     * 并根据计数需要决定是否计数.<br>
     *
     * @param stringIndex        现有的字符串索引数据结构.
     * @param textParsingOptions 分词选项
     * @param bRecordCount       是否需要计数
     * @param bArffIndex         Arff索引
     * @return 加入字符串索引的术语数量
     */
    public int addToStringIndex(final StringIndex stringIndex,
                                final TextParsingOptions textParsingOptions,
                                final boolean bRecordCount,
                                final boolean bArffIndex) {
        String sEncoded;
        int iStringPos;
        int iTermsChecked = 0;
        if (textParsingOptions.isBgIncludePunctuation()
                && textParsingOptions.getIgNgramSize() == 1
                && !textParsingOptions.isBgUseTranslations()
                && !textParsingOptions.isBgAddEmphasisCode()) {
            for (int i = 1; i <= this.igTermCount; ++i) {
                stringIndex.addString(this.term[i].getText(), bRecordCount);
            }

            iTermsChecked = this.igTermCount;
        } else {
            StringBuilder sText = new StringBuilder();
            int iCurrentTerm = 0;
            int iTermCount = 0;

            while (iCurrentTerm < this.igTermCount) {
                ++iCurrentTerm;
                if (textParsingOptions.isBgIncludePunctuation()
                        || !this.term[iCurrentTerm].isPunctuation()) {
                    ++iTermCount;
                    if (iTermCount > 1) {
                        sText.append(" ");
                    } else {
                        sText = new StringBuilder();
                    }

                    if (textParsingOptions.isBgUseTranslations()) {
                        sText.append(this.term[iCurrentTerm].getTranslation());
                    } else {
                        sText.append(this.term[iCurrentTerm].getOriginalText());
                    }

                    if (textParsingOptions.isBgAddEmphasisCode()
                            && this.term[iCurrentTerm].containsEmphasis()) {
                        sText.append("+");
                    }
                }

                if (iTermCount == textParsingOptions.getIgNgramSize()) {
                    if (bArffIndex) {
                        sEncoded = Arff.arffSafeWordEncode(
                                sText.toString().toLowerCase());
                        iStringPos = stringIndex.findString(sEncoded);
                        iTermCount = 0;
                        if (iStringPos > -1) {
                            stringIndex.add1ToCount(iStringPos);
                        }
                    } else {
                        stringIndex.addString(sText.toString().toLowerCase(),
                                bRecordCount);
                        iTermCount = 0;
                    }

                    iCurrentTerm += 1 - textParsingOptions.getIgNgramSize();
                    ++iTermsChecked;
                }
            }
        }

        return iTermsChecked;
    }

    /**
     * 设置句子,并初始化.
     * <br>
     * 给句子设定好分类器资源和分类器选项,然后根据选项中的相关设置,对给定的句子字符串进行分词后,分别转换为术语后存储在本对象中.
     *
     * @param sSentence                需要设置的句子
     * @param classResources           分类器资源
     * @param newClassificationOptions 分类器选项
     */
    public void setSentence(final String sSentence,
                            final ClassificationResources classResources,
                            final ClassificationOptions newClassificationOptions) {
        String sentence = sSentence;
        this.resources = classResources;
        this.options = newClassificationOptions;
        if (this.options.isBgAlwaysSplitWordsAtApostrophes()
                && sentence.contains("'")) {
            sentence = sentence.replace("'", " ");
        }

        String[] sSegmentList = sentence.split(" ");
        int iMaxTermListLength = sentence.length() + 1;
        this.term = new Term[iMaxTermListLength];
        this.bgSpaceAfterTerm = new boolean[iMaxTermListLength];
        int iPos;
        this.igTermCount = 0;

        for (String s : sSegmentList) {
            for (iPos = 0; iPos >= 0 && iPos < s.length();
                 this.bgSpaceAfterTerm[this.igTermCount] = false) {
                this.term[++this.igTermCount] = new Term();
                int iOffset
                        =
                        this.term[this.igTermCount].extractNextWordOrPunctuationOrEmoticon(
                        s.substring(iPos), this.resources, this.options);
                if (iOffset < 0) {
                    iPos = iOffset;
                } else {
                    iPos += iOffset;
                }
            }

            this.bgSpaceAfterTerm[this.igTermCount] = true;
        }

        this.bgSpaceAfterTerm[this.igTermCount] = false;
    }

    /**
     * 获取情绪词ID列表.
     * <br>
     * 如果没有生成情绪词ID列表则先生成,返回情绪词ID列表.
     *
     * @return 情绪词ID列表
     */
    public int[] getSentimentIDList() {
        if (!this.bSentimentIDListMade) {
            this.makeSentimentIDList();
        }

        return this.igSentimentIDList;
    }

    /**
     * 生成情绪ID列表.
     * <br>
     * 根据句子中的术语列表,生成对应的情绪ID列表,并按照ID大小排序,将bSentimentIDListMade置为true.
     */
    public void makeSentimentIDList() {
        int iSentimentIDTemp;
        this.igSentimentIDListCount = 0;

        int i;
        for (i = 1; i <= this.igTermCount; ++i) {
            if (this.term[i].getSentimentID() > 0) {
                ++this.igSentimentIDListCount;
            }
        }

        if (this.igSentimentIDListCount > 0) {
            this.igSentimentIDList = new int[this.igSentimentIDListCount + 1];
            this.igSentimentIDListCount = 0;

            for (i = 1; i <= this.igTermCount; ++i) {
                iSentimentIDTemp = this.term[i].getSentimentID();
                if (iSentimentIDTemp > 0) {
                    for (int j = 1; j <= this.igSentimentIDListCount; ++j) {
                        if (iSentimentIDTemp == this.igSentimentIDList[j]) {
                            iSentimentIDTemp = 0;
                            break;
                        }
                    }

                    if (iSentimentIDTemp > 0) {
                        this.igSentimentIDList[++this.igSentimentIDListCount]
                                = iSentimentIDTemp;
                    }
                }
            }

            Sort.quickSortInt(this.igSentimentIDList, 1,
                    this.igSentimentIDListCount);
        }

        this.bSentimentIDListMade = true;
    }

    /**
     * 获取带tag的句子.
     * <br>
     * 将句子中的每个术语获得其tag后,根据bgSpaceAfterTerm决定是否用空格连接.
     *
     * @return 每个术语被tag环绕后连接成的新句子, 加上换行标签
     */
    public String getTaggedSentence() {
        StringBuilder sTagged = new StringBuilder();

        for (int i = 1; i <= this.igTermCount; ++i) {
            if (this.bgSpaceAfterTerm[i]) {
                sTagged.append(this.term[i].getTag()).append(" ");
            } else {
                sTagged.append(this.term[i].getTag());
            }
        }

        return sTagged + "<br>";
    }

    /**
     * 获取分类理由.
     * <br>UC-25 Explain the classification<br>
     *
     * @return 分类理由
     */
    public String getClassificationRationale() {
        return this.sgClassificationRationale;
    }

    /**
     * 获取翻译后的句子.
     * <br>
     * 将句子中的每一个术语分别翻译后用空格连接起来,得到句子的翻译.
     *
     * @return 翻译后的句子.加上换行标签
     */
    public String getTranslatedSentence() {
        StringBuilder sTranslated = new StringBuilder();

        for (int i = 1; i <= this.igTermCount; ++i) {
            if (this.term[i].isWord()) {
                sTranslated.append(this.term[i].getTranslatedWord());
            } else if (this.term[i].isPunctuation()) {
                sTranslated.append(this.term[i].getTranslatedPunctuation());
            } else if (this.term[i].isEmoticon()) {
                sTranslated.append(this.term[i].getEmoticon());
            }

            if (this.bgSpaceAfterTerm[i]) {
                sTranslated.append(" ");
            }
        }

        return sTranslated + "<br>";
    }

    /**
     * 重新计算句子的情感得分.
     */
    public void recalculateSentenceSentimentScore() {
        this.calculateSentenceSentimentScore();
    }

    /**
     * 因为情绪改变重新分类已经分类过的句子.
     * <br>
     * 将句子制作情绪ID列表,并进行排序后,重新计算句子的情绪得分.<br>
     *
     * @param iSentimentWordID 强度词的id索引
     */
    public void reClassifyClassifiedSentenceForSentimentChange(
            final int iSentimentWordID) {
        if (this.igNegativeSentiment == 0) {
            this.calculateSentenceSentimentScore();
        } else {
            if (!this.bSentimentIDListMade) {
                this.makeSentimentIDList();
            }

            if (this.igSentimentIDListCount != 0) {
                if (Sort.findIntPositionInSortedArray(iSentimentWordID,
                        this.igSentimentIDList, 1, this.igSentimentIDListCount)
                        >= 0) {
                    this.calculateSentenceSentimentScore();
                }

            }
        }
    }

    /**
     * 获取句子的积极情绪.
     * <br>
     * 如果没有计算情绪得分则先计算情绪得分.然后返回句子的积极情绪得分.<br>
     *
     * @return 句子的积极情绪得分.
     */
    public int getSentencePositiveSentiment() {
        if (this.igPositiveSentiment == 0) {
            this.calculateSentenceSentimentScore();
        }

        return this.igPositiveSentiment;
    }

    /**
     * 获取句子的消极情绪.
     * <br>
     * 如果没有计算情绪得分则先计算情绪得分.然后返回句子的消极情绪得分.<br>
     *
     * @return 句子的消极情绪得分.
     */
    public int getSentenceNegativeSentiment() {
        if (this.igNegativeSentiment == 0) {
            this.calculateSentenceSentimentScore();
        }

        return this.igNegativeSentiment;
    }

    /**
     * 标记术语的分类有效性.
     * <br>
     * 首先标记句子是否含有术语,然后根据options的参数,忽略掉一些非关键词后,再判断是否有需要分类的术语.<br>
     */
    private void markTermsValidToClassify() {
        this.bgIncludeTerm = new boolean[this.igTermCount + 1];
        int iTermsSinceValid;
        if (this.options.isBgIgnoreSentencesWithoutKeywords()) {
            this.bgNothingToClassify = true;

            int iTerm;
            for (iTermsSinceValid = 1; iTermsSinceValid <= this.igTermCount;
                 ++iTermsSinceValid) {
                this.bgIncludeTerm[iTermsSinceValid] = false;
                if (this.term[iTermsSinceValid].isWord()) {
                    for (iTerm = 0;
                         iTerm < this.options.getSgSentimentKeyWords().length;
                         ++iTerm) {
                        if (this.term[iTermsSinceValid].matchesString(
                                this.options.getSgSentimentKeyWords()[iTerm],
                                true)) {
                            this.bgIncludeTerm[iTermsSinceValid] = true;
                            this.bgNothingToClassify = false;
                        }
                    }
                }
            }

            if (!this.bgNothingToClassify) {
                iTermsSinceValid = TERM_SINCE_VALID_INITIAL;

                for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                    if (this.bgIncludeTerm[iTerm]) {
                        iTermsSinceValid = 0;
                    } else if (iTermsSinceValid
                            < this.options.getIgWordsToIncludeAfterKeyword()) {
                        this.bgIncludeTerm[iTerm] = true;
                        if (this.term[iTerm].isWord()) {
                            ++iTermsSinceValid;
                        }
                    }
                }

                iTermsSinceValid = TERM_SINCE_VALID_INITIAL;

                for (iTerm = this.igTermCount; iTerm >= 1; --iTerm) {
                    if (this.bgIncludeTerm[iTerm]) {
                        iTermsSinceValid = 0;
                    } else if (iTermsSinceValid
                            < this.options.getIgWordsToIncludeBeforeKeyword()) {
                        this.bgIncludeTerm[iTerm] = true;
                        if (this.term[iTerm].isWord()) {
                            ++iTermsSinceValid;
                        }
                    }
                }
            }
        } else {
            for (iTermsSinceValid = 1; iTermsSinceValid <= this.igTermCount;
                 ++iTermsSinceValid) {
                this.bgIncludeTerm[iTermsSinceValid] = true;
            }

            this.bgNothingToClassify = false;
        }

    }

    /**
     * 计算句子的情绪得分.
     * <br>
     * 分别根据增强词,习语,对象评估,对每个术语(单词,标点,表情)分别进行得分计算,
     * 还需要考虑多个词的重复增强,全部大写增强,情绪词前的否定,情绪词后的否定,
     * 多个积极/消极词对得分的增强的影响.并根据是否需要说明,添加相关说明.<br>
     * UC-2 Assigning Sentiment Scores for Phrases<br>
     * UC-4 Booster Word Rule <br>
     * UC-5 Negating Word Rule<br>
     * UC-6 Repeated Letter Rule<br>
     * UC-8 Exclamation Mark Rule<br>
     * UC-9 Repeated Punctuation Rule<br>
     * UC-10 Negative Sentiment Ignored in Questions<br>
     * UC-25 Explain the classification<br>
     */
    private void calculateSentenceSentimentScore() {
        if (this.options.isBgExplainClassification()
                && this.sgClassificationRationale.length() > 0) {
            this.sgClassificationRationale = ""; // 重置分类理由
        }

        this.igNegativeSentiment = 1; //负面评分
        this.igPositiveSentiment = 1; //正面评分
        int iWordTotal = 0; //总共的词
        int iLastBoosterWordScore = 0; //最终的增强词分数
        int iTemp; //暂时
        if (this.igTermCount == 0) { //如果没有术语
            this.bgNothingToClassify = true; //设置没什么可分类的
            this.igNegativeSentiment = -1; // 负面评分和正面评分置于最小
        } else {
            this.markTermsValidToClassify(); //标记需要分类的术语
            if (this.bgNothingToClassify) { //如果没什么可分类的则把评置于最小
                this.igNegativeSentiment = -1;
                this.igPositiveSentiment = 1;
            } else {
                boolean bSentencePunctuationBoost = false; //句子标点增强
                int iWordsSinceNegative =
                        this.options.getIgMaxWordsBeforeSentimentToNegate() + 2;
                float[] fSentiment = new float[this.igTermCount + 1]; //浮点数组
                if (this.options.isBgUseIdiomLookupTable()) { //如果有习语
                    this.overrideTermStrengthsWithIdiomStrengths(false);
                }

                if (this.options.isBgUseObjectEvaluationTable()) { //如果有对象评估
                    this.overrideTermStrengthsWithObjectEvaluationStrengths(
                            false);
                }

                for (int iTerm = 1; iTerm <= this.igTermCount;
                     ++iTerm) { //遍历每个术语
                    if (this.bgIncludeTerm[iTerm]) { //如果含有术语
                        int iTermsChecked; //已经check过的术语强度
                        if (!this.term[iTerm].isWord()) { // 如果不是单词
                            if (this.term[iTerm].isEmoticon()) { //如果是表情符号
                                iTermsChecked
                                        =
                                        this.term[iTerm].getEmoticonSentimentStrength();
                                if (iTermsChecked != 0) {
                                    if (iWordTotal > 0) {
                                        fSentiment[iWordTotal]
                                                += (float) this.term[iTerm].getEmoticonSentimentStrength();
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + this.term[iTerm].getEmoticon()
                                                            + " ["
                                                            + this.term[iTerm].getEmoticonSentimentStrength()
                                                            + " emoticon] ";
                                        }
                                    } else {
                                        ++iWordTotal; //总数加一
                                        fSentiment[iWordTotal]
                                                = (float) iTermsChecked;
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + this.term[iTerm].getEmoticon()
                                                            + " ["
                                                            + this.term[iTerm].getEmoticonSentimentStrength()
                                                            + " emoticon]";
                                        }
                                    }
                                }
                            } else if (this.term[iTerm].isPunctuation()) {
                                //如果是标点
                                if (this.term[iTerm].getPunctuationEmphasisLength()
                                        >= this.options.getIgMinPunctuationWithExclamationToChangeSentenceSentiment()
                                        && this.term[iTerm].punctuationContains(
                                        "!") && iWordTotal > 0) {
                                    bSentencePunctuationBoost = true;
                                }
                                if (this.options.isBgExplainClassification()) {
                                    this.sgClassificationRationale =
                                            this.sgClassificationRationale
                                                    + this.term[iTerm].getOriginalText();
                                }
                            }
                        } else {
                            ++iWordTotal;
                            if (iTerm == 1 || !this.term[iTerm].isProperNoun()
                                    || this.term[iTerm - 1].getOriginalText()
                                    .equals(":")
                                    || this.term[iTerm - 1].getOriginalText()
                                            .length() > MIN_TEXT_LENGTH
                                    && this.term[iTerm
                                                    - 1].getOriginalText()
                                                    .charAt(0) == '@') {
                                fSentiment[iWordTotal]
                                        =
                                        (float) this.term[iTerm].getSentimentValue();

                                if (this.options.isBgExplainClassification()) {
                                    iTemp
                                            =
                                            this.term[iTerm].getSentimentValue();
                                    if (iTemp < 0) {
                                        --iTemp;
                                    } else {
                                        ++iTemp;
                                    }

                                    if (iTemp == 1) {
                                        this.sgClassificationRationale =
                                                this.sgClassificationRationale
                                                        + this.term[iTerm].getOriginalText()
                                                        + " ";
                                    } else {
                                        this.sgClassificationRationale =
                                                this.sgClassificationRationale
                                                        + this.term[iTerm].getOriginalText()
                                                        + "[" + iTemp + "] ";
                                    }
                                }
                            } else if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + this.term[iTerm].getOriginalText()
                                                + " [proper noun] ";
                            }

                            if (this.options.isBgMultipleLettersBoostSentiment()
                                    && this.term[iTerm].getWordEmphasisLength()
                                    >= this.options.getIgMinRepeatedLettersForBoost()
                                    && (iTerm == 1 || !this.term[iTerm
                                    - 1].isPunctuation() || !this.term[iTerm
                                    - 1].getOriginalText().equals("@"))) {
                                String sEmphasis
                                        = this.term[iTerm].getWordEmphasis()
                                        .toLowerCase();
                                if (!sEmphasis.contains("xx")
                                        && !sEmphasis.contains("ww")
                                        && !sEmphasis.contains("ha")) {
                                    if (fSentiment[iWordTotal] < 0.0F) {
                                        fSentiment[iWordTotal] = (float) (
                                                (double) fSentiment[iWordTotal]
                                                        - BOOST_INFLUENCE);
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + "[-0.6 spelling "
                                                            + "emphasis] ";
                                        }
                                    } else if (fSentiment[iWordTotal] > 0.0F) {
                                        fSentiment[iWordTotal] = (float) (
                                                (double) fSentiment[iWordTotal]
                                                        + BOOST_INFLUENCE);
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + "[+0.6 spelling "
                                                            + "emphasis] ";
                                        }
                                    } else if (
                                            this.options.getIgMoodToInterpretNeutralEmphasis()
                                                    > 0) {
                                        fSentiment[iWordTotal] = (float) (
                                                (double) fSentiment[iWordTotal]
                                                        + BOOST_INFLUENCE);
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + "[+0.6 spelling "
                                                            + "mood emphasis] ";
                                        }
                                    } else if (
                                            this.options.getIgMoodToInterpretNeutralEmphasis()
                                                    < 0) {
                                        fSentiment[iWordTotal] = (float) (
                                                (double) fSentiment[iWordTotal]
                                                        - BOOST_INFLUENCE);
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + "[-0.6 spelling "
                                                            + "mood emphasis] ";
                                        }
                                    }
                                }
                            }

                            int var10002;
                            if (this.options.isBgCapitalsBoostTermSentiment()
                                    && fSentiment[iWordTotal] != 0.0F
                                    && this.term[iTerm].isAllCapitals()) {
                                if (fSentiment[iWordTotal] > 0.0F) {
                                    var10002 = (int) fSentiment[iWordTotal]++;
                                    if (this.options.isBgExplainClassification()) {
                                        this.sgClassificationRationale =
                                                this.sgClassificationRationale
                                                        + "[+1 CAPITALS] ";
                                    }
                                } else {
                                    var10002 = (int) fSentiment[iWordTotal]--;
                                    if (this.options.isBgExplainClassification()) {
                                        this.sgClassificationRationale =
                                                this.sgClassificationRationale
                                                        + "[-1 CAPITALS] ";
                                    }
                                }
                            }

                            if (this.options.isBgBoosterWordsChangeEmotion()) {
                                if (iLastBoosterWordScore != 0) {
                                    if (fSentiment[iWordTotal] > 0.0F) {
                                        fSentiment[iWordTotal]
                                                += (float) iLastBoosterWordScore;
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + "[+"
                                                            + iLastBoosterWordScore
                                                            + " booster word] ";
                                        }
                                    } else if (fSentiment[iWordTotal] < 0.0F) {
                                        fSentiment[iWordTotal]
                                                -= (float) iLastBoosterWordScore;
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + "[-"
                                                            + iLastBoosterWordScore
                                                            + " booster word] ";
                                        }
                                    }
                                }

                                iLastBoosterWordScore
                                        =
                                        this.term[iTerm].getBoosterWordScore();
                            }

                            if (this.options.isBgNegatingWordsOccurBeforeSentiment()) {
                                if (this.options.isBgNegatingWordsFlipEmotion()) {
                                    if (iWordsSinceNegative
                                            <= this.options.getIgMaxWordsBeforeSentimentToNegate()) {
                                        fSentiment[iWordTotal] =
                                                -fSentiment[iWordTotal]
                                                        * this.options.getFgStrengthMultiplierForNegatedWords();
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + "[*-"
                                                            + this.options.getFgStrengthMultiplierForNegatedWords()
                                                            + " approx. negated"
                                                            + " multiplier] ";
                                        }
                                    }
                                } else {
                                    if (this.options.isBgNegatingNegativeNeutralisesEmotion()
                                            && fSentiment[iWordTotal] < 0.0F
                                            && iWordsSinceNegative
                                            <= this.options.getIgMaxWordsBeforeSentimentToNegate()) {
                                        fSentiment[iWordTotal] = 0.0F;
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + "[=0 negation] ";
                                        }
                                    }

                                    if (this.options.isBgNegatingPositiveFlipsEmotion()
                                            && fSentiment[iWordTotal] > 0.0F
                                            && iWordsSinceNegative
                                            <= this.options.getIgMaxWordsBeforeSentimentToNegate()) {
                                        fSentiment[iWordTotal] =
                                                -fSentiment[iWordTotal]
                                                        * this.options.getFgStrengthMultiplierForNegatedWords();
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + "[*-"
                                                            + this.options.getFgStrengthMultiplierForNegatedWords()
                                                            + " approx. negated"
                                                            + " multiplier] ";
                                        }
                                    }
                                }
                            }

                            if (this.term[iTerm].isNegatingWord()) {
                                iWordsSinceNegative = -1;
                            }

                            if (iLastBoosterWordScore == 0) {
                                ++iWordsSinceNegative;
                            }

                            if (this.term[iTerm].isNegatingWord()
                                    && this.options.isBgNegatingWordsOccurAfterSentiment()) {
                                iTermsChecked = 0;

                                for (int iPriorWord = iWordTotal - 1;
                                     iPriorWord > 0; --iPriorWord) {
                                    if (this.options.isBgNegatingWordsFlipEmotion()) {
                                        fSentiment[iPriorWord] =
                                                -fSentiment[iPriorWord]
                                                        * this.options.getFgStrengthMultiplierForNegatedWords();
                                        if (this.options.isBgExplainClassification()) {
                                            this.sgClassificationRationale =
                                                    this.sgClassificationRationale
                                                            + "[*-"
                                                            + this.options.getFgStrengthMultiplierForNegatedWords()
                                                            + " approx. negated"
                                                            + " multiplier] ";
                                        }
                                    } else {
                                        if (this.options.isBgNegatingNegativeNeutralisesEmotion()
                                                && fSentiment[iPriorWord]
                                                < 0.0F) {
                                            fSentiment[iPriorWord] = 0.0F;
                                            if (this.options.isBgExplainClassification()) {
                                                this.sgClassificationRationale =
                                                        this.sgClassificationRationale
                                                                + "[=0 negation"
                                                                + "] ";
                                            }
                                        }

                                        if (this.options.isBgNegatingPositiveFlipsEmotion()
                                                && fSentiment[iPriorWord]
                                                > 0.0F) {
                                            fSentiment[iPriorWord] =
                                                    -fSentiment[iPriorWord]
                                                            * this.options.getFgStrengthMultiplierForNegatedWords();
                                            if (this.options.isBgExplainClassification()) {
                                                this.sgClassificationRationale =
                                                        this.sgClassificationRationale
                                                                + "[*-"
                                                                + this.options.getFgStrengthMultiplierForNegatedWords()
                                                                + " approx. "
                                                                + "negated "
                                                                + "multiplier"
                                                                + "] ";
                                            }
                                        }
                                    }

                                    ++iTermsChecked;
                                    if (iTermsChecked
                                            > this.options.getIgMaxWordsAfterSentimentToNegate()) {
                                        break;
                                    }
                                }
                            }

                            if (this.options.isBgAllowMultipleNegativeWordsToIncreaseNegativeEmotion()
                                    && fSentiment[iWordTotal] < -1.0F
                                    && iWordTotal > 1
                                    && fSentiment[iWordTotal - 1] < -1.0F) {
                                var10002 = (int) fSentiment[iWordTotal]--;
                                if (this.options.isBgExplainClassification()) {
                                    this.sgClassificationRationale =
                                            this.sgClassificationRationale
                                                    + "[-1 consecutive negative"
                                                    + " words] ";
                                }
                            }

                            if (this.options.isBgAllowMultiplePositiveWordsToIncreasePositiveEmotion()
                                    && fSentiment[iWordTotal] > 1.0F
                                    && iWordTotal > 1
                                    && fSentiment[iWordTotal - 1] > 1.0F) {
                                var10002 = (int) fSentiment[iWordTotal]++;
                                if (this.options.isBgExplainClassification()) {
                                    this.sgClassificationRationale =
                                            this.sgClassificationRationale
                                                    + "[+1 consecutive positive"
                                                    + " words] ";
                                }
                            }

                        }
                    }
                }

                float fTotalNeg = 0.0F;
                float fTotalPos = 0.0F;
                float fMaxNeg = 0.0F;
                float fMaxPos = 0.0F;
                int iPosWords = 0;
                int iNegWords = 0;

                int iTerm;
                for (iTerm = 1; iTerm <= iWordTotal; ++iTerm) {
                    if (fSentiment[iTerm] < 0.0F) {
                        fTotalNeg += fSentiment[iTerm];
                        ++iNegWords;
                        if (fMaxNeg > fSentiment[iTerm]) {
                            fMaxNeg = fSentiment[iTerm];
                        }
                    } else if (fSentiment[iTerm] > 0.0F) {
                        fTotalPos += fSentiment[iTerm];
                        ++iPosWords;
                        if (fMaxPos < fSentiment[iTerm]) {
                            fMaxPos = fSentiment[iTerm];
                        }
                    }
                }
                igSentiCount = iNegWords + iPosWords;
                --fMaxNeg;
                ++fMaxPos;
                int var10000 = this.options.getIgEmotionSentenceCombineMethod();
                this.options.getClass();
                if (var10000 == 1) {
                    if (iPosWords == 0) {
                        this.igPositiveSentiment = 1;
                    } else {
                        this.igPositiveSentiment = (int) Math.round(
                                ((double) (fTotalPos + (float) iPosWords)
                                        + POSITIVE_AMEND) / (double) iPosWords);
                    }

                    if (iNegWords == 0) {
                        this.igNegativeSentiment = -1;
                    } else {
                        this.igNegativeSentiment = (int) Math.round(
                                ((double) (fTotalNeg - (float) iNegWords)
                                        + NEGATIVE_AMEND) / (double) iNegWords);
                    }
                } else {
                    var10000 = this.options.getIgEmotionSentenceCombineMethod();
                    this.options.getClass();
                    if (var10000 == 2) {
                        this.igPositiveSentiment = Math.round(fTotalPos)
                                + iPosWords;
                        this.igNegativeSentiment = Math.round(fTotalNeg)
                                - iNegWords;
                    } else {
                        this.igPositiveSentiment = Math.round(fMaxPos);
                        this.igNegativeSentiment = Math.round(fMaxNeg);
                    }
                }

                if (this.options.isBgReduceNegativeEmotionInQuestionSentences()
                        && this.igNegativeSentiment < -1) {
                    for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                        if (this.term[iTerm].isWord()) {
                            if (this.resources.getQuestionWords().questionWord(
                                    this.term[iTerm].getTranslatedWord()
                                            .toLowerCase())) {
                                ++this.igNegativeSentiment;
                                if (this.options.isBgExplainClassification()) {
                                    this.sgClassificationRationale =
                                            this.sgClassificationRationale
                                                    + "[+1 negative for "
                                                    + "question word]";
                                }
                                break;
                            }
                        } else if (this.term[iTerm].isPunctuation()
                                && this.term[iTerm].punctuationContains("?")) {
                            ++this.igNegativeSentiment;
                            if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + "[+1 negative for question "
                                                + "mark ?]";
                            }
                            break;
                        }
                    }
                }

                if (this.igPositiveSentiment == 1
                        && this.options.isBgMissCountsAsPlus2()) {
                    for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                        if (this.term[iTerm].isWord()
                                && this.term[iTerm].getTranslatedWord()
                                        .toLowerCase().compareTo("miss") == 0) {
                            this.igPositiveSentiment = 2;
                            if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + "[pos = 2 for term 'miss']";
                            }
                            break;
                        }
                    }
                }

                if (bSentencePunctuationBoost) {
                    if (this.igPositiveSentiment < -this.igNegativeSentiment) {
                        --this.igNegativeSentiment;
                        if (this.options.isBgExplainClassification()) {
                            this.sgClassificationRationale =
                                    this.sgClassificationRationale
                                            + "[-1 punctuation emphasis] ";
                        }
                    } else if (this.igPositiveSentiment
                            > -this.igNegativeSentiment) {
                        ++this.igPositiveSentiment;
                        if (this.options.isBgExplainClassification()) {
                            this.sgClassificationRationale =
                                    this.sgClassificationRationale
                                            + "[+1 punctuation emphasis] ";
                        }
                    } else if (
                            this.options.getIgMoodToInterpretNeutralEmphasis()
                                    > 0) {
                        ++this.igPositiveSentiment;
                        if (this.options.isBgExplainClassification()) {
                            this.sgClassificationRationale =
                                    this.sgClassificationRationale
                                            + "[+1 punctuation mood emphasis] ";
                        }
                    } else if (
                            this.options.getIgMoodToInterpretNeutralEmphasis()
                                    < 0) {
                        --this.igNegativeSentiment;
                        if (this.options.isBgExplainClassification()) {
                            this.sgClassificationRationale =
                                    this.sgClassificationRationale
                                            + "[-1 punctuation mood emphasis] ";
                        }
                    }
                }

                if (this.igPositiveSentiment == 1
                        && this.igNegativeSentiment == -1
                        && this.options.isBgExclamationInNeutralSentenceCountsAsPlus2()) {
                    for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                        if (this.term[iTerm].isPunctuation()
                                && this.term[iTerm].punctuationContains("!")) {
                            this.igPositiveSentiment = 2;
                            if (this.options.isBgExplainClassification()) {
                                this.sgClassificationRationale =
                                        this.sgClassificationRationale
                                                + "[pos = 2 for !]";
                            }
                            break;
                        }
                    }
                }

                if (this.igPositiveSentiment == 1
                        && this.igNegativeSentiment == -1
                        && this.options.isBgYouOrYourIsPlus2UnlessSentenceNegative()) {
                    for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                        if (this.term[iTerm].isWord()) {
                            String sTranslatedWord
                                    = this.term[iTerm].getTranslatedWord()
                                    .toLowerCase();
                            if (sTranslatedWord.compareTo("you") == 0
                                    || sTranslatedWord.compareTo("your") == 0
                                    || sTranslatedWord.compareTo("whats")
                                    == 0) {
                                this.igPositiveSentiment = 2;
                                if (this.options.isBgExplainClassification()) {
                                    this.sgClassificationRationale =
                                            this.sgClassificationRationale
                                                    + "[pos = 2 for "
                                                    + "you/your/whats]";
                                }
                                break;
                            }
                        }
                    }
                }

                this.adjustSentimentForIrony();
                var10000 = this.options.getIgEmotionSentenceCombineMethod();
                this.options.getClass();
                if (var10000 != 2) {
                    if (this.igPositiveSentiment > POSITIVE_MAX) {
                        this.igPositiveSentiment = POSITIVE_MAX;
                    }

                    if (this.igNegativeSentiment < NEGATIVE_MAX) {
                        this.igNegativeSentiment = NEGATIVE_MAX;
                    }
                }

                if (this.options.isBgExplainClassification()) {
                    this.sgClassificationRationale =
                            this.sgClassificationRationale + "[sentence: "
                                    + this.igPositiveSentiment + ","
                                    + this.igNegativeSentiment + "]";
                }

            }
        }
    }

    /**
     * 因讽刺而调整情绪.
     * <br>
     * 在判断句子的积极得分大于引用或标点符号和术语的讽刺需要的最小得分后,如果是引用或标点,则直接反转,如果是术语,
     * 则和讽刺词表对照后决定是否要调整情绪.
     * 这个过程可以根据需要添加相关的变化原因.<br>
     * UC-25 Explain the classification<br>
     */
    private void adjustSentimentForIrony() {
        int iTerm;
        if (this.igPositiveSentiment
                >= this.options.getIgMinSentencePosForQuotesIrony()) {
            for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.term[iTerm].isPunctuation()
                        && this.term[iTerm].getText().indexOf(IRONY_POSITION)
                        >= 0) {
                    if (this.igNegativeSentiment > -this.igPositiveSentiment) {
                        this.igNegativeSentiment = 1 - this.igPositiveSentiment;
                    }

                    this.igPositiveSentiment = 1;
                    this.sgClassificationRationale =
                            this.sgClassificationRationale
                                    + "[Irony change: pos = 1, neg = "
                                    + this.igNegativeSentiment + "]";
                    return;
                }
            }
        }

        if (this.igPositiveSentiment
                >= this.options.getIgMinSentencePosForPunctuationIrony()) {
            for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.term[iTerm].isPunctuation()
                        && this.term[iTerm].punctuationContains("!")
                        && this.term[iTerm].getPunctuationEmphasisLength()
                        > 0) {
                    if (this.igNegativeSentiment > -this.igPositiveSentiment) {
                        this.igNegativeSentiment = 1 - this.igPositiveSentiment;
                    }

                    this.igPositiveSentiment = 1;
                    this.sgClassificationRationale =
                            this.sgClassificationRationale
                                    + "[Irony change: pos = 1, neg = "
                                    + this.igNegativeSentiment + "]";
                    return;
                }
            }
        }

        if (this.igPositiveSentiment
                >= this.options.getIgMinSentencePosForTermsIrony()) {
            for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.resources.getIronyList()
                        .termIsIronic(this.term[iTerm].getText())) {
                    if (this.igNegativeSentiment > -this.igPositiveSentiment) {
                        this.igNegativeSentiment = 1 - this.igPositiveSentiment;
                    }

                    this.igPositiveSentiment = 1;
                    this.sgClassificationRationale =
                            this.sgClassificationRationale
                                    + "[Irony change: pos = 1, neg = "
                                    + this.igNegativeSentiment + "]";
                    return;
                }
            }
        }

    }

    /**
     * 用对象评估来重写术语强度.
     * <br>
     * 如果对象评估还没有使用过且整体分数还没有重新计算过,则针对每个术语是否匹配对象评估列表中的某一项来重新给该术语进行强度评估
     * .并将对象评估标记为已使用过.<br>
     *
     * @param recalculateIfAlreadyDone 是否已经重新计算过
     */
    public void overrideTermStrengthsWithObjectEvaluationStrengths(
            final boolean recalculateIfAlreadyDone) {
        boolean bMatchingObject;
        boolean bMatchingEvaluation;
        if (!this.bgObjectEvaluationsApplied || recalculateIfAlreadyDone) {
            for (int iObject = 1; iObject < this.resources.getEvaluativeTerms()
                    .getIgObjectEvaluationCount(); ++iObject) {
                bMatchingObject = false;
                bMatchingEvaluation = false;

                int iTerm;
                for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                    if (this.term[iTerm].isWord()
                            && this.term[iTerm].matchesStringWithWildcard(
                            this.resources.getEvaluativeTerms()
                                    .getSgObject()[iObject], true)) {
                        bMatchingObject = true;
                        break;
                    }
                }

                if (bMatchingObject) {
                    for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                        if (this.term[iTerm].isWord()
                                && this.term[iTerm].matchesStringWithWildcard(
                                this.resources.getEvaluativeTerms()
                                        .getSgObjectEvaluation()[iObject],
                                true)) {
                            bMatchingEvaluation = true;
                            break;
                        }
                    }
                }

                if (bMatchingEvaluation) {
                    if (this.options.isBgExplainClassification()) {
                        this.sgClassificationRationale =
                                this.sgClassificationRationale
                                        + "[term weight changed by "
                                        + "object/evaluation]";
                    }

                    this.term[iTerm].setSentimentOverrideValue(
                            this.resources.getEvaluativeTerms()
                                    .getIgObjectEvaluationStrength()[iObject]);
                }
            }

            this.bgObjectEvaluationsApplied = true;
        }

    }

    /**
     * 用对习语来重写术语强度.
     * <br>
     * 如果习语还没有使用过且整体分数还没有重新计算过,则针对每个术语是否匹配习语列表中的某一项来重新给该术语进行强度评估.并将习语标记为已使用过
     * .<br>
     *
     * @param recalculateIfAlreadyDone 是否已经重新计算过
     */
    public void overrideTermStrengthsWithIdiomStrengths(
            final boolean recalculateIfAlreadyDone) {
        if (!this.bgIdiomsApplied || recalculateIfAlreadyDone) {
            for (int iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.term[iTerm].isWord()) {
                    for (int iIdiom = 1; iIdiom <= this.resources.getIdiomList()
                            .getIgIdiomCount(); ++iIdiom) {
                        if (iTerm + this.resources.getIdiomList()
                                .getIdiomWordCount()[iIdiom] - 1
                                <= this.igTermCount) {
                            boolean bMatchingIdiom = true;

                            int iIdiomTerm;
                            for (iIdiomTerm = 0;
                                 iIdiomTerm < this.resources.getIdiomList()
                                         .getIdiomWordCount()[iIdiom];
                                 ++iIdiomTerm) {
                                if (!this.term[iTerm
                                        + iIdiomTerm].matchesStringWithWildcard(
                                        this.resources.getIdiomList()
                                                .getSgIdiomWords()[iIdiom][iIdiomTerm],
                                        true)) {
                                    bMatchingIdiom = false;
                                    break;
                                }
                            }

                            if (bMatchingIdiom) {
                                if (this.options.isBgExplainClassification()) {
                                    this.sgClassificationRationale =
                                            this.sgClassificationRationale
                                                    + "[term weight(s) changed "
                                                    + "by idiom "
                                                    + this.resources.getIdiomList()
                                                    .getIdiom(iIdiom) + "]";
                                }

                                this.term[iTerm].setSentimentOverrideValue(
                                        this.resources.getIdiomList()
                                                .getIgIdiomStrength()[iIdiom]);

                                for (iIdiomTerm = 1;
                                     iIdiomTerm < this.resources.getIdiomList()
                                             .getIdiomWordCount()[iIdiom];
                                     ++iIdiomTerm) {
                                    this.term[iTerm
                                            + iIdiomTerm].setSentimentOverrideValue(
                                            0);
                                }
                            }
                        }
                    }
                }
            }

            this.bgIdiomsApplied = true;
        }

    }
}
