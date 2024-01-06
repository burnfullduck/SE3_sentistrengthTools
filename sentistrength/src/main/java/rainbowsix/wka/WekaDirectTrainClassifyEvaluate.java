package rainbowsix.wka;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Date;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibLINEAR;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
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
 * weka分类模型的直接训练和评估，评估结果的输出.
 *
 * @author 注释编写 朱甲豪 詹美瑛
 */
public final class WekaDirectTrainClassifyEvaluate {
    /**
     * jar包路径.
     */
    private static final String SG_JAR_FOLDER = "C:/jars/";

    /**
     * 构造函数.
     */
    private WekaDirectTrainClassifyEvaluate() {
    }

    /**
     * 训练分类器并进行评估.
     * <br>
     * 根据训练数据和指定的分类器训练分类模型,然后用评估数据对模型进行评估，并将详细和概要结果分别写入不同的文件。<br>
     *
     * @param arffTrainFile          训练数据文件
     * @param arffEvalFile           评估数据文件
     * @param classifierName         分类器的名字
     * @param classifierExclude      排除的分类器
     * @param allResultsFilename     写入详细评估结果的文件
     * @param summaryResultsFilename 写入概要结果的文件
     * @throws Exception 如果文件路径无效或方法调用失败，则抛出异常
     */
    public static void directClassifyAllArff(final String arffTrainFile,
                                             final String arffEvalFile,
                                             final String classifierName,
                                             final String classifierExclude,
                                             final String allResultsFilename,
                                             final String summaryResultsFilename)
            throws Exception {
        Date start = Utilities.getNow();
        System.out.print("Loading data ... ");
        BufferedReader reader = new BufferedReader(
                new FileReader(arffTrainFile));
        Instances trainData = new Instances(reader);
        reader.close();
        trainData.setClassIndex(trainData.numAttributes() - 1);
        reader = new BufferedReader(new FileReader(arffEvalFile));
        Instances evalData = new Instances(reader);
        reader.close();
        evalData.setClassIndex(evalData.numAttributes() - 1);
        String options = null;
        Evaluation eval = new Evaluation(evalData);
        System.out.println(
                Utilities.timeGap(start, Utilities.getNow()) + " taken");
        if (classifierName.indexOf(",liblin,") > 0) {
            try {
                Utilities.printNameAndWarning("LibLINEAR");
                start = Utilities.getNow();
                Utilities.addToClassPath(SG_JAR_FOLDER + "liblinear-1.51.jar");
                LibLINEAR schemeLibLINEAR = new LibLINEAR();
                options = " -i -o \u2013t";
                schemeLibLINEAR.setOptions(Utils.splitOptions(options));
                schemeLibLINEAR.buildClassifier(trainData);
                eval.evaluateModel(schemeLibLINEAR, evalData);
                printClassificationResultsAllData(eval, arffTrainFile, "LibLin",
                        options, allResultsFilename, summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println(
                        "Error with LibLINEAR on " + arffTrainFile + " "
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
                LibSVM schemeLibSVM = new LibSVM();
                options = "-s 0";
                schemeLibSVM.setOptions(Utils.splitOptions(options));
                schemeLibSVM.buildClassifier(trainData);
                eval.evaluateModel(schemeLibSVM, evalData);
                printClassificationResultsAllData(eval, arffTrainFile, "LibSVM",
                        options, allResultsFilename, summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with LibSVM on " + arffTrainFile + " "
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
                schemeSMO.buildClassifier(trainData);
                eval.evaluateModel(schemeSMO, evalData);
                printClassificationResultsAllData(eval, arffTrainFile, "SMO",
                        options, allResultsFilename, summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with SMO on " + arffTrainFile + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",slog,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("SMO")) {
            try {
                Utilities.printNameAndWarning("SLOG  ");
                start = Utilities.getNow();
                eval = new Evaluation(evalData);
                SimpleLogistic schemeSLOG = new SimpleLogistic();
                options = "-I 0 -M 500 -H 50 -W 0.0";
                schemeSLOG.setOptions(Utils.splitOptions(options));
                schemeSLOG.buildClassifier(trainData);
                eval.evaluateModel(schemeSLOG, evalData);
                printClassificationResultsAllData(eval, arffTrainFile, "SLOG",
                        options, allResultsFilename, summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with SLOG on " + arffTrainFile + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",bayes,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("BAYES")) {
            try {
                Utilities.printNameAndWarning("BAYES ");
                start = Utilities.getNow();
                eval = new Evaluation(evalData);
                NaiveBayes schemeBayes = new NaiveBayes();
                schemeBayes.buildClassifier(trainData);
                eval.evaluateModel(schemeBayes, evalData);
                printClassificationResultsAllData(eval, arffTrainFile, "BAYES",
                        options, allResultsFilename, summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with BAYES on " + arffTrainFile + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",ada,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("ADA")) {
            try {
                Utilities.printNameAndWarning("ADA   ");
                start = Utilities.getNow();
                eval = new Evaluation(evalData);
                AdaBoostM1 schemeAda = new AdaBoostM1();
                options
                        = "-P 100 -S 1 -I 10 -W weka.classifiers.trees"
                        + ".DecisionStump";
                schemeAda.setOptions(Utils.splitOptions(options));
                schemeAda.buildClassifier(trainData);
                eval.evaluateModel(schemeAda, evalData);
                printClassificationResultsAllData(eval, arffTrainFile, "ADA",
                        options, allResultsFilename, summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with ADA on " + arffTrainFile + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",smoreg,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("SMOreg")) {
            try {
                Utilities.printNameAndWarning("SMOreg");
                start = Utilities.getNow();
                eval = new Evaluation(evalData);
                ClassificationViaRegression schemeSMOreg
                        = new ClassificationViaRegression();
                options
                        = "-W weka.classifiers.functions.SMOreg -- -C 1.0 -N "
                        + "0 -I \"weka.classifiers.functions.supportVector"
                        + ".RegSMOImproved -L 0.0010 -W 1 -P 1.0E-12 -T 0"
                        + ".0010 -V\" -K \"weka.classifiers.functions"
                        + ".supportVector.PolyKernel -C 250007 -E 1.0\"";
                schemeSMOreg.setOptions(Utils.splitOptions(options));
                schemeSMOreg.buildClassifier(trainData);
                eval.evaluateModel(schemeSMOreg, evalData);
                printClassificationResultsAllData(eval, arffTrainFile, "SMOreg",
                        options, allResultsFilename, summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with SMOreg on " + arffTrainFile + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",jrip,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("JRIP")) {
            try {
                Utilities.printNameAndWarning("JRIP  ");
                start = Utilities.getNow();
                JRip schemeJrip = new JRip();
                options = "-F 3 -N 2.0 -O 2 -S 1";
                schemeJrip.setOptions(Utils.splitOptions(options));
                schemeJrip.buildClassifier(trainData);
                eval.evaluateModel(schemeJrip, evalData);
                printClassificationResultsAllData(eval, arffTrainFile, "JRIP",
                        options, allResultsFilename, summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with JRIP on " + arffTrainFile + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",dec,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("DEC")) {
            try {
                Utilities.printlnNameAndWarning("DEC   ");
                start = Utilities.getNow();
                eval = new Evaluation(evalData);
                DecisionTable schemeDec = new DecisionTable();
                options
                        = "-X 1 -S \"weka.attributeSelection.BestFirst -D 1 "
                        + "-N 5\"";
                schemeDec.setOptions(Utils.splitOptions(options));
                schemeDec.buildClassifier(trainData);
                eval.evaluateModel(schemeDec, evalData);
                printClassificationResultsAllData(eval, arffTrainFile, "DEC",
                        options, allResultsFilename, summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with DEC on " + arffTrainFile + " "
                        + e.getMessage());
            }
        }
        if ((classifierName.indexOf(",j48,") > 0
                || classifierName.indexOf(",all,") > 0)
                && !classifierExclude.equals("J48")) {
            try {
                Utilities.printNameAndWarning("J48   ");
                start = Utilities.getNow();
                eval = new Evaluation(evalData);
                J48 schemeJ48 = new J48();
                options = "-C 0.25 -M 2";
                schemeJ48.setOptions(Utils.splitOptions(options));
                schemeJ48.buildClassifier(trainData);
                eval.evaluateModel(schemeJ48, evalData);
                printClassificationResultsAllData(eval, arffTrainFile, "J48",
                        options, allResultsFilename, summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with J48 on " + arffTrainFile + " "
                        + e.getMessage());
            }
        }
        if (classifierName.indexOf(",mlp,") > 0) {
            try {
                Utilities.printlnNameAndWarning("MLP   ");
                start = Utilities.getNow();
                eval = new Evaluation(evalData);
                MultilayerPerceptron schemeMLP = new MultilayerPerceptron();
                options = "-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H a";
                schemeMLP.setOptions(Utils.splitOptions(options));
                schemeMLP.buildClassifier(trainData);
                eval.evaluateModel(schemeMLP, evalData);
                printClassificationResultsAllData(eval, arffTrainFile, "MLP",
                        options, allResultsFilename, summaryResultsFilename);
                System.out.println(Utilities.timeGap(start, Utilities.getNow())
                        + " taken");
            } catch (Exception e) {
                System.out.println("Error with MLP on " + arffTrainFile + " "
                        + e.getMessage());
            }
        }
    }

    /**
     * 将评估结果输出。<br>
     * 输出详细评估结果和概要的评估结果.
     * <br>
     *
     * @param eval                   评估结果
     * @param arffFilename           训练数据文件名
     * @param classifierName         分类器名
     * @param options                可选参数
     * @param allResultsFilename     详细结果输出到的文件
     * @param summaryResultsFilename 概要结果输出的文件
     * @throws Exception 如果文件路径无效或方法调用失败，则抛出异常
     */
    public static void printClassificationResultsAllData(final Evaluation eval,
                                                         final String arffFilename,
                                                         final String classifierName,
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
        allResultsPrintStream.println(eval.toSummaryString());
        allResultsPrintStream.println(eval.toClassDetailsString());
        allResultsPrintStream.println(eval.toMatrixString());
        fout.close();
        fout = new FileOutputStream(summaryResultsFilename, true);
        PrintStream summaryResultsPrintStream = new PrintStream(fout);
        summaryResultsPrintStream.println(
                classifierName + "\t" + eval.pctCorrect() + "%\t" + options
                        + "\t" + arffFilename);
        fout.close();
    }

}
