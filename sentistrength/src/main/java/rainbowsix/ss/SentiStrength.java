package rainbowsix.ss;

import rainbowsix.utilities.FileOps;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

/**
 * 情绪强度类.
 * <br>初始化算法程序、运行算法、计算句子情绪强度、获取语料库。
 * <br>UC-14 Listen at a port for texts to classify
 * <br>UC-15 Run interactively from the command line
 * <br>UC-19 Location of output folder
 * <br>UC-20 File name extension for output
 * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
 * strength separately
 * <br>UC-22 Use trinary classification (positive-negative-neutral)
 * <br>UC-23 Use binary classification (positive-negative)
 * <br>UC-24 Use a single positive-negative scale classification
 * <br>UC-26 Set Classification Algorithm Parameters
 *
 * @author 注释编写 徐晨
 */
public class SentiStrength {
    /**
     * 用于浮点数转换的进制.
     */
    private static final float MULTIPLE_HUNDRED = 100.0F;
    /**
     * 用于切割http请求字符串的位置.
     */
    private static final int CUT_POSITION = 5;
    /**
     * 语料库.
     */
    private Corpus c;
    /**
     * 模式.
     */
    private Mode mode = new NegPosMode();

    /**
     * 构造方法.
     * <br>新建语料库。
     */
    public SentiStrength() {
        this.c = new Corpus();
    }

    /**
     * 构造方法.
     * <br>新建语料库；初始化、运行情绪强度算法。
     *
     * @param args 输入参数
     */
    public SentiStrength(final String[] args) {
        this.c = new Corpus();
        this.initialiseAndRun(args);
    }

    /**
     * 入口.
     * <br>新建情绪强度类，初始化、运行情绪强度算法。
     *
     * @param args 输入参数
     */
    public static void main(final String[] args) {
        SentiStrength classifier = new SentiStrength();
        classifier.initialiseAndRun(args);
    }

