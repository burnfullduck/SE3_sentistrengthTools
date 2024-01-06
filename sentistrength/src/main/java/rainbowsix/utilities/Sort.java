package rainbowsix.utilities;

/**
 * 各种排序方法.
 * <br>
 * 都按照从小到大顺序排序，字符串排序均按照字典序<br>
 * 需要用到排序时会用到此类。<br>
 *
 * @author 注释添加 朱甲豪
 */
public final class Sort {
    /**
     * 构造函数.
     */
    private Sort() {
    }

    /**
     * 字符串快排.
     * <br>
     *
     * @param sArray 需要排序的字符串数组
     * @param l      需要排序的数组的起始索引
     * @param r      需要排序的数组的终止索引
     */
    public static void quickSortStrings(final String[] sArray, final int l,
                                        final int r) {
        String sMiddle = sArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (sMiddle.compareTo(sArray[i]) > 0 && i < r) {
                ++i;
            }

            while (sMiddle.compareTo(sArray[j]) < 0 && j > l) {
                --j;
            }

            if (i < j) {
                String s = sArray[i];
                sArray[i] = sArray[j];
                sArray[j] = s;
            }

            if (i <= j) {
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortStrings(sArray, l, j);
        }

        if (i < r) {
            quickSortStrings(sArray, i, r);
        }

    }

    /**
     * int快排.
     * <br>
     *
     * @param iArray int数组
     * @param l      需要排序的数组的起始索引
     * @param r      需要排序的数组的终止索引
     */

    public static void quickSortInt(final int[] iArray, final int l,
                                    final int r) {
        int i2 = iArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (iArray[i] < i2 && i < r) {
                ++i;
            }

            while (i2 < iArray[j] && j > l) {
                --j;
            }

            if (i <= j) {
                int i1 = iArray[i];
                iArray[i] = iArray[j];
                iArray[j] = i1;
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortInt(iArray, l, j);
        }

        if (i < r) {
            quickSortInt(iArray, i, r);
        }

    }

    /**
     * 带有int对应值的浮点数快排,按浮点数大小排序.
     *
     * @param fArray  要排序的浮点数数组
     * @param iArray2 对应的int索引数组
     * @param l       需要排序的数组的起始索引
     * @param r       需要排序的数组的终止索引
     */
    public static void quickSortDoubleWithInt(final double[] fArray,
                                              final int[] iArray2, final int l,
                                              final int r) {
        double x = fArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (fArray[i] < x && i < r) {
                ++i;
            }

            while (x < fArray[j] && j > l) {
                --j;
            }

            if (i <= j) {
                double fTemp = fArray[i];
                int iTemp = iArray2[i];
                fArray[i] = fArray[j];
                iArray2[i] = iArray2[j];
                fArray[j] = fTemp;
                iArray2[j] = iTemp;
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortDoubleWithInt(fArray, iArray2, l, j);
        }

        if (i < r) {
            quickSortDoubleWithInt(fArray, iArray2, i, r);
        }

    }

    /**
     * 带有int对应值的整数快排，按照iArray数组中的整数大小排序.
     *
     * @param iArray  要排序的整数数组
     * @param iArray2 对应的int数组
     * @param l       需要排序的数组的起始索引
     * @param r       需要排序的数组的终止索引
     */
    public static void quickSortIntWithInt(final int[] iArray,
                                           final int[] iArray2, final int l,
                                           final int r) {
        int i1 = iArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (iArray[i] < i1 && i < r) {
                ++i;
            }

            while (i1 < iArray[j] && j > l) {
                --j;
            }

            if (i <= j) {
                int iTemp = iArray[i];
                int iTemp2 = iArray2[i];
                iArray[i] = iArray[j];
                iArray2[i] = iArray2[j];
                iArray[j] = iTemp;
                iArray2[j] = iTemp2;
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortIntWithInt(iArray, iArray2, l, j);
        }

        if (i < r) {
            quickSortIntWithInt(iArray, iArray2, i, r);
        }

    }

