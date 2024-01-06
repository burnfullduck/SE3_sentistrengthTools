package rainbowsix.ss;

import java.util.Objects;

/**
 * 术语类.
 * <br>
 * 需要分析的单词,标点符号和表情符号都被称为一个术语,并有其对应的类型和相关信息. <br>
 * UC-1 Assigning Sentiment Scores for Words<br>
 * UC-2 Assigning Sentiment Scores for Phrases<br>
 * UC-3 Spelling Correction<br>
 * UC-6 Repeated Letter Rule<br>
 * UC-7 Emoji Rule<br>
 * UC-9 Repeated Punctuation Rule<br>
 *
 * @author 注释添加:hcx
 */
public class Term {
    /**
     * 内容类型.
     */
    private int igContentType = 0;
    /**
     * 最初的单词.
     */
    private String sgOriginalWord = "";
    /**
     * 单词小写形式.
     */
    private String sgLCaseWord = "";
    /**
     * 翻译后的单词.
     */
    private String sgTranslatedWord = "";
    /**
     * 提取单词中重复的部分.
     */
    private String sgWordEmphasis = "";
    /**
     * 单次的索引id.
     */
    private int igWordSentimentID = 0;
    /**
     * 是否是否定词.
     */
    private boolean bgNegatingWord = false;
    /**
     * 若是否定词，是否被计算过.
     */
    private boolean bgNegatingWordCalculated = false;
    /**
     * 是否被计算过索引id.
     */
    private boolean bgWordSentimentIDCalculated = false;
    /**
     * 是否是个正确拼写的单词.
     */
    private boolean bgProperNoun = false;
    /**
     * 若正确拼写，是否被计算过.
     */
    private boolean bgProperNounCalculated = false;
    /**
     * 标点部分.
     */
    private String sgPunctuation = "";
    /**
     * 标点的重复部分.
     */
    private String sgPunctuationEmphasis = "";
    /**
     * 表情符部分.
     */
    private String sgEmoticon = "";
    /**
     * 表情符的强度.
     */
    private int igEmoticonStrength = 0;
    /**
     * 增强词的默认分数.
     */
    private static final int BOOST_WORD_SCORE_INITIAL = 999;
    /**
     * 增强词的分数.
     */
    private int igBoosterWordScore = BOOST_WORD_SCORE_INITIAL;
    /**
     * 分类所需的资源.
     */
    private ClassificationResources resources;
    /**
     * 文本分类器选项.
     */
    private ClassificationOptions options;
    /**
     * 是否全大写.
     */
    private boolean bgAllCapitals = false;
    /**
     * 若全大写，是否被计算过.
     */
    private boolean bgAllCaptialsCalculated = false;
    /**
     * 分数是否被重新计算过.
     */
    private boolean bgOverrideSentimentScore = false;
    /**
     * 重新计算后的得分.
     */
    private int igOverrideSentimentScore = 0;
    /**
     * 词类型.
     */
    private static final int WORD = 1;
    /**
     * 标点符号类型.
     */
    private static final int PUNCTUATION = 2;
    /**
     * 表情符号类型.
     */
    private static final int EMOJI = 3;
    /**
     * 最大长度.
     */
    private static final int MAX_LENGTH = 5;
    /**
     * 截取字符串的起点.
     */
    private static final int CUT_START_POSITION = 3;

    /**
     * 提取下一个单词,标点符号或表情符号.
     * <br>
     * 从给定的文本中提取出下一个单词,标点符号或表情符号,并将分类器资源和分类器选项设置为给定的资源和选项. <br>
     *
     * @param sWordAndPunctuation 需要提取的文本
     * @param classResources      分类器资源
     * @param classOptions        分类器选项
     * @return 提取出的单词或标点符号或表情符号的最后一个字符的位置.如果无法提取则返回-1.
     */
    public int extractNextWordOrPunctuationOrEmoticon(
            final String sWordAndPunctuation,
            final ClassificationResources classResources,
            final ClassificationOptions classOptions) {
        int iPos = 0;
        int iLastCharType = 0;
        String sChar;
        this.resources = classResources;
        this.options = classOptions;
        int iTextLength = sWordAndPunctuation.length();
        if (!this.codeEmoticon(sWordAndPunctuation)) {
            for (; iPos < iTextLength; ++iPos) {
                sChar = sWordAndPunctuation.substring(iPos, iPos + 1);
                if (Character.isLetterOrDigit(sWordAndPunctuation.charAt(iPos))
                        || (!this.options.isBgAlwaysSplitWordsAtApostrophes()
                        && sChar.equals("'") && iPos > 0
                        && iPos < iTextLength - 1 && Character.isLetter(
                        sWordAndPunctuation.charAt(iPos + 1))) || sChar.equals(
                        "$") || sChar.equals("£") || sChar.equals("@")
                        || sChar.equals("_")) {
                    if (iLastCharType == 2) {
                        this.codePunctuation(
                                sWordAndPunctuation.substring(0, iPos));
                        return iPos;
                    }
                    iLastCharType = 1;
                } else {
                    if (iLastCharType == 1) {
                        this.codeWord(sWordAndPunctuation.substring(0, iPos));
                        return iPos;
                    }

                    iLastCharType = 2;
                }
            }

            switch (iLastCharType) {
                case 1 -> this.codeWord(sWordAndPunctuation);
                case 2 -> this.codePunctuation(sWordAndPunctuation);
                default -> {
                    int i = 1; //没用的
                }
            }

        }
        return -1;
    }

