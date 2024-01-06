// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   WekaCrossValidateNoSelection.java
package rainbowsix.wka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Date;
import java.util.Random;

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

/**
 * 使用无选择交叉验证方式，进行机器学习.
 *
 * @author 注释编写 朱甲豪 詹美瑛
 */
// Referenced classes of package r6.wka:
//            Utilities

public class WekaCrossValidateNoSelection {
    /**
     * jar包路径.
     */
    private static final String SG_JAR_FOLDER = "C:/jars/";
    /**
     * iteration初始值.
     */
    private static final int ITERATION_INITIAL = 30;
    /**
     * 折数,默认为10.
     */
    private static final int FOLDS_NUMBER = 10;

    /**
     * 构造函数.
     */
    protected WekaCrossValidateNoSelection() {
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
        String classifierName = ",,all,";
        String addToClasspath = "";
        String classifierExclude = "";
        String inputArffFilename = "-";
        String resultsFileName = "-";
        String summaryResultsFileName = "-";
        String instructionFilename = "-";
        Random random = new Random();
        int iterations = ITERATION_INITIAL;
        for (int i = 0; i < args.length; i++) {
            bArgumentRecognised[i] = false;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("noselection")) {
                bArgumentRecognised[i] = true;
            }
            if (i < args.length - 1) {
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

        reportParameters(iterations, classifierName, classifierExclude,
                addToClasspath, instructionFilename, inputArffFilename,
                resultsFileName, summaryResultsFileName);
        File f;
        if (instructionFilename.equals("-")) {
            if (inputArffFilename.equals("-") || resultsFileName.equals("-")
                    || summaryResultsFileName.equals("-")) {
                System.out.println(
                        "Must specify instructions file or input, results and"
                                + " summary file. Giving up.");
                return;
            }
        } else {
            f = new File(instructionFilename);
            if (f.exists()) {
                BufferedReader reader = new BufferedReader(
                        new FileReader(instructionFilename));
                inputArffFilename = reader.readLine();
                resultsFileName = reader.readLine();
                summaryResultsFileName = reader.readLine();
                reader.close();
            } else {
                System.out.println("Instructions file " + instructionFilename
                        + " not found - giving up.");
                return;
            }
        }
        if (inputArffFilename.equals("-")) {
            System.out.println(
                    "No input ARFF file specified - giving up without running"
                            + " analysis.");
            return;
        }
        f = new File(inputArffFilename);
        if (!f.exists()) {
            System.out.println("Input ARFF file " + inputArffFilename
                    + " not found - giving up without running analysis.");
            return;
        }
        System.out.println("Started processing " + inputArffFilename);
        for (int i = 1; i <= iterations; i++) {
            int randomSeed = random.nextInt();
            classifyAllArff(inputArffFilename, classifierName,
                    classifierExclude, randomSeed, resultsFileName,
                    summaryResultsFileName);
            System.out.println(i + "/" + iterations + " iterations done");
        }

        System.out.println(
                "For correlations, use Windows SentiStrength, Data "
                        + "Mining|Summarise Average... menu item");
    }

    /**
     * 使用无选择交叉验证的方法进行分类.
     * <br>
     * 直接使用交叉验证的方式，对指定分类器进行数据集上的交叉验证测试，并输出分类结果。
     *
     * @param arffFileName           数据集
     * @param classifierName         分类器名
     * @param classifierExclude      排除的分类器
     * @param randomSeed             随机种子
     * @param allResultsFilename     详细结果输出文件
     * @param summaryResultsFilename 概要结果输出文件
     * @throws Exception 如果文件路径无效或方法调用失败，则抛出异常
     */
    public static void classifyAllArff(final String arffFileName,
                                       final String classifierName,
                                       final String classifierExclude,
                                       final int randomSeed,
                                       final String allResultsFilename,
                                       final String summaryResultsFilename)
            throws Exception {
        Date start = Utilities.getNow();
        System.out.print("Loading data ... ");
        BufferedReader reader = new BufferedReader(
                new FileReader(arffFileName));
        Instances data = new Instances(reader);
        reader.close();
        data.setClassIndex(data.numAttributes() - 1);
        String options = null;
        Evaluation eval = new Evaluation(data);
        System.out.println(
                Utilities.timeGap(start, Utilities.getNow()) + " taken");
        if (classifierName.indexOf(",liblin,") > 0) {
            try {
                Utilities.addToClassPath(SG_JAR_FOLDER + "liblinear-1.51.jar");
                start = Utilities.getNow();
                Utilities.printNameAndWarning("LibLINEAR");
                start = Utilities.getNow();
                LibLINEAR schemeLibLINEAR = new LibLINEAR();
                options = " -i -o \u2013t";
                schemeLibLINEAR.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeLibLINEAR, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResultsAllData(eval, arffFileName, "LibLin",
                        randomSeed, options, allResultsFilename,
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
                Utilities.addToClassPath(SG_JAR_FOLDER + "libsvm.jar");
                start = Utilities.getNow();
                Utilities.printNameAndWarning("LibSVM");
                eval = new Evaluation(data);
                LibSVM schemeLibSVM = new LibSVM();
                options = "-s 0";
                schemeLibSVM.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeLibSVM, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResultsAllData(eval, arffFileName, "LibSVM",
                        randomSeed, options, allResultsFilename,
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
                SMO schemeSMO = new SMO();
                options
                        = "-C 1.0 -L 0.0010 -P 1.0E-12 -N 0 -V -1 -W 1 -K "
                        + "\"weka.classifiers.functions.supportVector"
                        + ".PolyKernel -C 250007 -E 1.0\"";
                schemeSMO.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeSMO, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResultsAllData(eval, arffFileName, "SMO",
                        randomSeed, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with SMO on " + arffFileName + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",slog,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("SLOG")) {
            try {
                Utilities.printNameAndWarning("SLOG  ");
                start = Utilities.getNow();
                eval = new Evaluation(data);
                SimpleLogistic schemeSLOG = new SimpleLogistic();
                options = "-I 0 -M 500 -H 50 -W 0.0";
                schemeSLOG.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeSLOG, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResultsAllData(eval, arffFileName, "SLOG",
                        randomSeed, options, allResultsFilename,
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
                eval = new Evaluation(data);
                NaiveBayes schemeBayes = new NaiveBayes();
                eval.crossValidateModel(schemeBayes, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResultsAllData(eval, arffFileName, "BAYES",
                        randomSeed, options, allResultsFilename,
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
                eval = new Evaluation(data);
                AdaBoostM1 schemeAda = new AdaBoostM1();
                options
                        = "-P 100 -S 1 -I 10 -W weka.classifiers.trees"
                        + ".DecisionStump";
                schemeAda.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeAda, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResultsAllData(eval, arffFileName, "ADA",
                        randomSeed, options, allResultsFilename,
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
                eval = new Evaluation(data);
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
                printClassificationResultsAllData(eval, arffFileName, "SMOreg",
                        randomSeed, options, allResultsFilename,
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
                eval = new Evaluation(data);
                JRip schemeJrip = new JRip();
                options = "-F 3 -N 2.0 -O 2 -S 1";
                schemeJrip.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeJrip, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResultsAllData(eval, arffFileName, "JRIP",
                        randomSeed, options, allResultsFilename,
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
                eval = new Evaluation(data);
                DecisionTable schemeDec = new DecisionTable();
                options
                        = "-X 1 -S \"weka.attributeSelection.BestFirst -D 1 "
                        + "-N 5\"";
                schemeDec.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeDec, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResultsAllData(eval, arffFileName, "DEC",
                        randomSeed, options, allResultsFilename,
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
                eval = new Evaluation(data);
                J48 schemeJ48 = new J48();
                options = "-C 0.25 -M 2";
                schemeJ48.setOptions(Utils.splitOptions(options));
                eval.crossValidateModel(schemeJ48, data, FOLDS_NUMBER,
                        new Random(randomSeed));
                printClassificationResultsAllData(eval, arffFileName, "J48",
                        randomSeed, options, allResultsFilename,
                        summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with J48 on " + arffFileName + " "
                        + e.getMessage());
            }
        }
    }

    /**
     * 输出参数信息.
     * <br>
     *
     * @param iterations             迭代次数
     * @param classifierName         分类器
     * @param classifierExclude      排除的分类器
     * @param addToClasspath         文件添加到的路径
     * @param instructionFilename    指令文件
     * @param inputFilename          输入文件名
     * @param resultsFileName        详细结果输出文件
     * @param summaryResultsFileName 概要结果输出文件
     */
    public static void reportParameters(final int iterations,
                                        final String classifierName,
                                        final String classifierExclude,
                                        final String addToClasspath,
                                        final String instructionFilename,
                                        final String inputFilename,
                                        final String resultsFileName,
                                        final String summaryResultsFileName) {
        System.out.println(
                "No feature selection method: defaults or set by command "
                        + "line:");
        System.out.println(" " + iterations + " [iterations]");
        System.out.println(" " + classifierName
                + " [classifier] LibSVM/LibLin/ALL=SMO/SLOG/BAYES/ADA/SMOreg/JRIP/DEC/J48");
        System.out.println(" " + classifierExclude
                + " [classifierExclude] SMO/SLOG/BAYES/ADA/SMOreg/JRIP/DEC/J48");
        System.out.println(" " + instructionFilename
                + " [instructions] file (train., eval., results file triples list)");
        System.out.println(" " + inputFilename + " [input] ARFF file");
        System.out.println(" " + resultsFileName + " [results] file");
        System.out.println(" " + summaryResultsFileName
                + " [summary] results file (just accuracy)");
        System.out.println(" " + addToClasspath
                + " [addToClasspath] file to add to classpath");
        System.out.println();
    }

    /**
     * 输出分类的结果.
     *
     * @param eval                   测试结果
     * @param arffFilename           数据集文件名
     * @param classifierName         分类器
     * @param randomSeed             随机种子
     * @param options                可选参数
     * @param allResultsFilename     详细结果输出文件
     * @param summaryResultsFilename 概要结果输出文件
     * @throws Exception 如果文件路径无效或方法调用失败，则抛出异常
     */
    public static void printClassificationResultsAllData(final Evaluation eval,
                                                         final String arffFilename,
                                                         final String classifierName,
                                                         final int randomSeed,
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
        allResultsPrintStream.println("Relation: .Randomize-S" + randomSeed);
        allResultsPrintStream.println(eval.toSummaryString());
        allResultsPrintStream.println(eval.toClassDetailsString());
        allResultsPrintStream.println(eval.toMatrixString());
        fout.close();
        fout = new FileOutputStream(summaryResultsFilename, true);
        PrintStream summaryResultsPrintStream = new PrintStream(fout);
        summaryResultsPrintStream.println(
                classifierName + "\t" + randomSeed + "\t" + eval.pctCorrect()
                        + "%\t" + options + "\t" + arffFilename);
        fout.close();
    }

}
