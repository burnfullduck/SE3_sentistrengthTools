package rainbowsix.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件操作相关的工具类.
 * <br>
 *
 * @author 注释添加 朱甲豪
 */
public final class FileOps {
    /**
     * 文件最大编号.
     */
    private static final int MAX_NUM = 1000;

    private FileOps() {
    }

    /**
     * 备份文件以及删除源文件操作.
     * <br>
     * 若文件没有备份，则进行备份<br>
     * 若文件备份数量达到最大备份数，则删除最后备份的文件，之后将剩下的备份文件重新备份后删除原备份文件，源文件保留<br>
     * 若文件有备份，且数量少于最大备份数，则对源文件再进行一次备份
     *
     * @param sFileName   文件名
     * @param iMaxBackups 最大备份数
     * @return 返回操作是否成功的布尔值
     */
    public static boolean backupFileAndDeleteOriginal(final String sFileName,
                                                      final int iMaxBackups) {
        int iLastBackup;
        File f;
        for (iLastBackup = iMaxBackups; iLastBackup >= 0; --iLastBackup) {
            f = new File(sFileName + iLastBackup + ".bak");
            if (f.exists()) {
                break;
            }
        }

        if (iLastBackup < 1) {
            f = new File(sFileName);
            if (f.exists()) {
                f.renameTo(new File(sFileName + "1.bak"));
                return true;
            } else {
                return false;
            }
        } else {
            if (iLastBackup == iMaxBackups) {
                f = new File(sFileName + iLastBackup + ".bak");
                f.delete();
                --iLastBackup;
            }

            for (int i = iLastBackup; i > 0; --i) {
                f = new File(sFileName + i + ".bak");
                f.renameTo(new File(sFileName + (i + 1) + ".bak"));
            }

            f = new File(sFileName);
            f.renameTo(new File(sFileName + "1.bak"));
            return true;
        }
    }

    /**
     * 获取文件总行数.
     * <br>
     * 计算一个文本文件中文本的行数，如果没有找到文件或发生其他IO错误，返回-1。
     *
     * @param sFileLocation 文件路径
     * @return int 文件总行数
     */
    public static int iCountLinesInTextFile(final String sFileLocation) {
        int iLines = 0;

        try {
            BufferedReader rReader;
            for (rReader = new BufferedReader(new FileReader(sFileLocation));
                 rReader.ready(); ++iLines) {
                String sLine = rReader.readLine();
            }

            rReader.close();
            return iLines;
        } catch (IOException var5) {
            var5.printStackTrace();
            return -1;
        }
    }

    /**
     * 寻找下一个可用的文件名.
     * <br>
     * 用于输出文件，防止生成输出文件名不可用
     *
     * @param sFileNameStart 文件名起始字符串
     * @param sFileNameEnd   文件名结束字符串
     * @return 生成的文件名，若找不到则返回空字符串
     */
    public static String getNextAvailableFilename(final String sFileNameStart,
                                                  final String sFileNameEnd) {
        for (int i = 0; i <= MAX_NUM; ++i) {
            String sFileName = sFileNameStart + i + sFileNameEnd;
            File f = new File(sFileName);
            if (!f.isFile()) {
                return sFileName;
            }
        }

        return "";
    }

    /**
     * 删除文件扩展名.
     *
     * @param sFilename 文件名
     * @return 不带后缀的文件名
     */
    public static String sChopFileNameExtension(final String sFilename) {
        String filename = sFilename;
        if (filename != null && !filename.equals("")) {
            int iLastDotPos = filename.lastIndexOf(".");
            if (iLastDotPos > 0) {
                filename = filename.substring(0, iLastDotPos);
            }
        }

        return filename;
    }
}