    /**
     * 获取html的对应Tag.
     * <br>
     * 根据术语对象的类型返回对应的被html标签包围的对应文本. <br>
     *
     * @return 被对应tag包围的单词, 标点符号或表情符号.
     */
    public String getTag() {
        switch (this.igContentType) {
            case WORD -> {
                if (!Objects.equals(this.sgWordEmphasis, "")) {
                    return "<w equiv=\"" + this.sgTranslatedWord + "\" em=\""
                            + this.sgWordEmphasis + "\">" + this.sgOriginalWord
                            + "</w>";
                }
                return "<w>" + this.sgOriginalWord + "</w>";
            }
            case PUNCTUATION -> {
                if (!Objects.equals(this.sgPunctuationEmphasis, "")) {
                    return "<p equiv=\"" + this.sgPunctuation + "\" em=\""
                            + this.sgPunctuationEmphasis + "\">"
                            + this.sgPunctuation + this.sgPunctuationEmphasis
                            + "</p>";
                }
                return "<p>" + this.sgPunctuation + "</p>";
            }
            case EMOJI -> {
                if (this.getIgEmoticonStrength() == 0) {
                    return "<e>" + this.sgEmoticon + "</e>";
                } else {
                    if (this.getIgEmoticonStrength() == 1) {
                        return "<e em=\"+\">" + this.sgEmoticon + "</e>";
                    }

                    return "<e em=\"-\">" + this.sgEmoticon + "</e>";
                }
            }
            default -> {
                return "";
            }
        }
    }

    /**
     * 获取情绪词ID.
     * <br>
     * UC-1 Assigning Sentiment Scores for Words<br>
     * UC-2 Assigning Sentiment Scores for Phrases<br>
     *
     * @return 对应的情绪词id
     */
    public int getSentimentID() {
        if (!this.bgWordSentimentIDCalculated) {
            this.igWordSentimentID = this.resources.getSentimentWords()
                    .getSentimentID(this.sgTranslatedWord.toLowerCase());
            this.bgWordSentimentIDCalculated = true;
        }
        return this.igWordSentimentID;
    }

    /**
     * 设置重写的情绪得分.
     * <br>将本术语的情绪得分重写为指定的数值.
     *
     * @param iSentiment 情绪分数
     */
    public void setSentimentOverrideValue(final int iSentiment) {
        this.bgOverrideSentimentScore = true;
        this.igOverrideSentimentScore = iSentiment;
    }

    /**
     * 获取情绪得分.
     * <br>
     * 如果已经被重写,则返回重写后的得分;否则返回原来的得分.
     *
     * @return 情绪得分
     */
    public int getSentimentValue() {
        if (this.bgOverrideSentimentScore) {
            return this.igOverrideSentimentScore;
        } else {
            return this.getSentimentID() < 1 ? 0
                    : this.resources.getSentimentWords()
                            .getSentiment(this.igWordSentimentID);
        }
    }

    /**
     * 获取单词的重复强调长度.
     *
     * @return 返回单词的重复强调长度
     */
    public int getWordEmphasisLength() {
        return this.sgWordEmphasis.length();
    }

    /**
     * 获取单词的重复强调部分.
     *
     * @return 返回单词的重复强调部分.
     */
    public String getWordEmphasis() {
        return this.sgWordEmphasis;
    }

