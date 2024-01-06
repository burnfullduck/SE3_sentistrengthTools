package rainbowsix.wka;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import rainbowsix.ss.ClassificationOptions;
import rainbowsix.ss.ClassificationResources;
import rainbowsix.ss.Paragraph;
import rainbowsix.ss.TextParsingOptions;
import rainbowsix.utilities.FileOps;
import rainbowsix.utilities.Sort;
import rainbowsix.utilities.StringIndex;

/**
 * arff文件类.
 * <br>
 * arff格式是一种用于机器学习软件Weka的数据格式，它包含了数据集的属性和实例信息。<br>
 * 包含了对与机器学习的arff文件操作相关的方法。
 *
 * @author 注释编写 朱甲豪 詹美瑛
 */
public class Arff {
    /**
     * 是否带编号存储arff.
     */
    private static final boolean BG_SAVE_ARFF_AS_CONDENSED = true;
    /**
     * 默认的滑动大小.
     */
    private static final int I_NGRAMS = 3;

    /**
     * 使用scale分类.
     */
    private static final int SCALE = 3;
    /**
     * 使用二元分类.
     */
    private static final int BINARY = 1;
    /**
     * 使用三元分类.
     **/
    private static final int TRINARY = 2;
    /**
     * 使用消极和积极检测.
     */
    private static final int POS_NEG = 4;
    /**
     * 文件路径经过的最大目录数.
     */
    private static final int MAX_FILE_PATH = 100;
    /**
     * 加入字符索引类的初始编号.
     */
    private static final int VAR_NUM = 898989;
    /**
     * 最大属性数目.
     */
    private static final int MAX_ATTRIBUTE_NUM = 1000;

    /**
     * 无效的第一个分类.
     */
    private static final int INVALID_FIRST_CLASS = 999999;

    /**
     * 一个arff的项的包含的列数.
     */
    private static final int ARFF_ITEM_NUM = 3;
    /**
     * 一个arff项的长度.
     */
    private static final int ARFF_ITEM_LEN = 4;

    /**
     * 标记的arff文件名在数组中的位置.
     */
    private static final int LABELED_ARFF_FILE_POSITION = 3;


