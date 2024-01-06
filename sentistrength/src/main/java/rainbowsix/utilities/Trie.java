package rainbowsix.utilities;

/**
 * 字典树trie类.
 * <br>
 * Trie是一种特殊的树形数据结构，它由节点和边组成，每个节点最多有n个子节点，边表示字符串中的单个字符。<br>
 * 本类只实现了查找
 *
 * @author 朱甲豪
 */
public final class Trie {
    /**
     * 构造函数.
     */
    private Trie() {
    }

    /**
     * 获取字符串在字典树中的位置。如果字典树为空则直接插入。若字典树不为空但没有找到，则根据bDontAddNewString判断是否需要插入.
     * <br>
     * 方法的逻辑如下：
     * 如果iLastElement小于iFirstElement，说明Trie为空，直接将sText放在sArray[iFirstElement
     * ]位置，并将iLessPointer[iFirstElement]和iMorePointer[iFirstElement]设为-1
     * ，表示没有子树。然后返回iFirstElement作为结果。<br>
     * 否则，从iFirstElement开始遍历Trie，比较sText和当前节点对应的字符串。<br>
     * 如果sText
     * 小于当前节点对应的字符串，则沿着左子树继续遍历；如果大于，则沿着右子树继续遍历；如果等于，则说明找到了匹配项，直接返回当前节点对应的索引作为结果。<br>
     * 如果遍历过程中遇到空指针（即没有找到匹配项），则判断bDontAddNewString是否为真。如果为真，则返回-1；否则，在sArray
     * [iLastElement+1]位置插入新字符串，并将其左右指针设为-1
     * 。同时更新上一个节点对应方向（左或右）指针指向新插入元素，并返回新插入元素对应索引作为结果。<br>
     *
     * @param sText             需要查找的字符串，若没找到则根据bDontAddNewString的值判断是否需要插入
     * @param sArray            用于存储Trie中所有字符串的数组
     * @param iLessPointer      用于存储每个节点左子树（小于当前字符）的索引的数组
     * @param iMorePointer      用于存储每个节点右子树（大于或等于当前字符）的索引的数组
     * @param iFirstElement     Trie中第一个元素在sArray中的索引
     * @param iLastElement      Trie中最后一个元素在sArray中的索引
     * @param bDontAddNewString 一个布尔值，表示是否允许插入新字符串
     * @return 这个方法返回sText在sArray中对应位置（如果存在）或者插入位置（如果不存在）。如果bDontAddNewString
     * 为真，则不会插入新字符串，而是返回-1。
     */
    public static int getTriePositionForString(final String sText,
                                               final String[] sArray,
                                               final int[] iLessPointer,
                                               final int[] iMorePointer,
                                               final int iFirstElement,
                                               final int iLastElement,
                                               final boolean bDontAddNewString) {
        int lastElement = iLastElement;
        int iTriePosition;
        int iLastTriePosition;
        if (lastElement < iFirstElement) {
            sArray[iFirstElement] = sText;
            iLessPointer[iFirstElement] = -1;
            iMorePointer[iFirstElement] = -1;
            return iFirstElement;
        } else {
            iTriePosition = iFirstElement;

//         int iLastTriePosition;
            label33:
            //当sText小于当前节点对应的字符串时，就会跳转到这个位置，继续遍历左子树。
            do {
                do {
                    iLastTriePosition = iTriePosition;
                    if (sText.compareTo(sArray[iTriePosition]) < 0) {
                        iTriePosition = iLessPointer[iTriePosition];
                        continue label33;
                    }

                    if (sText.compareTo(sArray[iTriePosition]) <= 0) {
                        return iTriePosition;
                    }

                    iTriePosition = iMorePointer[iTriePosition];
                } while (iTriePosition != -1);

                if (bDontAddNewString) {
                    return -1;
                }

                ++lastElement;
                sArray[lastElement] = sText;
                iLessPointer[lastElement] = -1;
                iMorePointer[lastElement] = -1;
                iMorePointer[iLastTriePosition] = lastElement;
                return lastElement;
            } while (iTriePosition != -1);

            if (bDontAddNewString) {
                return -1;
            } else {
                ++lastElement;
                sArray[lastElement] = sText;
                iLessPointer[lastElement] = -1;
                iMorePointer[lastElement] = -1;
                iLessPointer[iLastTriePosition] = lastElement;
                return lastElement;
            }
        }
    }

