// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   WekaCrossValidateInfoGain.java
package rainbowsix.wka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Date;
import java.util.Random;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibLINEAR;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.ClassificationViaRegression;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.instance.Randomize;

/**
 * weka交叉验证信息增益类.
 * <br>
 * 使用Weka,利用信息增益对数据集进行交叉验证。<br>
 * 信息增益是一种衡量属性对分类结果影响程度的指标，值越大表示属性越重要。<br>
 * 交叉验证是一种评估模型泛化能力的方法，它将数据集分成若干份，每次用一份作为测试集，其余作为训练集，然后计算模型在测试集上的性能，并取平均值。
 *
 * @author 注释编写 朱甲豪 詹美瑛
 */
public class WekaCrossValidateInfoGain {
    /**
     * JAR包路径.
     */
    private static final String SG_JAR_FOLDER = "C:/jars/";
    /**
     * 最小特征数.
     */
    private static final int MIN_FEATURES = 100;
    /**
     * 最大特征数.
     */
    private static final int MAX_FEATURES = 1000;
    /**
     * 每一步的特征数量.
     */
    private static final int STEP_FEATURES = 100;
    /**
     * 折数,默认为10.
     */
    private static final double THRESHOLD = 1.7976931348623157E+308D;
    /**
     * rank的界限.
     */
    private static final int FOLDS_NUMBER = 10;
    /**
     * iteration初始值.
     */
    private static final int ITERATION_INITIAL = 30;

    /**
     * 构造方法.
     */
    protected WekaCrossValidateInfoGain() {
    }

