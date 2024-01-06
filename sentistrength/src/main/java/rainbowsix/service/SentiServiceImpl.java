package rainbowsix.service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rainbowsix.pojo.FIleFolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

@Service
public class SentiServiceImpl implements SentiService{
    @Override
    public String processText(String text, String para) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // 解析参数。
        ArrayList<String> list = parseParas(para, text, "text");
        String[] paras = list.toArray(new String[list.size()]);

        // 运行
        rainbowsix.ss.SentiStrength.main(paras);

        return outContent.toString().replaceAll("\\s+$", "");
    }

    @Override
    public String processFile(MultipartFile file, String para, HttpServletRequest request) throws IOException {
        // 将文件保存到本地.
        String path = request.getServletContext().getRealPath("/upload");
        String filename_r = saveFile(file, path, "");
        String filename = path + File.separator+filename_r;

        // 解析参数。
        ArrayList<String> list = parseParas(para, filename, "input");
        String extension = getExtension(list);
        String[] paras = list.toArray(new String[list.size()]);
//        System.out.println("************************************"+para);
//        System.out.println("**********************************"+ Arrays.toString(paras));

        // 运行
        rainbowsix.ss.SentiStrength.main(paras);

        // 返回结果
        String result = filename_r.substring(0, filename_r.indexOf("."))+"0"+extension+filename_r.substring(filename_r.indexOf("."));
        File testFile = new File(path+ File.separator + result);
        if(! testFile.exists()){
            result = filename_r;
        }
        // 返回结果的文件名。
        return result;
    }

    @Override
    public String[] processFileFolder(MultipartFile[] files, String para, HttpServletRequest request) throws IOException {
        // 将文件夹保存到本地。
        String path = request.getServletContext().getRealPath("/upload");
        FIleFolder folder = saveFolder(files, request);

        // 解析参数。
        ArrayList<String> list = parseParas(para,folder.getVirtualPath(), "inputFolder");
        String extension = getExtension(list);
        String[] paras = list.toArray(new String[list.size()]);

        // 运行
        rainbowsix.ss.SentiStrength.main(paras);

        // 返回结果
        String[] names = folder.getFileNames();
        String filename_r = names[0];
        String result = filename_r.substring(0, filename_r.indexOf("."))+extension+filename_r.substring(filename_r.indexOf("."));
        File testFile = new File(path+ File.separator + result);
        if(testFile.exists()){
            for (int j = 0; j < names.length; j++) {
                names[j] = names[j].substring(0, names[j].indexOf("."))+extension+names[j].substring(names[j].indexOf("."));
            }
        }
        return names;
    }

    @Override
    public Object downloadFileByName(HttpServletResponse response, HttpServletRequest request, String filePath) throws IOException {
        // 清空输出流
        String path = request.getServletContext().getRealPath("/upload");
        response.reset();
        response.setContentType("application/x-download;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String(filePath.getBytes("utf-8"), "utf-8"));


        String downPath = path+File.separator+filePath;
        //读取目标文件
        File f = new File(downPath);
        //创建输入流
        InputStream is = new FileInputStream(f);
        //做一些业务判断，我这里简单点直接输出，你也可以封装到实体类返回具体信息
        if (is == null) {
            System.out.println("文件不存在");
        }
        //利用IOUtils将输入流的内容 复制到输出流
        //org.apache.tomcat.util.http.fileupload.IOUtils
        //项目搭建是自动集成了这个类 直接使用即可
        OutputStream os = response.getOutputStream();
        IOUtils.copy(is, os);
        os.flush();
        is.close();
        os.close();
        return null;
    }


    private String saveFile(MultipartFile file, String path, String folder) throws IOException {
        String fileName = file.getOriginalFilename();

        String localDir;
        localDir = (folder.equals(""))?(path+folder):(path+ File.separator +folder);
        File dirFile = new File(localDir);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }

        fileName = fileName.replaceAll("\\\\|/", "-");

        String uuid = UUID.randomUUID().toString()
                .replace("-", "");

        String realFileName = uuid + fileName;


        String filePathAll = localDir + File.separator+realFileName;

        File realFile = new File(filePathAll);

        file.transferTo(realFile);
        return (folder.equals(""))?(folder +realFileName):(folder + File.separator+realFileName);
    }

    private FIleFolder saveFolder(MultipartFile[] files, HttpServletRequest request ) throws IOException {
        FIleFolder fIleFolder = new FIleFolder();
        String path = request.getServletContext().getRealPath("/upload");
        String[] virtualPaths = new String[files.length];

        String fileName = files[0].getOriginalFilename();
        String pattern = "\\\\|/";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(fileName);
        boolean io = matcher.find();

        String folder="";
        if(io) {
            folder = fileName.substring(0,matcher.start())+UUID.randomUUID().toString().replace("-", "");
        } else {
            folder = UUID.randomUUID().toString().replace("-", "");
        }

        int i=0;
        for (MultipartFile file:files){
            virtualPaths[i] = saveFile(file,path,folder);
            i++;
        }
        fIleFolder.setFileNames(virtualPaths);
        fIleFolder.setVirtualPath(path + File.separator+folder);
        return fIleFolder;
    }

    private ArrayList<String> parseParas(String para, String orient, String type){

        String[] basic = {"sentidata", "./src/main/dict/"};
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, basic);
        String[] optionList = para.split("\t");
        Collections.addAll(list, optionList);
        int position = list.indexOf(type);
        list.add(position+1, orient );
        list.add("overwrite");
        return list;
    }

    private String getExtension(ArrayList<String> list){
        int i = list.indexOf("resultsExtension");
        String extension = "_out";
        if(i != -1){
            extension = list.get(i+1);
            list.set(i+1, extension+".txt");
        }
        return extension;
    }
}
