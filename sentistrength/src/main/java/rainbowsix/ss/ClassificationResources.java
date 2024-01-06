// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   ClassificationResources.java

package rainbowsix.ss;

import java.io.File;


import rainbowsix.ss.list.BoosterWordsList;
import rainbowsix.ss.list.CorrectSpellingsList;
import rainbowsix.ss.list.EmoticonsList;
import rainbowsix.ss.list.EvaluativeTerms;
import rainbowsix.ss.list.IdiomList;
import rainbowsix.ss.list.IronyList;
import rainbowsix.ss.list.Lemmatiser;
import rainbowsix.ss.list.NegatingWordList;
import rainbowsix.ss.list.QuestionWords;
import rainbowsix.ss.list.SentimentWords;
import rainbowsix.utilities.FileOps;

// Referenced classes of package uk.ac.wlv.ss:
// EmoticonsList, CorrectSpellingsList, SentimentWords, NegatingWordList,
// QuestionWords, BoosterWordsList, IdiomList, EvaluativeTerms,
// IronyList, Lemmatiser, ClassificationOptions

/**
 * 分类器资源类.
 * <br>将分类器需要的各种资源进行初始化和管理。<br>
 * UC-17 Location of linguistic data folder<br>
 * UC-18 Location of sentiment term weights<br>
 *
 * @author 注释编写 徐晨 胡才轩 朱甲豪
 */
public class ClassificationResources {

    /**
     * 表情符号列表.
     */
    private EmoticonsList emoticons;
    /**
     * 正确拼写列表.
     */
    private CorrectSpellingsList correctSpellings;
    /**
     * 情绪词.
     */
    private SentimentWords sentimentWords;
    /**
     * 否定词.
     */
    private NegatingWordList negatingWords;
    /**
     * 疑问词.
     */
    private QuestionWords questionWords;
    /**
     * 强化词.
     */
    private BoosterWordsList boosterWords;
    /**
     * 习语列表.
     */
    private IdiomList idiomList;
    /**
     * 评估项目.
     */
    private EvaluativeTerms evaluativeTerms;
    /**
     * 讽刺词列表.
     */
    private IronyList ironyList;
    /**
     * 词根列表.
     */
    private Lemmatiser lemmatiser;
    /**
     * SentiStrength的文件夹.
     */
    private String sgSentiStrengthFolder;
    /**
     * 情绪词文件.
     */
    private String sgSentimentWordsFile;
    /**
     * 情绪词文件2.
     */
    private String sgSentimentWordsFile2;
    /**
     * 表情符号查询表.
     */
    private String sgEmoticonLookupTable;
    /**
     * 正确拼写词汇表所在文件名.
     */
    private String sgCorrectSpellingFileName;
    /**
     * 正确拼写词汇表所在文件名2.
     */
    private String sgCorrectSpellingFileName2;
    /**
     * 俚语查找表.
     */
    private String sgSlangLookupTable;
    /**
     * 否定词列表文件.
     */
    private String sgNegatingWordListFile;
    /**
     * 增强词列表文件.
     */
    private String sgBoosterListFile;
    /**
     * 习语查找表文件.
     */
    private String sgIdiomLookupTableFile;
    /**
     * 疑问词查找表文件.
     */
    private String sgQuestionWordListFile;
    /**
     * 讽刺词列表文件.
     */
    private String sgIronyWordListFile;
    /**
     * 附加文件.
     */
    private String sgAdditionalFile;
    /**
     * 词根文件.
     */
    private String sgLemmaFile;

    /**
     * 构造方法.
     */
    public ClassificationResources() {
        setEmoticons(new EmoticonsList());
        setCorrectSpellings(new CorrectSpellingsList());
        setSentimentWords(new SentimentWords());
        setNegatingWords(new NegatingWordList());
        setQuestionWords(new QuestionWords());
        setBoosterWords(new BoosterWordsList());
        setIdiomList(new IdiomList());
        setEvaluativeTerms(new EvaluativeTerms());
        setIronyList(new IronyList());
        setLemmatiser(new Lemmatiser());
        setSgSentiStrengthFolder(
                System.getProperty("user.dir") + "/src/main/dict/");
        setSgSentimentWordsFile("EmotionLookupTable.txt");
        setSgSentimentWordsFile2("SentimentLookupTable.txt");
        setSgEmoticonLookupTable("EmoticonLookupTable.txt");
        setSgCorrectSpellingFileName("Dictionary.txt");
        setSgCorrectSpellingFileName2("EnglishWordList.txt");
        setSgSlangLookupTable("SlangLookupTable_NOT_USED.txt");
        setSgNegatingWordListFile("NegatingWordList.txt");
        setSgBoosterListFile("BoosterWordList.txt");
        setSgIdiomLookupTableFile("IdiomLookupTable.txt");
        setSgQuestionWordListFile("QuestionWords.txt");
        setSgIronyWordListFile("IronyTerms.txt");
        setSgAdditionalFile("");
        setSgLemmaFile("");
    }