    /**
     * 判断单词是否有重复强调部分.
     * <br>
     * UC-6 Repeated Letter Rule<br>
     * UC-9 Repeated Punctuation Rule<br>
     *
     * @return 有重复强调部分true, 否则false
     */
    public boolean containsEmphasis() {
        if (this.igContentType == 1) {
            return this.sgWordEmphasis.length() > 1;
        } else if (this.igContentType == 2) {
            return this.sgPunctuationEmphasis.length() > 1;
        } else {
            return false;
        }
    }

    /**
     * 获取翻译后的单词.
     *
     * @return 翻译后的单词.
     */
    public String getTranslatedWord() {
        return this.sgTranslatedWord;
    }

    /**
     * 获取翻译.
     * <br>
     * 如果是单词返回其翻译结果,如果是标点符号或表情符号,则返回其本身,都不是,则返回空字符串. <br>
     *
     * @return 翻译结果
     */
    public String getTranslation() {
        if (this.igContentType == 1) {
            return this.sgTranslatedWord;
        } else if (this.igContentType == 2) {
            return this.sgPunctuation;
        } else {
            return this.igContentType == EMOJI ? this.sgEmoticon : "";
        }
    }

    /**
     * 获取增强词的分数.
     * <br>
     * 如果没有保存过增强词则先获取增强词分数,然后返回增强词分数.
     *
     * @return 增强词的分数
     */
    public int getBoosterWordScore() {
        if (this.igBoosterWordScore == BOOST_WORD_SCORE_INITIAL) {
            this.setBoosterWordScore();
        }

        return this.igBoosterWordScore;
    }

    /**
     * 判断术语是否全部大写.
     * <br>
     * 如果该术语全部都是大写,则将对应成员变量标记为true,否则标记为false;并返回结果
     *
     * @return 本术语是否全部为大写
     */
    public boolean isAllCapitals() {
        if (!this.bgAllCaptialsCalculated) {
            this.bgAllCapitals = Objects.equals(this.sgOriginalWord,
                    this.sgOriginalWord.toUpperCase());

            this.bgAllCaptialsCalculated = true;
        }

        return this.bgAllCapitals;
    }

    /**
     * 从增强词表中查询增强词的分数并保存在此对象中.
     * <br>
     * UC-1 Assigning Sentiment Scores for Words<br>
     * UC-2 Assigning Sentiment Scores for Phrases<br>
     */
    public void setBoosterWordScore() {
        this.igBoosterWordScore = this.resources.getBoosterWords()
                .getBoosterStrength(this.sgTranslatedWord);
    }

    /**
     * 判断给定文本中是否含有特定标点符号.
     * <br>
     * 如果本术语不是标点则返回false;如果文本中含有本术语对应的标点符号或标点符号的重复强调,返回true,否则返回false
     *
     * @param sPunctuation 需要判断是否含有标点的文本
     * @return 是否含有对应标点或标点强调
     */
    public boolean punctuationContains(final String sPunctuation) {
        if (this.igContentType != 2) {
            return false;
        } else if (this.sgPunctuation.contains(sPunctuation)) {
            return true;
        } else {
            return !Objects.equals(this.sgPunctuationEmphasis, "")
                    && this.sgPunctuationEmphasis.contains(sPunctuation);
        }
    }

    /**
     * 获取标点符号的重复强调的长度.
     *
     * @return 标点符号的重复强调长度.
     */
    public int getPunctuationEmphasisLength() {
        return this.sgPunctuationEmphasis.length();
    }

    /**
     * 获取表情符号的情绪强度.
     *
     * @return 表情符号的情绪强度.
     */
    public int getEmoticonSentimentStrength() {
        return this.getIgEmoticonStrength();
    }

    /**
     * 获取表情符号.
     *
     * @return 表情符号的字符串
     */
    public String getEmoticon() {
        return this.sgEmoticon;
    }

    /**
     * 获取翻译后的标点符号.
     *
     * @return 本术语对应的标点符号
     */
    public String getTranslatedPunctuation() {
        return this.sgPunctuation;
    }

    /**
     * 判断是否是单词.
     *
     * @return 本术语是否是单词
     */
    public boolean isWord() {
        return this.igContentType == WORD;
    }

    /**
     * 判断是否是标点符号.
     *
     * @return 本术语是否是标点符号.
     */
    public boolean isPunctuation() {
        return this.igContentType == PUNCTUATION;
    }

