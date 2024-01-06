// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   EvaluativeTerms.java

package rainbowsix.ss.list;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


import rainbowsix.ss.ClassificationOptions;
import rainbowsix.utilities.FileOps;

// Referenced classes of package:
//            ClassificationOptions, IdiomList, SentimentWords

/**
 * 可评估术语类.
 * <br>用来保存可评估术语和对应的评估及强度。
 * <br>UC-28 Suggest new sentiment terms (from terms in misclassified texts)
 *
 * @author 注释编写 徐晨 胡才轩
 */
public class EvaluativeTerms {
    /**
     * 最大的评估对象值.
     */
    private int igObjectEvaluationMax;
    /**
     * 对象.
     */
    private String[] sgObject;
    /**
     * 对象的评估.
     */
    private String[] sgObjectEvaluation;
    /**
     * 对象的评估强度.
     */
    private int[] igObjectEvaluationStrength;
    /**
     * 对象评估计数.
     */
    private int igObjectEvaluationCount;

    /**
     * 构造方法.
     */
    public EvaluativeTerms() {
        igObjectEvaluationMax = 0;
        setIgObjectEvaluationCount(0);
    }

    /**
     * 初始化可评估术语类.
     * <br>从文件中读取评估条目(对象,评估,强度),并分别存在三个数组中,如果遇到了没有评估的习语或者新情绪词语,
     * 则在对应的习语字典或情绪词词典中添加新条目。
     * <br>UC-28 Suggest new sentiment terms (from terms in misclassified
     * texts)<br>
     *
     * @param sSourceFile    评估条目文件路径
     * @param options        分类器选项
     * @param idiomList      现存习语列表
     * @param sentimentWords 情感词列表
     * @return 是否初始化成功
     */
    public boolean initialise(final String sSourceFile,
                              final ClassificationOptions options,
                              final IdiomList idiomList,
                              final SentimentWords sentimentWords) {
        if (getIgObjectEvaluationCount() > 0) {
            return true;
        }
        File f = new File(sSourceFile);
        if (!f.exists()) {
            System.out.println(
                    "Could not find additional (object/evaluation) file: "
                            + sSourceFile);
            return false;
        }
        int iStrength;
        boolean bIdiomsAdded = false;
        boolean bSentimentWordsAdded = false;
        try {
            igObjectEvaluationMax = FileOps.iCountLinesInTextFile(sSourceFile)
                    + 2;
            setIgObjectEvaluationCount(0);
            setSgObject(new String[igObjectEvaluationMax]);
            setSgObjectEvaluation(new String[igObjectEvaluationMax]);
            setIgObjectEvaluationStrength(new int[igObjectEvaluationMax]);
            BufferedReader rReader;
            if (options.isBgForceUTF8()) {
                rReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(sSourceFile),
                                StandardCharsets.UTF_8));
            } else {
                rReader = new BufferedReader(new FileReader(sSourceFile));
            }
            String sLine;
            while ((sLine = rReader.readLine()) != null) {
                if (sLine.indexOf("##") != 0 && sLine.indexOf("\t") > 0) {
                    String[] sData = sLine.split("\t");
                    if (sData.length > 2 && sData[2].indexOf("##") != 0) {
                        getSgObject()[++igObjectEvaluationCount] = sData[0];
                        getSgObjectEvaluation()[getIgObjectEvaluationCount()]
                                = sData[1];
                        try {
                            getIgObjectEvaluationStrength()[getIgObjectEvaluationCount()]
                                    = Integer.parseInt(sData[2].trim());
                            if (getIgObjectEvaluationStrength()[getIgObjectEvaluationCount()]
                                    > 0) {
                                getIgObjectEvaluationStrength()[getIgObjectEvaluationCount()]--;
                            } else if (
                                    getIgObjectEvaluationStrength()[getIgObjectEvaluationCount()]
                                            < 0) {
                                getIgObjectEvaluationStrength()[getIgObjectEvaluationCount()]++;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(
                                    "Failed to identify integer weight for "
                                            + "object/evaluation! Ignoring "
                                            + "object/evaluation");
                            System.out.println("Line: " + sLine);
                            setIgObjectEvaluationCount(
                                    getIgObjectEvaluationCount() - 1);
                        }
                    } else if (sData[0].indexOf(" ") > 0) {
                        try {
                            iStrength = Integer.parseInt(sData[1].trim());
                            idiomList.addExtraIdiom(sData[0], iStrength, false);
                            bIdiomsAdded = true;
                        } catch (NumberFormatException e) {
                            System.out.println(
                                    "Failed to identify integer weight for "
                                            + "idiom in additional file! "
                                            + "Ignoring it");
                            System.out.println("Line: " + sLine);
                        }
                    } else {
                        try {

                            iStrength = Integer.parseInt(sData[1].trim());
                            sentimentWords.addOrModifySentimentTerm(sData[0],
                                    iStrength, false);
                            bSentimentWordsAdded = true;
                        } catch (NumberFormatException e) {
                            System.out.println(
                                    "Failed to identify integer weight for "
                                            + "sentiment term in additional "
                                            + "file! Ignoring it");
                            System.out.println("Line: " + sLine);
                            setIgObjectEvaluationCount(
                                    getIgObjectEvaluationCount() - 1);
                        }
                    }
                }
            }
            rReader.close();
            if (getIgObjectEvaluationCount() > 0) {
                options.setBgUseObjectEvaluationTable(true);
            }
            if (bSentimentWordsAdded) {
                sentimentWords.sortSentimentList();
            }
            if (bIdiomsAdded) {
                idiomList.convertIdiomStringsToWordLists();
            }
        } catch (FileNotFoundException e) {
            System.out.println(
                    "Could not find additional (object/evaluation) file: "
                            + sSourceFile);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(
                    "Found additional (object/evaluation) file but could not "
                            + "read from it: " + sSourceFile);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取对象.
     *
     * @return 对象.
     */
    public String[] getSgObject() {
        return sgObject;
    }

    /**
     * 设置对象.
     *
     * @param strings 对象.
     */
    public void setSgObject(final String[] strings) {
        this.sgObject = strings;
    }

    /**
     * 获取对象的评估.
     *
     * @return 对象的评估.
     */
    public String[] getSgObjectEvaluation() {
        return sgObjectEvaluation;
    }

    /**
     * 设置对象的评估.
     *
     * @param strings 对象的评估.
     */
    public void setSgObjectEvaluation(final String[] strings) {
        this.sgObjectEvaluation = strings;
    }

    /**
     * 获取对象的评估强度.
     *
     * @return 对象的评估强度.
     */
    public int[] getIgObjectEvaluationStrength() {
        return igObjectEvaluationStrength;
    }

    /**
     * 设置对象的评估强度.
     *
     * @param ints 对象的评估强度.
     */
    public void setIgObjectEvaluationStrength(final int[] ints) {
        this.igObjectEvaluationStrength = ints;
    }

    /**
     * 获取对象评估计数.
     *
     * @return 对象评估计数.
     */
    public int getIgObjectEvaluationCount() {
        return igObjectEvaluationCount;
    }

    /**
     * 设置对象评估计数.
     *
     * @param i 对象评估计数.
     */
    public void setIgObjectEvaluationCount(final int i) {
        this.igObjectEvaluationCount = i;
    }
}
