package rainbowsix.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * 字符串索引类，具体数据结构为字典树trie.
 *
 * @author 注释编写 朱甲豪 詹美瑛
 */
public class StringIndex {
    /**
     * 最大文本数量初始值.
     */
    private static final int TEXT_MAX_INITIAL = 10000000;
    /**
     * 最大文本数量.
     */
    private int igTextMax = TEXT_MAX_INITIAL;
    /**
     * 文本数组.
     */
    private String[] sgText;
    /**
     * 评论数组.
     */
    private String[] sgTextComment;
    /**
     * 文本左指针.
     */
    private int[] igTextLessPtr;
    /**
     * 文本右指针.
     */
    private int[] igTextMorePtr;
    /**
     * 文本数量.
     */
    private int[] igTextCount;
    /**
     * 最后一个文本的位置.
     */
    private int igTextLast = -1;
    /**
     * 是否已经计数过.
     */
    private boolean bgIncludeCounts = false;
    /**
     * 文本左指针存放位置.
     */
    private static final int LESS_PTR_INDEX = 1;
    /**
     * 文本右指针存放位置.
     */
    private static final int MORE_PTR_INDEX = 2;
    /**
     * 文本数量存放位置.
     */
    private static final int COUNT_INDEX = 3;
    /**
     * 最大备份值.
     */
    private static final int MAX_BACKUP = 10;

    /**
     * 初始化.
     *
     * @param iVocabMaxIfOverrideDefault 设置默认最大词汇量(默认10000000)
     * @param bIncludeCounts             是否要附带记录词汇总数
     * @param bIncludeComments           每个词是否需要注释
     */
    public void initialise(final int iVocabMaxIfOverrideDefault,
                           final boolean bIncludeCounts,
                           final boolean bIncludeComments) {
        this.bgIncludeCounts = bIncludeCounts;
        if (iVocabMaxIfOverrideDefault > 0) {
            this.igTextMax = iVocabMaxIfOverrideDefault;
        }

        this.setSgText(new String[this.igTextMax]);
        this.igTextLessPtr = new int[this.igTextMax];
        this.igTextMorePtr = new int[this.igTextMax];
        this.igTextLast = -1;
        int i;
        if (this.bgIncludeCounts) {
            this.igTextCount = new int[this.igTextMax];

            for (i = 0; i < this.igTextMax; ++i) {
                this.igTextCount[i] = 0;
            }
        }

        if (bIncludeComments) {
            this.setSgTextComment(new String[this.igTextMax]);

            for (i = 0; i < this.igTextMax; ++i) {
                this.igTextCount[i] = 0;
            }
        }

    }