    /**
     * 判断是否是正确的名词.
     * <br>
     * 如果不是单词,直接返回false;如果还没有判断过就根据单词的首字母和后续字母的大小写规则是否符合标准来判断是不是规范的名词,
     * 并修改bgProperNoun后返回.<br>
     * UC-3 Spelling Correction<br>
     *
     * @return 是否是正确的名词.
     */
    public boolean isProperNoun() {
        if (this.igContentType != WORD) {
            return false;
        } else {
            if (!this.bgProperNounCalculated) {
                if (this.sgOriginalWord.length() > 1) {
                    String sFirstLetter = this.sgOriginalWord.substring(0, 1);
                    if (!sFirstLetter.toLowerCase()
                            .equals(sFirstLetter.toUpperCase())
                            && !this.sgOriginalWord.substring(0, 2)
                            .equalsIgnoreCase("I'")) {
                        String sWordRemainder = this.sgOriginalWord.substring(
                                1);
                        if (sFirstLetter.equals(sFirstLetter.toUpperCase())
                                && sWordRemainder.equals(
                                sWordRemainder.toLowerCase())) {
                            this.bgProperNoun = true;
                        }
                    }
                }

                this.bgProperNounCalculated = true;
            }

            return this.bgProperNoun;
        }
    }

    /**
     * 判断是否是表情符号.
     *
     * @return 是否是表情符号
     */
    public boolean isEmoticon() {
        return this.igContentType == EMOJI;
    }

    /**
     * 获取文本.
     * <br>
     * 如果是单词就返回小写形式,如果是标点符号或表情就直接返回,如果都不是则返回空字符串. <br>
     *
     * @return 术语的文本
     */
    public String getText() {
        if (this.igContentType == WORD) {
            return this.sgTranslatedWord.toLowerCase();
        } else if (this.igContentType == PUNCTUATION) {
            return this.sgPunctuation;
        } else {
            return this.igContentType == EMOJI ? this.sgEmoticon : "";
        }
    }

    /**
     * 获取原始文本.
     * <br>
     * 如果是文本则返回原始文本,如果是标点符号则返回标点加上重复部分,如果是表情符号就直接返回.如果都不是,则返回空字符串. <br>
     *
     * @return 术语的原始文本
     */
    public String getOriginalText() {
        if (this.igContentType == WORD) {
            return this.sgOriginalWord;
        } else if (this.igContentType == PUNCTUATION) {
            return this.sgPunctuation + this.sgPunctuationEmphasis;
        } else {
            return this.igContentType == EMOJI ? this.sgEmoticon : "";
        }
    }

    /**
     * 判断是否为否定词.
     * <br>
     * 如果没有判断过是否为否定词,首先判断是否转换成为小写形式,如果没有则先转换,然后通过和否定词表的对比判断本文是否为否定词,
     * 然后修改已判断的标记,并将结果保存在对象的相应变量中后返回. <br>
     *
     * @return 术语是否为否定词
     */
    public boolean isNegatingWord() {
        if (!this.bgNegatingWordCalculated) {
            if (this.sgLCaseWord.length() == 0) {
                this.sgLCaseWord = this.sgTranslatedWord.toLowerCase();
            }

            this.bgNegatingWord = this.resources.getNegatingWords()
                    .negatingWord(this.sgLCaseWord);
            this.bgNegatingWordCalculated = true;
        }

        return this.bgNegatingWord;
    }

    /**
     * 判断是否匹配给定文本.
     * <br>
     * 如果长度不一致则返回false;如果需要将术语转化为小写形式,则先转化为小写形式,然后直接进行字符串的比较,并返回结果.
     *
     * @param sText               需要判断是否匹配的文本
     * @param bConvertToLowerCase 是否需要将术语转化为小写形式
     * @return 术语是否匹配给定文本.
     */
    public boolean matchesString(final String sText,
                                 final boolean bConvertToLowerCase) {
        if (sText.length() != this.sgTranslatedWord.length()) {
            return false;
        } else {
            if (bConvertToLowerCase) {
                if (this.sgLCaseWord.length() == 0) {
                    this.sgLCaseWord = this.sgTranslatedWord.toLowerCase();
                }

                return sText.equals(this.sgLCaseWord);
            } else {
                return sText.equals(this.sgTranslatedWord);
            }
        }
    }