    /**
     * main函数，对在命令行中输入的相关命令进行相应操作,并打印命令执行情况.
     * <br>
     *
     * @param args 输入的命令行参数
     * @throws Exception 如果文件路径无效或方法调用失败，则抛出异常
     */
    public static void main(final String[] args) throws Exception {
        boolean[] bArgumentRecognised = new boolean[args.length];
        boolean lessFeaturesRequestedThanData = true;
        String classifierName = ",,all,";
        String classifierExclude = "";
        String addToClasspath = "";
        String instructionFilename = "-";
        String inputArffFilename = "-";
        String resultsFileName = "-";
        String summaryResultsFileName = "-";
        int minFeatures = MIN_FEATURES;
        int maxFeatures = MAX_FEATURES;
        int stepFeatures = STEP_FEATURES;
        int iterations = ITERATION_INITIAL;
        Random random = new Random();
        for (int i = 0; i < args.length; i++) {
            bArgumentRecognised[i] = false;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("infogain")) {
                bArgumentRecognised[i] = true;
            }
            if (i < args.length - 1) {
                if (args[i].equals("min")) {
                    minFeatures = Integer.parseInt(args[i + 1].trim());
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equals("max")) {
                    maxFeatures = Integer.parseInt(args[i + 1].trim());
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equals("step")) {
                    stepFeatures = Integer.parseInt(args[i + 1].trim());
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equals("iterations")) {
                    iterations = Integer.parseInt(args[i + 1].trim());
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equals("classifier")) {
                    classifierName = ",," + args[i + 1].toLowerCase() + ",";
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equals("exclude")) {
                    classifierExclude = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equals("instructions")) {
                    instructionFilename = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equals("input")) {
                    inputArffFilename = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equals("results")) {
                    resultsFileName = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equals("summary")) {
                    summaryResultsFileName = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equals("addToClasspath")) {
                    addToClasspath = args[i + 1];
                    Utilities.addToClassPath(addToClasspath);
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
            }
        }

        for (int i = 0; i < args.length; i++) {
            if (!bArgumentRecognised[i]) {
                System.out.println(
                        "Unrecognised command - wrong spelling or case?: "
                                + args[i]);
            }
        }

        reportParameters(minFeatures, maxFeatures, stepFeatures, iterations,
                classifierName, classifierExclude, addToClasspath,
                instructionFilename, inputArffFilename, resultsFileName,
                summaryResultsFileName);
        if (instructionFilename.equals("-")) {
            if (inputArffFilename.equals("-") || resultsFileName.equals("-")
                    || summaryResultsFileName.equals("-")) {
                System.out.println(
                        "Must specify instructions file or input, results and"
                                + " summary files. Giving up.");
                return;
            }
            System.out.println("started processing " + inputArffFilename);
            for (int features = minFeatures; features <= maxFeatures;
                 features += stepFeatures) {
                System.out.println("Starting " + features + " features for "
                        + inputArffFilename);
                for (int i = 1; i <= iterations; i++) {
                    int randomSeed = random.nextInt();
                    lessFeaturesRequestedThanData = classifyArff(
                            inputArffFilename, classifierName,
                            classifierExclude, randomSeed, features,
                            resultsFileName, summaryResultsFileName);
                    System.out.println(
                            i + "/" + iterations + " iterations done");
                }

                if (!lessFeaturesRequestedThanData) {
                    System.out.println(
                            "More features requested than in data last time!");
                    return;
                }
            }

            System.out.println(
                    "For correlations, use Windows SentiStrength, Data "
                            + "Mining|Summarise Average... menu item");
        } else {
            File f = new File(instructionFilename);
            if (f.exists()) {
                BufferedReader reader;
                for (reader = new BufferedReader(
                        new FileReader(instructionFilename));
                     reader.ready();) {
                    inputArffFilename = reader.readLine();
                    resultsFileName = reader.readLine();
                    summaryResultsFileName = reader.readLine();
                    System.out.println(
                            "started processing " + inputArffFilename);
                    for (int features = minFeatures; features <= maxFeatures;
                         features += stepFeatures) {
                        System.out.println(
                                "Starting " + features + " features for "
                                        + inputArffFilename);
                        for (int i = 1; i <= iterations + 1; i++) {
                            int randomSeed = random.nextInt();
                            lessFeaturesRequestedThanData = classifyArff(
                                    inputArffFilename, classifierName,
                                    classifierExclude, randomSeed, features,
                                    resultsFileName, summaryResultsFileName);
                            System.out.println(i + "/" + iterations
                                    + " 10-fold iterations with " + features
                                    + " features done");
                        }

                        if (!lessFeaturesRequestedThanData) {
                            System.out.println(
                                    "More features requested than in data "
                                            + "last time!");
                            reader.close();
                            return;
                        }
                    }

                }

                reader.close();
            } else {
                System.out.println(instructionFilename
                        + " not found - should contain data file, results "
                        + "file, summary file.");
                System.out.println(
                        "Program terminating without running analysis.");
            }
        }
    }

    /**
     * 输出参数信息.
     * <br>
     *
     * @param minFeatures            最小特征值
     * @param maxFeatures            最大特征值
     * @param stepFeatures           特征值递增值
     * @param iterations             迭代次数
     * @param classifierName         分类器
     * @param classifierExclude      排除的分类器
     * @param addToClasspath         文件添加到的路径
     * @param instructionFilename    指令文件
     * @param inputFilename          输入文件
     * @param resultsFileName        输出详细结果的文件
     * @param summaryResultsFileName 输出概要结果的文件
     */
    public static void reportParameters(final int minFeatures,
                                        final int maxFeatures,
                                        final int stepFeatures,
                                        final int iterations,
                                        final String classifierName,
                                        final String classifierExclude,
                                        final String addToClasspath,
                                        final String instructionFilename,
                                        final String inputFilename,
                                        final String resultsFileName,
                                        final String summaryResultsFileName) {
        System.out.println(
                "InfoGain method: default options or values set by command "
                        + "line:");
        System.out.print(" " + minFeatures + " [min] features, ");
        System.out.print(maxFeatures + " [max] features, ");
        System.out.println(stepFeatures + " [step] features");
        System.out.println(" " + iterations + " [iterations]");
        System.out.println(" " + classifierName
                + " [classifier] LibSVM/LibLin/ALL = "
                + "SMO/SLOG/BAYES/ADA/SMOreg/JRIP/DEC/J48");
        System.out.println(" " + classifierExclude
                + " [classifierExclude] SMO/SLOG/BAYES/ADA/SMOreg/JRIP/DEC"
                + "/J48");
        System.out.println(" " + instructionFilename
                + " [instructions] file (train-eval-results triples)");
        System.out.println(" " + inputFilename + " [input] ARFF file");
        System.out.println(" " + resultsFileName + " [results] file");
        System.out.println(" " + summaryResultsFileName
                + " [summary] results file (just accuracy)");
        System.out.println(" " + addToClasspath
                + " [addToClasspath] file to add to classpath");
        System.out.println();
    }