    /**
     * 在已排好的字符串数组中寻找某个字符串的位置，使用二分法.
     *
     * @param sText  要寻找的字符串
     * @param sArray 已经排序好的字符串数组
     * @param iLower 数组的起始索引
     * @param iUpper 数组的终止索引
     * @return 要寻找的字符串所在位置
     */
    public static int findStringPositionInSortedArray(final String sText,
                                                      final String[] sArray,
                                                      final int iLower,
                                                      final int iUpper) {
        int lower = iLower;
        int upper = iUpper;
        int iMiddle;
        while (upper - lower > 2) {
            iMiddle = (lower + upper) / 2;
            if (sText.compareTo(sArray[iMiddle]) < 0) {
                upper = iMiddle;
            } else {
                lower = iMiddle;
            }
        }

        for (iMiddle = lower; iMiddle <= upper; ++iMiddle) {
            if (sArray[iMiddle].compareTo(sText) == 0) {
                return iMiddle;
            }
        }

        return -1;
    }

    /**
     * 在已排好的整数数组中寻找某个整数的位置，使用二分法.
     *
     * @param iFind  要寻找的整数
     * @param iArray 已经排序好的整数数组
     * @param iLower 数组的起始索引
     * @param iUpper 数组的终止索引
     * @return 要寻找的字符串所在位置
     */
    public static int findIntPositionInSortedArray(final int iFind,
                                                   final int[] iArray,
                                                   final int iLower,
                                                   final int iUpper) {
        int lower = iLower;
        int upper = iUpper;
        int iMiddle;
        while (upper - lower > 2) {
            iMiddle = (lower + upper) / 2;
            if (iFind < iArray[iMiddle]) {
                upper = iMiddle;
            } else {
                lower = iMiddle;
            }
        }

        for (iMiddle = lower; iMiddle <= upper; ++iMiddle) {
            if (iArray[iMiddle] == iFind) {
                return iMiddle;
            }
        }

        return -1;
    }

    /**
     * 字符串数组带缩写的快排.
     * <br>
     * 数组中字符串可能以*缩写，如string可能被缩写为str*
     *
     * @param sText  要寻找的字符串
     * @param sArray 已经排序好的字符串数组
     * @param iLower 数组的起始索引
     * @param iUpper 数组的终止索引
     * @return 要寻找的字符串所在位置
     */
    public static int findStringPositionInSortedArrayWithWildcardsInArray(
            final String sText, final String[] sArray, final int iLower,
            final int iUpper) {
        int lower = iLower;
        int upper = iUpper;
        int iOriginalLower = lower;
        int iOriginalUpper = upper;

        int iMiddle;
        while (upper - lower > 2) {
            iMiddle = (lower + upper) / 2;
            if (sText.compareTo(sArray[iMiddle]) < 0) {
                upper = iMiddle;
            } else {
                lower = iMiddle;
            }
        }

        for (iMiddle = upper; iMiddle >= lower; --iMiddle) {
            if (sArray[iMiddle].compareTo(sText) == 0) {
                return iMiddle;
            }
        }

        if (lower > iOriginalLower) {
            --lower;
        }

        if (lower > iOriginalLower) {
            --lower;
        }

        if (lower > iOriginalLower) {
            --lower;
        }

        if (upper < iOriginalUpper) {
            ++upper;
        }

        int iTextLength = sText.length();

        for (iMiddle = upper; iMiddle >= lower; --iMiddle) {
            int iLength = sArray[iMiddle].length();
            if (iLength > 1 && sArray[iMiddle].substring(iLength - 1, iLength)
                    .compareTo("*") == 0 && iTextLength >= iLength - 1
                    && sText.substring(0, iLength - 1).compareTo(
                            sArray[iMiddle].substring(0, iLength - 1)) == 0) {
                return iMiddle;
            }
        }

        return -1;
    }

