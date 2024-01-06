//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package rainbowsix.ss;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * 文本分类器选项类.
 * <br>
 * UC-26 Set Classification Algorithm Parameters<br>
 *
 * @author 注释编写 徐晨 詹美瑛 朱甲豪
 */
public class ClassificationOptions {
    /**
     * 是否使用TensiStrength.
     */
    private boolean bgTensiStrength = false;
    /**
     * 程序的名字.
     */
    private String sgProgramName = "SentiStrength";
    /**
     * 程序检测的内容.
     */
    private String sgProgramMeasuring = "sentiment";

    /**
     * 获取defaultNegativeSentimentMultiplier.
     *
     * @return defaultNegativeSentimentMultiplier
     */
    public static float getDefaultNegativeSentimentMultiplier() {
        return DEFAULT_NEGATIVE_SENTIMENT_MULTIPLIER;
    }

    /**
     * 获取defaultStrengthMultiplierForNegatedWords.
     *
     * @return defaultStrengthMultiplierForNegatedWords
     */
    public static float getDefaultStrengthMultiplierForNegatedWords() {
        return DEFAULT_STRENGTH_MULTIPLIER_FOR_NEGATED_WORDS;
    }

    /**
     * 获取defaultMinRepeatedLettersForBoost.
     *
     * @return defaultMinRepeatedLettersForBoost
     */
    public static int getDefaultMinRepeatedLettersForBoost() {
        return DEFAULT_MIN_REPEATED_LETTERS_FOR_BOOST;
    }

    /**
     * 获取defaultWordsToIncludeBeforeKeyword.
     *
     * @return defaultWordsToIncludeBeforeKeyword
     */
    public static int getDefaultWordsToIncludeBeforeKeyword() {
        return DEFAULT_WORDS_TO_INCLUDE_BEFORE_KEYWORD;
    }

    /**
     * 获取defaultWordsToIncludeAfterKeyword.
     *
     * @return defaultWordsToIncludeAfterKeyword
     */
    public static int getDefaultWordsToIncludeAfterKeyword() {
        return DEFAULT_WORDS_TO_INCLUDE_AFTER_KEYWORD;
    }

    /**
     * 获取defaultMinSentencePosForQuotesIrony.
     *
     * @return defaultMinSentencePosForQuotesIrony
     */
    public static int getDefaultMinSentencePosForQuotesIrony() {
        return DEFAULT_MIN_SENTENCE_POS_FOR_QUOTES_IRONY;
    }

    /**
     * 获取defaultMinSentencePosForPunctuationIrony.
     *
     * @return defaultMinSentencePosForPunctuationIrony
     */
    public static int getDefaultMinSentencePosForPunctuationIrony() {
        return DEFAULT_MIN_SENTENCE_POS_FOR_PUNCTUATION_IRONY;
    }

    /**
     * 获取defaultMinSentencePosForTermsIrony.
     *
     * @return defaultMinSentencePosForTermsIrony
     */
    public static int getDefaultMinSentencePosForTermsIrony() {
        return DEFAULT_MIN_SENTENCE_POS_FOR_TERMS_IRONY;
    }

    /**
     * 获取是否使用TensiStrength.
     *
     * @return 是否使用TensiStrength.
     */
    public boolean isBgTensiStrength() {
        return bgTensiStrength;
    }

    /**
     * 设置是否使用TensiStrength.
     *
     * @param b 是否使用TensiStrength.
     */
    public void setBgTensiStrength(final boolean b) {
        this.bgTensiStrength = b;
    }

    /**
     * 获取程序的名字.
     *
     * @return 程序的名字.
     */
    public String getSgProgramName() {
        return sgProgramName;
    }

    /**
     * 设置程序的名字.
     *
     * @param s 程序的名字.
     */
    public void setSgProgramName(final String s) {
        this.sgProgramName = s;
    }

    /**
     * 获取程序检测的内容.
     *
     * @return 程序检测的内容.
     */
    public String getSgProgramMeasuring() {
        return sgProgramMeasuring;
    }

    /**
     * 设置程序检测的内容.
     *
     * @param s 程序检测的内容.
     */
    public void setSgProgramMeasuring(final String s) {
        this.sgProgramMeasuring = s;
    }

    /**
     * 获取程序检测的正面值变量.
     *
     * @return 程序检测的正面值变量.
     */
    public String getSgProgramPos() {
        return sgProgramPos;
    }

    /**
     * 设置程序检测的正面值变量.
     *
     * @param s 程序检测的正面值变量.
     */
    public void setSgProgramPos(final String s) {
        this.sgProgramPos = s;
    }

    /**
     * 获取程序检测的负面值变量.
     *
     * @return 程序检测的负面值变量.
     */
    public String getSgProgramNeg() {
        return sgProgramNeg;
    }

    /**
     * 设置程序检测的负面值变量.
     *
     * @param s 程序检测的负面值变量.
     */
    public void setSgProgramNeg(final String s) {
        this.sgProgramNeg = s;
    }

    /**
     * 获取是否使用Scale模式.
     *
     * @return 是否使用Scale模式.
     */
    public boolean isBgScaleMode() {
        return bgScaleMode;
    }

    /**
     * 设置是否使用Scale模式.
     *
     * @param b 是否使用Scale模式.
     */
    public void setBgScaleMode(final boolean b) {
        this.bgScaleMode = b;
    }

    /**
     * 获取是否使用二元分类.
     *
     * @return 是否使用二元分类.
     */
    public boolean isBgTrinaryMode() {
        return bgTrinaryMode;
    }

    /**
     * 设置是否使用二元分类.
     *
     * @param b 是否使用二元分类.
     */
    public void setBgTrinaryMode(final boolean b) {
        this.bgTrinaryMode = b;
    }

    /**
     * 获取是否使用三元分类.
     *
     * @return 是否使用三元分类.
     */
    public boolean isBgBinaryVersionOfTrinaryMode() {
        return bgBinaryVersionOfTrinaryMode;
    }

    /**
     * 设置是否使用三元分类.
     *
     * @param b 是否使用三元分类.
     */
    public void setBgBinaryVersionOfTrinaryMode(final boolean b) {
        this.bgBinaryVersionOfTrinaryMode = b;
    }

    /**
     * 获取默认的二元分类结果.
     *
     * @return 默认的二元分类结果.
     */
    public int getIgDefaultBinaryClassification() {
        return igDefaultBinaryClassification;
    }

