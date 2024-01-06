package rainbowsix.service;


import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public interface SentiService {
    /**
     * 分析文本.
     * @param text 文本
     * @param para 参数
     * @return 分析结果
     */
    String processText(String text, String para);

    /**
     * 分析文件.
     * @param file 文件
     * @param para 参数
     * @param request http请求
     * @return 结果文件的文件名
     */
    String processFile(MultipartFile file, String para, HttpServletRequest request) throws IOException;

    /**
     * 分析文件夹
     * @param files 文件夹内的文件列表.
     * @param para 参数
     * @param request http请求
     * @return 结果文件的文件名列表
     */
    String[] processFileFolder(MultipartFile[] files,String para, HttpServletRequest request) throws IOException;

    /**
     * 下载对应文件名的文件.
     * @param response http响应
     * @param request http请求
     * @param filePath 文件名
     * @return null
     */
    Object downloadFileByName(HttpServletResponse response, HttpServletRequest request, String filePath) throws IOException;



}
