package rainbowsix.wka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * 机器学习类.
 * 使用weka对模型进行训练。<br>
 *
 * @author 注释编写 朱甲豪 詹美瑛
 */

public final class WekaMachineLearning {
    /**
     * 结果文件的最大行数.
     */
    private static final int SUMMARY_RESULT_FILE_MAX_LINE_NUMBER = 4;

    /**
     * 构造函数.
     */
    private WekaMachineLearning() {
    }

    /**
     * main函数，对在命令行中输入的相关命令进行相应操作.
     * <br>
     * 具体命令功能详见overallHelp()
     *
     * @param args 输入的命令行参数
     * @throws Exception 如果文件路径无效或方法调用失败，则抛出异常
     */
    public static void main(final String[] args) throws Exception {
        boolean[] bArgumentRecognised = new boolean[args.length];
        String arffTrainFile = "-";
        String arffEvalFile = "-";
        String classifierName = ",,all,";
        String classifierExclude = "none";
        String resultsFileName = "-";
        String addToClasspath = "";
        String summaryResultsFileName = "-";
        String instructionFilename = "-";
        overallHelp();
        for (int i = 0; i < args.length; i++) {
            bArgumentRecognised[i] = false;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("arff")) {
                Arff arff = new Arff();
                arff.main(args);
                return;
            }
            if (args[i].equalsIgnoreCase("predict")) {
                PredictClass.main(args);
                return;
            }
            if (args[i].equalsIgnoreCase("noselection")) {
                WekaCrossValidateNoSelection wekaCrossValidateNoSelection
                        = new WekaCrossValidateNoSelection();
                WekaCrossValidateNoSelection.main(args);
                return;
            }
            if (args[i].equalsIgnoreCase("infogain")) {
                WekaCrossValidateInfoGain wekaCrossValidateInfoGain
                        = new WekaCrossValidateInfoGain();
                WekaCrossValidateInfoGain.main(args);
                return;
            }
            if (args[i].equalsIgnoreCase("pre")) {
                bArgumentRecognised[i] = true;
            }
            if (i < args.length - 1) {
                if (args[i].equalsIgnoreCase("classifier")) {
                    classifierName = ",," + args[i + 1].toLowerCase() + ",";
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equalsIgnoreCase("exclude")) {
                    classifierExclude = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equalsIgnoreCase("instructions")) {
                    instructionFilename = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equalsIgnoreCase("arffTrain")) {
                    arffTrainFile = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equalsIgnoreCase("arffEval")) {
                    arffEvalFile = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equalsIgnoreCase("results")) {
                    resultsFileName = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equalsIgnoreCase("summary")) {
                    summaryResultsFileName = args[i + 1];
                    bArgumentRecognised[i] = true;
                    bArgumentRecognised[i + 1] = true;
                }
                if (args[i].equalsIgnoreCase("addToClasspath")) {
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
                return;
            }
        }

        reportParameters(classifierName, classifierExclude, addToClasspath,
                instructionFilename, arffTrainFile, arffEvalFile,
                resultsFileName, summaryResultsFileName);
        if (instructionFilename.equals("-")) {
            if (arffTrainFile.equals("-") || arffEvalFile.equals("-")
                    || resultsFileName.equals("-")
                    || summaryResultsFileName.equals("-")) {
                System.out.println(
                        "Must specify instructions or arffTrain, arffEval, "
                                + "results, and summary. Giving up.");
                return;
            }
            System.out.println("started training with " + arffTrainFile);
            WekaDirectTrainClassifyEvaluate.directClassifyAllArff(arffTrainFile,
                    arffEvalFile, classifierName, classifierExclude,
                    resultsFileName, summaryResultsFileName);
        } else {
            File f = new File(instructionFilename);
            if (f.exists()) {
                BufferedReader reader;
                for (reader = new BufferedReader(
                        new FileReader(instructionFilename));
                     reader.ready();) {
                    arffTrainFile = reader.readLine();
                    if (reader.ready()) {
                        arffEvalFile = reader.readLine();
                    }
                    if (reader.ready()) {
                        resultsFileName = reader.readLine();
                    }
                    if (reader.ready()) {
                        summaryResultsFileName = reader.readLine();
                        if (summaryResultsFileName.length()
                                > SUMMARY_RESULT_FILE_MAX_LINE_NUMBER) {
                            System.out.println(
                                    "started training with " + arffTrainFile);
                            WekaDirectTrainClassifyEvaluate.directClassifyAllArff(
                                    arffTrainFile, arffEvalFile, classifierName,
                                    classifierExclude, resultsFileName,
                                    summaryResultsFileName);
                        }
                    }
                }

                reader.close();
            } else {
                System.out.println(instructionFilename
                        + " not found. Must contain train file/eval "
                        + "file/results file/summary file instead");
            }
        }
        System.out.println("WekaAutoMachineLearning Done");
    }