    /**
     * 设置默认的二元分类结果.
     *
     * @param i 默认的二元分类结果.
     */
    public void setIgDefaultBinaryClassification(final int i) {
        this.igDefaultBinaryClassification = i;
    }

    /**
     * 获取表示采用何种方式计算计算段落的情感强度.
     *
     * @return 表示采用何种方式计算计算段落的情感强度.
     */
    public int getIgEmotionParagraphCombineMethod() {
        return igEmotionParagraphCombineMethod;
    }

    /**
     * 设置表示采用何种方式计算计算段落的情感强度.
     *
     * @param i 表示采用何种方式计算计算段落的情感强度.
     */
    public void setIgEmotionParagraphCombineMethod(final int i) {
        this.igEmotionParagraphCombineMethod = i;
    }

    /**
     * 获取表示采用段落中所有句子的情感强度的最大值.
     *
     * @return 表示采用段落中所有句子的情感强度的最大值.
     */
    public int getIgCombineMax() {
        return igCombineMax;
    }

    /**
     * 获取表示采用段落中所有句子的情感强度的平均值.
     *
     * @return 表示采用段落中所有句子的情感强度的平均值.
     */
    public int getIgCombineAverage() {
        return igCombineAverage;
    }

    /**
     * 获取表示采用段落中所有句子的情感强度的总和.
     *
     * @return 表示采用段落中所有句子的情感强度的总和.
     */
    public int getIgCombineTotal() {
        return igCombineTotal;
    }

    /**
     * 获取表示采用何种方式计算句子的情感强度.
     *
     * @return 表示采用何种方式计算句子的情感强度.
     */
    public int getIgEmotionSentenceCombineMethod() {
        return igEmotionSentenceCombineMethod;
    }

    /**
     * 设置表示采用何种方式计算句子的情感强度.
     *
     * @param i 表示采用何种方式计算句子的情感强度.
     */
    public void setIgEmotionSentenceCombineMethod(final int i) {
        this.igEmotionSentenceCombineMethod = i;
    }

    /**
     * 获取消极强度因子，当积极情绪强度大于消极强度乘以这个因子时，才判断整体为积极.
     *
     * @return 消极强度因子 ，当积极情绪强度大于消极强度乘以这个因子时，才判断整体为积极.
     */
    public float getFgNegativeSentimentMultiplier() {
        return fgNegativeSentimentMultiplier;
    }

    /**
     * 设置消极强度因子，当积极情绪强度大于消极强度乘以这个因子时，才判断整体为积极.
     *
     * @param v 消极强度因子，当积极情绪强度大于消极强度乘以这个因子时，才判断整体为积极.
     */
    public void setFgNegativeSentimentMultiplier(final float v) {
        this.fgNegativeSentimentMultiplier = v;
    }

    /**
     * 获取是否减少疑问句中的消极情绪.
     *
     * @return 是否减少疑问句中的消极情绪.
     */
    public boolean isBgReduceNegativeEmotionInQuestionSentences() {
        return bgReduceNegativeEmotionInQuestionSentences;
    }

    /**
     * 设置是否减少疑问句中的消极情绪.
     *
     * @param b 是否减少疑问句中的消极情绪.
     */
    public void setBgReduceNegativeEmotionInQuestionSentences(final boolean b) {
        this.bgReduceNegativeEmotionInQuestionSentences = b;
    }

    /**
     * 获取Miss这个词为强度是否为正2.
     *
     * @return Miss这个词为强度是否为正2.
     */
    public boolean isBgMissCountsAsPlus2() {
        return bgMissCountsAsPlus2;
    }

    /**
     * 设置Miss这个词为强度是否为正2.
     *
     * @param b Miss这个词为强度是否为正2.
     */
    public void setBgMissCountsAsPlus2(final boolean b) {
        this.bgMissCountsAsPlus2 = b;
    }

    /**
     * 获取you和your这两个词强度是否为正2，除非是否定句.
     *
     * @return you和your这两个词强度是否为正2 ，除非是否定句.
     */
    public boolean isBgYouOrYourIsPlus2UnlessSentenceNegative() {
        return bgYouOrYourIsPlus2UnlessSentenceNegative;
    }

    /**
     * 设置you和your这两个词强度是否为正2，除非是否定句.
     *
     * @param b you和your这两个词强度是否为正2，除非是否定句.
     */
    public void setBgYouOrYourIsPlus2UnlessSentenceNegative(final boolean b) {
        this.bgYouOrYourIsPlus2UnlessSentenceNegative = b;
    }

    /**
     * 获取中性句子中的感叹号是否设为正2.
     *
     * @return 中性句子中的感叹号是否设为正2.
     */
    public boolean isBgExclamationInNeutralSentenceCountsAsPlus2() {
        return bgExclamationInNeutralSentenceCountsAsPlus2;
    }

    /**
     * 设置中性句子中的感叹号是否设为正2.
     *
     * @param b 中性句子中的感叹号是否设为正2.
     */
    public void setBgExclamationInNeutralSentenceCountsAsPlus2(final
                                                               boolean b) {
        this.bgExclamationInNeutralSentenceCountsAsPlus2 = b;
    }

    /**
     * 获取能增强句子情感强度的带感叹号的最少标点数目.
     *
     * @return 能增强句子情感强度的带感叹号的最少标点数目.
     */
    public int getIgMinPunctuationWithExclamationToChangeSentenceSentiment() {
        return igMinPunctuationWithExclamationToChangeSentenceSentiment;
    }

    /**
     * 设置能增强句子情感强度的带感叹号的最少标点数目.
     *
     * @param i 能增强句子情感强度的带感叹号的最少标点数目.
     */
    public void setIgMinPunctuationWithExclamationToChangeSentenceSentiment(
            final int i) {
        this.igMinPunctuationWithExclamationToChangeSentenceSentiment = i;
    }

    /**
     * 获取是否使用习语表.
     *
     * @return 是否使用习语表.
     */
    public boolean isBgUseIdiomLookupTable() {
        return bgUseIdiomLookupTable;
    }

    /**
     * 设置是否使用习语表.
     *
     * @param b 是否使用习语表.
     */
    public void setBgUseIdiomLookupTable(final boolean b) {
        this.bgUseIdiomLookupTable = b;
    }

    /**
     * 获取是否使用对象评估表.
     *
     * @return 是否使用对象评估表.
     */
    public boolean isBgUseObjectEvaluationTable() {
        return bgUseObjectEvaluationTable;
    }