    /**
     * 使用特征选择交叉验证的方式进行分类.
     * <br>
     * 读取ARFF格式的数据文件，并将其转换为Instances对象。
     * 对Instances对象进行随机化处理和特征选择(使用最大信息增益属性为特征)。
     * 根据classifierName参数，使用不同的分类器对Instances对象进行十折交叉验证，并将结果保存到Evaluation对象中。
     * 将Evaluation对象中的评估指标打印出来，并写入文件中。
     * 使用特征选择方式和指定的分类器，在数据集上进行交叉验证的分类测试，并输出分类结果。<br>
     *
     * @param arffFileName           数据集
     * @param classifierName         分类器名
     * @param classifierExclude      排除的分类器
     * @param randomSeed             随机种子
     * @param features               特征数
     * @param allResultsFilename     详细结果输出文件
     * @param summaryResultsFilename 概要结果输出文件
     * @return 要求的特征是否比数据的属性少
     * @throws Exception 如果文件路径无效或方法调用失败，则抛出异常
     */
    public static boolean classifyArff(final String arffFileName,
                                       final String classifierName,
                                       final String classifierExclude,
                                       final int randomSeed, final int features,
                                       final String allResultsFilename,
                                       final String summaryResultsFilename)
            throws Exception {
        boolean lessFeaturesRequestedThanData = true;
        Date start = Utilities.getNow();
        System.out.print("Loading data ... ");
        BufferedReader reader = new BufferedReader(
                new FileReader(arffFileName));
        Instances data = new Instances(reader);
        reader.close();
        data.setClassIndex(data.numAttributes() - 1);
        Randomize randomize = new Randomize();
        randomize.setRandomSeed(randomSeed);
        randomize.setInputFormat(data);
        data = Filter.useFilter(data, randomize);
        Ranker ranker = new Ranker();
        if (features < data.numAttributes()) {
            ranker.setNumToSelect(features);
        } else {
            ranker.setNumToSelect(data.numAttributes());
            lessFeaturesRequestedThanData = false;
        }
        ranker.setGenerateRanking(true);
        ranker.setThreshold(THRESHOLD);
        InfoGainAttributeEval infoGainAttributeEval
                = new InfoGainAttributeEval();
        AttributeSelection attributeSelection = new AttributeSelection();
        attributeSelection.setEvaluator(infoGainAttributeEval);
        attributeSelection.setSearch(ranker);
        attributeSelection.setInputFormat(data);
        data = Filter.useFilter(data, attributeSelection);
        String options = null;
        System.out.println(
                Utilities.timeGap(start, Utilities.getNow()) + " taken");
        if (classifierName.indexOf(",liblin,") > 0) {
            try {
                Utilities.printNameAndWarning("LibLINEAR");
                start = Utilities.getNow();
                Utilities.addToClassPath(SG_JAR_FOLDER + "liblinear-1.51.jar");
                Evaluation eval = new Evaluation(data);
                LibLINEAR schemeLibLINEAR = new LibLINEAR();
                options = " -i -o \u2013t";
                schemeLibLINEAR.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeLibLINEAR, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResults(eval, arffFileName, "LibLin",
                        randomSeed, features, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println(
                        "Error with LibLINEAR on " + arffFileName + " "
                                + e.getMessage());
                System.out.println(
                        "Must have jar file in Jar folder " + SG_JAR_FOLDER
                                + " or classpath. Here is the current Java "
                                + "classpath");
                Utilities.printClasspath();
            }
        }
        if (classifierName.indexOf(",libsvm,") > 0) {
            try {
                Utilities.printNameAndWarning("LibSVM");
                start = Utilities.getNow();
                Utilities.addToClassPath(SG_JAR_FOLDER + "libsvm.jar");
                Evaluation eval = new Evaluation(data);
                LibSVM schemeLibSVM = new LibSVM();
                options = "-s 0";
                schemeLibSVM.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeLibSVM, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResults(eval, arffFileName, "LibSVM",
                        randomSeed, features, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with LibSVM on " + arffFileName + " "
                        + e.getMessage());
                System.out.println(
                        "Must have jar file in Jar folder " + SG_JAR_FOLDER
                                + " or classpath. Here is the current Java "
                                + "classpath");
                Utilities.printClasspath();
            }
        }
        if ((classifierName.indexOf(",smo,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("SMO")) {
            try {
                Utilities.printNameAndWarning("SMO   ");
                start = Utilities.getNow();
                Evaluation eval = new Evaluation(data);
                SMO schemeSMO = new SMO();
                options
                        = "-C 1.0 -L 0.00FOLDS_NUMBER -P 1.0E-12 -N 0 -V -1 "
                        + "-W 1 -K \"weka.classifiers.functions.supportVector"
                        + ".PolyKernel -C 250007 -E 1.0\"";
                schemeSMO.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeSMO, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResults(eval, arffFileName, "SMO",
                        randomSeed, features, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with SMO on " + arffFileName + " "
                        + e.getMessage());
                System.out.println(
                        "Must have jar file in classpath (not dir!). Here is "
                                + "the current Java classpath");
                Utilities.printClasspath();
            }
        }
        if ((classifierName.indexOf(",slog,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("SLOG")) {
            try {
                Utilities.printNameAndWarning("SLOG  ");
                start = Utilities.getNow();
                Evaluation eval = new Evaluation(data);
                SimpleLogistic schemeSLOG = new SimpleLogistic();
                options = "-I 0 -M 500 -H 50 -W 0.0";
                schemeSLOG.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeSLOG, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResults(eval, arffFileName, "SLOG",
                        randomSeed, features, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with SLOG on " + arffFileName + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",bayes,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("BAYES")) {
            try {
                Utilities.printNameAndWarning("BAYES ");
                start = Utilities.getNow();
                Evaluation eval = new Evaluation(data);
                NaiveBayes schemeBayes = new NaiveBayes();
                eval.crossValidateModel(schemeBayes, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResults(eval, arffFileName, "BAYES",
                        randomSeed, features, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with BAYES on " + arffFileName + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",ada,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("ADA")) {
            try {
                Utilities.printNameAndWarning("ADA   ");
                start = Utilities.getNow();
                Evaluation eval = new Evaluation(data);
                AdaBoostM1 schemeAda = new AdaBoostM1();
                options
                        = "-P 100 -S 1 -I 10 -W weka.classifiers.trees"
                        + ".DecisionStump";
                schemeAda.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeAda, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResults(eval, arffFileName, "ADA",
                        randomSeed, features, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with ADA on " + arffFileName + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",smoreg,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("SMOreg")) {
            try {
                Utilities.printNameAndWarning("SMOreg");
                start = Utilities.getNow();
                Evaluation eval = new Evaluation(data);
                ClassificationViaRegression schemeSMOreg
                        = new ClassificationViaRegression();
                options
                        = "-W weka.classifiers.functions.SMOreg -- -C 1.0 -N "
                        + "0 -I \"weka.classifiers.functions.supportVector"
                        + ".RegSMOImproved -L 0.0010 -W 1 -P 1.0E-12 -T 0"
                        + ".0010 -V\" -K \"weka.classifiers.functions"
                        + ".supportVector.PolyKernel -C 250007 -E 1.0\"";
                schemeSMOreg.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeSMOreg, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResults(eval, arffFileName, "SMOreg",
                        randomSeed, features, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with SMOreg on " + arffFileName + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",jrip,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("JRIP")) {
            try {
                Utilities.printNameAndWarning("JRIP  ");
                start = Utilities.getNow();
                Evaluation eval = new Evaluation(data);
                JRip schemeJrip = new JRip();
                options = "-F 3 -N 2.0 -O 2 -S 1";
                schemeJrip.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeJrip, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResults(eval, arffFileName, "JRIP",
                        randomSeed, features, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with JRIP on " + arffFileName + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",dec,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("DEC")) {
            try {
                Utilities.printNameAndWarning("DEC   ");
                start = Utilities.getNow();
                Evaluation eval = new Evaluation(data);
                DecisionTable schemeDec = new DecisionTable();
                options
                        = "-X 1 -S \"weka.attributeSelection.BestFirst -D 1 "
                        + "-N 5\"";
                schemeDec.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeDec, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResults(eval, arffFileName, "DEC",
                        randomSeed, features, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with DEC on " + arffFileName + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",j48,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("J48")) {
            try {
                Utilities.printNameAndWarning("J48   ");
                start = Utilities.getNow();
                Evaluation eval = new Evaluation(data);
                J48 schemeJ48 = new J48();
                options = "-C 0.25 -M 2";
                schemeJ48.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeJ48, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResults(eval, arffFileName, "J48",
                        randomSeed, features, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with J48 on " + arffFileName + " "
                        + e.getMessage());
            }
        }
        return lessFeaturesRequestedThanData;
    }

    /**
     * 输出分类结果.
     * <br>
     *
     * @param eval                   评估实例
     * @param arffFilename           数据集
     * @param classifierName         分类器名称
     * @param randomSeed             随机种子
     * @param features               特征
     * @param options                可选参数
     * @param allResultsFilename     输出详细结果的文件
     * @param summaryResultsFilename 输出概要结果的文件
     * @throws Exception 如果文件路径无效或方法调用失败，则抛出异常
     */
    public static void printClassificationResults(final Evaluation eval,
                                                  final String arffFilename,
                                                  final String classifierName,
                                                  final int randomSeed,
                                                  final int features,
                                                  final String options,
                                                  final String allResultsFilename,
                                                  final String summaryResultsFilename)
            throws Exception {
        FileOutputStream fout = new FileOutputStream(allResultsFilename, true);
        PrintStream allResultsPrintStream = new PrintStream(fout);
        allResultsPrintStream.println();
        allResultsPrintStream.println(arffFilename);
        allResultsPrintStream.println("=== Evaluation result ===");
        allResultsPrintStream.println("Scheme: " + classifierName);
        allResultsPrintStream.println("Options: " + options);
        allResultsPrintStream.println(
                "Relation: .Randomize-S" + randomSeed + "Features-N"
                        + features);
        allResultsPrintStream.println(eval.toSummaryString());
        allResultsPrintStream.println(eval.toClassDetailsString());
        allResultsPrintStream.println(eval.toMatrixString());
        fout.close();
        fout = new FileOutputStream(summaryResultsFilename, true);
        PrintStream summaryResultsPrintStream = new PrintStream(fout);
        summaryResultsPrintStream.println(
                classifierName + "\t" + randomSeed + "\t" + features + "\t"
                        + eval.pctCorrect() + "%\t" + options + "\t"
                        + arffFilename);
        fout.close();
    }

}