    /**
     * 本方法用于在标准输出流中打印机器学习相关的参数.
     * <br>
     * 主要有以下几个参数
     *
     * @param classifierName         要使用的分类器的名称。它可以是以下值之一：ALL、SMO、SLOG、BAYES
     *                               、ADA、SMOreg、JRIP、DEC、J48、MLP、LibSVM或LibLin。最后三个不包括在ALL中。
     * @param classifierExclude      要从运行中排除的分类器的名称。它可以是以下值之一：SMO、SLOG、BAYES
     *                               、ADA、SMOreg、JRIP、DEC或J48。此参数仅在classifierName为ALL时有效。
     * @param addToClasspath         运行分类器之前要添加到类路径的文件的名称。
     * @param instructionFilename    包含分类器每次运行的三元组列表（训练文件名、评估文件名和结果文件名）的文件名。
     * @param arffTrainFile          包含分类器训练数据的ARFF文件的名称。
     * @param arffEvalFile           包含分类器评估数据的ARFF文件的名称。
     * @param resultsFileName        将存储在每对train和eval文件上运行分类器的结果的文件的名称。
     * @param summaryResultsFileName 将存储分类器每次运行的结果(大概是精度值)的文件的名称。
     */
    public static void reportParameters(final String classifierName,
                                        final String classifierExclude,
                                        final String addToClasspath,
                                        final String instructionFilename,
                                        final String arffTrainFile,
                                        final String arffEvalFile,
                                        final String resultsFileName,
                                        final String summaryResultsFileName) {
        System.out.println("Pre method defaults or set by command line:");
        System.out.println(" " + classifierName
                + " [classifier] ALL/SMO/SLOG/BAYES/ADA/SMOreg/JRIP/DEC/J48 "
                + "/MLP/LibSVM/LibLin -last 3 not in ALL");
        System.out.println(" " + classifierExclude
                + " [exclude] SMO/SLOG/BAYES/ADA/SMOreg/JRIP/DEC/J48 "
                + "classifier to exclude");
        System.out.println(" " + instructionFilename
                + " [instructions] file name (train., eval., results file "
                + "triples list)");
        System.out.println(" " + arffTrainFile + " [arffTrain] file");
        System.out.println(" " + arffEvalFile + " [arffEval] file");
        System.out.println(" " + resultsFileName + " [results] file");
        System.out.println(" " + summaryResultsFileName
                + " [summary] results file (just accuracy)");
        System.out.println(" " + addToClasspath
                + " [addToClasspath] file to add to classpath");
    }

    /**
     * 在标准输出流打印全部使用方法.
     */
    public static void overallHelp() {
        System.out.println("Approaches available (via command name)");
        System.out.println(
                "  [pre] (default) - input pre-calcuated folds and pre-selected features");
        System.out.println(
                "                  e.g., 10-fold evaluation from SentiStrength");
        System.out.println(
                "  [infogain] process raw single ARFF with feature selection, auto folds");
        System.out.println(
                "  [noselection] process raw single ARFF without feature selection, auto folds");
        System.out.println(
                "  [arff] convert plain text to ARFF, with/wo ML prediction");
        System.out.println();
    }
}