    /**
     * 初始化分类器资源类.
     * <br>
     * 将分类器资源类持有的各个类分别初始化。<br>
     * UC-17 Location of linguistic data folder<br>
     * UC-18 Location of sentiment term weights<br>
     *
     * @param options 分类器选项
     * @return 是否初始化成功
     */
    public boolean initialise(final ClassificationOptions options) {
        int iExtraLinesToReserve = 0;
        if (getSgAdditionalFile().compareTo("") != 0) {
            iExtraLinesToReserve = FileOps.iCountLinesInTextFile(
                    getSgSentiStrengthFolder() + getSgAdditionalFile());
            if (iExtraLinesToReserve < 0) {
                System.out.println(
                        "No lines found in additional file! Ignoring "
                                + getSgAdditionalFile());
                return false;
            }
        }
        if (options.isBgUseLemmatisation() && !getLemmatiser().initialise(
                getSgSentiStrengthFolder() + getSgLemmaFile(), false)) {
            System.out.println("Can't load lemma file! " + getSgLemmaFile());
            return false;
        }
        File f = new File(
                getSgSentiStrengthFolder() + getSgSentimentWordsFile());
        if (!f.exists() || f.isDirectory()) {
            setSgSentimentWordsFile(getSgSentimentWordsFile2());
        }
        File f2 = new File(
                getSgSentiStrengthFolder() + getSgCorrectSpellingFileName());
        if (!f2.exists() || f2.isDirectory()) {
            setSgCorrectSpellingFileName(getSgCorrectSpellingFileName2());
        }
        if (getEmoticons().initialise(
                getSgSentiStrengthFolder() + getSgEmoticonLookupTable(),
                options) && getCorrectSpellings().initialise(
                getSgSentiStrengthFolder() + getSgCorrectSpellingFileName(),
                options) && getSentimentWords().initialise(
                getSgSentiStrengthFolder() + getSgSentimentWordsFile(), options,
                iExtraLinesToReserve) && getNegatingWords().initialise(
                getSgSentiStrengthFolder() + getSgNegatingWordListFile(),
                options) && getQuestionWords().initialise(
                getSgSentiStrengthFolder() + getSgQuestionWordListFile(),
                options) && getIronyList().initialise(
                getSgSentiStrengthFolder() + getSgIronyWordListFile(), options)
                && getBoosterWords().initialise(
                getSgSentiStrengthFolder() + getSgBoosterListFile(), options,
                iExtraLinesToReserve) && getIdiomList().initialise(
                getSgSentiStrengthFolder() + getSgIdiomLookupTableFile(),
                options, iExtraLinesToReserve)) {
            if (iExtraLinesToReserve > 0) {
                return getEvaluativeTerms().initialise(
                        getSgSentiStrengthFolder() + getSgAdditionalFile(),
                        options, getIdiomList(), getSentimentWords());
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 获取表情符号列表.
     *
     * @return 表情符号列表.
     */
    public EmoticonsList getEmoticons() {
        return emoticons;
    }

    /**
     * 设置表情符号列表.
     *
     * @param emoticonsList 表情符号列表.
     */
    public void setEmoticons(final EmoticonsList emoticonsList) {
        this.emoticons = emoticonsList;
    }

    /**
     * 获取正确拼写列表.
     *
     * @return 正确拼写列表.
     */
    public CorrectSpellingsList getCorrectSpellings() {
        return correctSpellings;
    }

    /**
     * 设置正确拼写列表.
     *
     * @param correctSpellingsList 正确拼写列表.
     */
    public void setCorrectSpellings(
            final CorrectSpellingsList correctSpellingsList) {
        this.correctSpellings = correctSpellingsList;
    }

    /**
     * 获取情绪词.
     *
     * @return 情绪词.
     */
    public SentimentWords getSentimentWords() {
        return sentimentWords;
    }

    /**
     * 设置情绪词.
     *
     * @param words 情绪词.
     */
    public void setSentimentWords(final SentimentWords words) {
        this.sentimentWords = words;
    }

    /**
     * 获取否定词.
     *
     * @return 否定词.
     */
    public NegatingWordList getNegatingWords() {
        return negatingWords;
    }

    /**
     * 设置否定词.
     *
     * @param negatingWordList 否定词.
     */
    public void setNegatingWords(final NegatingWordList negatingWordList) {
        this.negatingWords = negatingWordList;
    }

    /**
     * 获取疑问词.
     *
     * @return 疑问词.
     */
    public QuestionWords getQuestionWords() {
        return questionWords;
    }

    /**
     * 设置疑问词.
     *
     * @param words 疑问词.
     */
    public void setQuestionWords(final QuestionWords words) {
        this.questionWords = words;
    }

    /**
     * 获取强化词.
     *
     * @return 强化词.
     */
    public BoosterWordsList getBoosterWords() {
        return boosterWords;
    }

    /**
     * 设置强化词.
     *
     * @param boosterWordsList 强化词.
     */
    public void setBoosterWords(final BoosterWordsList boosterWordsList) {
        this.boosterWords = boosterWordsList;
    }

    /**
     * 获取习语列表.
     *
     * @return 习语列表.
     */
    public IdiomList getIdiomList() {
        return idiomList;
    }

    /**
     * 设置习语列表.
     *
     * @param list 习语列表.
     */
    public void setIdiomList(final IdiomList list) {
        this.idiomList = list;
    }

    /**
     * 获取评估项目.
     *
     * @return 评估项目.
     */
    public EvaluativeTerms getEvaluativeTerms() {
        return evaluativeTerms;
    }

    /**
     * 设置评估项目.
     *
     * @param terms 评估项目.
     */
    public void setEvaluativeTerms(final EvaluativeTerms terms) {
        this.evaluativeTerms = terms;
    }

    /**
     * 返回讽刺词列表.
     * <br>
     *
     * @return 讽刺词列表
     */
    public IronyList getIronyList() {
        return ironyList;
    }

    /**
     * 设置讽刺词列表.
     *
     * @param list 讽刺词列表
     */
    public void setIronyList(final IronyList list) {
        this.ironyList = list;
    }

    /**
     * 获取词根列表.
     *
     * @return 词根列表.
     */
    public Lemmatiser getLemmatiser() {
        return lemmatiser;
    }

    /**
     * 设置词根列表.
     *
     * @param sLemmatiser 词根列表.
     */
    public void setLemmatiser(final Lemmatiser sLemmatiser) {
        this.lemmatiser = sLemmatiser;
    }

    /**
     * 获取SentiStrength的文件夹.
     *
     * @return SentiStrength的文件夹.
     */
    public String getSgSentiStrengthFolder() {
        return sgSentiStrengthFolder;
    }

    /**
     * 设置SentiStrength的文件夹.
     *
     * @param s SentiStrength的文件夹.
     */
    public void setSgSentiStrengthFolder(final String s) {
        this.sgSentiStrengthFolder = s;
    }

    /**
     * 获取情绪词文件.
     *
     * @return 情绪词文件.
     */
    public String getSgSentimentWordsFile() {
        return sgSentimentWordsFile;
    }

    /**
     * 设置情绪词文件.
     *
     * @param s 情绪词文件.
     */
    public void setSgSentimentWordsFile(final String s) {
        this.sgSentimentWordsFile = s;
    }

    /**
     * 获取情绪词文件2.
     *
     * @return 情绪词文件2.
     */
    public String getSgSentimentWordsFile2() {
        return sgSentimentWordsFile2;
    }

    /**
     * 设置情绪词文件2.
     *
     * @param s 情绪词文件2.
     */
    public void setSgSentimentWordsFile2(final String s) {
        this.sgSentimentWordsFile2 = s;
    }

    /**
     * 获取表情符号查询表.
     *
     * @return 表情符号查询表.
     */
    public String getSgEmoticonLookupTable() {
        return sgEmoticonLookupTable;
    }

    /**
     * 设置表情符号查询表.
     *
     * @param s 表情符号查询表.
     */
    public void setSgEmoticonLookupTable(final String s) {
        this.sgEmoticonLookupTable = s;
    }

    /**
     * 获取正确拼写词汇表所在文件名.
     *
     * @return 正确拼写词汇表所在文件名.
     */
    public String getSgCorrectSpellingFileName() {
        return sgCorrectSpellingFileName;
    }

    /**
     * 设置正确拼写词汇表所在文件名.
     *
     * @param s 正确拼写词汇表所在文件名.
     */
    public void setSgCorrectSpellingFileName(final String s) {
        this.sgCorrectSpellingFileName = s;
    }

    /**
     * 获取正确拼写词汇表所在文件名2.
     *
     * @return 正确拼写词汇表所在文件名2.
     */
    public String getSgCorrectSpellingFileName2() {
        return sgCorrectSpellingFileName2;
    }

    /**
     * 设置正确拼写词汇表所在文件名2.
     *
     * @param s 正确拼写词汇表所在文件名2.
     */
    public void setSgCorrectSpellingFileName2(final String s) {
        this.sgCorrectSpellingFileName2 = s;
    }

    /**
     * 获取俚语查找表.
     *
     * @return 俚语查找表.
     */
    public String getSgSlangLookupTable() {
        return sgSlangLookupTable;
    }

    /**
     * 设置俚语查找表.
     *
     * @param s 俚语查找表.
     */
    public void setSgSlangLookupTable(final String s) {
        this.sgSlangLookupTable = s;
    }

    /**
     * 获取否定词列表文件.
     *
     * @return 否定词列表文件.
     */
    public String getSgNegatingWordListFile() {
        return sgNegatingWordListFile;
    }

    /**
     * 设置否定词列表文件.
     *
     * @param s 否定词列表文件.
     */
    public void setSgNegatingWordListFile(final String s) {
        this.sgNegatingWordListFile = s;
    }

    /**
     * 获取增强词列表文件.
     *
     * @return 增强词列表文件.
     */
    public String getSgBoosterListFile() {
        return sgBoosterListFile;
    }

    /**
     * 设置增强词列表文件.
     *
     * @param s 增强词列表文件.
     */
    public void setSgBoosterListFile(final String s) {
        this.sgBoosterListFile = s;
    }

    /**
     * 获取习语查找表文件.
     *
     * @return 习语查找表文件.
     */
    public String getSgIdiomLookupTableFile() {
        return sgIdiomLookupTableFile;
    }

    /**
     * 设置习语查找表文件.
     *
     * @param s 习语查找表文件.
     */
    public void setSgIdiomLookupTableFile(final String s) {
        this.sgIdiomLookupTableFile = s;
    }

    /**
     * 获取疑问词查找表文件.
     *
     * @return 疑问词查找表文件.
     */
    public String getSgQuestionWordListFile() {
        return sgQuestionWordListFile;
    }

    /**
     * 设置疑问词查找表文件.
     *
     * @param s 疑问词查找表文件.
     */
    public void setSgQuestionWordListFile(final String s) {
        this.sgQuestionWordListFile = s;
    }

    /**
     * 获取讽刺词列表文件.
     *
     * @return 讽刺词列表文件.
     */
    public String getSgIronyWordListFile() {
        return sgIronyWordListFile;
    }

    /**
     * 设置讽刺词列表文件.
     *
     * @param s 讽刺词列表文件.
     */
    public void setSgIronyWordListFile(final String s) {
        this.sgIronyWordListFile = s;
    }

    /**
     * 获取附加文件.
     *
     * @return 附加文件.
     */
    public String getSgAdditionalFile() {
        return sgAdditionalFile;
    }

    /**
     * 设置附加文件.
     *
     * @param s 附加文件.
     */
    public void setSgAdditionalFile(final String s) {
        this.sgAdditionalFile = s;
    }

    /**
     * 获取词根文件.
     *
     * @return 词根文件.
     */
    public String getSgLemmaFile() {
        return sgLemmaFile;
    }

    /**
     * 设置词根文件.
     *
     * @param s 词根文件.
     */
    public void setSgLemmaFile(final String s) {
        this.sgLemmaFile = s;
    }
}