    /**
     * 设置是否使用对象评估表.
     *
     * @param b 是否使用对象评估表.
     */
    public void setBgUseObjectEvaluationTable(final boolean b) {
        this.bgUseObjectEvaluationTable = b;
    }

    /**
     * 获取是否将中性情绪计算为1的积极情绪.
     *
     * @return 是否将中性情绪计算为1的积极情绪.
     */
    public boolean isBgCountNeutralEmotionsAsPositiveForEmphasis1() {
        return bgCountNeutralEmotionsAsPositiveForEmphasis1;
    }

    /**
     * 设置是否将中性情绪计算为1的积极情绪.
     *
     * @param b 是否将中性情绪计算为1的积极情绪.
     */
    public void setBgCountNeutralEmotionsAsPositiveForEmphasis1(
            final boolean b) {
        this.bgCountNeutralEmotionsAsPositiveForEmphasis1 = b;
    }

    /**
     * 获取将中性情绪翻译为多大的强度.
     *
     * @return 将中性情绪翻译为多大的强度.
     */
    public int getIgMoodToInterpretNeutralEmphasis() {
        return igMoodToInterpretNeutralEmphasis;
    }

    /**
     * 设置将中性情绪翻译为多大的强度.
     *
     * @param i 将中性情绪翻译为多大的强度.
     */
    public void setIgMoodToInterpretNeutralEmphasis(final int i) {
        this.igMoodToInterpretNeutralEmphasis = i;
    }

    /**
     * 获取是否允许多个积极词增强积极情绪.
     *
     * @return 是否允许多个积极词增强积极情绪.
     */
    public boolean isBgAllowMultiplePositiveWordsToIncreasePositiveEmotion() {
        return bgAllowMultiplePositiveWordsToIncreasePositiveEmotion;
    }

    /**
     * 设置是否允许多个积极词增强积极情绪.
     *
     * @param b 是否允许多个积极词增强积极情绪.
     */
    public void setBgAllowMultiplePositiveWordsToIncreasePositiveEmotion(
            final boolean b) {
        this.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion = b;
    }

    /**
     * 获取是否允许多个否定词增强否定情绪.
     *
     * @return 是否允许多个否定词增强否定情绪.
     */
    public boolean isBgAllowMultipleNegativeWordsToIncreaseNegativeEmotion() {
        return bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion;
    }

    /**
     * 设置是否允许多个否定词增强否定情绪.
     *
     * @param b 是否允许多个否定词增强否定情绪.
     */
    public void setBgAllowMultipleNegativeWordsToIncreaseNegativeEmotion(
            final boolean b) {
        this.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion = b;
    }

    /**
     * 获取是否忽略否定后的增强词.
     *
     * @return 是否忽略否定后的增强词.
     */
    public boolean isBgIgnoreBoosterWordsAfterNegatives() {
        return bgIgnoreBoosterWordsAfterNegatives;
    }

    /**
     * 设置是否忽略否定后的增强词.
     *
     * @param b 是否忽略否定后的增强词.
     */
    public void setBgIgnoreBoosterWordsAfterNegatives(final boolean b) {
        this.bgIgnoreBoosterWordsAfterNegatives = b;
    }

    /**
     * 获取是否使用词典进行拼写纠正.
     *
     * @return 是否使用词典进行拼写纠正.
     */
    public boolean isBgCorrectSpellingsUsingDictionary() {
        return bgCorrectSpellingsUsingDictionary;
    }

    /**
     * 设置是否使用词典进行拼写纠正.
     *
     * @param b 是否使用词典进行拼写纠正.
     */
    public void setBgCorrectSpellingsUsingDictionary(final boolean b) {
        this.bgCorrectSpellingsUsingDictionary = b;
    }

    /**
     * 获取是否纠正多余字母的拼写错误.
     *
     * @return 是否纠正多余字母的拼写错误.
     */
    public boolean isBgCorrectExtraLetterSpellingErrors() {
        return bgCorrectExtraLetterSpellingErrors;
    }

    /**
     * 设置是否纠正多余字母的拼写错误.
     *
     * @param b 是否纠正多余字母的拼写错误.
     */
    public void setBgCorrectExtraLetterSpellingErrors(final boolean b) {
        this.bgCorrectExtraLetterSpellingErrors = b;
    }

    /**
     * 获取单词中的非法双字母部分，即不可能在单词中部连续出现两次的字母.
     *
     * @return 单词中的非法双字母部分 ，即不可能在单词中部连续出现两次的字母.
     */
    public String getSgIllegalDoubleLettersInWordMiddle() {
        return sgIllegalDoubleLettersInWordMiddle;
    }

    /**
     * 设置单词中的非法双字母部分，即不可能在单词中部连续出现两次的字母.
     *
     * @param s 单词中的非法双字母部分，即不可能在单词中部连续出现两次的字母.
     */
    public void setSgIllegalDoubleLettersInWordMiddle(final String s) {
        this.sgIllegalDoubleLettersInWordMiddle = s;
    }

    /**
     * 获取不可能在单词结尾出现两次的字母.
     *
     * @return 不可能在单词结尾出现两次的字母.
     */
    public String getSgIllegalDoubleLettersAtWordEnd() {
        return sgIllegalDoubleLettersAtWordEnd;
    }

    /**
     * 设置不可能在单词结尾出现两次的字母.
     *
     * @param s 不可能在单词结尾出现两次的字母.
     */
    public void setSgIllegalDoubleLettersAtWordEnd(final String s) {
        this.sgIllegalDoubleLettersAtWordEnd = s;
    }

    /**
     * 获取是否重复字母增强情感.
     *
     * @return 是否重复字母增强情感.
     */
    public boolean isBgMultipleLettersBoostSentiment() {
        return bgMultipleLettersBoostSentiment;
    }

    /**
     * 设置是否重复字母增强情感.
     *
     * @param b 是否重复字母增强情感.
     */
    public void setBgMultipleLettersBoostSentiment(final boolean b) {
        this.bgMultipleLettersBoostSentiment = b;
    }

    /**
     * 获取增强词是否改变情感.
     *
     * @return 增强词是否改变情感.
     */
    public boolean isBgBoosterWordsChangeEmotion() {
        return bgBoosterWordsChangeEmotion;
    }

    /**
     * 设置增强词是否改变情感.
     *
     * @param b 增强词是否改变情感.
     */
    public void setBgBoosterWordsChangeEmotion(final boolean b) {
        this.bgBoosterWordsChangeEmotion = b;
    }