    /**
     * 判断是否匹配具有通配符的字符串.
     * <br>
     * 将具有通配符的字符串分段分别与术语进行匹配;根据要求可能会进行小写转换. <br>
     *
     * @param sTextWithWildcard   需要判断是否匹配的含有通配符的字符串
     * @param bConvertToLowerCase 是否需要将术语转化为小写形式
     * @return 术语是否匹配给定文本.
     */
    public boolean matchesStringWithWildcard(String sTextWithWildcard,
                                             final boolean bConvertToLowerCase) {
        int iStarPos = sTextWithWildcard.lastIndexOf("*");
        if (iStarPos >= 0 && iStarPos == sTextWithWildcard.length() - 1) {
            sTextWithWildcard = sTextWithWildcard.substring(0, iStarPos);
            if (bConvertToLowerCase) {
                if (this.sgLCaseWord.length() == 0) {
                    this.sgLCaseWord = this.sgTranslatedWord.toLowerCase();
                }

                if (sTextWithWildcard.equals(this.sgLCaseWord)) {
                    return true;
                }

                if (sTextWithWildcard.length() >= this.sgLCaseWord.length()) {
                    return false;
                }

                return sTextWithWildcard.equals(this.sgLCaseWord.substring(0,
                        sTextWithWildcard.length()));
            } else {
                if (sTextWithWildcard.equals(this.sgTranslatedWord)) {
                    return true;
                }

                if (sTextWithWildcard.length()
                        >= this.sgTranslatedWord.length()) {
                    return false;
                }

                return sTextWithWildcard.equals(
                        this.sgTranslatedWord.substring(0,
                                sTextWithWildcard.length()));
            }
        } else {
            return this.matchesString(sTextWithWildcard, bConvertToLowerCase);
        }
    }

    /**
     * 编码单词.
     * <br>
     * 根据选项要求,对单词进行正确拼写处理,词中/词尾的重复字母处理,翻译处理,拼写检查和词根处理后,将结果保存在对象中.<br>
     * UC-3 Spelling Correction<br>
     * UC-6 Repeated Letter Rule<br>
     *
     * @param sWord 需要编码的单词.
     */
    private void codeWord(final String sWord) {
        StringBuilder sWordNew = new StringBuilder();
        StringBuilder sEm = new StringBuilder();
        if (this.options.isBgCorrectExtraLetterSpellingErrors()) {
            int iSameCount = 0;
            int iLastCopiedPos = 0;
            int iWordEnd = sWord.length() - 1;

            int iPos;
            for (iPos = 1; iPos <= iWordEnd; ++iPos) {
                if (sWord.substring(iPos, iPos + 1)
                        .compareToIgnoreCase(sWord.substring(iPos - 1, iPos))
                        == 0) {
                    ++iSameCount;
                } else {
                    if (iSameCount > 0
                            && this.options.getSgIllegalDoubleLettersInWordMiddle()
                            .contains(sWord.substring(iPos - 1, iPos))) {
                        ++iSameCount;
                    }

                    if (iSameCount > 1) {
                        if (sEm.toString().equals("")) {
                            sWordNew = new StringBuilder(
                                    sWord.substring(0, iPos - iSameCount + 1));
                            sEm = new StringBuilder(
                                    sWord.substring(iPos - iSameCount,
                                            iPos - 1));
                        } else {
                            sWordNew.append(sWord, iLastCopiedPos,
                                    iPos - iSameCount + 1);
                            sEm.append(sWord, iPos - iSameCount, iPos - 1);
                        }
                        iLastCopiedPos = iPos;
                    }

                    iSameCount = 0;
                }
            }

            if (iSameCount > 0
                    && this.options.getSgIllegalDoubleLettersAtWordEnd()
                    .contains(sWord.substring(iPos - 1, iPos))) {
                ++iSameCount;
            }

            if (iSameCount > 1) {
                if (sEm.toString().equals("")) {
                    sWordNew = new StringBuilder(
                            sWord.substring(0, iPos - iSameCount + 1));
                    sEm = new StringBuilder(
                            sWord.substring(iPos - iSameCount + 1));
                } else {
                    sWordNew.append(sWord, iLastCopiedPos,
                            iPos - iSameCount + 1);
                    sEm.append(sWord.substring(iPos - iSameCount + 1));
                }
            } else if (!sEm.toString().equals("")) {
                sWordNew.append(sWord.substring(iLastCopiedPos));
            }
        }

        if (sWordNew.toString().equals("")) {
            sWordNew = new StringBuilder(sWord);
        }

        this.igContentType = 1;
        this.sgOriginalWord = sWord;
        this.sgWordEmphasis = sEm.toString();
        this.sgTranslatedWord = sWordNew.toString();
        if (!this.sgTranslatedWord.contains("@")) {
            if (this.options.isBgCorrectSpellingsUsingDictionary()) {
                this.correctSpellingInTranslatedWord();
            }

            if (this.options.isBgUseLemmatisation()) {
                if (this.sgTranslatedWord.equals("")) {
                    sWordNew = new StringBuilder(this.resources.getLemmatiser()
                            .lemmatise(this.sgOriginalWord));
                    if (!sWordNew.toString().equals(this.sgOriginalWord)) {
                        this.sgTranslatedWord = sWordNew.toString();
                    }
                } else {
                    this.sgTranslatedWord = this.resources.getLemmatiser()
                            .lemmatise(this.sgTranslatedWord);
                }
            }
        }

    }

