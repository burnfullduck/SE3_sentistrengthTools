// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   ClassificationStatistics.java

package rainbowsix.ss;

/**
 * 文本分类统计数据类.
 * <br>工具类.
 *
 * @author 注释编写 徐晨 朱甲豪
 */
public final class ClassificationStatistics {
    /**
     * 最大类别数.
     */
    private static final int MAX_CLASS_COUNT = 100;

    private ClassificationStatistics() {
    }

    /**
     * 计算数据绝对值的相关系数.
     *
     * @param iCorrect   实际值数组
     * @param iPredicted 预测值数组
     * @param iCount     数组容量
     * @return 绝对值相关系数
     */
    public static double correlationAbs(final int[] iCorrect,
                                        final int[] iPredicted,
                                        final int iCount) {
        double fMeanC = 0.0D;
        double fMeanP = 0.0D;
        double fProdCP = 0.0D;
        double fSumCSq = 0.0D;
        double fSumPSq = 0.0D;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            fMeanC += Math.abs(iCorrect[iRow]);
            fMeanP += Math.abs(iPredicted[iRow]);
        }

        fMeanC /= iCount;
        fMeanP /= iCount;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            fProdCP += ((double) Math.abs(iCorrect[iRow]) - fMeanC)
                    * ((double) Math.abs(iPredicted[iRow]) - fMeanP);
            fSumPSq +=
                    Math.pow((double) Math.abs(iPredicted[iRow]) - fMeanP, 2D);
            fSumCSq += Math.pow((double) Math.abs(iCorrect[iRow]) - fMeanC, 2D);
        }

        return fProdCP / (Math.sqrt(fSumPSq) * Math.sqrt(fSumCSq));
    }

    /**
     * 相关系数.
     *
     * @param iCorrect   实际值数组
     * @param iPredicted 预测值数组
     * @param iCount     数组容量
     * @return 相关系数
     */
    public static double correlation(final int[] iCorrect,
                                     final int[] iPredicted, final int iCount) {
        double fMeanC = 0.0D;
        double fMeanP = 0.0D;
        double fProdCP = 0.0D;
        double fSumCSq = 0.0D;
        double fSumPSq = 0.0D;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            fMeanC += iCorrect[iRow];
            fMeanP += iPredicted[iRow];
        }

        fMeanC /= iCount;
        fMeanP /= iCount;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            fProdCP += ((double) iCorrect[iRow] - fMeanC)
                    * ((double) iPredicted[iRow] - fMeanP);
            fSumPSq += Math.pow((double) iPredicted[iRow] - fMeanP, 2D);
            fSumCSq += Math.pow((double) iCorrect[iRow] - fMeanC, 2D);
        }

        return fProdCP / (Math.sqrt(fSumPSq) * Math.sqrt(fSumCSq));
    }

    /**
     * 三元或二元的混淆表（混淆矩阵）.
     *
     * @param iTrinaryEstimate 三元预测值数组
     * @param iTrinaryCorrect  三元实际值数组
     * @param iDataCount       数组容量
     * @param estCorr          混淆矩阵
     */
    public static void trinaryOrBinaryConfusionTable(
            final int[] iTrinaryEstimate, final int[] iTrinaryCorrect,
            final int iDataCount, final int[][] estCorr) {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                estCorr[i][j] = 0;
            }

        }

        for (int i = 1; i <= iDataCount; i++) {
            if (iTrinaryEstimate[i] >= -1 && iTrinaryEstimate[i] <= 1
                    && iTrinaryCorrect[i] >= -1 && iTrinaryCorrect[i] <= 1) {
                estCorr[iTrinaryEstimate[i] + 1][iTrinaryCorrect[i] + 1]++;
            } else {
                System.out.println("Estimate or correct value " + i
                        + " out of range -1 to +1 (data count may be wrong): "
                        + iTrinaryEstimate[i] + " " + iTrinaryCorrect[i]);
            }
        }

    }

    /**
     * 计算实际值数组和预测值数组的相关系数.
     *
     * @param iCorrect   实际值数组
     * @param iPredicted 预测值数组
     * @param bSelected  记录是否被选择的数组
     * @param bInvert    是否倒转
     * @param iCount     数组容量
     * @return 相关系数
     */
    public static double correlationAbs(final int[] iCorrect,
                                        final int[] iPredicted,
                                        final boolean[] bSelected,
                                        final boolean bInvert,
                                        final int iCount) {
        double fMeanC = 0.0D;
        double fMeanP = 0.0D;
        double fProdCP = 0.0D;
        double fSumCSq = 0.0D;
        double fSumPSq = 0.0D;
        int iDataCount = 0;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if (bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert) {
                fMeanC += Math.abs(iCorrect[iRow]);
                fMeanP += Math.abs(iPredicted[iRow]);
                iDataCount++;
            }
        }

        fMeanC /= iDataCount;
        fMeanP /= iDataCount;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if (bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert) {
                fProdCP += ((double) Math.abs(iCorrect[iRow]) - fMeanC)
                        * ((double) Math.abs(iPredicted[iRow]) - fMeanP);
                fSumPSq +=
                        Math.pow((double) Math.abs(iPredicted[iRow]) - fMeanP,
                                2D);
                fSumCSq += Math.pow((double) Math.abs(iCorrect[iRow]) - fMeanC,
                        2D);
            }
        }

        return fProdCP / (Math.sqrt(fSumPSq) * Math.sqrt(fSumCSq));
    }

    /**
     * 计算预测正确个数.
     * <br>差值绝对值为0时，记为预测正确。
     *
     * @param iCorrect              实际值数组
     * @param iPredicted            预测值数组
     * @param iCount                数组容量
     * @param bChangeSignOfOneArray 预测值数组是否变号（是否为neg
     * @return 预测正确个数
     */
    public static int accuracy(final int[] iCorrect, final int[] iPredicted,
                               final int iCount,
                               final boolean bChangeSignOfOneArray) {
        int iCorrectCount = 0;
        if (bChangeSignOfOneArray) {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                if (iCorrect[iRow] == -iPredicted[iRow]) {
                    iCorrectCount++;
                }
            }

        } else {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                if (iCorrect[iRow] == iPredicted[iRow]) {
                    iCorrectCount++;
                }
            }

        }
        return iCorrectCount;
    }

    /**
     * 计算预测正确个数.
     * <br>带有对于数据的选择性和反转性来计算预测正确个数；差值绝对值为0时，记为预测正确。
     *
     * @param iCorrect   实际值数组
     * @param iPredicted 预测值数组
     * @param bSelected  是否选择的boolean数组
     * @param bInvert    是否反转
     * @param iCount     数组容量
     * @return 预测正确个数
     */
    public static int accuracy(final int[] iCorrect, final int[] iPredicted,
                               final boolean[] bSelected, final boolean bInvert,
                               final int iCount) {
        int iCorrectCount = 0;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if ((bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert)
                    && iCorrect[iRow] == iPredicted[iRow]) {
                iCorrectCount++;
            }
        }

        return iCorrectCount;
    }

    /**
     * 计算预测正确个数.
     * <br>带有对于数据的选择性和反转性来计算预测正确个数；差值绝对值为1或0时，记为预测正确。
     *
     * @param iCorrect   实际值数组
     * @param iPredicted 预测值数组
     * @param bSelected  是否选择的boolean数组
     * @param bInvert    是否反转
     * @param iCount     数组容量
     * @return 预测正确个数
     */
    public static int accuracyWithin1(final int[] iCorrect,
                                      final int[] iPredicted,
                                      final boolean[] bSelected,
                                      final boolean bInvert, final int iCount) {
        int iCorrectCount = 0;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if ((bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert)
                    && Math.abs(iCorrect[iRow] - iPredicted[iRow]) <= 1) {
                iCorrectCount++;
            }
        }

        return iCorrectCount;
    }

    /**
     * 计算预测正确个数.
     * <br>差值绝对值为1或0时，记为预测正确。
     *
     * @param iCorrect              实际值数组
     * @param iPredicted            预测值数组
     * @param iCount                数组容量
     * @param bChangeSignOfOneArray 预测值数组是否变号（是否为neg
     * @return 预测正确个数
     */
    public static int accuracyWithin1(final int[] iCorrect,
                                      final int[] iPredicted, final int iCount,
                                      final boolean bChangeSignOfOneArray) {
        int iCorrectCount = 0;
        if (bChangeSignOfOneArray) {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                if (Math.abs(iCorrect[iRow] + iPredicted[iRow]) <= 1) {
                    iCorrectCount++;
                }
            }

        } else {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                if (Math.abs(iCorrect[iRow] - iPredicted[iRow]) <= 1) {
                    iCorrectCount++;
                }
            }

        }
        return iCorrectCount;
    }

    /**
     * 计算平均绝对误差.
     * <br>带有对于数据的选择性和反转性来计算平均绝对误差。
     *
     * @param iCorrect   实际值数组
     * @param iPredicted 预测值数组
     * @param bSelected  是否选择的boolean数组
     * @param bInvert    是否反转
     * @param iCount     数组容量
     * @return 平均绝对误差
     */
    public static double absoluteMeanPercentageErrorNoDivision(
            final int[] iCorrect, final int[] iPredicted,
            final boolean[] bSelected, final boolean bInvert,
            final int iCount) {
        int iDataCount = 0;
        double fAMeanPE = 0.0D;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if (bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert) {
                fAMeanPE += Math.abs(iPredicted[iRow] - iCorrect[iRow]);
                iDataCount++;
            }
        }

        return fAMeanPE / (double) iDataCount;
    }

    /**
     * 计算平均绝对百分比误差.
     * <br>带有对于数据的选择性和反转性来计算平均绝对百分比误差。
     *
     * @param iCorrect   实际值数组
     * @param iPredicted 预测值数组
     * @param bSelected  是否选择的boolean数组
     * @param bInvert    是否反转
     * @param iCount     数组容量
     * @return 平均绝对百分比误差
     */
    public static double absoluteMeanPercentageError(final int[] iCorrect,
                                                     final int[] iPredicted,
                                                     final boolean[] bSelected,
                                                     final boolean bInvert,
                                                     final int iCount) {
        int iDataCount = 0;
        double fAMeanPE = 0.0D;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if (bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert) {
                fAMeanPE += Math.abs(
                        (double) (iPredicted[iRow] - iCorrect[iRow])
                                / (double) iCorrect[iRow]);
                iDataCount++;
            }
        }

        return fAMeanPE / (double) iDataCount;
    }

    /**
     * 计算平均绝对误差.
     *
     * @param iCorrect              实际值数组
     * @param iPredicted            预测值数组
     * @param iCount                数组容量
     * @param bChangeSignOfOneArray 预测值数组是否变号（是否为neg
     * @return 平均绝对误差
     */
    public static double absoluteMeanPercentageErrorNoDivision(
            final int[] iCorrect, final int[] iPredicted, final int iCount,
            final boolean bChangeSignOfOneArray) {
        double fAMeanPE = 0.0D;
        if (bChangeSignOfOneArray) {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                fAMeanPE += Math.abs(iPredicted[iRow] + iCorrect[iRow]);
            }

        } else {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                fAMeanPE += Math.abs(iPredicted[iRow] - iCorrect[iRow]);
            }

        }
        return fAMeanPE / (double) iCount;
    }

    /**
     * 用基线精度计算分类中众数所占比例.
     *
     * @param iCorrect 实际值数组
     * @param iCount   数组容量
     * @return 实际值数组中的众数所占比例
     */
    public static double baselineAccuracyMajorityClassProportion(
            final int[] iCorrect, final int iCount) {
        if (iCount == 0) {
            return 0.0D;
        }
        int[] iClassCount = new int[MAX_CLASS_COUNT];
        int iMinClass = iCorrect[1];
        int iMaxClass = iCorrect[1];
        for (int i = 2; i <= iCount; i++) {
            if (iCorrect[i] < iMinClass) {
                iMinClass = iCorrect[i];
            }
            if (iCorrect[i] > iMaxClass) {
                iMaxClass = iCorrect[i];
            }
        }

        if (iMaxClass - iMinClass >= MAX_CLASS_COUNT) {
            return 0.0D;
        }
        for (int i = 0; i <= iMaxClass - iMinClass; i++) {
            iClassCount[i] = 0;
        }

        for (int i = 1; i <= iCount; i++) {
            iClassCount[iCorrect[i] - iMinClass]++;
        }

        int iMaxClassCount = 0;
        for (int i = 0; i <= iMaxClass - iMinClass; i++) {
            if (iClassCount[i] > iMaxClassCount) {
                iMaxClassCount = iClassCount[i];
            }
        }

        return (double) iMaxClassCount / (double) iCount;
    }

    /**
     * 用基线精度做最大的分类预测.
     * <br>将预测结果置为正确结果中的最大值。
     *
     * @param iCorrect    实际值数组
     * @param iPredict    预测值数组
     * @param iCount      数组容量
     * @param bChangeSign 是否改变符号
     */
    public static void baselineAccuracyMakeLargestClassPrediction(
            final int[] iCorrect, final int[] iPredict, final int iCount,
            final boolean bChangeSign) {
        if (iCount == 0) {
            return;
        }
        int[] iClassCount = new int[MAX_CLASS_COUNT];
        int iMinClass = iCorrect[1];
        int iMaxClass = iCorrect[1];
        for (int i = 2; i <= iCount; i++) {
            if (iCorrect[i] < iMinClass) {
                iMinClass = iCorrect[i];
            }
            if (iCorrect[i] > iMaxClass) {
                iMaxClass = iCorrect[i];
            }
        }

        if (iMaxClass - iMinClass >= MAX_CLASS_COUNT) {
            return;
        }
        for (int i = 0; i <= iMaxClass - iMinClass; i++) {
            iClassCount[i] = 0;
        }

        for (int i = 1; i <= iCount; i++) {
            iClassCount[iCorrect[i] - iMinClass]++;
        }

        int iMaxClassCount = 0;
        int iLargestClass = 0;
        for (int i = 0; i <= iMaxClass - iMinClass; i++) {
            if (iClassCount[i] > iMaxClassCount) {
                iMaxClassCount = iClassCount[i];
                iLargestClass = i + iMinClass;
            }
        }

        if (bChangeSign) {
            for (int i = 1; i <= iCount; i++) {
                iPredict[i] = -iLargestClass;
            }

        } else {
            for (int i = 1; i <= iCount; i++) {
                iPredict[i] = iLargestClass;
            }

        }
    }

    /**
     * 平均绝对百分比误差.
     *
     * @param iCorrect              实际值数组
     * @param iPredicted            预测值数组
     * @param iCount                数组容量
     * @param bChangeSignOfOneArray 预测值数组是否变号（是否为neg
     * @return 平均绝对百分比误差
     */
    public static double absoluteMeanPercentageError(final int[] iCorrect,
                                                     final int[] iPredicted,
                                                     final int iCount,
                                                     final boolean
                                                     bChangeSignOfOneArray) {
        double fAMeanPE = 0.0D;
        if (bChangeSignOfOneArray) {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                fAMeanPE += Math.abs(
                        (double) (iPredicted[iRow] + iCorrect[iRow])
                                / (double) iCorrect[iRow]);
            }

        } else {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                fAMeanPE += Math.abs(
                        (double) (iPredicted[iRow] - iCorrect[iRow])
                                / (double) iCorrect[iRow]);
            }

        }
        return fAMeanPE / (double) iCount;
    }


}