    /**
     * 获取是否总是使用撇号分割单词.
     *
     * @return 是否总是使用撇号分割单词.
     */
    public boolean isBgAlwaysSplitWordsAtApostrophes() {
        return bgAlwaysSplitWordsAtApostrophes;
    }

    /**
     * 设置是否总是使用撇号分割单词.
     *
     * @param b 是否总是使用撇号分割单词.
     */
    public void setBgAlwaysSplitWordsAtApostrophes(final boolean b) {
        this.bgAlwaysSplitWordsAtApostrophes = b;
    }

    /**
     * 获取否定词是否出现在情感词前面.
     *
     * @return 否定词是否出现在情感词前面.
     */
    public boolean isBgNegatingWordsOccurBeforeSentiment() {
        return bgNegatingWordsOccurBeforeSentiment;
    }

    /**
     * 设置否定词是否出现在情感词前面.
     *
     * @param b 否定词是否出现在情感词前面.
     */
    public void setBgNegatingWordsOccurBeforeSentiment(final boolean b) {
        this.bgNegatingWordsOccurBeforeSentiment = b;
    }

    /**
     * 获取在否定词和情感词之间的最大单词数.
     *
     * @return 在否定词和情感词之间的最大单词数.
     */
    public int getIgMaxWordsBeforeSentimentToNegate() {
        return igMaxWordsBeforeSentimentToNegate;
    }

    /**
     * 设置在否定词和情感词之间的最大单词数.
     *
     * @param i 在否定词和情感词之间的最大单词数.
     */
    public void setIgMaxWordsBeforeSentimentToNegate(final int i) {
        this.igMaxWordsBeforeSentimentToNegate = i;
    }

    /**
     * 获取否定词是否出现在情感词后面.
     *
     * @return 否定词是否出现在情感词后面.
     */
    public boolean isBgNegatingWordsOccurAfterSentiment() {
        return bgNegatingWordsOccurAfterSentiment;
    }

    /**
     * 设置否定词是否出现在情感词后面.
     *
     * @param b 否定词是否出现在情感词后面.
     */
    public void setBgNegatingWordsOccurAfterSentiment(final boolean b) {
        this.bgNegatingWordsOccurAfterSentiment = b;
    }

    /**
     * 获取情感词和否定词之间的最大单词数.
     *
     * @return 情感词和否定词之间的最大单词数.
     */
    public int getIgMaxWordsAfterSentimentToNegate() {
        return igMaxWordsAfterSentimentToNegate;
    }

    /**
     * 设置情感词和否定词之间的最大单词数.
     *
     * @param i 情感词和否定词之间的最大单词数.
     */
    public void setIgMaxWordsAfterSentimentToNegate(final int i) {
        this.igMaxWordsAfterSentimentToNegate = i;
    }

    /**
     * 获取是否使用否定词反转积极情绪.
     *
     * @return 是否使用否定词反转积极情绪.
     */
    public boolean isBgNegatingPositiveFlipsEmotion() {
        return bgNegatingPositiveFlipsEmotion;
    }

    /**
     * 设置是否使用否定词反转积极情绪.
     *
     * @param b 是否使用否定词反转积极情绪.
     */
    public void setBgNegatingPositiveFlipsEmotion(final boolean b) {
        this.bgNegatingPositiveFlipsEmotion = b;
    }

    /**
     * 获取是否使用否定词将消极情绪转为中性.
     *
     * @return 是否使用否定词将消极情绪转为中性.
     */
    public boolean isBgNegatingNegativeNeutralisesEmotion() {
        return bgNegatingNegativeNeutralisesEmotion;
    }

    /**
     * 设置是否使用否定词将消极情绪转为中性.
     *
     * @param b 是否使用否定词将消极情绪转为中性.
     */
    public void setBgNegatingNegativeNeutralisesEmotion(final boolean b) {
        this.bgNegatingNegativeNeutralisesEmotion = b;
    }

    /**
     * 获取是否使用否定词反转情绪.
     *
     * @return 是否使用否定词反转情绪.
     */
    public boolean isBgNegatingWordsFlipEmotion() {
        return bgNegatingWordsFlipEmotion;
    }

    /**
     * 设置是否使用否定词反转情绪.
     *
     * @param b 是否使用否定词反转情绪.
     */
    public void setBgNegatingWordsFlipEmotion(final boolean b) {
        this.bgNegatingWordsFlipEmotion = b;
    }

    /**
     * 获取否定词的强度因子.
     *
     * @return 否定词的强度因子.
     */
    public float getFgStrengthMultiplierForNegatedWords() {
        return fgStrengthMultiplierForNegatedWords;
    }

    /**
     * 设置否定词的强度因子.
     *
     * @param v 否定词的强度因子.
     */
    public void setFgStrengthMultiplierForNegatedWords(final float v) {
        this.fgStrengthMultiplierForNegatedWords = v;
    }

    /**
     * 获取是否纠正重复字母的单词.
     *
     * @return 是否纠正重复字母的单词.
     */
    public boolean isBgCorrectSpellingsWithRepeatedLetter() {
        return bgCorrectSpellingsWithRepeatedLetter;
    }

    /**
     * 设置是否纠正重复字母的单词.
     *
     * @param b 是否纠正重复字母的单词.
     */
    public void setBgCorrectSpellingsWithRepeatedLetter(final boolean b) {
        this.bgCorrectSpellingsWithRepeatedLetter = b;
    }

    /**
     * 获取是否使用表情符.
     *
     * @return 是否使用表情符.
     */
    public boolean isBgUseEmoticons() {
        return bgUseEmoticons;
    }

    /**
     * 设置是否使用表情符.
     *
     * @param b 是否使用表情符.
     */
    public void setBgUseEmoticons(final boolean b) {
        this.bgUseEmoticons = b;
    }

    /**
     * 获取大写字母是否增强术语的强度.
     *
     * @return 大写字母是否增强术语的强度.
     */
    public boolean isBgCapitalsBoostTermSentiment() {
        return bgCapitalsBoostTermSentiment;
    }

    /**
     * 设置大写字母是否增强术语的强度.
     *
     * @param b 大写字母是否增强术语的强度.
     */
    public void setBgCapitalsBoostTermSentiment(final boolean b) {
        this.bgCapitalsBoostTermSentiment = b;
    }

    /**
     * 获取增强情感所需要的最小重复字母数.
     *
     * @return 增强情感所需要的最小重复字母数.
     */
    public int getIgMinRepeatedLettersForBoost() {
        return igMinRepeatedLettersForBoost;
    }