    /**
     * @param sText             需要查找的字符串，若没找到则根据bDontAddNewString的值判断是否需要插入
     * @param sArray            用于存储Trie中所有字符串的数组
     * @param iLessPointer      用于存储每个节点左子树（小于当前字符）的索引的数组
     * @param iMorePointer      用于存储每个节点右子树（大于或等于当前字符）的索引的数组
     * @param iLastElement      Trie中最后一个元素在sArray中的索引
     * @param bDontAddNewString 一个布尔值，表示是否允许插入新字符串
     * @return 这个方法返回sText在sArray中对应位置（如果存在）或者插入位置（如果不存在）。如果bDontAddNewString
     * 为真，则不会插入新字符串，而是返回-1。
     * @deprecated 旧版字典树查找(已舍弃)<br>
     * 相比于新的少了iFirstElement参数故性能较差
     */
    public static int getTriePositionForStringOld(final String sText,
                                                  final String[] sArray,
                                                  final int[] iLessPointer,
                                                  final int[] iMorePointer,
                                                  final int iLastElement,
                                                  final boolean bDontAddNewString) {
        int lastElement = iLastElement;
        int iTriePosition;
        int iLastTriePosition;
        if (lastElement == 0) {
            lastElement = 1;
            sArray[lastElement] = sText;
            iLessPointer[lastElement] = 0;
            iMorePointer[lastElement] = 0;
            return 1;
        } else {
            iTriePosition = 1;

//         int iLastTriePosition;
            label33:
            do {
                do {
                    iLastTriePosition = iTriePosition;
                    if (sText.compareTo(sArray[iTriePosition]) < 0) {
                        iTriePosition = iLessPointer[iTriePosition];
                        continue label33;
                    }

                    if (sText.compareTo(sArray[iTriePosition]) <= 0) {
                        return iTriePosition;
                    }

                    iTriePosition = iMorePointer[iTriePosition];
                } while (iTriePosition != 0);

                if (bDontAddNewString) {
                    return 0;
                }

                ++lastElement;
                sArray[lastElement] = sText;
                iLessPointer[lastElement] = 0;
                iMorePointer[lastElement] = 0;
                iMorePointer[iLastTriePosition] = lastElement;
                return lastElement;
            } while (iTriePosition != 0);

            if (bDontAddNewString) {
                return 0;
            } else {
                ++lastElement;
                sArray[lastElement] = sText;
                iLessPointer[lastElement] = 0;
                iMorePointer[lastElement] = 0;
                iLessPointer[iLastTriePosition] = lastElement;
                return lastElement;
            }
        }
    }

    /**
     * 在字典树中进行字符串查找，若找到则计数加1，返回字符串在字典树中的位置。若未找且bDontAddNewString为false
     * 则返回插入的位置，反之返回-1.<br>
     * 本方法调用了i_GetTriePositionForString()
     *
     * @param sText             需要查找的字符串，若没找到则根据bDontAddNewString的值判断是否需要插入
     * @param sArray            用于存储Trie中所有字符串的数组
     * @param iCountArray       记录每个字符串数量的数组
     * @param iLessPointer      用于存储每个节点左子树（小于当前字符）的索引的数组
     * @param iMorePointer      用于存储每个节点右子树（大于或等于当前字符）的索引的数组
     * @param iFirstElement     Trie中第一个元素在sArray中的索引
     * @param iLastElement      Trie中最后一个元素在sArray中的索引
     * @param bDontAddNewString 一个布尔值，表示是否允许插入新字符串
     * @return 这个方法返回sText在sArray中对应位置（如果存在）或者插入位置（如果不存在）。如果bDontAddNewString
     * 为真，则不会插入新字符串，而是返回-1。
     */
    public static int getTriePositionForStringAndAddCount(final String sText,
                                                          final String[] sArray,
                                                          final int[] iCountArray,
                                                          final int[] iLessPointer,
                                                          final int[] iMorePointer,
                                                          final int iFirstElement,
                                                          final int iLastElement,
                                                          final boolean bDontAddNewString) {
        int iPos = getTriePositionForString(sText, sArray, iLessPointer,
                iMorePointer, iFirstElement, iLastElement, bDontAddNewString);
        if (iPos >= 0) { //能找到则计数加1
            int var10002 = iCountArray[iPos]++; //暂不清楚此变量意义
        }

        return iPos;
    }
}