    /**
     * 对翻译后的单词进行拼写更正.
     * <br>
     * 将翻译后的单词进行拼写检查, 将改正后的单词保存在对象中. 如果翻译的单词过长(大于5), 则检测其中的haha和 hehe,只保留一部分,
     * 剩余的作为重复强调部分.<br>
     * UC-3 Spelling Correction<br>
     */
    private void correctSpellingInTranslatedWord() {
        if (!this.resources.getCorrectSpellings()
                .correctSpelling(this.sgTranslatedWord.toLowerCase())) {
            int iLastChar = this.sgTranslatedWord.length() - 1;

            for (int iPos = 1; iPos <= iLastChar; ++iPos) {
                if (this.sgTranslatedWord.substring(iPos, iPos + 1).compareTo(
                        this.sgTranslatedWord.substring(iPos - 1, iPos)) == 0) {
                    String sReplaceWord =
                            this.sgTranslatedWord.substring(0, iPos)
                                    + this.sgTranslatedWord.substring(iPos + 1);
                    if (this.resources.getCorrectSpellings()
                            .correctSpelling(sReplaceWord.toLowerCase())) {
                        this.sgWordEmphasis = this.sgWordEmphasis
                                + this.sgTranslatedWord.charAt(iPos);
                        this.sgTranslatedWord = sReplaceWord;
                        return;
                    }
                }
            }

            if (iLastChar > MAX_LENGTH) {
                if (this.sgTranslatedWord.indexOf("haha") > 0) {
                    this.sgWordEmphasis = this.sgWordEmphasis
                            + this.sgTranslatedWord.substring(
                            CUT_START_POSITION,
                            this.sgTranslatedWord.indexOf("haha") + 2);
                    this.sgTranslatedWord = "haha";
                    return;
                }

                if (this.sgTranslatedWord.indexOf("hehe") > 0) {
                    this.sgWordEmphasis = this.sgWordEmphasis
                            + this.sgTranslatedWord.substring(
                            CUT_START_POSITION,
                            this.sgTranslatedWord.indexOf("hehe") + 2);
                    this.sgTranslatedWord = "hehe";
                }
            }

        }
    }

    /**
     * 判断是否是表情符.
     * <br>
     * 通过和表情符表的对比判断本文是否为表情符,然后修改已判断的标记,并将结果保存在对象的相应变量中后返回.<br>
     * UC-7 Emoji Rule<br>
     *
     * @param sPossibleEmoticon 要判断的可能是表情符的内容
     * @return 是否是表情符
     */
    private boolean codeEmoticon(final String sPossibleEmoticon) {
        int iEmoticonStrength = this.resources.getEmoticons()
                .getEmoticon(sPossibleEmoticon);
        if (iEmoticonStrength != BOOST_WORD_SCORE_INITIAL) {
            this.igContentType = EMOJI;
            this.sgEmoticon = sPossibleEmoticon;
            this.setIgEmoticonStrength(iEmoticonStrength);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 给标点符号编码.
     * <br>
     * 如果标点符号过长, 将其截断为标点和标点的重复强调两部分, 并将类型置为2.<br>
     * UC-9 Repeated Punctuation Rule<br>
     *
     * @param sPunctuation 需要编码的标点符号
     */
    private void codePunctuation(final String sPunctuation) {
        if (sPunctuation.length() > 1) {
            this.sgPunctuation = sPunctuation.substring(0, 1);
            this.sgPunctuationEmphasis = sPunctuation.substring(1);
        } else {
            this.sgPunctuation = sPunctuation;
            this.sgPunctuationEmphasis = "";
        }

        this.igContentType = 2;
    }

    /**
     * 获取表情符的强度.
     *
     * @return 表情符的强度.
     */
    public int getIgEmoticonStrength() {
        return igEmoticonStrength;
    }

    /**
     * 设置表情符的强度.
     *
     * @param i 表情符的强度.
     */
    public void setIgEmoticonStrength(final int i) {
        this.igEmoticonStrength = i;
    }
}