    /**
     * 设置增强情感所需要的最小重复字母数.
     *
     * @param i 增强情感所需要的最小重复字母数.
     */
    public void setIgMinRepeatedLettersForBoost(final int i) {
        this.igMinRepeatedLettersForBoost = i;
    }

    /**
     * 获得关键词列表.
     *
     * @return 关键词列表.
     */
    public String[] getSgSentimentKeyWords() {
        return sgSentimentKeyWords;
    }

    /**
     * 设置关键词列表.
     *
     * @param strings 关键词列表.
     */
    public void setSgSentimentKeyWords(final String[] strings) {
        this.sgSentimentKeyWords = strings;
    }

    /**
     * 获取是否忽视没有关键词的句子.
     *
     * @return 是否忽视没有关键词的句子.
     */
    public boolean isBgIgnoreSentencesWithoutKeywords() {
        return bgIgnoreSentencesWithoutKeywords;
    }

    /**
     * 设置是否忽视没有关键词的句子.
     *
     * @param b 是否忽视没有关键词的句子.
     */
    public void setBgIgnoreSentencesWithoutKeywords(final boolean b) {
        this.bgIgnoreSentencesWithoutKeywords = b;
    }

    /**
     * 获取关键词之前包含的单词个数.
     *
     * @return 关键词之前包含的单词个数.
     */
    public int getIgWordsToIncludeBeforeKeyword() {
        return igWordsToIncludeBeforeKeyword;
    }

    /**
     * 设置关键词之前包含的单词个数.
     *
     * @param i 关键词之前包含的单词个数.
     */
    public void setIgWordsToIncludeBeforeKeyword(final int i) {
        this.igWordsToIncludeBeforeKeyword = i;
    }

    /**
     * 获取关键词后包含的单词个数.
     *
     * @return 关键词后包含的单词个数.
     */
    public int getIgWordsToIncludeAfterKeyword() {
        return igWordsToIncludeAfterKeyword;
    }

    /**
     * 设置关键词后包含的单词个数.
     *
     * @param i 关键词后包含的单词个数.
     */
    public void setIgWordsToIncludeAfterKeyword(final int i) {
        this.igWordsToIncludeAfterKeyword = i;
    }

    /**
     * 获取是否对分类进行解释.
     *
     * @return 是否对分类进行解释.
     */
    public boolean isBgExplainClassification() {
        return bgExplainClassification;
    }

    /**
     * 设置是否对分类进行解释.
     *
     * @param b 是否对分类进行解释.
     */
    public void setBgExplainClassification(final boolean b) {
        this.bgExplainClassification = b;
    }

    /**
     * 获取是否输出原文本.
     *
     * @return 是否输出原文本.
     */
    public boolean isBgEchoText() {
        return bgEchoText;
    }

    /**
     * 设置是否输出原文本.
     *
     * @param b 是否输出原文本.
     */
    public void setBgEchoText(final boolean b) {
        this.bgEchoText = b;
    }

    /**
     * 获取是否按UTF-8编码文本.
     *
     * @return 是否按UTF -8编码文本.
     */
    public boolean isBgForceUTF8() {
        return bgForceUTF8;
    }

    /**
     * 设置是否按UTF-8编码文本.
     *
     * @param b 是否按UTF-8编码文本.
     */
    public void setBgForceUTF8(final boolean b) {
        this.bgForceUTF8 = b;
    }

    /**
     * 获取是否使用词形还原.
     *
     * @return 是否使用词形还原.
     */
    public boolean isBgUseLemmatisation() {
        return bgUseLemmatisation;
    }

    /**
     * 设置是否使用词形还原.
     *
     * @param b 是否使用词形还原.
     */
    public void setBgUseLemmatisation(final boolean b) {
        this.bgUseLemmatisation = b;
    }

    /**
     * 获取在积极句子中的表示讽刺的引用需要的最小得分.
     *
     * @return 在积极句子中的表示讽刺的引用需要的最小得分.
     */
    public int getIgMinSentencePosForQuotesIrony() {
        return igMinSentencePosForQuotesIrony;
    }

    /**
     * 设置在积极句子中的表示讽刺的引用需要的最小得分.
     *
     * @param i 在积极句子中的表示讽刺的引用需要的最小得分.
     */
    public void setIgMinSentencePosForQuotesIrony(final int i) {
        this.igMinSentencePosForQuotesIrony = i;
    }

    /**
     * 获取在积极句子中的表示讽刺的标点需要的最小得分.
     *
     * @return 在积极句子中的表示讽刺的标点需要的最小得分.
     */
    public int getIgMinSentencePosForPunctuationIrony() {
        return igMinSentencePosForPunctuationIrony;
    }

    /**
     * 设置在积极句子中的表示讽刺的标点需要的最小得分.
     *
     * @param i 在积极句子中的表示讽刺的标点需要的最小得分.
     */
    public void setIgMinSentencePosForPunctuationIrony(final int i) {
        this.igMinSentencePosForPunctuationIrony = i;
    }

    /**
     * 获取在积极句子中的表示讽刺的术语需要的最小得分.
     *
     * @return 在积极句子中的表示讽刺的术语需要的最小得分.
     */
    public int getIgMinSentencePosForTermsIrony() {
        return igMinSentencePosForTermsIrony;
    }

    /**
     * 设置在积极句子中的表示讽刺的术语需要的最小得分.
     *
     * @param i 在积极句子中的表示讽刺的术语需要的最小得分.
     */
    public void setIgMinSentencePosForTermsIrony(final int i) {
        this.igMinSentencePosForTermsIrony = i;
    }

    /**
     * 程序检测的正面值变量.
     */
    private String sgProgramPos = "positive sentiment";
    /**
     * 程序检测的负面值变量.
     */
    private String sgProgramNeg = "negative sentiment";
    /**
     * 是否使用Scale模式.
     */
    private boolean bgScaleMode = false;
    /**
     * 是否使用二元分类.
     */
    private boolean bgTrinaryMode = false;
    /**
     * 是否使用三元分类.
     */
    private boolean bgBinaryVersionOfTrinaryMode = false;
    /**
     * 默认的二元分类结果.
     */
    private int igDefaultBinaryClassification = 1;
    /**
     * 表示采用何种方式计算计算段落的情感强度.
     */
    private int igEmotionParagraphCombineMethod = 0;
    /**
     * 表示采用段落中所有句子的情感强度的最大值.
     */
    private final int igCombineMax = 0;
    /**
     * 表示采用段落中所有句子的情感强度的平均值.
     */
    private final int igCombineAverage = 1;
    /**
     * 表示采用段落中所有句子的情感强度的总和.
     */
    private final int igCombineTotal = 2;
    /**
     * 表示采用何种方式计算句子的情感强度.
     */
    private int igEmotionSentenceCombineMethod = 0;