    /**
     * 带有int对应值的按照字典序的字符串快排.
     *
     * @param sArray 要排序的字符串数组
     * @param iArray 对应的int数组
     * @param l      需要排序的数组的起始索引
     * @param r      需要排序的数组的终止索引
     */
    public static void quickSortStringsWithInt(final String[] sArray,
                                               final int[] iArray, final int l,
                                               final int r) {
        String sMiddle = sArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (sMiddle.compareTo(sArray[i]) > 0 && i < r) {
                ++i;
            }

            while (sMiddle.compareTo(sArray[j]) < 0 && j > l) {
                --j;
            }

            if (i < j) {
                String sTemp = sArray[i];
                int iTemp = iArray[i];
                sArray[i] = sArray[j];
                iArray[i] = iArray[j];
                sArray[j] = sTemp;
                iArray[j] = iTemp;
            }

            if (i <= j) {
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortStringsWithInt(sArray, iArray, l, j);
        }

        if (i < r) {
            quickSortStringsWithInt(sArray, iArray, i, r);
        }

    }

    /**
     * 带有string对应值的字符串快排，按照sArray字符串数组的字典序排序.
     *
     * @param sArray  要排序的字符串数组
     * @param sArray2 对应的字符串数组
     * @param l       需要排序的数组的起始索引
     * @param r       需要排序的数组的终止索引
     */
    public static void quickSortStringsWithStrings(final String[] sArray,
                                                   final String[] sArray2,
                                                   final int l, final int r) {
        String sMiddle = sArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (sMiddle.compareTo(sArray[i]) > 0 && i < r) {
                ++i;
            }

            while (sMiddle.compareTo(sArray[j]) < 0 && j > l) {
                --j;
            }

            if (i < j) {
                String sTemp = sArray[i];
                String sTemp2 = sArray2[i];
                sArray[i] = sArray[j];
                sArray2[i] = sArray2[j];
                sArray[j] = sTemp;
                sArray2[j] = sTemp2;
            }

            if (i <= j) {
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortStringsWithStrings(sArray, sArray2, l, j);
        }

        if (i < r) {
            quickSortStringsWithStrings(sArray, sArray2, i, r);
        }

    }

    /**
     * 将int数组打乱顺序.
     *
     * @param iArray 需要打乱位置的int数组
     */
    public static void makeRandomOrderList(final int[] iArray) {
        if (iArray != null) {
            int iArraySize = iArray.length;
            if (iArraySize >= 1) {
                double[] fRandArray = new double[iArraySize--];

                for (int i = 1; i <= iArraySize; ++i) {
                    iArray[i] = i;
                    fRandArray[i] = Math.random();
                }

                quickSortDoubleWithInt(fRandArray, iArray, 1, iArraySize);
            }
        }
    }

    /**
     * 对浮点数数组通过索引进行降序的快排.
     * <br>
     * 按照浮点数降序排序,索引跟着浮点数一起移动
     *
     * @param fArray      浮点数数组
     * @param iIndexArray 索引数组
     * @param l           需要排序的数组的起始索引
     * @param r           需要排序的数组的终止索引
     */
    public static void quickSortNumbersDescendingViaIndex(final double[] fArray,
                                                          final int[] iIndexArray,
                                                          final int l,
                                                          final int r) {
        int i = l;
        int j = r;
        double fX = fArray[iIndexArray[(l + r) / 2]];

        while (i <= j) {
            while (fArray[iIndexArray[i]] > fX && i < r) {
                ++i;
            }

            while (fX > fArray[iIndexArray[j]] && j > l) {
                --j;
            }

            if (i <= j) {
                int iTemp = iIndexArray[i];
                iIndexArray[i] = iIndexArray[j];
                iIndexArray[j] = iTemp;
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortNumbersDescendingViaIndex(fArray, iIndexArray, l, j);
        }

        if (i < r) {
            quickSortNumbersDescendingViaIndex(fArray, iIndexArray, i, r);
        }

    }
}