    /**
     * 初始化、运行情绪强度算法.
     * <br>记录参数对应的状态信息；初始化语料库；根据参数选择监听的具体模式；进行算法运行，若结果输出成功，打印输出文件名。
     * <br>若有参数格式错误或缺少参数的情况，打印错误信息；
     * 若有参数未被识别，输出错误的参数；若语料库初始化失败，打印初始化失败；
     * 输出语料库文件不存在，打印简短的帮助信息；若输入文件格式出错，打印相关错误。
     * <br>UC-14 Listen at a port for texts to classify
     * <br>UC-15 Run interactively from the command line
     * <br>UC-19 Location of output folder
     * <br>UC-20 File name extension for output
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     * <br>UC-22 Use trinary classification (positive-negative-neutral)
     * <br>UC-23 Use binary classification (positive-negative)
     * <br>UC-24 Use a single positive-negative scale classification
     * <br>UC-26 Set Classification Algorithm Parameters
     *
     * @param args 输入参数
     */
    public void initialiseAndRun(final String[] args) {
        Corpus corpus = this.c;
        // s是String，i是int，b是布尔，有g的是全局变量
        String sInputFile = "";
        String sInputFolder = "";
        String sTextToParse = "";
        String sOptimalTermStrengths = "";
        String sFileSubString = "\t";
        String sResultsFolder = "";
        String sResultsFileExtension = "_out.txt";
        boolean[] bArgumentRecognised = new boolean[args.length];
        int iIterations = 1;
        int iMinImprovement = 2;
        int iMultiOptimisations = 1;
        int iListenPort = 0;
        int iTextColForAnnotation = -1;
        int iIdCol = -1;
        int iTextCol = -1;
        boolean bDoAll = false;
        boolean bOkToOverwrite = false;
        boolean bTrain = false;
        boolean bReportNewTermWeightsForBadClassifications = false;
        boolean bWait = false;
        boolean bUseTotalDifference = true;
        boolean bURLEncoded = false;
        String sLanguage = "";

        int i;
        for (i = 0; i < args.length; i++) {
            bArgumentRecognised[i] = false;
        }

        for (i = 0; i < args.length; ++i) {
            try {
                if (args[i].equalsIgnoreCase("input")) {
                    sInputFile = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("inputfolder")) {
                    sInputFolder = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("outputfolder")) {
                    sResultsFolder = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("resultextension")) {
                    sResultsFileExtension = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("resultsextension")) {
                    sResultsFileExtension = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("filesubstring")) {
                    sFileSubString = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("overwrite")) {
                    bOkToOverwrite = true;
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("text")) {
                    sTextToParse = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("urlencoded")) {
                    bURLEncoded = true;
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("listen")) {
                    iListenPort = Integer.parseInt(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("optimise")) {
                    sOptimalTermStrengths = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("annotatecol")) {
                    iTextColForAnnotation = Integer.parseInt(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("textcol")) {
                    iTextCol = Integer.parseInt(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("idcol")) {
                    iIdCol = Integer.parseInt(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("lang")) {
                    sLanguage = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("train")) {
                    bTrain = true;
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("all")) {
                    bDoAll = true;
                    bTrain = true;
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("numcorrect")) {
                    bUseTotalDifference = false;
                    bTrain = true;
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("iterations")) {
                    iIterations = Integer.parseInt(args[i + 1]);
                    bTrain = true;
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("minimprovement")) {
                    iMinImprovement = Integer.parseInt(args[i + 1]);
                    bTrain = true;
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("multi")) {
                    iMultiOptimisations = Integer.parseInt(args[i + 1]);
                    bTrain = true;
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("termWeights")) {
                    bReportNewTermWeightsForBadClassifications = true;
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("wait")) {
                    bWait = true;
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("help")) {
                    this.printCommandLineOptions();
                    return;
                }
            } catch (NumberFormatException var32) {
                System.out.println("Error in argument for " + args[i]
                        + ". Integer expected!");
                return;
            } catch (Exception var33) {
                System.out.println("Error in argument for " + args[i]
                        + ". Argument missing?");
                return;
            }
        }

        this.parseParametersForCorpusOptions(args, bArgumentRecognised);
        if (sLanguage.length() > 1) {
            Locale l = new Locale(sLanguage);
            Locale.setDefault(l);
        }

        for (i = 0; i < args.length; ++i) {
            if (!bArgumentRecognised[i]) {
                System.out.println(
                        "Unrecognised command - wrong spelling or case?: "
                                + args[i]);
                this.showBriefHelp();
                return;
            }
        }

        if (corpus.initialise()) {
            if (!Objects.equals(sTextToParse, "")) {
                if (bURLEncoded) {
                    sTextToParse = URLDecoder.decode(sTextToParse,
                            StandardCharsets.UTF_8);
                } else {
                    sTextToParse = sTextToParse.replace("+", " ");
                }

                this.parseOneText(corpus, sTextToParse, bURLEncoded);
            } else if (iListenPort > 0) {
                this.listenAtPort(corpus, iListenPort);
            } else if (!bWait) {
                if (!Objects.equals(sOptimalTermStrengths, "")) {
                    if (Objects.equals(sInputFile, "")) {
                        System.out.println(
                                "Input file must be specified to optimise "
                                        + "term weights");
                        return;
                    }

                    if (corpus.setCorpus(sInputFile)) {
                        corpus.optimiseDictionaryWeightingsForCorpus(
                                iMinImprovement, bUseTotalDifference);
                        corpus.getResources().getSentimentWords()
                                .saveSentimentList(sOptimalTermStrengths,
                                        corpus);
                        System.out.println("Saved optimised term weights to "
                                + sOptimalTermStrengths);
                    } else {
                        System.out.println(
                                "Error: Too few texts in " + sInputFile);
                    }
                } else if (bReportNewTermWeightsForBadClassifications) {
                    if (corpus.setCorpus(sInputFile)) {
                        corpus.printCorpusUnusedTermsClassificationIndex(
                                FileOps.sChopFileNameExtension(sInputFile)
                                        + "_unusedTerms.txt", 1);
                    } else {
                        System.out.println(
                                "Error: Too few texts in " + sInputFile);
                    }
                } else if (iTextCol > 0 && iIdCol > 0) {
                    this.classifyAndSaveWithID(corpus, sInputFile, sInputFolder,
                            iTextCol, iIdCol);
                } else if (iTextColForAnnotation > 0) {
                    this.annotationTextCol(corpus, sInputFile, sInputFolder,
                            sFileSubString, iTextColForAnnotation,
                            bOkToOverwrite);
                } else {
                    if (!Objects.equals(sInputFolder, "")) {
                        System.out.println(
                                "Input folder specified but textCol and IDcol"
                                        + " or annotateCol needed");
                    }

                    if (Objects.equals(sInputFile, "")) {
                        System.out.println(
                                "No action taken because no input file nor "
                                        + "text specified");
                        this.showBriefHelp();
                        return;
                    }

                    String sOutputFile = FileOps.getNextAvailableFilename(
                            FileOps.sChopFileNameExtension(sInputFile),
                            sResultsFileExtension);
                    if (sResultsFolder.length() > 0) {
                        sOutputFile = sResultsFolder + (new File(
                                sOutputFile)).getName();
                    }

                    if (bTrain) {
                        this.runMachineLearning(c, sInputFile, bDoAll,
                                iMinImprovement, bUseTotalDifference,
                                iIterations, iMultiOptimisations, sOutputFile);
                    } else {
                        --iTextCol;
                        c.classifyAllLinesInInputFile(sInputFile, iTextCol,
                                sOutputFile);
                    }

                    System.out.println("Finished! Results in: " + sOutputFile);
                }

            }
        } else {
            System.out.println("Failed to initialise!");

            try {
                File f = new File(
                        corpus.getResources().getSgSentiStrengthFolder());
                if (!f.exists()) {
                    System.out.println(
                            "Folder does not exist! " + corpus.getResources()
                                    .getSgSentiStrengthFolder());
                }
            } catch (Exception var30) {
                System.out.println(
                        "Folder doesn't exist! " + corpus.getResources()
                                .getSgSentiStrengthFolder());
            }

            this.showBriefHelp();
        }

    }

    /**
     * 设置语料库选项.
     * <br>
     * UC-26 Set Classification Algorithm Parameters<br>
     *
     * @param args                参数
     * @param bArgumentRecognised 参数是否被设别的数组
     */
    private void parseParametersForCorpusOptions(final String[] args,
                                                 final boolean[]
                                                         bArgumentRecognised) {
        for (int i = 0; i < args.length; ++i) {
            try {
                if (args[i].equalsIgnoreCase("sentidata")) {
                    this.c.getResources().setSgSentiStrengthFolder(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("emotionlookuptable")) {
                    this.c.getResources().setSgSentimentWordsFile(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("additionalfile")) {
                    this.c.getResources().setSgAdditionalFile(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("keywords")) {
                    this.c.getOptions()
                            .parseKeywordList(args[i + 1].toLowerCase());
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("wordsBeforeKeywords")) {
                    this.c.getOptions().setIgWordsToIncludeBeforeKeyword(
                            Integer.parseInt(args[i + 1]));
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("wordsAfterKeywords")) {
                    this.c.getOptions().setIgWordsToIncludeAfterKeyword(
                            Integer.parseInt(args[i + 1]));
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("sentiment")) {
                    this.c.getOptions().nameProgram(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("stress")) {
                    this.c.getOptions().nameProgram(true);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("trinary")) {
                    mode = new TrinaryMode();
                    mode.setC(this.c);
                    this.c.getOptions().setBgTrinaryMode(true);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("binary")) {
                    mode = new BinaryMode();
                    mode.setC(this.c);
                    this.c.getOptions().setBgBinaryVersionOfTrinaryMode(true);
                    this.c.getOptions().setBgTrinaryMode(true);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("scale")) {
                    mode = new ScaleMode();
                    mode.setC(this.c);
                    this.c.getOptions().setBgScaleMode(true);
                    bArgumentRecognised[i] = true;
                    if (this.c.getOptions().isBgTrinaryMode()) {
                        System.out.println(
                                "Must choose binary/trinary OR scale mode");
                        return;
                    }
                }

                ClassificationOptions var10000;
                if (args[i].equalsIgnoreCase("sentenceCombineAv")) {
                    var10000 = this.c.getOptions();
                    this.c.getOptions().getClass();
                    var10000.setIgEmotionSentenceCombineMethod(1);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("sentenceCombineTot")) {
                    var10000 = this.c.getOptions();
                    this.c.getOptions().getClass();
                    var10000.setIgEmotionSentenceCombineMethod(2);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("paragraphCombineAv")) {
                    var10000 = this.c.getOptions();
                    this.c.getOptions().getClass();
                    var10000.setIgEmotionParagraphCombineMethod(1);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("paragraphCombineTot")) {
                    var10000 = this.c.getOptions();
                    this.c.getOptions().getClass();
                    var10000.setIgEmotionParagraphCombineMethod(2);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("negativeMultiplier")) {
                    this.c.getOptions().setFgNegativeSentimentMultiplier(
                            Float.parseFloat(args[i + 1]));
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("noBoosters")) {
                    this.c.getOptions().setBgBoosterWordsChangeEmotion(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase(
                        "noNegatingPositiveFlipsEmotion")) {
                    this.c.getOptions()
                            .setBgNegatingPositiveFlipsEmotion(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase(
                        "noNegatingNegativeNeutralisesEmotion")) {
                    this.c.getOptions()
                            .setBgNegatingNegativeNeutralisesEmotion(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("noNegators")) {
                    this.c.getOptions().setBgNegatingWordsFlipEmotion(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("noIdioms")) {
                    this.c.getOptions().setBgUseIdiomLookupTable(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("questionsReduceNeg")) {
                    this.c.getOptions()
                            .setBgReduceNegativeEmotionInQuestionSentences(
                                    true);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("noEmoticons")) {
                    this.c.getOptions().setBgUseEmoticons(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("exclamations2")) {
                    this.c.getOptions()
                            .setBgExclamationInNeutralSentenceCountsAsPlus2(
                                    true);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("minPunctuationWithExclamation")) {
                    this.c.getOptions()
                            .setIgMinPunctuationWithExclamationToChangeSentenceSentiment(
                                    Integer.parseInt(args[i + 1]));
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("mood")) {
                    this.c.getOptions().setIgMoodToInterpretNeutralEmphasis(
                            Integer.parseInt(args[i + 1]));
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("noMultiplePosWords")) {
                    this.c.getOptions()
                            .setBgAllowMultiplePositiveWordsToIncreasePositiveEmotion(
                                    false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("noMultipleNegWords")) {
                    this.c.getOptions()
                            .setBgAllowMultipleNegativeWordsToIncreaseNegativeEmotion(
                                    false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase(
                        "noIgnoreBoosterWordsAfterNegatives")) {
                    this.c.getOptions()
                            .setBgIgnoreBoosterWordsAfterNegatives(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("noDictionary")) {
                    this.c.getOptions()
                            .setBgCorrectSpellingsUsingDictionary(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("noDeleteExtraDuplicateLetters")) {
                    this.c.getOptions()
                            .setBgCorrectExtraLetterSpellingErrors(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase(
                        "illegalDoubleLettersInWordMiddle")) {
                    this.c.getOptions().setSgIllegalDoubleLettersInWordMiddle(
                            args[i + 1].toLowerCase());
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("illegalDoubleLettersAtWordEnd")) {
                    this.c.getOptions().setSgIllegalDoubleLettersAtWordEnd(
                            args[i + 1].toLowerCase());
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("noMultipleLetters")) {
                    this.c.getOptions()
                            .setBgMultipleLettersBoostSentiment(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("negatedWordStrengthMultiplier")) {
                    this.c.getOptions().setFgStrengthMultiplierForNegatedWords(
                            Float.parseFloat(args[i + 1]));
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase(
                        "maxWordsBeforeSentimentToNegate")) {
                    this.c.getOptions().setIgMaxWordsBeforeSentimentToNegate(
                            Integer.parseInt(args[i + 1]));
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase(
                        "negatingWordsDontOccurBeforeSentiment")) {
                    this.c.getOptions()
                            .setBgNegatingWordsOccurBeforeSentiment(false);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase(
                        "maxWordsAfterSentimentToNegate")) {
                    this.c.getOptions().setIgMaxWordsAfterSentimentToNegate(
                            Integer.parseInt(args[i + 1]));
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase(
                        "negatingWordsOccurAfterSentiment")) {
                    this.c.getOptions()
                            .setBgNegatingWordsOccurAfterSentiment(true);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("alwaysSplitWordsAtApostrophes")) {
                    this.c.getOptions()
                            .setBgAlwaysSplitWordsAtApostrophes(true);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("capitalsBoostTermSentiment")) {
                    this.c.getOptions().setBgCapitalsBoostTermSentiment(true);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("lemmaFile")) {
                    this.c.getOptions().setBgUseLemmatisation(true);
                    this.c.getResources().setSgLemmaFile(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("MinSentencePosForQuotesIrony")) {
                    this.c.getOptions().setIgMinSentencePosForQuotesIrony(
                            Integer.parseInt(args[i + 1]));
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase(
                        "MinSentencePosForPunctuationIrony")) {
                    this.c.getOptions().setIgMinSentencePosForPunctuationIrony(
                            Integer.parseInt(args[i + 1]));
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("MinSentencePosForTermsIrony")) {
                    this.c.getOptions().setIgMinSentencePosForTermsIrony(
                            Integer.parseInt(args[i + 1]));
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("MinSentencePosForAllIrony")) {
                    this.c.getOptions().setIgMinSentencePosForTermsIrony(
                            Integer.parseInt(args[i + 1]));
                    this.c.getOptions().setIgMinSentencePosForPunctuationIrony(
                            this.c.getOptions()
                                    .getIgMinSentencePosForTermsIrony());
                    this.c.getOptions().setIgMinSentencePosForQuotesIrony(
                            this.c.getOptions()
                                    .getIgMinSentencePosForTermsIrony());
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("explain")) {
                    this.c.getOptions().setBgExplainClassification(true);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("echo")) {
                    this.c.getOptions().setBgEchoText(true);
                    bArgumentRecognised[i] = true;
                }

                if (args[i].equalsIgnoreCase("UTF8")) {
                    this.c.getOptions().setBgForceUTF8(true);
                    bArgumentRecognised[i] = true;
                }

            } catch (NumberFormatException var5) {
                System.out.println("Error in argument for " + args[i]
                        + ". Integer expected!");
                return;
            } catch (Exception var6) {
                System.out.println("Error in argument for " + args[i]
                        + ". Argument missing?");
                return;
            }
        }

    }

    /**
     * 初始化情绪强度类.
     * <br>处理语料库选项参数；若有参数未被识别，输出错误的参数并打印简短的帮助信息；初始化语料库，若语料库初始化失败，打印初始化失败。
     * <br>UC-21 Classify positive (1 to 5) and negative (-1 to -5) sentiment
     * strength separately
     * <br>UC-22 Use trinary classification (positive-negative-neutral)
     * <br>UC-23 Use binary classification (positive-negative)
     * <br>UC-24 Use a single positive-negative scale classification
     * <br>UC-26 Set Classification Algorithm Parameters
     *
     * @param args 输入参数
     */
    public void initialise(final String[] args) {
        boolean[] bArgumentRecognised = new boolean[args.length];

        int i;
        for (i = 0; i < args.length; ++i) {
            bArgumentRecognised[i] = false;
        }

        this.parseParametersForCorpusOptions(args, bArgumentRecognised);

        for (i = 0; i < args.length; ++i) {
            if (!bArgumentRecognised[i]) {
                System.out.println(
                        "Unrecognised command - wrong spelling or case?: "
                                + args[i]);
                this.showBriefHelp();
                return;
            }
        }

        if (!this.c.initialise()) {
            System.out.println("Failed to initialise!");
        }

    }

    /**
     * 计算情绪得分.
     * <br>计算句子的积极消极情绪分数；根据模式（binary、trinary、single...）选定结果。
     *
     * @param sentence 需要分析情绪的句子
     * @return 得到的情绪分数结果
     */
    public String computeSentimentScores(final String sentence) {
        int iPos;
        int iNeg;
        int iTrinary;
        int iScale;
        Paragraph paragraph = new Paragraph();
        paragraph.setParagraph(sentence, this.c.getResources(),
                this.c.getOptions());
        iNeg = paragraph.getParagraphNegativeSentiment();
        iPos = paragraph.getParagraphPositiveSentiment();
        iTrinary = paragraph.getParagraphTrinarySentiment();
        iScale = paragraph.getParagraphScaleSentiment();
        String sRationale = "";
        if (this.c.getOptions().isBgEchoText()) {
            sRationale = " " + sentence;
        }

        if (this.c.getOptions().isBgExplainClassification()) {
            sRationale = " " + paragraph.getClassificationRationale();
        }

        return iPos + " " + iNeg + mode.outputSpace() + sRationale;
//        if (this.c.getOptions().isBgTrinaryMode()) {
//            return iPos + " " + iNeg + " " + iTrinary + sRationale;
//        } else {
//            return this.c.getOptions().isBgScaleMode() ? iPos + " " + iNeg
//            + " " + iScale + sRationale : iPos + " " + iNeg + sRationale;
//        }
    }

    /**
     * 运行机器学习.
     *
     * @param corpus              语料库
     * @param sInputFile          输入文件路径
     * @param bDoAll              是否分析所有文本
     * @param iMinImprovement     最小提升
     * @param bUseTotalDifference 是否用全差分方法
     * @param iIterations         迭代次数
     * @param iMultiOptimisations 优化次数
     * @param sOutputFile         输出文件路径
     */
    private void runMachineLearning(final Corpus corpus,
                                    final String sInputFile,
                                    final boolean bDoAll,
                                    final int iMinImprovement,
                                    final boolean bUseTotalDifference,
                                    final int iIterations,
                                    final int iMultiOptimisations,
                                    final String sOutputFile) {
        if (iMinImprovement < 1) {
            System.out.println("No action taken because min improvement < 1");
            this.showBriefHelp();
        } else {
            corpus.setCorpus(sInputFile);
            corpus.calculateCorpusSentimentScores();
            int corpusSize = corpus.getCorpusSize();
            if (corpus.getOptions().isBgTrinaryMode()) {
                if (corpus.getOptions().isBgBinaryVersionOfTrinaryMode()) {
                    System.out.print("Before training, binary accuracy: "
                            + corpus.getClassificationTrinaryNumberCorrect()
                            + " "
                            + (float) corpus.getClassificationTrinaryNumberCorrect()
                                    / (float) corpusSize * MULTIPLE_HUNDRED
                            + "%");
                } else {
                    System.out.print("Before training, trinary accuracy: "
                            + corpus.getClassificationTrinaryNumberCorrect()
                            + " "
                            + (float) corpus.getClassificationTrinaryNumberCorrect()
                                    / (float) corpusSize * MULTIPLE_HUNDRED
                            + "%");
                }
            } else if (corpus.getOptions().isBgScaleMode()) {
                System.out.print("Before training, scale accuracy: "
                        + corpus.getClassificationScaleNumberCorrect() + " "
                        + (float) corpus.getClassificationScaleNumberCorrect()
                                * MULTIPLE_HUNDRED / (float) corpusSize
                        + "% corr "
                        + corpus.getClassificationScaleCorrelationWholeCorpus());
            } else {
                System.out.print("Before training, positive: "
                        + corpus.getClassificationPositiveNumberCorrect() + " "
                        + corpus.getClassificationPositiveAccuracyProportion()
                        * MULTIPLE_HUNDRED + "% negative "
                        + corpus.getClassificationNegativeNumberCorrect() + " "
                        + corpus.getClassificationNegativeAccuracyProportion()
                        * MULTIPLE_HUNDRED + "% ");
                System.out.print("   Positive corr: "
                        + corpus.getClassificationPosCorrelationWholeCorpus()
                        + " negative "
                        + corpus.getClassificationNegCorrelationWholeCorpus());
            }

            System.out.println(" out of " + corpus.getCorpusSize());
            if (bDoAll) {
                System.out.println("Running " + iIterations
                        + " iteration(s) of all options on file " + sInputFile
                        + "; results in " + sOutputFile);
                corpus.run10FoldCrossValidationForAllOptionVariations(
                        iMinImprovement, bUseTotalDifference, iIterations,
                        iMultiOptimisations, sOutputFile);
            } else {
                System.out.println("Running " + iIterations
                        + " iteration(s) for standard or selected options on "
                        + "file " + sInputFile + "; results in " + sOutputFile);
                corpus.run10FoldCrossValidationMultipleTimes(iMinImprovement,
                        bUseTotalDifference, iIterations, iMultiOptimisations,
                        sOutputFile);
            }

        }
    }

    /**
     * 分类及和ID一起存储.
     *
     * @param corpus       语料库
     * @param sInputFile   输入文件路径
     * @param sInputFolder 输入文件夹路径
     * @param iTextCol     文本列
     * @param iIdCol       文本列id
     */
    private void classifyAndSaveWithID(final Corpus corpus,
                                       final String sInputFile,
                                       final String sInputFolder,
                                       final int iTextCol, final int iIdCol) {
        if (!sInputFile.equals("")) {
            corpus.classifyAllLinesAndRecordWithID(sInputFile, iTextCol - 1,
                    iIdCol - 1, FileOps.sChopFileNameExtension(sInputFile)
                            + "_classID.txt");
        } else {
            if (sInputFolder.equals("")) {
                System.out.println(
                        "No annotations done because no input file or folder "
                                + "specfied");
                this.showBriefHelp();
                return;
            }

            File folder = new File(sInputFolder);
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles == null) {
                System.out.println("Incorrect or empty input folder specfied");
                this.showBriefHelp();
                return;
            }

            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    System.out.println(
                            "Classify + save with ID: " + listOfFile.getName());
                    corpus.classifyAllLinesAndRecordWithID(
                            sInputFolder + "/" + listOfFile.getName(),
                            iTextCol - 1, iIdCol - 1,
                            sInputFolder + "/" + FileOps.sChopFileNameExtension(
                                    listOfFile.getName()) + "_classID.txt");
                }
            }
        }

    }

    /**
     * 注释文本列.
     *
     * @param corpus                语料库
     * @param sInputFile            输入文件路径
     * @param sInputFolder          输入文件夹路径
     * @param sFileSubString        文件子字符串
     * @param iTextColForAnnotation 注释文本行数
     * @param bOkToOverwrite        是否能覆写
     */
    private void annotationTextCol(final Corpus corpus, final String sInputFile,
                                   final String sInputFolder,
                                   final String sFileSubString,
                                   final int iTextColForAnnotation,
                                   final boolean bOkToOverwrite) {
        if (!bOkToOverwrite) {
            System.out.println("Must include parameter overwrite to annotate");
        } else {
            if (!sInputFile.equals("")) {
                corpus.annotateAllLinesInInputFile(sInputFile,
                        iTextColForAnnotation - 1);
            } else {
                if (sInputFolder.equals("")) {
                    System.out.println(
                            "No annotations done because no input file or "
                                    + "folder specfied");
                    this.showBriefHelp();
                    return;
                }
                File folder = new File(sInputFolder);
                File[] listOfFiles = folder.listFiles();
                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile()) {
                        if (!sFileSubString.equals("")
                                && listOfFile.getName().indexOf(sFileSubString)
                                <= 0) {
                            System.out.println(
                                    "  Ignoring " + listOfFile.getName());
                        } else {
                            System.out.println(
                                    "Annotate: " + listOfFile.getName());
                            corpus.annotateAllLinesInInputFile(
                                    sInputFolder + "/" + listOfFile.getName(),
                                    iTextColForAnnotation - 1);
                        }
                    }
                }
            }

        }
    }

    /**
     * 处理单个文本.
     * <br>UC-11 Classify a single text
     *
     * @param corpus            语料库
     * @param sTextToParse      要处理的文本
     * @param bURLEncodedOutput 是否URL编码输出
     */
    private void parseOneText(final Corpus corpus, final String sTextToParse,
                              final boolean bURLEncodedOutput) {
        int iPos;
        int iNeg;
        int iTrinary;
        int iScale;
        Paragraph paragraph = new Paragraph();
        paragraph.setParagraph(sTextToParse, corpus.getResources(),
                corpus.getOptions());
        iNeg = paragraph.getParagraphNegativeSentiment();
        iPos = paragraph.getParagraphPositiveSentiment();
        iTrinary = paragraph.getParagraphTrinarySentiment();
        iScale = paragraph.getParagraphScaleSentiment();
        String sRationale = "";
        if (corpus.getOptions().isBgEchoText()) {
            sRationale = " " + sTextToParse;
        }

        if (corpus.getOptions().isBgExplainClassification()) {
            sRationale = " " + paragraph.getClassificationRationale();
        }

        String sOutput;
        if (corpus.getOptions().isBgTrinaryMode()) {
            sOutput = iPos + " " + iNeg + " " + iTrinary + sRationale;
        } else if (corpus.getOptions().isBgScaleMode()) {
            sOutput = iPos + " " + iNeg + " " + iScale + sRationale;
        } else {
            sOutput = iPos + " " + iNeg + sRationale;
        }

        if (bURLEncodedOutput) {
            System.out.println(
                    URLEncoder.encode(sOutput, StandardCharsets.UTF_8));
        } else if (corpus.getOptions().isBgForceUTF8()) {
            System.out.println(
                    new String(sOutput.getBytes(StandardCharsets.UTF_8),
                            StandardCharsets.UTF_8));
        } else {
            System.out.println(sOutput);
        }

    }

    /**
     * 在指定端口监听.
     * <br>若监听成功则打印端口和IP信息，否则打印失败信息并返回；若端口输入输出错误，打印错误信息；根据进制和是否是强制UTF8选定输出文本。
     * <br>UC-14 Listen at a port for texts to classify
     *
     * @param corpus      语料库
     * @param iListenPort 监听端口号
     */
    private void listenAtPort(final Corpus corpus, final int iListenPort) {
        ServerSocket serverSocket;
        String decodedText = "";

        try {
            serverSocket = new ServerSocket(iListenPort);
        } catch (IOException var23) {
            System.out.println(
                    "Could not listen on port " + iListenPort + " because\n"
                            + var23.getMessage());
            return;
        }

        System.out.println("Listening on port: " + iListenPort + " IP: "
                + serverSocket.getInetAddress());

        while (true) {
            Socket clientSocket;

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException var20) {
                System.out.println("Accept failed at port: " + iListenPort);
                return;
            }

            PrintWriter out;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException var19) {
                System.out.println("IOException clientSocket.getOutputStream "
                        + var19.getMessage());
                var19.printStackTrace();
                return;
            }

            BufferedReader in;
            try {
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException var18) {
                System.out.println(
                        "IOException InputStreamReader " + var18.getMessage());
                var18.printStackTrace();
                return;
            }

            String inputLine;
            try {
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.indexOf("GET /") == 0) {
                        int lastSpacePos = inputLine.lastIndexOf(" ");
                        if (lastSpacePos < CUT_POSITION) {
                            lastSpacePos = inputLine.length();
                        }

                        decodedText = URLDecoder.decode(
                                inputLine.substring(CUT_POSITION, lastSpacePos),
                                StandardCharsets.UTF_8);
                        System.out.println("Analysis of text: " + decodedText);
                        break;
                    }

                    if (inputLine.equals("MikeSpecialMessageToEnd.")) {
                        break;
                    }
                }
            } catch (IOException var24) {
                System.out.println("IOException " + var24.getMessage());
                var24.printStackTrace();
                decodedText = "";
            } catch (Exception var25) {
                System.out.println("Non-IOException " + var25.getMessage());
                decodedText = "";
            }

            int iPos;
            int iNeg;
            int iTrinary;
            int iScale;
            Paragraph paragraph = new Paragraph();
            paragraph.setParagraph(decodedText, corpus.getResources(),
                    corpus.getOptions());
            iNeg = paragraph.getParagraphNegativeSentiment();
            iPos = paragraph.getParagraphPositiveSentiment();
            iTrinary = paragraph.getParagraphTrinarySentiment();
            iScale = paragraph.getParagraphScaleSentiment();
            mode.setValue(iTrinary, iScale);
            String sRationale = "";
            if (corpus.getOptions().isBgEchoText()) {
                sRationale = " " + decodedText;
            }

            if (corpus.getOptions().isBgExplainClassification()) {
                sRationale = " " + paragraph.getClassificationRationale();
            }

            String sOutput = iPos + " " + iNeg + " " + mode.outputSpace()
                    + sRationale;
//            if (corpus.getOptions().isBgTrinaryMode()) {
//                sOutput = iPos + " " + iNeg + " " + iTrinary + sRationale;
//            } else if (corpus.getOptions().isBgScaleMode()) {
//                sOutput = iPos + " " + iNeg + " " + iScale + sRationale;
//            } else {
//                sOutput = iPos + " " + iNeg + sRationale;
//            }

            if (corpus.getOptions().isBgForceUTF8()) {
                out.print(new String(sOutput.getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.UTF_8));
            } else {
                out.print(sOutput);
            }

            try {
                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException var21) {
                System.out.println("IOException closing streams or sockets"
                        + var21.getMessage());
                var21.printStackTrace();
            }
        }
    }

    /**
     * 显示简短帮助.
     * <br>打印简短的帮助信息，以及一些命令的示例。
     */
    private void showBriefHelp() {
        System.out.println();
        System.out.println("====" + this.c.getOptions().getSgProgramName()
                + "Brief Help====");
        System.out.println(
                "For most operations, a minimum of two parameters must be set");
        System.out.println("1) folder location for the linguistic files");
        System.out.println("   e.g., on Windows: C:/mike/Lexical_Data/");
        System.out.println("   e.g., on Mac/Linux/Unix: /usr/Lexical_Data/");
        if (this.c.getOptions().isBgTensiStrength()) {
            System.out.println(
                    "TensiiStrength_Data can be downloaded from...[not "
                            + "completed yet]");
        } else {
            System.out.println(
                    "SentiStrength_Data can be downloaded with the Windows "
                            + "version of SentiStrength from sentistrength"
                            + ".wlv.ac.uk");
        }

        System.out.println();
        System.out.println(
                "2) text to be classified or file name of texts to be "
                        + "classified");
        System.out.println("   e.g., To classify one text: text love+u");
        System.out.println(
                "   e.g., To classify a file of texts: input /bob/data.txt");
        System.out.println();
        System.out.println("Here is an example complete command:");
        if (this.c.getOptions().isBgTensiStrength()) {
            System.out.println(
                    "java -jar TensiStrength.jar sentidata C:/a/Stress_Data/ "
                            + "text am+stressed");
        } else {
            System.out.println("java -jar SentiStrength.jar sentidata "
                    + "C:/a/SentStrength_Data/ text love+u");
        }

        System.out.println();
        if (!this.c.getOptions().isBgTensiStrength()) {
            System.out.println(
                    "To list all commands: java -jar SentiStrength.jar help");
        }

    }

    /**
     * 打印命令行选项.
     */
    private void printCommandLineOptions() {
        System.out.println("====" + this.c.getOptions().getSgProgramName()
                + " Command Line Options====");
        System.out.println("=Source of data to be classified=");
        System.out.println(" text [text to process] OR");
        System.out.println(
                " input [filename] (each line of the file is classified "
                        + "SEPARATELY");
        System.out.println(
                "        May have +ve 1st col., -ve 2nd col. in evaluation "
                        + "mode) OR");
        System.out.println(
                " annotateCol [col # 1..] (classify text in col, result at "
                        + "line end) OR");
        System.out.println(
                " textCol, idCol [col # 1..] (classify text in col, result & "
                        + "ID in new file) OR");
        System.out.println(
                " inputFolder  [foldername] (all files in folder will be "
                        + "*annotated*)");
        System.out.println(
                " outputFolder [foldername where to put the output (default: "
                        + "folder of input)]");
        System.out.println(
                " resultsExtension [file-extension for output (default _out"
                        + ".txt)]");
        System.out.println(
                "  fileSubstring [text] (string must be present in files to "
                        + "annotate)");
        System.out.println("  Ok to overwrite files [overwrite]");
        System.out.println(
                " listen [port number to listen at - call http://127.0.0"
                        + ".1:81/text]");
        System.out.println(
                " wait (just initialise; allow calls to public String "
                        + "computeSentimentScores)");
        System.out.println("=Linguistic data source=");
        System.out.println(" sentidata [folder for " + this.c.getOptions()
                .getSgProgramName() + " data (end in slash, no spaces)]");
        System.out.println("=Options=");
        System.out.println(
                " keywords [comma-separated list - " + this.c.getOptions()
                        .getSgProgramMeasuring()
                        + " only classified close to these]");
        System.out.println(
                "   wordsBeforeKeywords [words to classify before keyword "
                        + "(default 4)]");
        System.out.println(
                "   wordsAfterKeywords [words to classify after keyword "
                        + "(default 4)]");
        System.out.println(
                " trinary (report positive-negative-neutral classifcation "
                        + "instead)");
        System.out.println(
                " binary (report positive-negative classifcation instead)");
        System.out.println(
                " scale (report single -4 to +4 classifcation instead)");
        System.out.println(
                " emotionLookupTable [filename (default: EmotionLookupTable"
                        + ".txt)]");
        System.out.println(
                " additionalFile [filename] (domain-specific terms and "
                        + "evaluations)");
        System.out.println(" lemmaFile [filename] (word tab lemma list for "
                + "lemmatisation)");
        System.out.println("=Classification algorithm parameters=");
        System.out.println(
                " noBoosters (ignore sentiment booster words (e.g., very))");
        System.out.println(
                " noNegators (don't use negating words (e.g., not) to flip "
                        + "sentiment) -OR-");
        System.out.println(
                " noNegatingPositiveFlipsEmotion (don't use negating words to"
                        + " flip +ve words)");
        System.out.println(
                " bgNegatingNegativeNeutralisesEmotion (negating words don't "
                        + "neuter -ve words)");
        System.out.println(
                " negatedWordStrengthMultiplier (strength multiplier when "
                        + "negated (default=0.5))");
        System.out.println(" negatingWordsOccurAfterSentiment (negate "
                + this.c.getOptions().getSgProgramMeasuring()
                + " occurring before negatives)");
        System.out.println("  maxWordsAfterSentimentToNegate (max words "
                + this.c.getOptions().getSgProgramMeasuring()
                + " to negator (default 0))");
        System.out.println(
                " negatingWordsDontOccurBeforeSentiment (don't negate "
                        + this.c.getOptions().getSgProgramMeasuring()
                        + " after negatives)");
        System.out.println(
                "   maxWordsBeforeSentimentToNegate (max from negator to "
                        + this.c.getOptions().getSgProgramMeasuring()
                        + " (default 0))");
        System.out.println(" noIdioms (ignore idiom list)");
        System.out.println(
                " questionsReduceNeg (-ve sentiment reduced in questions)");
        System.out.println(" noEmoticons (ignore emoticon list)");
        System.out.println(
                " exclamations2 (sentence with ! counts as +2 if otherwise "
                        + "neutral)");
        System.out.println(
                " minPunctuationWithExclamation (min punctuation with ! to "
                        + "boost term " + this.c.getOptions()
                        .getSgProgramMeasuring() + ")");
        System.out.println(
                " mood [-1,0,1] (default 1: -1 assume neutral emphasis is "
                        + "neg, 1, assume is pos");
        System.out.println(
                " noMultiplePosWords (multiple +ve words don't increase "
                        + this.c.getOptions().getSgProgramPos() + ")");
        System.out.println(
                " noMultipleNegWords (multiple -ve words don't increase "
                        + this.c.getOptions().getSgProgramNeg() + ")");
        System.out.println(
                " noIgnoreBoosterWordsAfterNegatives (don't ignore boosters "
                        + "after negating words)");
        System.out.println(
                " noDictionary (don't try to correct spellings using the "
                        + "dictionary)");
        System.out.println(
                " noMultipleLetters (don't use additional letters in a word "
                        + "to boost " + this.c.getOptions()
                        .getSgProgramMeasuring() + ")");
        System.out.println(
                " noDeleteExtraDuplicateLetters (don't delete extra duplicate"
                        + " letters in words)");
        System.out.println(
                " illegalDoubleLettersInWordMiddle [letters never duplicate "
                        + "in word middles]");
        System.out.println(
                "    default for English: ahijkquvxyz (specify list without "
                        + "spaces)");
        System.out.println(
                " illegalDoubleLettersAtWordEnd [letters never duplicate at "
                        + "word ends]");
        System.out.println(
                "    default for English: achijkmnpqruvwxyz (specify list "
                        + "without spaces)");
        System.out.println(" sentenceCombineAv (average " + this.c.getOptions()
                .getSgProgramMeasuring()
                + " strength of terms in each sentence) OR");
        System.out.println(
                " sentenceCombineTot (total the " + this.c.getOptions()
                        .getSgProgramMeasuring()
                        + " strength of terms in each sentence)");
        System.out.println(" paragraphCombineAv (average " + this.c.getOptions()
                .getSgProgramMeasuring()
                + " strength of sentences in each text) OR");
        System.out.println(
                " paragraphCombineTot (total the " + this.c.getOptions()
                        .getSgProgramMeasuring()
                        + " strength of sentences in each text)");
        System.out.println(
                "  *the default for the above 4 options is the maximum, not "
                        + "the total or average");
        System.out.println(
                " negativeMultiplier [negative total strength polarity "
                        + "multiplier, default 1.5]");
        System.out.println(" capitalsBoostTermSentiment (" + this.c.getOptions()
                .getSgProgramMeasuring() + " words in CAPITALS are stronger)");
        System.out.println(
                " alwaysSplitWordsAtApostrophes (e.g., t'aime -> t ' aime)");
        System.out.println(
                " MinSentencePosForQuotesIrony [integer] quotes in +ve "
                        + "sentences indicate irony");
        System.out.println(
                " MinSentencePosForPunctuationIrony [integer] +ve ending in "
                        + "!!+ indicates irony");
        System.out.println(
                " MinSentencePosForTermsIrony [integer] irony terms in +ve "
                        + "sent. indicate irony");
        System.out.println(
                " MinSentencePosForAllIrony [integer] all of the above irony "
                        + "terms");
        System.out.println(
                " lang [ISO-639 lower-case two-letter langauge code] set "
                        + "processing language");
        System.out.println("=Input and Output=");
        System.out.println(" explain (explain classification after results)");
        System.out.println(
                " echo (echo original text after results [for pipeline "
                        + "processes])");
        System.out.println(
                " UTF8 (force all processing to be in UTF-8 format)");
        System.out.println(
                " urlencoded (input and output text is URL encoded)");
        System.out.println(
                "=Advanced - machine learning [1st input line ignored]=");
        System.out.println(
                " termWeights (list terms in badly classified texts; must "
                        + "specify inputFile)");
        System.out.println(
                " optimise [Filename for optimal term strengths (eg. "
                        + "EmotionLookupTable2.txt)]");
        System.out.println(
                " train (evaluate " + this.c.getOptions().getSgProgramName()
                        + " by training term strengths on results in file)");
        System.out.println(
                "   all (test all option variations rather than use default)");
        System.out.println("   numCorrect (optimise by # correct - not total "
                + "classification difference)");
        System.out.println(
                "   iterations [number of 10-fold iterations] (default 1)");
        System.out.println(
                "   minImprovement [min. accuracy improvement to change "
                        + this.c.getOptions().getSgProgramMeasuring()
                        + " weights (default 1)]");
        System.out.println(
                "   multi [# duplicate term strength optimisations to change "
                        + this.c.getOptions().getSgProgramMeasuring()
                        + " weights (default 1)]");
    }

    /**
     * 获得语料库.
     *
     * @return 当前语料库
     */
    public Corpus getCorpus() {
        return this.c;
    }
}