    /**
     * 默认的消极强度因子.
     */
    private static final float DEFAULT_NEGATIVE_SENTIMENT_MULTIPLIER = 1.5F;

    /**
     * 消极强度因子，当积极情绪强度大于消极强度乘以这个因子时，才判断整体为积极.
     */
    private float fgNegativeSentimentMultiplier =
            getDefaultNegativeSentimentMultiplier();
    /**
     * 是否减少疑问句中的消极情绪.
     */
    private boolean bgReduceNegativeEmotionInQuestionSentences = false;
    /**
     * Miss这个词为强度是否为正2.
     */
    private boolean bgMissCountsAsPlus2 = true;
    /**
     * you和your这两个词强度是否为正2，除非是否定句.
     */
    private boolean bgYouOrYourIsPlus2UnlessSentenceNegative = false;
    /**
     * 中性句子中的感叹号是否设为正2.
     */
    private boolean bgExclamationInNeutralSentenceCountsAsPlus2 = false;
    /**
     * 能增强句子情感强度的带感叹号的最少标点数目.
     */
    private int igMinPunctuationWithExclamationToChangeSentenceSentiment = 0;
    /**
     * 是否使用习语表.
     */
    private boolean bgUseIdiomLookupTable = true;
    /**
     * 是否使用对象评估表.
     */
    private boolean bgUseObjectEvaluationTable = false;
    /**
     * 是否将中性情绪计算为1的积极情绪.
     */
    private boolean bgCountNeutralEmotionsAsPositiveForEmphasis1 = true;
    /**
     * 将中性情绪翻译为多大的强度.
     */
    private int igMoodToInterpretNeutralEmphasis = 1;
    /**
     * 是否允许多个积极词增强积极情绪.
     */
    private boolean bgAllowMultiplePositiveWordsToIncreasePositiveEmotion =
            true;
    /**
     * 是否允许多个否定词增强否定情绪.
     */
    private boolean bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion =
            true;
    /**
     * 是否忽略否定后的增强词.
     */
    private boolean bgIgnoreBoosterWordsAfterNegatives = true;
    /**
     * 是否使用词典进行拼写纠正.
     */
    private boolean bgCorrectSpellingsUsingDictionary = true;
    /**
     * 是否纠正多余字母的拼写错误.
     */
    private boolean bgCorrectExtraLetterSpellingErrors = true;
    /**
     * 单词中的非法双字母部分，即不可能在单词中部连续出现两次的字母.
     */
    private String sgIllegalDoubleLettersInWordMiddle = "ahijkquvxyz";
    /**
     * 不可能在单词结尾出现两次的字母.
     */
    private String sgIllegalDoubleLettersAtWordEnd = "achijkmnpqruvwxyz";
    /**
     * 是否重复字母增强情感.
     */
    private boolean bgMultipleLettersBoostSentiment = true;
    /**
     * 增强词是否改变情感.
     */
    private boolean bgBoosterWordsChangeEmotion = true;
    /**
     * 是否总是使用撇号分割单词.
     */
    private boolean bgAlwaysSplitWordsAtApostrophes = false;
    /**
     * 否定词是否出现在情感词前面.
     */
    private boolean bgNegatingWordsOccurBeforeSentiment = true;
    /**
     * 在否定词和情感词之间的最大单词数.
     */
    private int igMaxWordsBeforeSentimentToNegate = 0;
    /**
     * 否定词是否出现在情感词后面.
     */
    private boolean bgNegatingWordsOccurAfterSentiment = false;
    /**
     * 情感词和否定词之间的最大单词数.
     */
    private int igMaxWordsAfterSentimentToNegate = 0;
    /**
     * 是否使用否定词反转积极情绪.
     */
    private boolean bgNegatingPositiveFlipsEmotion = true;
    /**
     * 是否使用否定词将消极情绪转为中性.
     */
    private boolean bgNegatingNegativeNeutralisesEmotion = true;
    /**
     * 是否使用否定词反转情绪.
     */
    private boolean bgNegatingWordsFlipEmotion = false;

    /**
     * 默认的否定词强度因子.
     */
    private static final float DEFAULT_STRENGTH_MULTIPLIER_FOR_NEGATED_WORDS =
            0.5F;
    /**
     * 否定词的强度因子.
     */
    private float fgStrengthMultiplierForNegatedWords =
            getDefaultStrengthMultiplierForNegatedWords();
    /**
     * 是否纠正重复字母的单词.
     */
    private boolean bgCorrectSpellingsWithRepeatedLetter = true;
    /**
     * 是否使用表情符.
     */
    private boolean bgUseEmoticons = true;
    /**
     * 大写字母是否增强术语的强度.
     */
    private boolean bgCapitalsBoostTermSentiment = false;

    /**
     * 默认的增强情感所需要的最小重复字母数.
     */
    private static final int DEFAULT_MIN_REPEATED_LETTERS_FOR_BOOST = 2;
    /**
     * 增强情感所需要的最小重复字母数.
     */
    private int igMinRepeatedLettersForBoost =
            getDefaultMinRepeatedLettersForBoost();
    /**
     * 关键词列表.
     */
    private String[] sgSentimentKeyWords = null;
    /**
     * 是否忽视没有关键词的句子.
     */
    private boolean bgIgnoreSentencesWithoutKeywords = false;

    /**
     * 默认的关键词之前包含的单词个数.
     */
    private static final int DEFAULT_WORDS_TO_INCLUDE_BEFORE_KEYWORD = 4;
    /**
     * 关键词之前包含的单词个数.
     */
    private int igWordsToIncludeBeforeKeyword =
            getDefaultWordsToIncludeBeforeKeyword();

    /**
     * 默认的关键词之后包含的单词个数.
     */
    private static final int DEFAULT_WORDS_TO_INCLUDE_AFTER_KEYWORD = 4;
    /**
     * 关键词后包含的单词个数.
     */
    private int igWordsToIncludeAfterKeyword =
            getDefaultWordsToIncludeAfterKeyword();
    /**
     * 是否对分类进行解释.
     */
    private boolean bgExplainClassification = false;
    /**
     * 是否输出原文本.
     */
    private boolean bgEchoText = false;
    /**
     * 是否按UTF-8编码文本.
     */
    private boolean bgForceUTF8 = false;
    /**
     * 是否使用词形还原.
     */
    private boolean bgUseLemmatisation = false;