    /**
     * 读取文本文件并将其从文件中初始化.
     *
     * @param sVocabTermPtrsCountFileName 词汇表所在文件
     * @return 返回操作是否成功
     */
    public boolean load(final String sVocabTermPtrsCountFileName) {
        File f = new File(sVocabTermPtrsCountFileName);
        if (!f.exists()) {
            System.out.println("Could not find the vocab file: "
                    + sVocabTermPtrsCountFileName);
            return false;
        } else {
            try {
                BufferedReader rReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(
                                sVocabTermPtrsCountFileName),
                                StandardCharsets.UTF_8));
                String sLine = rReader.readLine();
                String[] sData;
                this.igTextLast = -1;

                while (rReader.ready()) {
                    sLine = rReader.readLine();
                    if (sLine.length() > 0) {
                        sData = sLine.split("\t");
                        if (sData.length > 2) {
                            if (this.igTextLast == this.igTextMax - 1) {
                                this.increaseArraySizes(this.igTextMax * 2);
                            }

                            this.getSgText()[++this.igTextLast] = sData[0];
                            this.igTextLessPtr[this.igTextLast]
                                    = Integer.parseInt(sData[LESS_PTR_INDEX]);
                            this.igTextMorePtr[this.igTextLast]
                                    = Integer.parseInt(sData[MORE_PTR_INDEX]);
                            this.igTextCount[this.igTextLast]
                                    = Integer.parseInt(sData[COUNT_INDEX]);
                        }
                    }
                }

                rReader.close();
                return true;
            } catch (IOException var7) {
                System.out.println(
                        "Could not open file for reading or read from file: "
                                + sVocabTermPtrsCountFileName);
                var7.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 获取最后一个词的ID.
     *
     * @return 返回最后一个词的int索引值
     */
    public int getLastWordID() {
        return this.igTextLast;
    }

    /**
     * 将字符串及索引信息(stringIndex)保存到给定的文件.
     *
     * @param sVocabTermPtrsCountFileName 需要保存入的文件
     * @return 返回操作是否成功的布尔值
     */
    public boolean save(final String sVocabTermPtrsCountFileName) {
        if (!FileOps.backupFileAndDeleteOriginal(sVocabTermPtrsCountFileName,
                MAX_BACKUP)) {
            System.out.println(
                    "Could not backup vocab! Perhaps no index exists yet.");
        }

        try {
            try (BufferedWriter wWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(sVocabTermPtrsCountFileName),
                            StandardCharsets.UTF_8))) {
                wWriter.write("Word\tLessPtr\tMorePtr\tAllTopics");

                for (int i = 0; i <= this.igTextLast; ++i) {
                    wWriter.write(
                            this.getSgText()[i] + "\t" + this.igTextLessPtr[i]
                                    + "\t" + this.igTextMorePtr[i] + "\t"
                                    + this.igTextCount[i] + "\n");
                }

            }
            return true;
        } catch (IOException var4) {
            System.out.println(
                    "Could not open file for writing or write to file: "
                            + sVocabTermPtrsCountFileName);
            var4.printStackTrace();
            return false;
        }
    }

    /**
     * 在此数据结构中增加一个字符串.
     *
     * @param sText        要增加的字符串
     * @param bRecordCount 是否记录该词的个数
     */
    public void addString(final String sText, final boolean bRecordCount) {
        if (this.igTextLast == this.igTextMax - 1) {
            this.increaseArraySizes(this.igTextMax * 2);
        }

        int iPos1;
        if (bRecordCount) {
            iPos1 = Trie.getTriePositionForStringAndAddCount(sText,
                    this.getSgText(), this.igTextCount, this.igTextLessPtr,
                    this.igTextMorePtr, 0, this.igTextLast, false);
        } else {
            iPos1 = Trie.getTriePositionForString(sText, this.getSgText(),
                    this.igTextLessPtr, this.igTextMorePtr, 0, this.igTextLast,
                    false);
        }

        if (iPos1 > this.igTextLast) {
            this.igTextLast = iPos1;
        }

    }

    /**
     * 通过字符串找到其在字典树(Trie)中的位置.
     * <br>
     * 调用了Trie.i_GetTriePositionForString()
     *
     * @param sText 要查找的单词
     * @return 该字符串的位置
     */
    public int findString(final String sText) {
        return Trie.getTriePositionForString(sText, this.getSgText(),
                this.igTextLessPtr, this.igTextMorePtr, 0, this.igTextLast,
                true);
    }

    /**
     * 给某个位置的字符串计数加1.
     *
     * @param iStringPos 字符串位置
     */
    public void add1ToCount(final int iStringPos) {
        int var10002 = this.igTextCount[iStringPos]++;
    }

    /**
     * 增加sgText, igTextLessPtr, igTextMorePtr的数组大小, 若有count则也要增加igTextCount的大小.
     *
     * @param iNewArraySize 新的数组大小
     */
    private void increaseArraySizes(final int iNewArraySize) {
        if (iNewArraySize > this.igTextMax) {
            String[] sgTextTemp = new String[iNewArraySize];
            int[] iTextLessPtrTemp = new int[iNewArraySize];
            int[] iTextMorePtrTemp = new int[iNewArraySize];
            System.arraycopy(this.getSgText(), 0, sgTextTemp, 0,
                    this.igTextMax);
            System.arraycopy(this.igTextLessPtr, 0, iTextLessPtrTemp, 0,
                    this.igTextMax);
            System.arraycopy(this.igTextMorePtr, 0, iTextMorePtrTemp, 0,
                    this.igTextMax);
            if (this.bgIncludeCounts) {
                int[] iVocabCountTemp = new int[iNewArraySize];
                System.arraycopy(this.igTextCount, 0, iVocabCountTemp, 0,
                        this.igTextMax);

                for (int i = this.igTextMax; i < iNewArraySize; ++i) {
                    this.igTextCount[i] = 0;
                }

                this.igTextCount = iVocabCountTemp;
            }

            this.igTextMax = iNewArraySize;
            this.igTextLessPtr = iTextLessPtrTemp;
            this.igTextMorePtr = iTextMorePtrTemp;
        }
    }

    /**
     * 扩大传入字符串数组的大小.
     *
     * @param sArray            传入字符串数组
     * @param iCurrentArraySize 目前数组大小
     * @param iNewArraySize     新的数组大小
     * @return 扩大后的字符串数组
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
     * 扩大传入int数组的大小.
     *
     * @param iArray            传入int数组
     * @param iCurrentArraySize 目前数组大小
     * @param iNewArraySize     新的数组大小
     * @return 扩大后的int数组
     */
    private static int[] increaseArraySize(final int[] iArray,
                                           final int iCurrentArraySize,
                                           final int iNewArraySize) {
        if (iNewArraySize <= iCurrentArraySize) {
            return iArray;
        } else {
            int[] iArrayTemp = new int[iNewArraySize];
            System.arraycopy(iArray, 0, iArrayTemp, 0, iCurrentArraySize);
            return iArrayTemp;
        }
    }

    /**
     * 根据索引获取字符串.
     *
     * @param iStringPos 字符串位置
     * @return 字符串
     */
    public String getString(final int iStringPos) {
        return this.getSgText()[iStringPos];
    }

    /**
     * 通过字符串在数组中的位置获取注释.
     *
     * @param iStringPos 字符串位置
     * @return 注释
     */
    public String getComment(final int iStringPos) {
        return this.getSgTextComment()[iStringPos] == null ? ""
                : this.getSgTextComment()[iStringPos];
    }

    /**
     * 给某个位置的字符串添加注释.
     *
     * @param iStringPos 字符串位置
     * @param sComment   注释
     */
    public void addComment(final int iStringPos, final String sComment) {
        this.getSgTextComment()[iStringPos] = sComment;
    }

    /**
     * 通过字符串位置得到某个字符串的个数.
     *
     * @param iStringPos 字符串位置
     * @return 该字符串的个数
     */
    public int getCount(final int iStringPos) {
        return this.igTextCount[iStringPos];
    }

    /**
     * 将对应位置字符串的个数设为0.
     *
     * @param iStringPos 需要设为0的字符串位置
     */
    public void setCountToZero(final int iStringPos) {
        this.igTextCount[iStringPos] = 0;
    }

    /**
     * 将所有字符串的计数都设为0.
     */
    public void setAllCountsToZero() {
        for (int i = 0; i <= this.igTextLast; ++i) {
            this.igTextCount[i] = 0;
        }

    }

    /**
     * 获取文本数组.
     *
     * @return 文本数组.
     */
    public String[] getSgText() {
        return sgText;
    }

    /**
     * 设置文本数组.
     *
     * @param strings 文本数组.
     */
    public void setSgText(final String[] strings) {
        this.sgText = strings;
    }

    /**
     * 获取评论数组.
     *
     * @return 评论数组.
     */
    public String[] getSgTextComment() {
        return sgTextComment;
    }

    /**
     * 设置评论数组.
     *
     * @param strings 评论数组.
     */
    public void setSgTextComment(final String[] strings) {
        this.sgTextComment = strings;
    }
}