    /**
     * main函数，对在命令行中输入的相关命令进行相应操作,并打印命令执行情况.
     * <br>具体命令功能详见overallHelp()。
     *
     * @param args 输入的命令行参数
     */
    public void main(final String[] args) {
        boolean[] bArgumentRecognised = new boolean[args.length];
        String sUnlabelledTextFile = "";
        String sLabelledTextFile = "";
        String sArffFileIn = "";
        String sTextFileOut = "";
        String sClassifier = "smo";
        int iNGrams = I_NGRAMS;
        int iMaxFeatures = 0;
        int iClassType = POS_NEG;
        int iClassFor0 = 0;
        int iMinFeatureFrequency = 1;
        boolean bCompleteProcessing = false;
        overallHelp();

        int i;
        for (i = 0; i < args.length; ++i) {
            bArgumentRecognised[i] = false;
        }

        for (i = 0; i < args.length; ++i) {
            if (args[i].equalsIgnoreCase("arff")) {
                bArgumentRecognised[i] = true;
            }

            if (args[i].equalsIgnoreCase("complete")) {
                bCompleteProcessing = true;
                bArgumentRecognised[i] = true;
            }

            if (args[i].equalsIgnoreCase("scale")) {
                iClassType = SCALE;
                bArgumentRecognised[i] = true;
            }

            if (args[i].equalsIgnoreCase("binary")) {
                iClassType = BINARY;
                bArgumentRecognised[i] = true;
            }

            if (args[i].equalsIgnoreCase("trinary")) {
                iClassType = TRINARY;
                bArgumentRecognised[i] = true;
            }

            if (args[i].equalsIgnoreCase("posneg")) {
                iClassType = POS_NEG;
                bArgumentRecognised[i] = true;
            }

            if (i < args.length - 1) {
                if (args[i].equalsIgnoreCase("unlabelledText")) {
                    sUnlabelledTextFile = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("labelledText")) {
                    sLabelledTextFile = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("arffFileIn")) {
                    sArffFileIn = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("textFileOut")) {
                    sTextFileOut = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("nGrams")) {
                    iNGrams = Integer.parseInt(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("maxFeatures")) {
                    iMaxFeatures = Integer.parseInt(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("classifier")) {
                    sClassifier = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("zeros")) {
                    iClassFor0 = Integer.parseInt(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }

                if (args[i].equalsIgnoreCase("minFeatureFreq")) {
                    iMinFeatureFrequency = Integer.parseInt(args[i + 1]);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
            }
        }

        for (i = 0; i < args.length; ++i) {
            if (!bArgumentRecognised[i]) {
                System.out.println(
                        "Unrecognised Arff command - wrong spelling or case?: "
                                + args[i]);
                return;
            }
        }

        if (bCompleteProcessing) {
            if (sUnlabelledTextFile.length() == 0) {
                System.out.println(
                        "An unlabelled text file must be specified [complete]");
                return;
            }

            if (sLabelledTextFile.length() == 0) {
                System.out.println(
                        "A labelled text file must be specified [complete]");
                return;
            }

            System.out.println("Complete processing starting...");
            System.out.println();
            System.out.println("Convert unlabelled texts " + sUnlabelledTextFile
                    + " to Arff based on labelled text file "
                    + sLabelledTextFile);
            System.out.println(
                    "Options: classtype " + iClassType + " Ngrams: 1-" + iNGrams
                            + " max features: " + iMaxFeatures
                            + " min freq for features: "
                            + iMinFeatureFrequency);
            System.out.println(
                    " Classtype: None=0, Binary=1, Trinary=2, Scale=3, "
                            + "PosNeg=4. max features = 0 => use all features (100 "
                            + "per 1k is optimal)");
            String[] sLabelledUnlabelled
                    = convertUnlabelledTextFileToArffBasedOnLabelledTextFile(
                    sLabelledTextFile, iClassType, iNGrams,
                    iMinFeatureFrequency, iMaxFeatures, sUnlabelledTextFile);
            String sClassifiedUnlabelledArff;
            String sClassifiedUnlabelledTextFile;
            String sMergedTextFile;
            if (iClassType == POS_NEG) {
                System.out.println("predictArffClass " + sLabelledUnlabelled[0]
                        + " training for " + sLabelledUnlabelled[2]);
                System.out.println();
                sClassifiedUnlabelledArff = PredictClass.predictArffClass(
                        sLabelledUnlabelled[0], sClassifier,
                        sLabelledUnlabelled[2], iClassFor0);
                sClassifiedUnlabelledTextFile = FileOps.sChopFileNameExtension(
                        sClassifiedUnlabelledArff) + "_Nout.txt";
                System.out.println(
                        "convertArffToText " + sClassifiedUnlabelledArff
                                + " -> " + sClassifiedUnlabelledTextFile);
                System.out.println();
                convertArffToText(sClassifiedUnlabelledArff,
                        sClassifiedUnlabelledTextFile);
                sMergedTextFile = FileOps.sChopFileNameExtension(
                        sClassifiedUnlabelledTextFile) + "_Nmerged.txt";
                System.out.println("mergeLabelledAndUnlabelledTextFiles "
                        + sClassifiedUnlabelledTextFile + ", "
                        + sUnlabelledTextFile + " -> " + sMergedTextFile);
                mergeLabelledAndUnlabelledTextFiles(
                        sClassifiedUnlabelledTextFile, sUnlabelledTextFile,
                        sMergedTextFile);
                System.out.println("predictArffClass " + sLabelledUnlabelled[1]
                        + " training for "
                        + sLabelledUnlabelled[LABELED_ARFF_FILE_POSITION]);
                System.out.println();
                sClassifiedUnlabelledArff = PredictClass.predictArffClass(
                        sLabelledUnlabelled[1], sClassifier,
                        sLabelledUnlabelled[LABELED_ARFF_FILE_POSITION],
                        iClassFor0); // 这个3就是数组下标，和数组中存的数据有关，不宜声明在外部。
                sClassifiedUnlabelledTextFile = FileOps.sChopFileNameExtension(
                        sClassifiedUnlabelledArff) + "_Pout.txt";
                System.out.println(
                        "convertArffToText " + sClassifiedUnlabelledArff
                                + " -> " + sClassifiedUnlabelledTextFile);
                System.out.println();
                convertArffToText(sClassifiedUnlabelledArff,
                        sClassifiedUnlabelledTextFile);
                sMergedTextFile = FileOps.sChopFileNameExtension(
                        sClassifiedUnlabelledTextFile) + "_Pmerged.txt";
            } else {
                System.out.println("predictArffClass " + sLabelledUnlabelled[0]
                        + " training for " + sLabelledUnlabelled[1]);
                System.out.println();
                sClassifiedUnlabelledArff = PredictClass.predictArffClass(
                        sLabelledUnlabelled[0], sClassifier,
                        sLabelledUnlabelled[1], iClassFor0);
                sClassifiedUnlabelledTextFile = FileOps.sChopFileNameExtension(
                        sClassifiedUnlabelledArff) + "_out.txt";
                System.out.println(
                        "convertArffToText " + sClassifiedUnlabelledArff
                                + " -> " + sClassifiedUnlabelledTextFile);
                System.out.println();
                convertArffToText(sClassifiedUnlabelledArff,
                        sClassifiedUnlabelledTextFile);
                sMergedTextFile = FileOps.sChopFileNameExtension(
                        sClassifiedUnlabelledTextFile) + "_merged.txt";
            }
            System.out.println("mergeLabelledAndUnlabelledTextFiles "
                    + sClassifiedUnlabelledTextFile + ", " + sUnlabelledTextFile
                    + " -> " + sMergedTextFile);
            mergeLabelledAndUnlabelledTextFiles(sClassifiedUnlabelledTextFile,
                    sUnlabelledTextFile, sMergedTextFile);
        } else if (sUnlabelledTextFile.length() > 0
                && sLabelledTextFile.length() > 0
                && sTextFileOut.length() > 0) {
            System.out.println(
                    "mergeLabelledAndUnlabelledTextFiles " + sLabelledTextFile
                            + ", " + sUnlabelledTextFile + ", " + sTextFileOut);
            mergeLabelledAndUnlabelledTextFiles(sLabelledTextFile,
                    sUnlabelledTextFile, sTextFileOut);
        } else if (sLabelledTextFile.length() > 0
                && sUnlabelledTextFile.length() > 0) {
            System.out.println(
                    "convertUnlabelledTextFileToArffBasedOnLabelledTextFile "
                            + sLabelledTextFile + ", " + sUnlabelledTextFile);
            convertUnlabelledTextFileToArffBasedOnLabelledTextFile(
                    sLabelledTextFile, iClassType, iNGrams,
                    iMinFeatureFrequency, iMaxFeatures, sUnlabelledTextFile);
        } else if (sArffFileIn.length() > 0 && sTextFileOut.length() > 0) {
            System.out.println(
                    "convertArffToText " + sArffFileIn + ", " + sTextFileOut);
            convertArffToText(sArffFileIn, sTextFileOut);
        } else {
            System.out.println(
                    "Not enough parameters entered to run a process from the "
                    + "arff submenu. Must enter one of the following:");
            System.out.println(
                    " complete - and parameters, to make classify "
                            + "unclassified text with ML");
            System.out.println(
                    " labelledText, unlabelledText and textFileOut - merges "
                    + "labelled and unlabelled files");
            System.out.println(
                    " labelledText, unlabelledText - converts unlabelled to "
                    + "ARFF based on labelled");
            System.out.println(
                    " arffFileIn, textFileOut - converts ARFF to plain text");
        }

        System.out.println("[arff] finished");
    }

    /**
     * 将未标签文本文件转换为基于标签文本的Arff文件.
     * <br>
     * 这段代码首先创建了一个TextParsingOptions对象和一个ClassificationOptions对象，
     * 分别用于设置文本解析和分类的选项，然后创建了一个ClassificationResources对象，用于初始化分类所需的资源<br>
     * 然后调用了convertSentimentTextToArffMultiple方法，
     * 该方法可以将标注过或未标注过的文本文件转换成多个arff格式的文件，并返回一个字符串数组，其中存储了生成的arff文件名。
     *
     * @param sLabelledTextFile    标注过的文本文件
     * @param iClassType           类别类型（0~4）
     * @param iNGrams              n元语法大小
     * @param iMinFeatureFrequency 最小特征频率
     * @param iMaxFeatures         最大特征数
     * @param sUnlabelledTextFile  未标注过的文本文件
     * @return 返回一个储存了生成的arff文件名的字符串数组
     */
    private static String[] convertUnlabelledTextFileToArffBasedOnLabelledTextFile(
            final String sLabelledTextFile, final int iClassType,
            final int iNGrams, final int iMinFeatureFrequency,
            final int iMaxFeatures, final String sUnlabelledTextFile) {
        TextParsingOptions textParsingOptions = new TextParsingOptions();
        ClassificationOptions classOptions = new ClassificationOptions();
        textParsingOptions.setIgNgramSize(iNGrams);
        ClassificationResources resources = new ClassificationResources();
        resources.setSgSentiStrengthFolder("c:/SentStrength_Data/");
        resources.initialise(classOptions);
        String[] sLabelledArffFiles = convertSentimentTextToArffMultiple(
                sLabelledTextFile, true, textParsingOptions, classOptions,
                resources, iClassType, iMinFeatureFrequency, "");
        int i;
        for (i = 0; i < MAX_FILE_PATH - 1 && sLabelledArffFiles[i] != null
                && !sLabelledArffFiles[i].equals("");) {
            ++i;
        }

        int iLabelledArffFileCount = i;
        String[] sLabelledArffFilesReduced;
        if (iMaxFeatures > 0) {
            sLabelledArffFilesReduced = new String[sLabelledArffFiles.length];

            for (i = 0; i < iLabelledArffFileCount; ++i) {
                sLabelledArffFilesReduced[i] =
                        FileOps.sChopFileNameExtension(sLabelledArffFiles[i])
                                + " " + iMaxFeatures + ".arff";
                makeArffWithTopNAttributes(sLabelledArffFiles[i], iMaxFeatures,
                        sLabelledArffFilesReduced[i]);
            }

            sLabelledArffFiles = sLabelledArffFilesReduced;
        }

        sLabelledArffFilesReduced = convertSentimentTextToArffMultiple(
                sUnlabelledTextFile, true, textParsingOptions, classOptions,
                resources, iClassType, 1, sLabelledArffFiles[i - 1]);
        String[] sResults;
        if (iClassType == POS_NEG) {
            sResults = new String[]{
                    sLabelledArffFiles[iLabelledArffFileCount - 1],
                    sLabelledArffFiles[iLabelledArffFileCount - 2],
                    sLabelledArffFilesReduced[iLabelledArffFileCount - 1],
                    sLabelledArffFilesReduced[iLabelledArffFileCount - 2]};
        } else {
            sResults = new String[]{
                    sLabelledArffFiles[iLabelledArffFileCount - 1],
                    sLabelledArffFilesReduced[iLabelledArffFileCount - 1]};
        }

        return sResults;
    }

    /**
     * 将命令使用方法打印到标准输出流.
     */
    private static void overallHelp() {
        System.out.println(
                "--------------------------------------------------------------------------");
        System.out.println(
                "- Text processing and ML prediction commands - arff to "
                        + "trigger this menu -");
        System.out.println(
                "--------------------------------------------------------------------------");
        System.out.println(
                "NB There is no command to convert labelled text to ARFF");
        System.out.println(
                "A) Convert unlabelled textfile to ARFF using features in "
                        + "labelled textfile");
        System.out.println("unlabelledText [filename]");
        System.out.println("labelledText [filename]");
        System.out.println(" nGrams [3] 3 means all 1-3grams");
        System.out.println(" maxFeatures [0] 0=no feature reduction");
        System.out.println(" minFeatureFreq [1] ignore less frequent features");
        System.out.println(" scale binary trinary posneg(default)");
        System.out.println(" zeros [class] - class if 0 predicted. Default 0");
        System.out.println("B) Convert Arff to labelled text file");
        System.out.println("arffFileIn [filename] convert to textfile");
        System.out.println("textFileOut [filename] target textfile");
        System.out.println("C) Merge Unlabelled and labelled text files");
        System.out.println("unlabelledText [filename]");
        System.out.println("labelledText [filename]");
        System.out.println("textFileOut [filename]");
        System.out.println("D) Do all above");
        System.out.println(
                "complete - input labelled, unlabelled, output classified "
                + "text");
        System.out.println(
                " classifier [smo] classifier name for complete (slog, "
                + "smoreg, ada, dec, libsvm, j48, mlp, jrip, bayes, liblin");
        System.out.println(
                "*run this via command line in parallel with "
                + "wkaMachineLearning");
        System.out.println(
                "*ALL DATA must have header row, unless specified otherwise");
        System.out.println(
                "-----------------------------------------------------------------------------");
    }

    /**
     * 将情感文本文件转换成arff格式的文件.
     * <br>
     * 首先判断arffStringIndex
     * 参数是否为空，如果不为空，则表示已经有一个现有的字符串索引可以使用，否则需要创建一个新的字符串索引对象，并初始化它。<br>
     * 调用了buildIndexFromTextFile方法和writeArffFromIndex方法
     *
     * @param sSentiTextFileIn     输入的情感文本文件
     * @param sArffFileOut         输出的arff文件
     * @param bHeaderLine          是否有表头行
     * @param textParsingOptions   文本解析选项
     * @param classOptions         分类选项
     * @param resources            分类资源
     * @param iSentimentType       情感类型
     * @param iMinFeatureFrequency 最小特征频率
     * @param arffStringIndex      arff字符串索引
     * @return 恒为true
     */
    public static boolean convertSentimentTextToArff(
            final String sSentiTextFileIn, final String sArffFileOut,
            final boolean bHeaderLine,
            final TextParsingOptions textParsingOptions,
            final ClassificationOptions classOptions,
            final ClassificationResources resources, final int iSentimentType,
            final int iMinFeatureFrequency, final StringIndex arffStringIndex) {
        if (arffStringIndex != null) {
            buildIndexFromTextFile(sSentiTextFileIn, bHeaderLine,
                    textParsingOptions, classOptions, resources, iSentimentType,
                    arffStringIndex, true);
            writeArffFromIndex(sSentiTextFileIn, arffStringIndex, bHeaderLine,
                    textParsingOptions, classOptions, resources, iSentimentType,
                    iMinFeatureFrequency, sArffFileOut, true);
        } else {
            StringIndex stringIndex = new StringIndex();
            stringIndex.initialise(0, true, false);
            buildIndexFromTextFile(sSentiTextFileIn, bHeaderLine,
                    textParsingOptions, classOptions, resources, iSentimentType,
                    stringIndex, false);
            writeArffFromIndex(sSentiTextFileIn, stringIndex, bHeaderLine,
                    textParsingOptions, classOptions, resources, iSentimentType,
                    iMinFeatureFrequency, sArffFileOut, false);
        }

        return true;
    }

    /**
     * 根据字符串索引对象中的特征和类别信息，生成arff格式的文件，并输出到指定的输出路径中.
     * <br>
     * 首先创建了一个布尔数组bIndexEntryUsed，用来记录哪些字符串索引条目被使用过。
     * 然后创建了一个缓冲写入器wWriter，用来写入arff文件。
     * 调用了writeArffHeadersFromIndex方法,根据字符串索引对象中的属性信息，生成arff文件的表头部分，并写入
     * 之后读取情感文本文件中的每一行,循环中处理每一行文本。若bHeaderLine参数为真，则跳过第一行
     * 在循环中根据iSentimentType的不同进行不同的处理
     * (大致上是将情感文本文件每一行转换为paragraph对象后，调用addToStringIndex方法。以不同形式写入arff文件)
     *
     * @param sSentiTextFileIn     情感文本文件输入
     * @param stringIndex          字符串索引
     * @param bHeaderLine          是否有表头行
     * @param textParsingOptions   文本解析选项
     * @param classOptions         分类选项
     * @param resources            分类资源
     * @param iSentimentType       情感类型
     * @param iMinFeatureFrequency 最小特征频率
     * @param sArffFileOut         arff文件输出
     * @param bArffIndex           是否使用arff索引
     * @return 当所有行都处理完成后，返回true；若有找不到文件的异常则返回false
     */
    private static boolean writeArffFromIndex(final String sSentiTextFileIn,
                                              final StringIndex stringIndex,
                                              final boolean bHeaderLine,
                                              final TextParsingOptions textParsingOptions,
                                              final ClassificationOptions classOptions,
                                              final ClassificationResources resources,
                                              final int iSentimentType,
                                              final int iMinFeatureFrequency,
                                              final String sArffFileOut,
                                              final boolean bArffIndex) {
        String[] sData = null;
        boolean[] bIndexEntryUsed = new boolean[stringIndex.getLastWordID()
                + 2];
        boolean bOnlyCountNgramsUsed = false;

        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(sArffFileOut),
                            StandardCharsets.UTF_8));
            writeArffHeadersFromIndex(sSentiTextFileIn, stringIndex,
                    iSentimentType, textParsingOptions.getIgNgramSize(),
                    iMinFeatureFrequency, bArffIndex, bIndexEntryUsed, wWriter);
            BufferedReader rReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(sSentiTextFileIn),
                            StandardCharsets.UTF_8));
            String sLine;
            if (bHeaderLine && rReader.ready()) {
                sLine = rReader.readLine();
            }

            while (true) {
                do {
                    if (!rReader.ready()) {
                        rReader.close();
                        wWriter.close();
                        return true;
                    }

                    sLine = rReader.readLine();
                } while (sLine.length() == 0);

                stringIndex.setAllCountsToZero();
                Paragraph para = new Paragraph();
                if (iSentimentType == POS_NEG) {
                    sData = sLine.split("\t");
                    if (sData.length > 2) {
                        para.setParagraph(sData[2], resources, classOptions);
                    }

                    if (sData.length == 1) {
                        para.setParagraph(sLine, resources, classOptions);
                    }
                } else if (iSentimentType == 0) {
                    para.setParagraph(sLine, null, null);
                } else {
                    sData = sLine.split("\t");
                    if (sData.length > 1) {
                        para.setParagraph(sData[1], resources, classOptions);
                    }

                    if (sData.length == 1) {
                        para.setParagraph(sLine, resources, classOptions);
                    }
                }

                int iNgramCount1 = para.addToStringIndex(stringIndex,
                        textParsingOptions, true, bArffIndex);

                int iClassOffset = 0;
                if (iSentimentType == POS_NEG) {
                    iClassOffset = 2;
                    if (sData.length > 2) {
                        if (sData[1].length() > 1
                                && sData[1].charAt(0) == '-') {
                            sData[1] = sData[1].substring(1);
                        }

                        if (BG_SAVE_ARFF_AS_CONDENSED) {
                            wWriter.write("{0 " + sData[0].trim() + ",1 "
                                    + sData[1].trim() + ",");
                        } else {
                            wWriter.write(
                                    sData[0].trim() + "," + sData[1].trim()
                                            + ",");
                        }
                    } else if (BG_SAVE_ARFF_AS_CONDENSED) {
                        wWriter.write("{0 1,1 1,");
                    } else {
                        wWriter.write("1,1,");
                    }
                } else if (iSentimentType != 0) {
                    iClassOffset = 1;
                    if (sData.length > 1) {
                        if (BG_SAVE_ARFF_AS_CONDENSED) {
                            wWriter.write("{0 " + sData[0].trim() + ",");
                        } else {
                            wWriter.write(sData[0].trim() + ",");
                        }
                    } else if (BG_SAVE_ARFF_AS_CONDENSED) {
                        wWriter.write("{0 1,");
                    } else {
                        wWriter.write("1,");
                    }
                } else if (BG_SAVE_ARFF_AS_CONDENSED) {
                    wWriter.write("{");
                }

                int iAttUsed = -1;

                for (int w = 0; w <= stringIndex.getLastWordID(); ++w) {
                    if (bIndexEntryUsed[w]) {
                        ++iAttUsed;
                        if (BG_SAVE_ARFF_AS_CONDENSED) {
                            if (stringIndex.getCount(w) != 0) {
                                wWriter.write(iAttUsed + iClassOffset + " "
                                        + stringIndex.getCount(w) + ",");
                            }
                        } else {
                            wWriter.write(stringIndex.getCount(w) + ",");
                        }
                    }

                }

                if (BG_SAVE_ARFF_AS_CONDENSED) {
                    ++iAttUsed;
                    wWriter.write(iAttUsed + iClassOffset + " " + iNgramCount1
                            + "}\n");
                } else {
                    wWriter.write(iNgramCount1 + "\n");
                }
            }
        } catch (IOException var21) {
            System.out.println(
                    "Could not open file for writing or write to file: "
                            + sArffFileOut);
            var21.printStackTrace();
            return false;
        }
    }

    /**
     * 将有标签和无标签的文本文件合并成一个新的文本文件，并写入到指定的输出路径中。<br>
     * 这可能是为了进行半监督学习1，即利用有标签和无标签数据来训练机器学习模型.<br>
     * 在循环中，这段代码首先从有标签和无标签的文本文件中分别读取一行，并将其分割成字符串数组sDataL和sLineU。
     * 然后根据sDataL数组的长度不同，将其第一列（表示类别）、第二列（表示内容）或者整行与sLineU
     * 数组拼接起来，并加上制表符和换行符，写入到wWriter中。
     *
     * @param sLabelledTextFileIn   有标签的文本文件输入
     * @param sUnlabelledTextFileIn 无标签的文本文件输入
     * @param sTextFileOut          文本文件输出
     */
    private static void mergeLabelledAndUnlabelledTextFiles(
            final String sLabelledTextFileIn,
            final String sUnlabelledTextFileIn, final String sTextFileOut) {
        try {
            BufferedReader rLabelled = new BufferedReader(new InputStreamReader(
                    new FileInputStream(sLabelledTextFileIn),
                    StandardCharsets.UTF_8));
            BufferedReader rUnlabelled = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(sUnlabelledTextFileIn),
                            StandardCharsets.UTF_8));
            BufferedWriter wWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(sTextFileOut)));

            while (rLabelled.ready() && rUnlabelled.ready()) {
                String sLineL = rLabelled.readLine();
                String[] sDataL = sLineL.split("\t");
                String sLineU = rUnlabelled.readLine();
                if (sDataL.length > 1) {
                    wWriter.write(sDataL[0] + "\t" + sLineU + "\t" + sDataL[1]
                            + "\n");
                } else if (sDataL.length == 1) {
                    wWriter.write("0\t" + sLineU + "\t" + sDataL[0] + "\n");
                } else {
                    System.out.println(
                            "short labelled line "
                            + "[mergeLabelledAndUnlabelledTextFiles]\n"
                                    + sLineL);
                }
            }

            rLabelled.close();
            rUnlabelled.close();
            wWriter.close();
        } catch (Exception var9) {
            System.out.println("Error [mergeLabelledAndUnlabelledTextFiles]");
            var9.printStackTrace();
        }

    }

    /**
     * 根据索引写入arff文件的头部信息.
     * <br>
     * 包括文件来源、日期、文件名等。
     * 写入@relation行到ARFF文件中，指定关系名为AllTerms。
     * 根据iSentimentType参数的值，写入@attribute行到ARFF文件中。
     * 写入@attribute行到ARFF文件中，指定n-gram计数属性名和类型。<br>
     * 遍历stringIndex中所有的字符串，并根据一些条件判断是否将它们作为特征属性。
     * 如果bArffIndex为真，则只使用索引中带有"+"字符个数小于iNgram并且出现次数大于等于iMinFeatureFrequency
     * 的字符串；
     * 如果bArffIndex为假，则只使用原始字符串中不含空格或者空格个数小于iNgram
     * 并且出现次数大于等于iMinFeatureFrequency 的字符串。
     * 对于每个符合条件的字符串，在bArffIndexEntryUsed数组中对应位置标记为真，并且写入@attribute行到ARFF文件中。
     * 如果bArffIndex为真，则根据字符串是否以Q_或R_开头来决定使用索引还是注释作为属性名；
     * 如果bArffIndex为假，则对原始字符串进行编码后作为属性名。
     *
     * @param sSourceFile          源文件名
     * @param stringIndex          字符串索引
     * @param iSentimentType       情感分析的类型
     * @param iNgram               要使用的n-gram的大小，1（单词）、2（双词）、3（三词）
     * @param iMinFeatureFrequency 最小特征频率
     * @param bArffIndex           是否使用索引中的字符串作为特征名
     * @param bArffIndexEntryUsed  用来记录哪些索引中的字符串被用作了特征的布尔值数组
     * @param wWriter              BufferedWriter对象，用来写入ARFF文件
     * @return 是否成功写入了ARFF文件的头部信息。
     */
    private static boolean writeArffHeadersFromIndex(final String sSourceFile,
                                                     final StringIndex stringIndex,
                                                     final int iSentimentType,
                                                     final int iNgram,
                                                     final int iMinFeatureFrequency,
                                                     final boolean bArffIndex,
                                                     final boolean[] bArffIndexEntryUsed,
                                                     final BufferedWriter wWriter) {
        String sIndexWord;

        try {
            wWriter.write("%Arff file from Arff.java\n");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            wWriter.write("%Date: " + dateFormat.format(date) + "\n");
            wWriter.write("%filename: " + sSourceFile + "\n");
            wWriter.write("@relation AllTerms\n");
            if (iSentimentType == POS_NEG) {
                wWriter.write("@attribute Pos {1,2,3,4,5}\n");
                wWriter.write("@attribute Neg {1,2,3,4,5}\n");
            } else if (iSentimentType == BINARY) {
                wWriter.write("@attribute Binary {-1,1}\n");
            } else if (iSentimentType == TRINARY) {
                wWriter.write("@attribute Trinary {-1,0,1}\n");
            } else if (iSentimentType == SCALE) {
                wWriter.write("@attribute Scale {-4,-3,-2,-1,0,1,2,3,4}\n");
            }

            for (int w = 0; w <= stringIndex.getLastWordID(); ++w) {
                if (bArffIndex) {
                    sIndexWord = stringIndex.getString(w);
                    if (iCharsInString(sIndexWord, '+') < iNgram
                            && stringIndex.getCount(w)
                            >= iMinFeatureFrequency) {
                        bArffIndexEntryUsed[w] = true;
                        if (sIndexWord.indexOf("Q_") != 0
                                && sIndexWord.indexOf("R_") != 0) {
                            wWriter.write(
                                    "@attribute " + sIndexWord + " numeric\n");
                        } else {
                            wWriter.write(
                                    "@attribute " + stringIndex.getComment(w)
                                            + " numeric\n");
                        }
                    } else {
                        bArffIndexEntryUsed[w] = false;
                    }
                } else if (
                        iCharsInString(stringIndex.getString(w), ' ') < iNgram
                                && stringIndex.getCount(w)
                                >= iMinFeatureFrequency) {
                    bArffIndexEntryUsed[w] = true;
                    wWriter.write("@attribute " + arffSafeWordEncode(
                            stringIndex.getString(w)) + " numeric\n");
                } else {
                    bArffIndexEntryUsed[w] = false;
                }
            }

            wWriter.write("@attribute Ngram_" + iNgram + "count numeric\n");
            wWriter.write("@data\n");
            return true;
        } catch (IOException var12) {
            System.out.println(
                    "Could not write ARFF headers to file "
                            + "[writeArffHeadersFromIndex]");
            var12.printStackTrace();
            return false;
        }
    }

    /**
     * 获得一个字符在某个字符串中出现的次数.
     * <br>
     *
     * @param sText 字符串
     * @param sChar 字符
     * @return 出现次数
     */
    private static int iCharsInString(final String sText, final char sChar) {
        int iCount = 0;

        for (int i = 0; i < sText.length(); ++i) {
            try {
                if (sText.charAt(i) == sChar) {
                    ++iCount;
                }
            } catch (Exception var5) {
                System.out.println("i_CharsInString error with text [" + sText
                        + "] at position i = " + i);
                System.out.println(var5.getMessage());
            }
        }

        return iCount;
    }

    /**
     * 这段代码的作用是将一个单词编码为ARFF文件格式的安全形式.
     * <br>
     *
     * @param sWord 单词
     * @return 返回转换后的单词，若无变化则在开头加上"U_"，若有变化则"E_"
     */
    public static String arffSafeWordEncode(final String sWord) {
        String sEncodedWord;

        sEncodedWord = URLEncoder.encode(sWord, StandardCharsets.UTF_8);

        if (sEncodedWord.equals(sWord)) {
            return "U_" + sWord;
        } else {
            if (sEncodedWord.contains("%")) {
                sEncodedWord = sEncodedWord.replace("%", "_pc");
            }

            if (sEncodedWord.contains("}")) {
                sEncodedWord = sEncodedWord.replace("}", "_brak");
            }

            return "E_" + sEncodedWord;
        }
    }

    /**
     * 从文本文件中建立索引.
     * <br>
     * 使用Paragraph对象，调用addToStringIndex方法
     *
     * @param sSentiTextFileIn   情感文本文件输入
     * @param bHeaderLine        是否有表头行
     * @param textParsingOptions 文本解析选项
     * @param classOptions       分类选项
     * @param resources          分类资源
     * @param iSentimentType     情感类型
     * @param stringIndex        字符串索引
     * @param bArffIndex         是否使用arff索引
     * @return 操作是否成功
     */
    private static boolean buildIndexFromTextFile(final String sSentiTextFileIn,
                                                  final boolean bHeaderLine,
                                                  final TextParsingOptions textParsingOptions,
                                                  final ClassificationOptions classOptions,
                                                  final ClassificationResources resources,
                                                  final int iSentimentType,
                                                  final StringIndex stringIndex,
                                                  final boolean bArffIndex) {
        File f = new File(sSentiTextFileIn);
        if (!f.exists()) {
            System.out.println(
                    "Could not find the vocab file: " + sSentiTextFileIn);
            return false;
        } else {
            try {
                BufferedReader rReader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(sSentiTextFileIn),
                                StandardCharsets.UTF_8));
                String sLine;
                if (bHeaderLine && rReader.ready()) {
                    sLine = rReader.readLine();
                }

                while (rReader.ready()) {
                    sLine = rReader.readLine();
                    if (sLine.length() > 0) {
                        Paragraph para = new Paragraph();
                        String[] sData;
                        if (iSentimentType == POS_NEG) {
                            sData = sLine.split("\t");
                            if (sData.length > 2) {
                                para.setParagraph(sData[2], resources,
                                        classOptions);
                            }

                            if (sData.length == 1) {
                                para.setParagraph(sLine, resources,
                                        classOptions);
                            }
                        } else if (iSentimentType == 0) {
                            para.setParagraph(sLine, null, null);
                        } else {
                            sData = sLine.split("\t");
                            if (sData.length > 1) {
                                para.setParagraph(sData[1], resources,
                                        classOptions);
                            }

                            if (sData.length == 1) {
                                para.setParagraph(sLine, resources,
                                        classOptions);
                            }
                        }

                        para.addToStringIndex(stringIndex, textParsingOptions,
                                true, bArffIndex);
                    }
                }

                rReader.close();
                return true;
            } catch (IOException var14) {
                System.out.println(
                        "Could not open file for reading or read from file: "
                                + sSentiTextFileIn);
                var14.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 从arff文件建立stringIndex索引.
     * <br>
     *
     * @param sArffFileIn arff输入文件
     * @return 建立的字符串索引。若失败则返回null
     */
    private static StringIndex buildIndexFromArff(final String sArffFileIn) {
        File f = new File(sArffFileIn);
        if (!f.exists()) {
            System.out.println("Could not find the ARFF file: " + sArffFileIn);
            return null;
        } else {
            StringIndex stringIndex = new StringIndex();
            stringIndex.initialise(0, true, true);
            int iPos;
            int var8 = VAR_NUM;

            try {
                BufferedReader rReader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(sArffFileIn)));

                while (rReader.ready()) {
                    String sLine = rReader.readLine();
                    if (sLine.contains("@data")) {
                        break;
                    }

                    if (sLine.length() > 0) {
                        String[] sData = sLine.split(" ");
                        if (sData.length == ARFF_ITEM_NUM && sData[0].equals(
                                "@attribute") && sData[2].equals("numeric")
                                && sData[1].length() > 2 && !sData[1].contains(
                                "Ngram")) { // 这个3和数据结构有关，不应该在外面定义。
                            int iStringLastOld1 = stringIndex.getLastWordID();
                            if (sData[1].substring(1).equals("Q")) {
                                iPos = sData[1].indexOf("_");
                                if (iPos > 0) {
                                    stringIndex.addString(
                                            sData[1].substring(iPos), false);
                                    if (iStringLastOld1
                                            != stringIndex.getLastWordID()) {
                                        stringIndex.addComment(
                                                iStringLastOld1 + 1, sLine);
                                    }
                                } else {
                                    System.out.println(
                                            "Invalid Q index entry: " + sLine
                                                    + " in " + sArffFileIn);
                                }
                            } else {
                                stringIndex.addString(sData[1], false);
                            }

                            for (;
                                 iStringLastOld1 == stringIndex.getLastWordID();
                                 System.out.println(
                                         "Invalid or duplicate index entry: "
                                                 + sLine + " in "
                                                 + sArffFileIn)) {
                                stringIndex.addString("R_" + var8++, false);
                                if (iStringLastOld1
                                        != stringIndex.getLastWordID()) {
                                    stringIndex.addComment(iStringLastOld1 + 1,
                                            sLine);
                                }
                            }
                        }
                    }
                }

                rReader.close();
                return stringIndex;
            } catch (IOException var10) {
                System.out.println("Couldn't open/read from: " + sArffFileIn);
                var10.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 将两个arff文件合并为一个.
     * <br>
     * 使用BufferedReader读取文件。
     * 调用printArffHeader方法，将两个输入文件的头部信息。（包括@relation和@attribute
     * 标签）写入输出文件中，其中第二个输入文件需要跳过@relation标签
     * 调用loadArffAttributes方法，将两个输入文件的属性名称和类型存储到字符串数组sAttributes1
     * 和sAttributes2中，并将属性个数赋值给iAttributeArray[0]。
     * 调用printNonDuplicateAttributes
     * 方法，将第二个输入文件中与第一个输入文件不重复的属性写入输出文件中，并返回一个布尔数组bDuplicate2
     * ，表示第二个输入文件中哪些属性是重复的。
     * 调用printDataWithoutDuplicates方法，将两个输入文件中的数据部分（包括@data
     * 标签和实例值）写入输出文件中，并根据bDuplicate2数组跳过重复属性。
     *
     * @param sArffFile1      第一个arff文件
     * @param sArffFile2      第二个arff文件
     * @param bVerbose        是否打印详细信息
     * @param sMergedArffFile 合并后的arff文件
     * @return 是否成功合并。
     */
    public static boolean combineTwoARFFs(final String sArffFile1,
                                          final String sArffFile2,
                                          final boolean bVerbose,
                                          final String sMergedArffFile) {
        File f = new File(sArffFile1);
        if (!f.exists()) {
            System.out.println("Couldn't find Arff file: " + sArffFile1);
            return false;
        } else {
            f = new File(sArffFile2);
            if (!f.exists()) {
                System.out.println("Couldn't find Arff file: " + sArffFile2);
                return false;
            } else {
                int[] iAttributeArray = new int[1];

                try {
                    BufferedReader rReader1 = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(sArffFile1)));
                    BufferedReader rReader2 = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(sArffFile2)));
                    BufferedWriter wWriter = new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(sMergedArffFile)));
                    printArffHeader(rReader1, wWriter, false);
                    printArffHeader(rReader2, wWriter, true);
                    String[] sAttributes1 = loadArffAttributes(rReader1,
                            iAttributeArray);
                    int iAttributes1Count = iAttributeArray[0];
                    String[] sAttributes2 = loadArffAttributes(rReader2,
                            iAttributeArray);
                    int iAttributes2Count = iAttributeArray[0];
                    boolean[] bDuplicate2 = printNonDuplicateAttributes(
                            sAttributes1, iAttributes1Count, sAttributes2,
                            iAttributes2Count, bVerbose, wWriter);
                    printDataWithoutDuplicates(rReader1, rReader2, bDuplicate2,
                            iAttributes1Count, iAttributes2Count, wWriter);
                    rReader1.close();
                    rReader2.close();
                    wWriter.close();
                    return true;
                } catch (IOException var14) {
                    System.out.println(
                            "I/O error with input or output file, e.g.,: "
                                    + sArffFile1);
                    var14.printStackTrace();
                    return false;
                }
            }
        }
    }

    /**
     * 删除arff文件中的某列并将余下的第一列移动到最后一列.
     * <br>
     * 调用deleteColumnFromArff和moveColumnToEndOfArff方法
     *
     * @param sArffFileIn  输入arff文件
     * @param iColToDelete 需要删除的列
     * @param sArffFileOut 输出的arff文件
     * @return 是否成功
     */
    public static boolean deleteColAndMoveRemainingFirstColToEnd(
            final String sArffFileIn, final int iColToDelete,
            final String sArffFileOut) {
        File f = new File(sArffFileIn);
        if (!f.exists()) {
            System.out.println("Could not find Arff file: " + sArffFileIn);
            return false;
        } else {
            String sArffTemp = sArffFileIn + ".temp";
            f = new File(sArffTemp);
            if (f.exists()) {
                f.delete();
            }

            deleteColumnFromArff(sArffFileIn, iColToDelete, sArffTemp);
            moveColumnToEndOfArff(sArffTemp, 1, sArffFileOut);
            f = new File(sArffTemp);
            if (f.exists()) {
                f.delete();
            }

            return true;
        }
    }

    /**
     * 将某一列移到arff文件的最后一列.
     * <br>
     * 简单的文件操作，调用了printArffHeader，printSelectedAttributes和printSelectedData方法
     *
     * @param sArffFileIn  arff输入文件
     * @param iColToMove   需要移动的列
     * @param sArffFileOut arff输出文件
     * @return 是否成功
     */
    public static boolean moveColumnToEndOfArff(final String sArffFileIn,
                                                final int iColToMove,
                                                final String sArffFileOut) {
        File f = new File(sArffFileIn);
        if (!f.exists()) {
            System.out.println("Could not find Arff file: " + sArffFileIn);
            return false;
        } else {
            String[] sAttributes;
            int[] iAttArr = new int[1];

            try {
                BufferedReader srArff = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(sArffFileIn)));
                BufferedWriter swNew = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(sArffFileOut)));
                printArffHeader(srArff, swNew, true);
                sAttributes = loadArffAttributes(srArff, iAttArr);
                int iAttributesCount = iAttArr[0];
                boolean[] bDelete = new boolean[iAttributesCount + 1];

                for (int i = 0; i <= iAttributesCount; ++i) {
                    bDelete[i] = false;
                }

                bDelete[iColToMove] = true;
                printSelectedAttributes(sAttributes, iAttributesCount, bDelete,
                        swNew);
                swNew.write(sAttributes[iColToMove] + "\n");
                printSelectedData(srArff, bDelete, iAttributesCount, swNew,
                        true, false);
                swNew.close();
                srArff.close();
                return true;
            } catch (IOException var11) {
                System.out.println(
                        "File i/o error [moveColumnToEndOfArff]" + sArffFileIn
                                + " or " + sArffFileOut);
                var11.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 生成n-gram文件名.
     * <br>
     *
     * @param sSentiTextFileIn 输入的情感文本文件
     * @param iNgram           要使用的n-gram(n元语法)的大小
     * @return 生成的文件名
     */
    public static String ngramFileName(final String sSentiTextFileIn,
                                       final int iNgram) {
        return FileOps.sChopFileNameExtension(sSentiTextFileIn) + "_" + iNgram
                + ".arff";
    }

    /**
     * 生成n-gram文件名，但是形式为"_1-".
     * <br>
     *
     * @param sSentiTextFileIn 输入的情感文本文件
     * @param iNgram           要使用的n-gram(n元语法)的大小
     * @return 生成的文件名
     */
    public static String oneToNgramFileName(final String sSentiTextFileIn,
                                            final int iNgram) {
        return FileOps.sChopFileNameExtension(sSentiTextFileIn) + "_1-" + iNgram
                + ".arff";
    }

    /**
     * 生成n-gram文件名,但后缀为pos.arff或neg.arff.
     * <br>
     *
     * @param sSentiTextFileIn 输入的情感文本文件
     * @param iNgram           要使用的n-gram(n元语法)的大小
     * @param bPos             正面还是负面情感
     * @return 生成的文件名
     */
    public static String ngramFileNamePosNeg(final String sSentiTextFileIn,
                                             final int iNgram,
                                             final boolean bPos) {
        return bPos ? FileOps.sChopFileNameExtension(sSentiTextFileIn) + "_"
                + iNgram + "pos.arff"
                : FileOps.sChopFileNameExtension(sSentiTextFileIn) + "_"
                        + iNgram + "neg.arff";
    }

    /**
     * 在"_1-"基础上生成n-gram文件名,但后缀为pos.arff或neg.arff.
     * <br>
     *
     * @param sSentiTextFileIn 输入的情感文本文件
     * @param iNgram           要使用的n-gram(n元语法)的大小
     * @param bPos             正面还是负面情感
     * @return 生成的文件名
     */
    public static String oneToNgramFileNamePosNeg(final String sSentiTextFileIn,
                                                  final int iNgram,
                                                  final boolean bPos) {
        return bPos ? FileOps.sChopFileNameExtension(sSentiTextFileIn) + "_1-"
                + iNgram + "pos.arff"
                : FileOps.sChopFileNameExtension(sSentiTextFileIn) + "_1-"
                        + iNgram + "neg.arff";
    }

    /**
     * 将一个文本文件中的情感分析数据转换为多个ARFF文件，每个文件包含不同的n-gram特征.
     * <br>
     * 首先，它检查输入的情感文本文件是否存在，如果不存在则返回null。
     * 然后，它根据输入的ARFF文件名创建一个字符串索引对象，用于过滤特征。
     * 接着，它根据文本解析选项中指定的n-gram大小，对情感文本文件进行分词和特征提取，并将结果保存为ARFF格式的文件。
     * 然后，它将不同n-gram大小的ARFF文件合并为一个ARFF文件，包含所有的特征和类别标签。
     * 最后，如果情感类型是4（即二分类），它会将合并后的ARFF文件分成两个子文件，分别对应正面和负面情感，并删除多余的列。
     *
     * @param sSentiTextFileIn                  输入的情感文本文件
     * @param bHeaderLine                       是否有表头行
     * @param textParsingOptions                文本解析选项
     * @param classOptions                      分类选项
     * @param resources                         分类资源
     * @param iSentimentType                    情感类型
     * @param iMinFeatureFrequency              最小特征频率
     * @param sArffFileForPermittedFeaturesList 一个字符串，表示允许使用的特征列表所在的ARFF文件路径
     * @return 一个字符串数组，表示输出的ARFF文件路径。
     */
    public static String[] convertSentimentTextToArffMultiple(
            final String sSentiTextFileIn, final boolean bHeaderLine,
            final TextParsingOptions textParsingOptions,
            final ClassificationOptions classOptions,
            final ClassificationResources resources, final int iSentimentType,
            final int iMinFeatureFrequency,
            final String sArffFileForPermittedFeaturesList) {
        File f = new File(sSentiTextFileIn);
        if (!f.exists()) {
            System.out.println(
                    "Could not find sentiment file: " + sSentiTextFileIn);
            return null;
        } else {
            StringIndex arffStringIndex = null;
            if (!sArffFileForPermittedFeaturesList.equals("")) {
                arffStringIndex = buildIndexFromArff(
                        sArffFileForPermittedFeaturesList);
            }

            int iNgramMax = textParsingOptions.getIgNgramSize();
            String sLastCombinedOutFile = "";

            int iNgram;
            String sOutFile;
            for (iNgram = 1; iNgram <= iNgramMax; ++iNgram) {
                textParsingOptions.setIgNgramSize(iNgram);
                sOutFile = ngramFileName(sSentiTextFileIn, iNgram);
                f = new File(sOutFile);
                if (f.exists()) {
                    f.delete();
                }

                convertSentimentTextToArff(sSentiTextFileIn, sOutFile,
                        bHeaderLine, textParsingOptions, classOptions,
                        resources, iSentimentType, iMinFeatureFrequency,
                        arffStringIndex);
                if (iNgram > 1) {
                    String sNewCombinedOutFile = oneToNgramFileName(
                            sSentiTextFileIn, iNgram);
                    f = new File(sNewCombinedOutFile);
                    if (f.exists()) {
                        f.delete();
                    }

                    combineTwoARFFs(sLastCombinedOutFile, sOutFile, false,
                            sNewCombinedOutFile);
                    sLastCombinedOutFile = sNewCombinedOutFile;
                } else {
                    sLastCombinedOutFile = sOutFile;
                }
            }

            int iOutfileLast = -1;
            String[] sFinalOutFile = new String[MAX_FILE_PATH];
            if (iSentimentType == POS_NEG) {
                for (iNgram = 1; iNgram <= iNgramMax; ++iNgram) {
                    sOutFile = ngramFileName(sSentiTextFileIn, iNgram);
                    ++iOutfileLast;
                    sFinalOutFile[iOutfileLast] = ngramFileNamePosNeg(
                            sSentiTextFileIn, iNgram, true);
                    deleteColAndMoveRemainingFirstColToEnd(sOutFile, 2,
                            sFinalOutFile[iOutfileLast]);
                    ++iOutfileLast;
                    sFinalOutFile[iOutfileLast] = ngramFileNamePosNeg(
                            sSentiTextFileIn, iNgram, false);
                    deleteColAndMoveRemainingFirstColToEnd(sOutFile, 1,
                            sFinalOutFile[iOutfileLast]);
                    f = new File(sOutFile);
                    f.delete();
                    if (iNgram > 1) {
                        sOutFile = oneToNgramFileName(sSentiTextFileIn, iNgram);
                        ++iOutfileLast;
                        sFinalOutFile[iOutfileLast] = oneToNgramFileNamePosNeg(
                                sSentiTextFileIn, iNgram, true);
                        deleteColAndMoveRemainingFirstColToEnd(sOutFile, 2,
                                sFinalOutFile[iOutfileLast]);
                        ++iOutfileLast;
                        sFinalOutFile[iOutfileLast] = oneToNgramFileNamePosNeg(
                                sSentiTextFileIn, iNgram, false);
                        deleteColAndMoveRemainingFirstColToEnd(sOutFile, 1,
                                sFinalOutFile[iOutfileLast]);
                        f = new File(sOutFile);
                        f.delete();
                    }
                }
            } else if (iSentimentType == BINARY || iSentimentType == TRINARY
                    || iSentimentType == SCALE) {
                for (iNgram = 1; iNgram <= iNgramMax; ++iNgram) {
                    ++iOutfileLast;
                    sFinalOutFile[iOutfileLast] = ngramFileName(
                            sSentiTextFileIn, iNgram);
                    File g = new File(sFinalOutFile[iOutfileLast] + ".temp");
                    f = new File(sFinalOutFile[iOutfileLast]);
                    f.renameTo(g);
                    moveColumnToEndOfArff(sFinalOutFile[iOutfileLast] + ".temp",
                            1, sFinalOutFile[iOutfileLast]);
                    g.delete();
                    if (iNgram > 1) {
                        ++iOutfileLast;
                        sFinalOutFile[iOutfileLast] = oneToNgramFileName(
                                sSentiTextFileIn, iNgram);
                        g = new File(sFinalOutFile[iOutfileLast] + ".temp");
                        f = new File(sFinalOutFile[iOutfileLast]);
                        f.renameTo(g);
                        moveColumnToEndOfArff(
                                sFinalOutFile[iOutfileLast] + ".temp", 1,
                                sFinalOutFile[iOutfileLast]);
                        g.delete();
                    }
                }
            }

            return sFinalOutFile;
        }
    }

    /**
     * 删除arff文件的某一列.
     * <br>
     *
     * @param sArffFile    文件
     * @param iColToRemove 需要删除的列的位置
     * @param sNewArffFile 生成的新文件
     * @return 返回操作是否成功
     */
    public static boolean deleteColumnFromArff(final String sArffFile,
                                               final int iColToRemove,
                                               final String sNewArffFile) {
        File f = new File(sArffFile);
        if (!f.exists()) {
            System.out.println("Could not find Arff file: " + sArffFile);
            return false;
        } else {
            String[] sAttributes;
            int iAttributesCount;
            int[] iAttArr = new int[1];

            try {
                BufferedReader rArff = new BufferedReader(
                        new InputStreamReader(new FileInputStream(sArffFile)));
                BufferedWriter wNew = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(sNewArffFile)));
                printArffHeader(rArff, wNew, true);
                sAttributes = loadArffAttributes(rArff, iAttArr);
                iAttributesCount = iAttArr[0];
                boolean[] bDelete = new boolean[iAttributesCount + 1];

                for (int i = 0; i <= iAttributesCount; ++i) {
                    bDelete[i] = false;
                }

                bDelete[iColToRemove] = true;
                printSelectedAttributes(sAttributes, iAttributesCount, bDelete,
                        wNew);
                printSelectedData(rArff, bDelete, iAttributesCount, wNew, false,
                        true);
                wNew.close();
                rArff.close();
                return true;
            } catch (IOException var11) {
                System.out.println(
                        "I/O error with input or output file, e.g.,: "
                                + sArffFile);
                var11.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 将选定的属性打印到arff文件中.
     *
     * @param sAttributes      需要打印的arff文件的属性字符串数组
     * @param iAttributesCount 需要打印的属性总数
     * @param bDelete          布尔值数组。表现哪些属性该被删除
     * @param swNew            用于写入输出文件的bufferWriter
     * @return 操作是否成功
     */
    private static boolean printSelectedAttributes(final String[] sAttributes,
                                                   final int iAttributesCount,
                                                   final boolean[] bDelete,
                                                   final BufferedWriter swNew) {
        StringBuilder sDelList = new StringBuilder();

        try {
            if (sAttributes[0] != null) {
                swNew.write(sAttributes[0] + "\n");
            }

            for (int i = 1; i <= iAttributesCount; ++i) {
                if (!bDelete[i]) {
                    swNew.write(sAttributes[i] + "\n");
                }
            }

            return true;
        } catch (IOException var9) {
            System.out.println("Error writing [printSelectedAttributes]");
            var9.printStackTrace();
            return false;
        }
    }

    /**
     * 将输入arff文件中选定的数据打印到输出文件中.
     * <br>
     * 调用了printCondensedData方法
     *
     * @param srArff                 arff文件的reader
     * @param bDeleteCol             标记列是否需要删除的布尔数组
     * @param iAttributeCount        属性总数
     * @param swOutput               输出的writer
     * @param bPrintDeletedColsAtEnd 是否要打印删除的列
     * @param bVerbose               是否需要打印详细信息
     * @return 操作是否成功
     */
    private static boolean printSelectedData(final BufferedReader srArff,
                                             final boolean[] bDeleteCol,
                                             final int iAttributeCount,
                                             final BufferedWriter swOutput,
                                             final boolean bPrintDeletedColsAtEnd,
                                             final boolean bVerbose) {
        int[] iAttID = new int[iAttributeCount + 1];
        int[] iData = new int[iAttributeCount + 1];
        int iPairs;
        int iCount = 0;
        int iLastPrintedAttribute = 0;
        int[] iNewAttributeID = new int[iAttributeCount + 1];
        int iAttUsed = 0;

        int iCol;
        for (iCol = 1; iCol <= iAttributeCount; ++iCol) {
            if (!bDeleteCol[iCol]) {
                iLastPrintedAttribute = iCol;
                iNewAttributeID[iCol - 1] = iAttUsed++;
            }
        }

        for (iCol = 1; iCol <= iAttributeCount; ++iCol) {
            if (bDeleteCol[iCol]) {
                iNewAttributeID[iCol - 1] = iAttUsed++;
            }
        }

        try {
            swOutput.write("@data\n");

            label105:
            while (true) {
                while (true) {
                    String sLine;
                    do {
                        if (!srArff.ready()) {
                            break label105;
                        }

                        sLine = srArff.readLine();
                        ++iCount;
                    } while (sLine.length() == 0);

                    String[] sData;
                    if (BG_SAVE_ARFF_AS_CONDENSED) {
                        iPairs = -1;
                        sData = sLine.substring(1, sLine.length() - 1)
                                .split(",");

                        for (String sDatum : sData) {
                            if (sDatum.length() > 2) {
                                String[] sIDVal = sDatum.trim().split(" ");
                                int iSourceID = Integer.parseInt(sIDVal[0]);
                                if (bPrintDeletedColsAtEnd || !bDeleteCol[
                                        iSourceID + 1]) {
                                    ++iPairs;
                                    iAttID[iPairs] = iNewAttributeID[iSourceID];

                                    try {
                                        iData[iPairs] = Integer.parseInt(
                                                sIDVal[1]);
                                    } catch (Exception var21) {
                                        iData[iPairs] = 0;
                                    }
                                }
                            }
                        }

                        printCondensedData(swOutput, iAttID, iData, iPairs);
                    } else {
                        StringBuilder sDeletedCols = new StringBuilder();
                        sData = sLine.split(",");

                        for (iCol = 1; iCol < iLastPrintedAttribute; ++iCol) {
                            if (!bDeleteCol[iCol]) {
                                swOutput.write(sData[iCol - 1] + ",");
                            } else {
                                sDeletedCols.append(sData[iCol - 1])
                                        .append(",");
                            }
                        }

                        if (!bPrintDeletedColsAtEnd) {
                            swOutput.write(
                                    sData[iLastPrintedAttribute - 1] + "\n");
                        } else {
                            for (iCol = iLastPrintedAttribute;
                                 iCol <= iAttributeCount; ++iCol) {
                                if (bDeleteCol[iCol]) {
                                    sDeletedCols.append(sData[iCol - 1])
                                            .append(",");
                                }
                            }

                            if (sDeletedCols.length() > 0) {
                                swOutput.write(
                                        sData[iLastPrintedAttribute - 1] + ","
                                                + sDeletedCols.substring(0,
                                                sDeletedCols.length() - 1)
                                                + "\n");
                            } else {
                                swOutput.write(sData[iLastPrintedAttribute - 1]
                                        + "\n");
                            }
                        }
                    }
                }
            }
        } catch (IOException var22) {
            System.out.println(
                    "I/O error with input or output file [printSelectedData]");
            var22.printStackTrace();
            return false;
        }

        if (bVerbose) {
            System.out.println(iCount + " lines of data saved");
        }

        return true;
    }

    /**
     * 将一对整数数组（iAtt和iData）按照ARFF的稀疏格式输出到一个缓冲写入器（swArff）中.
     * <br>
     *
     * @param swArff    arff文件writer
     * @param iAtt      属性
     * @param iData     属性对应的数据
     * @param iLastPair 最后一对属性和数据的指针
     */
    private static void printCondensedData(final BufferedWriter swArff,
                                           final int[] iAtt, final int[] iData,
                                           final int iLastPair) {
        Sort.quickSortIntWithInt(iAtt, iData, 0, iLastPair);

        try {
            swArff.write("{");
            if (iLastPair > -1) {
                swArff.write(iAtt[0] + " " + iData[0]);
            }

            for (int iPair = 1; iPair <= iLastPair; ++iPair) {
                swArff.write("," + iAtt[iPair] + " " + iData[iPair]);
            }

            swArff.write("}\n");
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    /**
     * 合并两个arff文件并且去除重复属性列.
     * <br>
     * bgSaveArffAsCondensed为真，则使用稀疏格式来保存数据，即只写入非零值和其对应的属性索引；
     * 否则使用普通格式来保存数据，即按照逗号分隔每个值。
     *
     * @param rArff1            第一个arff文件的reader
     * @param rArff2            第二个arff文件的reader
     * @param bDuplicate2       用于标记第二个文件中哪些属性列是与第一个文件重复的布尔数组
     * @param iAttributes1Count 第一个文件中属性列的数量
     * @param iAttributes2Count 第二个文件中属性列的数量
     * @param wMerged           输出的arff文件的writer
     */
    private static void printDataWithoutDuplicates(final BufferedReader rArff1,
                                                   final BufferedReader rArff2,
                                                   final boolean[] bDuplicate2,
                                                   final int iAttributes1Count,
                                                   final int iAttributes2Count,
                                                   final BufferedWriter wMerged) {
        int iAttUsed = 0;
        int[] iAttribute2ID = new int[iAttributes2Count + 1];

        int iCol;
        for (iCol = 1; iCol <= iAttributes2Count; ++iCol) {
            if (!bDuplicate2[iCol]) {
                iAttribute2ID[iCol] = iAttUsed++ + iAttributes1Count;
            }
        }

        try {
            wMerged.write("@data\n");
            while (true) {
                String sLine1;
                String sLine2;
                do {
                    if (!rArff1.ready() || !rArff2.ready()) {
                        return;
                    }

                    sLine1 = rArff1.readLine();
                    sLine2 = rArff2.readLine();
                } while (sLine2.equals(""));

                String[] sData2;
                if (BG_SAVE_ARFF_AS_CONDENSED) {
                    wMerged.write(sLine1.substring(0, sLine1.length() - 1));
                    sData2 = sLine2.substring(1, sLine2.length() - 1)
                            .split(",");

                    for (String s : sData2) {
                        try {
                            String[] sIDValue = s.trim().split(" ");
                            iCol = Integer.parseInt(sIDValue[0]) + 1;
                            if (!bDuplicate2[iCol]) {
                                wMerged.write(", " + iAttribute2ID[iCol] + " "
                                        + sIDValue[1]);
                            }
                        } catch (Exception var15) {
                            System.out.println(
                                    "Error processing ID value pair " + s
                                            + " [printDataWithoutDuplicates]");
                            var15.printStackTrace();
                        }
                    }

                    wMerged.write("}\n");
                } else {
                    wMerged.write(sLine1);
                    sData2 = sLine2.split(",");

                    for (iCol = 1; iCol <= iAttributes2Count; ++iCol) {
                        if (!bDuplicate2[iCol]) {
                            wMerged.write("," + sData2[iCol - 1]);
                        }
                    }

                    wMerged.write("\n");
                }
            }
        } catch (IOException var16) {
            System.out.println(
                    "Error writing to file [printDataWithoutDuplicates]");
            var16.printStackTrace();
        }
    }

    /**
     * 将两组属性列合并且去除重复的属性列并将其写入wMerged中.
     *
     * @param sAttributes1      第一个文件的属性列数组
     * @param iAttributes1Count 第一个文件的属性个数
     * @param sAttributes2      第二个文件的属性列数组
     * @param iAttributes2Count 第二个文件的属性个数
     * @param bVerbose          是否打印详细信息
     * @param wMerged           合并后的writer
     * @return 标志第二个属性数组中哪些属性和第一个属性数组重复的布尔数组
     */
    private static boolean[] printNonDuplicateAttributes(
            final String[] sAttributes1, final int iAttributes1Count,
            final String[] sAttributes2, final int iAttributes2Count,
            final boolean bVerbose, final BufferedWriter wMerged) {
        int iDupCount = 0;
        StringBuilder sDuplicateList = new StringBuilder();
        boolean[] bDuplicate2 = new boolean[iAttributes2Count + 1];

        try {
            int i;
            for (i = 1; i <= iAttributes1Count; ++i) {
                wMerged.write(sAttributes1[i] + "\n");
            }

            for (int j = 1; j <= iAttributes2Count; ++j) {
                for (i = 1; i <= iAttributes1Count; ++i) {
                    if (sAttributes2[j].equals(sAttributes1[i])) {
                        if (bVerbose) {
                            sDuplicateList.append(sAttributes1[i])
                                    .append(" | ");
                        }

                        bDuplicate2[j] = true;
                        ++iDupCount;
                        break;
                    }
                }

                if (!bDuplicate2[j]) {
                    wMerged.write(sAttributes2[j] + "\n");
                }
            }
        } catch (IOException var12) {
            System.out.println(
                    "Error writing to file file [printNonDuplicateAttributes]");
            var12.printStackTrace();
        }

        if (bVerbose) {
            System.out.println(
                    iDupCount + " duplicates found out of " + iAttributes1Count
                            + "\n" + sDuplicateList);
        }

        return bDuplicate2;
    }

    /**
     * 将输入文件的头部信息打印到输出文件中，并且返回头部信息总行数.
     *
     * @param rArffIn        输入arff文件的reader
     * @param wArffOut       输出arff文件的writer
     * @param bPrintRelation 是否打印@relation内容
     * @return 文件头部信息总行数
     */
    private static int printArffHeader(final BufferedReader rArffIn,
                                       final BufferedWriter wArffOut,
                                       final boolean bPrintRelation) {
        int iLineCount = 0;
        String sLine = "";

        try {
            if (rArffIn.ready()) {
                sLine = rArffIn.readLine();
            }

            while (rArffIn.ready() && sLine.indexOf("@relation ") != 0) {
                wArffOut.write(sLine + "\n");
                ++iLineCount;
                sLine = rArffIn.readLine();
            }

            if (bPrintRelation) {
                wArffOut.write(sLine + "\n");
            }
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return iLineCount;
    }

    /**
     * 将arff文件中的所有属性提取到一个字符串数组中并返回，并将属性数放入iAttributeCountArr[0]中.
     *
     * @param rArffIn            输入arff文件的BufferedReader
     * @param iAttributeCountArr 存放属性数的数组
     * @return 记录文件中所有属性的字符串数组
     */
    private static String[] loadArffAttributes(final BufferedReader rArffIn,
                                               final int[] iAttributeCountArr) {
        String sLine = "";
        int iMaxAttributes = MAX_ATTRIBUTE_NUM;
        int iAttributesCount = 0;
        String[] sAttributes = new String[iMaxAttributes];

        try {
            if (rArffIn.ready()) {
                sLine = rArffIn.readLine();
            }

            for (; rArffIn.ready() && sLine.indexOf("@data") != 0;
                 sLine = rArffIn.readLine()) {
                if (!sLine.equals("") && sLine.charAt(0) != '%') {
                    if (sLine.indexOf("@relation ") == 0) {
                        sAttributes[0] = sLine;
                    } else {
                        ++iAttributesCount;
                        if (iAttributesCount == iMaxAttributes - 1) {
                            sAttributes = increaseArraySize(sAttributes,
                                    iMaxAttributes, 2 * iMaxAttributes);
                            iMaxAttributes *= 2;
                        }

                        sAttributes[iAttributesCount] = sLine;
                    }
                }
            }
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        iAttributeCountArr[0] = iAttributesCount;
        return sAttributes;
    }

    /**
     * 扩大字符串数组长度.
     *
     * @param sArray            需要被扩大的字符串数组
     * @param iCurrentArraySize 当前数组长度
     * @param iNewArraySize     新数组长度
     * @return 扩大后的数组
     */
    private static String[] increaseArraySize(final String[] sArray,
                                              final int iCurrentArraySize,
                                              final int iNewArraySize) {
        if (iNewArraySize <= iCurrentArraySize) {
            return sArray;
        } else {
            String[] sArrayTemp = new String[iNewArraySize];
            System.arraycopy(sArray, 0, sArrayTemp, 0, iCurrentArraySize);
            return sArrayTemp;
        }
    }

    /**
     * 根据信息增益的值来选择最优的属性列，并将结果存储在一个布尔数组中.
     * <br>
     * 信息增益是一种衡量属性对分类结果影响程度的指标，值越大表示属性越重要。
     * 具体实现便是简单的降序排序
     *
     * @param fColIG            用于存储每个属性列的信息增益值的浮点数组
     * @param iAttributeCount   属性列的数量
     * @param iFeaturesToSelect 要选择的属性列的数量
     * @param bUseCol           标记哪些属性列被选中的布尔数组
     */
    private static void selectTopNAttributes(final double[] fColIG,
                                             final int iAttributeCount,
                                             final int iFeaturesToSelect,
                                             final boolean[] bUseCol) {
        int[] iIndex = new int[iAttributeCount + 1];
        int featuresToSelect = iFeaturesToSelect;
        int i;
        for (i = 1; i <= iAttributeCount; ++i) {
            iIndex[i] = i;
            bUseCol[i] = false;
        }

        Sort.quickSortNumbersDescendingViaIndex(fColIG, iIndex, 1,
                iAttributeCount);
        bUseCol[iAttributeCount] = true;
        bUseCol[0] = true;
        if (featuresToSelect > 0) {
            bUseCol[iIndex[1]] = true;
            --featuresToSelect;

            for (i = 2; i <= iAttributeCount && featuresToSelect >= 1; ++i) {
                bUseCol[iIndex[i]] = true;
                --featuresToSelect;
            }
        }

    }

    /**
     * 将每个属性列的信息增益值和属性名按照降序排列，并输出到一个文件中.
     * <br>
     * 使用了降序快排来实现
     *
     * @param fColIG          用于存储每个属性列的信息增益值的浮点数组
     * @param sAttributes     属性名称数组
     * @param iAttributeCount 属性列的数量
     * @param sIGListOut      输出文件的路径
     */
    private static void printInformationGainValues(final double[] fColIG,
                                                   final String[] sAttributes,
                                                   final int iAttributeCount,
                                                   final String sIGListOut) {
        DecimalFormat df = new DecimalFormat(
                "#.######"); //df是一个格式化对象，用于将浮点数转换为六位小数的字符串
        int[] iIndex = new int[iAttributeCount + 1];

        int iCol;
        for (iCol = 1; iCol <= iAttributeCount;) {
            iIndex[iCol] = iCol++;
        }

        Sort.quickSortNumbersDescendingViaIndex(fColIG, iIndex, 1,
                iAttributeCount);

        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(sIGListOut)));

            for (iCol = 1; iCol <= iAttributeCount; ++iCol) {
                wWriter.write(sAttributes[iIndex[iCol]] + " " + df.format(
                        fColIG[iIndex[iCol]]) + "\r\n");
            }

            wWriter.close();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }

    /**
     * 计算每个属性列的信息增益值，并将结果储存在一个浮点数组中<br>
     * 这段代码可以用于进行特征选择，即从原始数据集中筛选出最有用或最相关的特征子集，以减少维度、降低复杂度、提高准确率等
     * 使用一个循环遍历除了类别标签之外的所有属性列，并对每一列进行以下操作：<br>
     * 1.初始化iAttributeValueCount为0，并清空iAttributeValue数组。<br>
     * 2.使用另一个循环遍历该属性列中所有实例，并将出现过的不同属性值记录到iAttributeValue
     * 数组中，并更新iAttributeValueCount。<br>
     * 3.初始化fAttributeEntropySum为0.0。<br>
     * 4. 使用另一个循环遍历该属性列中出现过的所有不同属性值，并对每个属性值进行以下操作：<br>
     * 1. 初始化iAttributeValueFreq为0，并清空iAttributeValueClassCount数组。<br>
     * 2. 使用另一个循环遍历该属性列中所有实例，并统计该属性值出现的频数以及对应各个类别标签出现的频数。<br>
     * 3. 计算该属性值条件下数据集子集（即包含该属性值实例）的熵fAttributeEntropy
     * 。熵计算公式为：fAttributeEntropy = -sum(p * log2(p))其中p为某个类别标签在数据集子集中占比。<br>
     * 4. 将fAttributeEntropy乘以该数据集子集占总数据集比例并累加到fAttributeEntropySum上。<br>
     * 5. 计算该属性列相对于总数据集熵降低了多少，即信息增益fColIG[iCol]。信息增益计算公式为：
     * fColIG[iCol] =fOverallEntropy - fAttributeEntropySum<br>
     *
     * @param iData    * 用于存储数据集的一个整数二维数组，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例用于存储数据集，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例
     * @param iAttributeCount 属性列的数量
     * @param iDataCount      实例行的数量
     * @param fColIG          用于存储每个属性列的信息增益值的浮点数组
     */
    private static void calculateInformationGainOfData(final int[][] iData,
                                                       final int iAttributeCount,
                                                       final int iDataCount,
                                                       final double[] fColIG) {
        int[] iAttributeValue = new int[MAX_ATTRIBUTE_NUM
                + 1]; //用于存储某个属性列中出现过的不同属性值的整数数组
        int iFirstClass = findFirstClassInData(iData, iDataCount,
                iAttributeCount); //第一个和最后一个类别标签
        int iLastClass = findLastClassInData(iData, iDataCount,
                iAttributeCount);
        int[] iClass = new int[iLastClass + 1]; //用于存储每个类别标签出现的频数
        double fOverallEntropy = calculateClassesAndEntropyOfData(iData,
                iAttributeCount, iDataCount, iClass, iFirstClass,
                iLastClass); //数据集整体的熵值，表示分类结果的不确定性程度，值越大表示不确定性越高
        int[] iAttributeValueClassCount = new int[iLastClass
                + 1]; //用于存储某个属性值对应的各个类别标签出现的频数

        for (int iCol = 1; iCol < iAttributeCount; ++iCol) {
            int iAttributeValueCount = 0;

            int iRow;
            int i;
            for (iRow = 1; iRow <= iDataCount; ++iRow) {
                boolean bFound = false;

                for (i = 1; i <= iAttributeValueCount; ++i) {
                    if (iAttributeValue[i] == iData[iCol][iRow]) {
                        bFound = true;
                        break;
                    }
                }

                if (!bFound) {
                    ++iAttributeValueCount;
                    iAttributeValue[iAttributeValueCount] = iData[iCol][iRow];
                }
            }

            double fAttributeEntropySum = 0.0D;

            for (i = 1; i <= iAttributeValueCount; ++i) {
                int iAttributeValueFreq = 0;

                int j;
                for (j = iFirstClass; j <= iLastClass; ++j) {
                    iAttributeValueClassCount[j] = 0;
                }

                for (iRow = 1; iRow <= iDataCount; ++iRow) {
                    if (iAttributeValue[i] == iData[iCol][iRow]) {
                        ++iAttributeValueClassCount[iData[iAttributeCount][iRow]];
                        ++iAttributeValueFreq;
                    }
                }

                double fAttributeEntropy = 0.0D;

                for (j = iFirstClass; j <= iLastClass; ++j) {
                    double p = (double) iAttributeValueClassCount[j]
                            / (double) iAttributeValueFreq;
                    if (p > 0.0D) {
                        fAttributeEntropy -= p * Math.log(p) / Math.log(2.0D);
                    }
                }

                fAttributeEntropySum +=
                        fAttributeEntropy * (double) iAttributeValueFreq
                                / (double) iDataCount;
            }

            fColIG[iCol] = fOverallEntropy - fAttributeEntropySum;
        }

    }

    /**
     * 计算数据集整体的熵值，并将结果返回。熵值越大表示不确定性越高.
     * <br>
     * 代码中首先初始化fOverallEntropy为0.0。
     * 然后使用一个循环遍历所有实例，并统计每个类别标签出现的频数到iClass数组中。
     * 接着使用另一个循环遍历所有类别标签，并计算其在数据集中占比p。
     * 如果p大于0.0，则将其乘以自身对数再除以2为底对数，并累加到fOverallEntropy上。
     * 最后返回fOverallEntropy。
     *
     * @param iData
      用于存储数据集的一个整数二维数组，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例用于存储数据集，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例
     * @param iAttributeCount 属性列的数量
     * @param iDataCount      实例行的数量
     * @param iClass          用于存储每个类别标签出现的频数的整数数组
     * @param iFirstClass     第一个类别标签的在iClass数组中的位置
     * @param iLastClass      最后一个类别标签的在iClass数组中的位置
     * @return 数据集整体的熵值
     */
    private static double calculateClassesAndEntropyOfData(final int[][] iData,
                                                           final int iAttributeCount,
                                                           final int iDataCount,
                                                           final int[] iClass,
                                                           final int iFirstClass,
                                                           final int iLastClass) {
        double fOverallEntropy = 0.0D;

        int i;
        for (i = 1; i <= iDataCount; ++i) {
            ++iClass[iData[iAttributeCount][i]];
        }

        for (i = iFirstClass; i <= iLastClass; ++i) {
            double p = (double) iClass[i] / (double) iDataCount;
            if (p > 0.0D) {
                fOverallEntropy -= p * Math.log(p) / Math.log(2.0D);
            }
        }

        return fOverallEntropy;
    }

    /**
     * 在iData中找到第一个类别标签(class)并将其返回.
     *
     * @param iData    用于存储数据集的一个整数二维数组，每一列代表一个属性，
     *                 最后一列代表类别标签，每一行代表一个实例用于存储数据集，
     *                 每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例
     * @param iDataCount      实例行的数量
     * @param iAttributeCount 属性列的数量
     * @return 第一个类别标签的位置
     */
    private static int findFirstClassInData(final int[][] iData,
                                            final int iDataCount,
                                            final int iAttributeCount) {
        int iFirstClass = INVALID_FIRST_CLASS;
        for (int i = 1; i <= iDataCount; ++i) {
            if (iData[iAttributeCount][i] < iFirstClass) {
                iFirstClass = iData[iAttributeCount][i];
            }
        }

        return iFirstClass;
    }

    /**
     * 在iData中找到最后一个类别标签(class)并将其返回.
     *
     * @param iData              * 用于存储数据集的一个整数二维数组，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例用于存储数据集，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例
     * @param iDataCount      实例行的数量
     * @param iAttributeCount 属性列的数量
     * @return 最后一个类别标签的位置
     */
    private static int findLastClassInData(final int[][] iData,
                                           final int iDataCount,
                                           final int iAttributeCount) {
        int iLastClass = 0;

        for (int i = 1; i <= iDataCount; ++i) {
            if (iData[iAttributeCount][i] > iLastClass) {
                iLastClass = iData[iAttributeCount][i];
            }
        }

        return iLastClass;
    }

    /**
     * 将arff文件批量转换为文本文件.
     *
     * @param sArffIn      输入的arff文件数组
     * @param iArffInCount arff文件总数
     * @param sTextOut     输出的文本文件数组
     */
    public static void convertArffToTextMultiple(final String[] sArffIn,
                                                 final int iArffInCount,
                                                 final String[] sTextOut) {
        for (int i = 0; i < iArffInCount; ++i) {
            sTextOut[i] = FileOps.sChopFileNameExtension(sArffIn[i]) + " out"
                    + ".txt";
            convertArffToText(sArffIn[i], sTextOut[i]);
        }

    }

    /**
     * 将arff文件转换为文本文件.
     * <br>
     * 调用了readArffAttributesAndData和writeArffAttributesAndDataToText方法
     *
     * @param sArffIn  输入arff文件
     * @param sTextOut 输出文本文件
     */
    public static void convertArffToText(final String sArffIn,
                                         final String sTextOut) {
        int[] iAttData = countAttributesAndDataInArff(sArffIn);
        int iAttributeCount = iAttData[0];
        int iDataCount = iAttData[1];
        int[][] iData = new int[iAttributeCount + 1][iDataCount + 1];
        String[] sAttributes = new String[iAttributeCount + 1];
        readArffAttributesAndData(sArffIn, iAttributeCount, sAttributes, iData);
        writeArffAttributesAndDataToText(sAttributes, iData, iAttributeCount,
                iDataCount, sTextOut);
    }

    /**
     * 批量将输入文件信息增益最大的N个属性转化为输出arff文件.
     * <br>
     * 循环调用makeArffWithTopNAttributes
     *
     * @param sArffIn         输入arff文件数组
     * @param iArffInCount    输入arff文件总数
     * @param iTopNAttributes 增益最大的属性数
     * @param sArffOut        输出arff文件数组
     */
    public static void makeArffsWithTopNAttributes(final String[] sArffIn,
                                                   final int iArffInCount,
                                                   final int iTopNAttributes,
                                                   final String[] sArffOut) {
        for (int i = 0; i < iArffInCount; ++i) {
            sArffOut[i] = FileOps.sChopFileNameExtension(sArffIn[i]) + " "
                    + iTopNAttributes + ".arff";
            makeArffWithTopNAttributes(sArffIn[i], iTopNAttributes,
                    sArffOut[i]);
        }

    }

    /**
     * 批量将输入文件信息增益最大的N个属性转化为输出arff文件.
     * <br>
     * 循环调用makeArffWithTopNAttributes
     *
     * @param sArffIn         输入arff文件
     * @param iTopNAttributes 增益最大的属性数
     * @param sArffOut        输出arff文件
     */
    public static void makeArffWithTopNAttributes(final String sArffIn,
                                                  final int iTopNAttributes,
                                                  final String sArffOut) {
        int[] iAttData = countAttributesAndDataInArff(sArffIn);
        int iAttributeCount = iAttData[0];
        int iDataCount = iAttData[1];

        try {
            System.out.println(
                    "AttributeSelection: Attributes " + iAttributeCount
                            + " data " + iDataCount + " attribute x data "
                            + (long) (iAttributeCount + 1) * (long) (
                            iAttributeCount + 1));
            int[][] iData = new int[iAttributeCount + 1][iDataCount + 1];
            double[] fColIG = new double[iAttributeCount + 1];
            boolean[] bUseCol = new boolean[iAttributeCount + 1];
            String[] sAttributes = new String[iAttributeCount + 1];
            String sHeader = readArffAttributesAndData(sArffIn, iAttributeCount,
                    sAttributes, iData);
            calculateInformationGainOfData(iData, iAttributeCount, iDataCount,
                    fColIG);
            selectTopNAttributes(fColIG, iAttributeCount, iTopNAttributes,
                    bUseCol);
            printInformationGainValues(fColIG, sAttributes, iAttributeCount,
                    sArffOut + "_IG.txt");
            writeArffAttributesAndData(sHeader, sAttributes, iData,
                    iAttributeCount, iDataCount, bUseCol, sArffOut);
        } catch (Exception var11) {
            System.out.println(
                    "makeArffWithTopNAttributes error - probably insufficient to create attribute x data array");
            System.out.println(
                    "attribute " + iAttributeCount + " data " + iDataCount
                            + " attribute x data " + (iAttributeCount + 1) * (
                            iAttributeCount + 1));
            var11.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * 获取arff文件的属性和数据总数.
     *
     * @param sArffIn 输入arff文件
     * @return 保存属性和数据总数的int数组
     */
    private static int[] countAttributesAndDataInArff(final String sArffIn) {
        int iAttCount = 0;
        int iDataCount = 0;

        try {
            BufferedReader rArff = new BufferedReader(
                    new InputStreamReader(new FileInputStream(sArffIn)));
            String sLine = "";
            if (rArff.ready()) {
                sLine = rArff.readLine();
            }

            for (; rArff.ready() && sLine.indexOf("@data") != 0;
                 sLine = rArff.readLine()) {
                if (sLine.indexOf("@attribute ") == 0) {
                    ++iAttCount;
                }
            }

            while (rArff.ready()) {
                sLine = rArff.readLine();
                if (sLine.length() > 0) {
                    ++iDataCount;
                }
            }

            rArff.close();
        } catch (Exception var5) {
            System.out.println(
                    "[countAttributesAndDataInArff]Error reading file "
                            + sArffIn);
            var5.printStackTrace();
        }

        return new int[]{iAttCount, iDataCount};
    }

    /**
     * 读取arff文件的属性和数据.
     *
     * @param sArffIn         输入arff文件
     * @param iAttributeCount 属性总数
     * @param sAttributes     保存属性名数组
     * @param iData           用于存储数据集的一个整数二维数组，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例用于存储数据集，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例
     * @return arff文件的头部信息
     */
    private static String readArffAttributesAndData(final String sArffIn,
                                                    final int iAttributeCount,
                                                    final String[] sAttributes,
                                                    final int[][] iData) {
        StringBuilder sHeader = new StringBuilder();
        String sLine = "";
        int iAtt = 0;

        try {
            BufferedReader rArff = new BufferedReader(
                    new InputStreamReader(new FileInputStream(sArffIn)));
            if (rArff.ready()) {
                sLine = rArff.readLine();
            }

            for (; rArff.ready() && sLine.indexOf("@data") != 0;
                 sLine = rArff.readLine()) {
                if (sLine.length() > 0 && sLine.charAt(0) != '%') {
                    if (sLine.indexOf("@relation ") == 0) {
                        sAttributes[0] = sLine;
                    } else {
                        ++iAtt;
                        sAttributes[iAtt] = sLine;
                    }
                } else {
                    sHeader.append(sLine).append("\n");
                }
            }

            int iDataCount = 0;

            while (true) {
                String[] sData;
                do {
                    while (true) {
                        do {
                            if (!rArff.ready()) {
                                rArff.close();
                                return sHeader.toString();
                            }

                            sLine = rArff.readLine();
                        } while (sLine.length() <= 1);

                        ++iDataCount;
                        if (sLine.contains("{")) {
                            for (iAtt = 1; iAtt <= iAttributeCount; ++iAtt) {
                                iData[iAtt][iDataCount] = 0;
                            }
                            break;
                        }

                        sData = sLine.split(",");

                        for (iAtt = 1; iAtt <= iAttributeCount; ++iAtt) {
                            iData[iAtt][iDataCount] = Integer.parseInt(
                                    sData[iAtt - 1].trim());
                        }
                    }
                } while (sLine.length() <= ARFF_ITEM_LEN);

                sData = sLine.substring(1, sLine.length() - 1).split(",");

                for (String sDatum : sData) {
                    String[] sIDValue = sDatum.trim().split(" ");
                    iData[Integer.parseInt(sIDValue[0]) + 1][iDataCount]
                            = Integer.parseInt(sIDValue[1]);
                }
            }
        } catch (Exception var12) {
            System.out.println(
                    "[readArffAttributesAndData]Error reading file" + " "
                            + sArffIn);
            var12.printStackTrace();
            return sHeader.toString();
        }
    }

    /**
     * 将属性和数据写入arff文件中.
     *
     * @param sHeader         头部信息
     * @param sAttribute      属性名数组
     * @param iData           用于存储数据集的一个整数二维数组，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例用于存储数据集，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例
     * @param iAttributeCount 属性总数
     * @param iDataCount      数据总数
     * @param bUseCol         长度和属性列数相同的布尔数组。用来指示哪些列需要被写入到ARFF文件中，哪些列可以被忽略。
     * @param sArffOut        输出文件
     */
    private static void writeArffAttributesAndData(final String sHeader,
                                                   final String[] sAttribute,
                                                   final int[][] iData,
                                                   final int iAttributeCount,
                                                   final int iDataCount,
                                                   final boolean[] bUseCol,
                                                   final String sArffOut) {
        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(sArffOut)));
            wWriter.write(sHeader);

            int iCol;
            for (iCol = 0; iCol <= iAttributeCount; ++iCol) {
                if (bUseCol[iCol]) {
                    wWriter.write(sAttribute[iCol] + "\n");
                }
            }

            wWriter.write("@data\n");

            for (int iDat = 1; iDat <= iDataCount; ++iDat) {
                if (BG_SAVE_ARFF_AS_CONDENSED) {
                    wWriter.write("{");
                    int iColUsed = 0;

                    for (iCol = 1; iCol < iAttributeCount; ++iCol) {
                        if (bUseCol[iCol]) {
                            ++iColUsed;
                            if (iData[iCol][iDat] > 0) {
                                wWriter.write(
                                        iColUsed - 1 + " " + iData[iCol][iDat]
                                                + ",");
                            }
                        }
                    }

                    wWriter.write(iColUsed + " " + iData[iAttributeCount][iDat]
                            + "}\n");
                } else {
                    for (iCol = 1; iCol < iAttributeCount; ++iCol) {
                        if (bUseCol[iCol]) {
                            wWriter.write(iData[iCol][iDat] + ",");
                        }
                    }

                    wWriter.write(iData[iAttributeCount][iDat] + "\n");
                }
            }

            wWriter.close();
        } catch (Exception var11) {
            System.out.println(
                    "[writeArffAttributesAndData]Error writing " + "file "
                            + sArffOut);
            var11.printStackTrace();
        }

    }

    /**
     * 将属性和数据写入文本文件中.
     *
     * @param sAttribute      属性名数组
     * @param iData           用于存储数据集的一个整数二维数组，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例用于存储数据集，每一列代表一个属性，最后一列代表类别标签，每一行代表一个实例
     * @param iAttributeCount 属性总数
     * @param iDataCount      数据总数
     * @param sTextOut        输出的文本文件
     */
    private static void writeArffAttributesAndDataToText(
            final String[] sAttribute, final int[][] iData,
            final int iAttributeCount, final int iDataCount,
            final String sTextOut) {
        int iCol;
        for (iCol = 1; iCol <= iAttributeCount; ++iCol) {
            String[] sData = sAttribute[iCol].split(" ");
            sAttribute[iCol] = sData[1];
            int iPos = sAttribute[iCol].indexOf("_");
            if (iPos > 0) {
                sAttribute[iCol] = sAttribute[iCol].substring(iPos + 1);
            }

            iPos = sAttribute[iCol].indexOf("_pc");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("_pc", "%");
            }

            iPos = sAttribute[iCol].indexOf("%2C");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("%2C", ",");
            }

            iPos = sAttribute[iCol].indexOf("%28");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("%28", "(");
            }

            iPos = sAttribute[iCol].indexOf("%29");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("%29", ")");
            }

            iPos = sAttribute[iCol].indexOf("%3F");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("%3F", "?");
            }

            iPos = sAttribute[iCol].indexOf("%21");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("%21", "!");
            }

            iPos = sAttribute[iCol].indexOf("%25");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("%25", "%");
            }

            iPos = sAttribute[iCol].indexOf("%26");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("%26", "&");
            }

            iPos = sAttribute[iCol].indexOf("%27");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("%27", "'");
            }

            iPos = sAttribute[iCol].indexOf("%2F");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("%2F", "/");
            }

            iPos = sAttribute[iCol].indexOf("%3A");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("%3A", ":");
            }

            iPos = sAttribute[iCol].indexOf("%3B");
            if (iPos >= 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("%3B", ";");
            }

            iPos = sAttribute[iCol].indexOf("+");
            if (iPos > 0) {
                sAttribute[iCol] = sAttribute[iCol].replace("+", "_");
            }
        }

        try {
            BufferedWriter wWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(sTextOut)));
            wWriter.write(sAttribute[iAttributeCount] + "\tText\n");

            for (int iDat = 1; iDat <= iDataCount; ++iDat) {
                wWriter.write(iData[iAttributeCount][iDat] + "\t");

                for (iCol = 1; iCol < iAttributeCount; ++iCol) {
                    if (iData[iCol][iDat] > 1) {
                        wWriter.write(sAttribute[iCol] + "[" + iData[iCol][iDat]
                                + "] ");
                    } else if (iData[iCol][iDat] == 1) {
                        wWriter.write(sAttribute[iCol] + " ");
                    }
                }

                wWriter.write("\r\n");
            }

            wWriter.close();
        } catch (Exception var10) {
            System.out.println(
                    "[writeArffAttributesAndDataToText]Error writing file "
                            + sTextOut);
            var10.printStackTrace();
        }

    }
}