    /**
     * 默认的在积极句子中的表示讽刺的引用需要的最小得分.
     */
    private static final int DEFAULT_MIN_SENTENCE_POS_FOR_QUOTES_IRONY = 10;
    /**
     * 在积极句子中的表示讽刺的引用需要的最小得分.
     */
    private int igMinSentencePosForQuotesIrony =
            getDefaultMinSentencePosForQuotesIrony();

    /**
     * 默认的在积极句子中的表示讽刺的标点需要的最小得分.
     */
    private static final int DEFAULT_MIN_SENTENCE_POS_FOR_PUNCTUATION_IRONY =
            10;
    /**
     * 在积极句子中的表示讽刺的标点需要的最小得分.
     */
    private int igMinSentencePosForPunctuationIrony =
            getDefaultMinSentencePosForPunctuationIrony();

    /**
     * 默认的在积极句子中的表示讽刺的术语需要的最小得分.
     */
    private static final int DEFAULT_MIN_SENTENCE_POS_FOR_TERMS_IRONY = 10;
    /**
     * 在积极句子中的表示讽刺的术语需要的最小得分.
     */
    private int igMinSentencePosForTermsIrony =
            getDefaultMinSentencePosForTermsIrony();

    /**
     * 构造方法.
     */
    public ClassificationOptions() {
    }

    /**
     * 设置关键词列表.
     * <br>
     * 处理用逗号分隔的关键词序列字符串.
     *
     * @param sKeywordList 用逗号分隔的关键词序列字符串
     */
    public void parseKeywordList(final String sKeywordList) {
        this.setSgSentimentKeyWords(sKeywordList.split(","));
        this.setBgIgnoreSentencesWithoutKeywords(true);
    }

    /**
     * 打印文本分类器选项.
     * <br>
     *
     * @param wWriter             缓冲流
     * @param iMinImprovement     在训练阶段调整术语的权重所需要的额外正确分类的最小个数
     * @param bUseTotalDifference 是否使用预测值和实际值的差别进行优化
     * @param iMultiOptimisations 优化的次数
     * @return 是否打印成功
     */
    public boolean printClassificationOptions(final BufferedWriter wWriter,
                                              final int iMinImprovement,
                                              final boolean bUseTotalDifference,
                                              final int iMultiOptimisations) {
        try {

            if (this.getIgEmotionParagraphCombineMethod() == 0) {
                wWriter.write("Max");
            } else if (this.getIgEmotionParagraphCombineMethod() == 1) {
                wWriter.write("Av");
            } else {
                wWriter.write("Tot");
            }

            if (this.getIgEmotionSentenceCombineMethod() == 0) {
                wWriter.write("\tMax");
            } else if (this.getIgEmotionSentenceCombineMethod() == 1) {
                wWriter.write("\tAv");
            } else {
                wWriter.write("\tTot");
            }

            if (bUseTotalDifference) {
                wWriter.write("\tTotDiff");
            } else {
                wWriter.write("\tExactCount");
            }

            wWriter.write("\t" + iMultiOptimisations + "\t"
                    + this.isBgReduceNegativeEmotionInQuestionSentences() + "\t"
                    + this.isBgMissCountsAsPlus2() + "\t"
                    + this.isBgYouOrYourIsPlus2UnlessSentenceNegative() + "\t"
                    + this.isBgExclamationInNeutralSentenceCountsAsPlus2()
                    + "\t" + this.isBgUseIdiomLookupTable() + "\t"
                    + this.getIgMoodToInterpretNeutralEmphasis() + "\t"
                    + this.
                    isBgAllowMultiplePositiveWordsToIncreasePositiveEmotion()
                    + "\t"
                    + this.
                    isBgAllowMultipleNegativeWordsToIncreaseNegativeEmotion()
                    + "\t" + this.isBgIgnoreBoosterWordsAfterNegatives() + "\t"
                    + this.isBgMultipleLettersBoostSentiment() + "\t"
                    + this.isBgBoosterWordsChangeEmotion() + "\t"
                    + this.isBgNegatingWordsFlipEmotion() + "\t"
                    + this.isBgNegatingPositiveFlipsEmotion() + "\t"
                    + this.isBgNegatingNegativeNeutralisesEmotion() + "\t"
                    + this.isBgCorrectSpellingsWithRepeatedLetter() + "\t"
                    + this.isBgUseEmoticons() + "\t"
                    + this.isBgCapitalsBoostTermSentiment() + "\t"
                    + this.getIgMinRepeatedLettersForBoost() + "\t"
                    + this.getIgMaxWordsBeforeSentimentToNegate() + "\t"
                    + iMinImprovement);
            return true;
        } catch (IOException var6) {
            var6.printStackTrace();
            return false;
        }
    }

    /**
     * 打印空分类器选项.
     * <br>
     *
     * @param wWriter 缓冲流
     * @return 是否打印成功
     */
    public boolean printBlankClassificationOptions(
            final BufferedWriter wWriter) {
        try {
            wWriter.write("~");
            wWriter.write("\t~");
            wWriter.write("\tBaselineMajorityClass");
            wWriter.write(
                    "\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~"
                            + "\t~\t~\t~\t~\t~\t~\t~");
            return true;
        } catch (IOException var3) {
            var3.printStackTrace();
            return false;
        }
    }

    /**
     * 打印分类器选项头部.
     * <br>
     *
     * @param wWriter 缓冲流
     * @return 是否打印成功
     */
    public boolean printClassificationOptionsHeadings(
            final BufferedWriter wWriter) {
        try {
            wWriter.write(
        "EmotionParagraphCombineMethod\t"
                + "EmotionSentenceCombineMethod\t"
                + "DifferenceCalculationMethodForTermWeightAdjustments\t"
             + "MultiOptimisations\tReduceNegativeEmotionInQuestionSentences\t"
                + "MissCountsAsPlus2\tYouOrYourIsPlus2UnlessSentenceNegative\t"
                + "ExclamationCountsAsPlus2\t"
                + "UseIdiomLookupTable\t"
                + "MoodToInterpretNeutralEmphasis\t"
                + "AllowMultiplePositiveWordsToIncreasePositiveEmotion\t"
                + "AllowMultipleNegativeWordsToIncreaseNegativeEmotion\t"
                + "IgnoreBoosterWordsAfterNegatives\t"
                + "MultipleLettersBoostSentiment\t"
                + "BoosterWordsChangeEmotion\t"
                + "NegatingWordsFlipEmotion\tNegatingPositiveFlipsEmotion\t"
                + "NegatingNegativeNeutralisesEmotion\t"
                + "CorrectSpellingsWithRepeatedLetter\t"
                + "UseEmoticons\t"
                + "CapitalsBoostTermSentiment\t"
                + "MinRepeatedLettersForBoost\t"
                + "WordsBeforeSentimentToNegate\t"
                + "MinImprovement");
            return true;
        } catch (IOException var3) {
            var3.printStackTrace();
            return false;
        }
    }

    /**
     * 设置文本分类器选项.
     * <br>从某个文件读取其分类器选项.<br>
     * UC-26 Set Classification Algorithm Parameters<br>
     *
     * @param sFilename 文本分类器选项文件路径
     * @return 是否设置成功
     */
    public boolean setClassificationOptions(final String sFilename) {
        try {
            BufferedReader rReader =
                    new BufferedReader(new FileReader(sFilename));
            while (rReader.ready()) {
                String sLine = rReader.readLine();
                int iTabPos = sLine.indexOf("\t");
                if (iTabPos > 0) {
                    String[] sData = sLine.split("\t");
                    if (Objects.equals(sData[0],
                            "EmotionParagraphCombineMethod")) {
                        if (sData[1].contains("Max")) {
                            this.setIgEmotionParagraphCombineMethod(0);
                        }

                        if (sData[1].contains("Av")) {
                            this.setIgEmotionParagraphCombineMethod(1);
                        }

                        if (sData[1].contains("Tot")) {
                            this.setIgEmotionParagraphCombineMethod(2);
                        }
                    } else if (Objects.equals(sData[0],
                            "EmotionSentenceCombineMethod")) {
                        if (sData[1].contains("Max")) {
                            this.setIgEmotionSentenceCombineMethod(0);
                        }
                        if (sData[1].contains("Av")) {
                            this.setIgEmotionSentenceCombineMethod(1);
                        }
                        if (sData[1].contains("Tot")) {
                            this.setIgEmotionSentenceCombineMethod(2);
                        }
                    } else if (Objects.equals(sData[0],
                            "IgnoreNegativeEmotionInQuestionSentences")) {
                        this.setBgReduceNegativeEmotionInQuestionSentences(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0], "MissCountsAsPlus2")) {
                        this.setBgMissCountsAsPlus2(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0],
                            "YouOrYourIsPlus2UnlessSentenceNegative")) {
                        this.setBgYouOrYourIsPlus2UnlessSentenceNegative(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0],
                            "ExclamationCountsAsPlus2")) {
                        this.setBgExclamationInNeutralSentenceCountsAsPlus2(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0],
                            "UseIdiomLookupTable")) {
                        this.setBgUseIdiomLookupTable(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0], "Mood")) {
                        this.setIgMoodToInterpretNeutralEmphasis(
                                Integer.parseInt(sData[1]));
                    } else if (Objects.equals(sData[0],
                    "AllowMultiplePositiveWordsToIncreasePositiveEmotion")) {
                    this.
                    setBgAllowMultiplePositiveWordsToIncreasePositiveEmotion(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0],
                    "AllowMultipleNegativeWordsToIncreaseNegativeEmotion")) {
                    this.
                    setBgAllowMultipleNegativeWordsToIncreaseNegativeEmotion(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0],
                            "IgnoreBoosterWordsAfterNegatives")) {
                        this.setBgIgnoreBoosterWordsAfterNegatives(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0],
                            "MultipleLettersBoostSentiment")) {
                        this.setBgMultipleLettersBoostSentiment(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0],
                            "BoosterWordsChangeEmotion")) {
                        this.setBgBoosterWordsChangeEmotion(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0],
                            "NegatingWordsFlipEmotion")) {
                        this.setBgNegatingWordsFlipEmotion(
                                Boolean.parseBoolean(sData[1]));
                        this.setBgNegatingPositiveFlipsEmotion(
                                Boolean.parseBoolean(sData[1]));
                        this.setBgNegatingNegativeNeutralisesEmotion(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0],
                            "CorrectSpellingsWithRepeatedLetter")) {
                        this.setBgCorrectSpellingsWithRepeatedLetter(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0], "UseEmoticons")) {
                        this.setBgUseEmoticons(Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0],
                            "CapitalsAreSentimentBoosters")) {
                        this.setBgCapitalsBoostTermSentiment(
                                Boolean.parseBoolean(sData[1]));
                    } else if (Objects.equals(sData[0],
                            "MinRepeatedLettersForBoost")) {
                        this.setIgMinRepeatedLettersForBoost(
                                Integer.parseInt(sData[1]));
                    } else if (Objects.equals(sData[0],
                            "WordsBeforeSentimentToNegate")) {
                        this.setIgMaxWordsBeforeSentimentToNegate(
                                Integer.parseInt(sData[1]));
                    } else if (Objects.equals(sData[0], "Trinary")) {
                        this.setBgTrinaryMode(true);
                    } else if (Objects.equals(sData[0], "Binary")) {
                        this.setBgTrinaryMode(true);
                        this.setBgBinaryVersionOfTrinaryMode(true);
                    } else {
                        if (!Objects.equals(sData[0], "Scale")) {
                            rReader.close();
                            return false;
                        }

                        this.setBgScaleMode(true);
                    }
                }
            }

            rReader.close();
            return true;
        } catch (IOException var7) {
            var7.printStackTrace();
            return false;
        }
    }

    /**
     * 设置分类器模式.
     * <br>
     * 根据bTensiStrength来改变程序变量以设置模式.<br>
     * UC-26 Set Classification Algorithm Parameters<br>
     *
     * @param bTensiStrength 是否是焦虑强度模式
     */
    public void nameProgram(final boolean bTensiStrength) {
        this.setBgTensiStrength(bTensiStrength);
        if (bTensiStrength) {
            this.setSgProgramName("TensiStrength");
            this.setSgProgramMeasuring("stress and relaxation");
            this.setSgProgramPos("relaxation");
            this.setSgProgramNeg("stress");
        } else {
            this.setSgProgramName("SentiStrength");
            this.setSgProgramMeasuring("sentiment");
            this.setSgProgramPos("positive sentiment");
            this.setSgProgramNeg("negative sentiment");
        }

    }
}
