package rainbowsix.backend;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

@RestController
@RequestMapping("/api")
public class SentiController {

	@PostMapping("/text")
	@CrossOrigin
	public String processText(@RequestParam("text") String text, @RequestParam("para") String para, HttpServletRequest request) throws IOException {
		out.println(text);

		// 解析参数。
		String[] basic = {"java", "-jar", "senti/R6_senti-0.2_phase1_Done.jar", "sentidata", "senti/dict/"};
		ArrayList<String> list = new ArrayList<>();
		Collections.addAll(list, basic);
		String[] optionList = para.split("\t");
		Collections.addAll(list, optionList);
		int position = list.indexOf("text");
		list.add(position+1, text );
		list.add("overwrite");
		int i = list.indexOf("resultsExtension");
		String extension = "_out";
		if(i != -1){
			extension += list.get(i+1);
			list.set(i+1, extension+".txt");
		}
		String[] paras = list.toArray(new String[list.size()]);
		out.println("*************************"+ Arrays.toString(paras));
		// 调用外部jar包
		ProcessBuilder builder = new ProcessBuilder(paras);
		builder.directory(new File("."));
		builder.redirectErrorStream(true);
		Process process = builder.start();
		InputStream is = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		// 读取外部jar包输出并返回
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	@PostMapping("/fileApi")
	@CrossOrigin
	public String processFile(@RequestParam("file") MultipartFile file, @RequestParam("para") String para, HttpServletRequest request) throws IOException {
		// 先将文件保存到服务器，获得本地上的文件路径。
		String path = request.getServletContext().getRealPath("/upload");
		String filename_r =  saveFile(file, path, "");
		String filename = path + File.separator+filename_r;
		out.println("*******************************"+para);

		// 解析参数。
		String[] basic = {"java", "-jar", "senti/R6_senti-0.2_phase1_Done.jar", "sentidata", "senti/dict/"};
		ArrayList<String> list = new ArrayList<>();
		Collections.addAll(list, basic);
		String[] optionList = para.split("\t");
		Collections.addAll(list, optionList);
		int position = list.indexOf("input");
		list.add(position+1, filename );
		list.add("overwrite");
		int i = list.indexOf("resultsExtension");
		String extension = "_out";
		if(i != -1){
			extension = list.get(i+1);
			list.set(i+1, extension+".txt");
		}
		String[] paras = list.toArray(new String[list.size()]);

		out.println("**************************"+Arrays.toString(paras));

		ProcessBuilder builder = new ProcessBuilder(paras);
		builder.directory(new File("."));
		builder.redirectErrorStream(true);
		Process process = builder.start();

		InputStream is = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		// 读取外部jar包输出并返回
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		saveResult(path, "result"+filename_r ,sb.toString());

		String result = filename_r.substring(0, filename_r.indexOf("."))+"0"+extension+filename_r.substring(filename_r.indexOf("."));
		out.println("***************************************"+result);
		File testFile = new File(path+ File.separator + result);
		if(! testFile.exists()){
			result = filename_r;
		}
		// 返回结果的文件名。
		return result;

	}

	@PostMapping(value = "/folderApi")
	@CrossOrigin
	public String[] processFileFolder(@RequestParam("files") MultipartFile[] files,@RequestParam("para") String para, HttpServletRequest request) throws IOException{
		String path = request.getServletContext().getRealPath("/upload");
		FIleFolder folder = saveFolder(files, request);
		out.println(folder.getVirtualPath());

		String[] basic = {"java", "-jar", "senti/R6_senti-0.2_phase1_Done.jar", "sentidata", "senti/dict/"};
		ArrayList<String> list = new ArrayList<>();
		Collections.addAll(list, basic);
		String[] optionList = para.split("\t");
		Collections.addAll(list, optionList);
		int position = list.indexOf("inputFolder");
		list.add(position+1, folder.getVirtualPath() );
		list.add("overwrite");
		int i = list.indexOf("resultsExtension");
		String extension = "_out";
		if(i != -1){
			extension = list.get(i+1);
			list.set(i+1, extension+".txt");
		}
		String[] paras = list.toArray(new String[list.size()]);

		out.println("************************"+ Arrays.toString(paras));

		ProcessBuilder builder = new ProcessBuilder(paras);
		builder.directory(new File("."));
		builder.redirectErrorStream(true);
		Process process = builder.start();
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

	@GetMapping("/downloadFile")
	@CrossOrigin
	public Object downloadFile(HttpServletResponse response, HttpServletRequest request, String filePath) throws IOException {
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

	public String saveResult(String path, String fileName, String re) throws IOException {
			File testFile = new File(path+ File.separator + fileName);
			File fileParent = testFile.getParentFile();//返回的是File类型,可以调用exsit()等方法
			String fileParentPath = testFile.getParent();//返回的是String类型
			if (!fileParent.exists()) {
				fileParent.mkdirs();// 能创建多级目录
			}
			if (!testFile.exists())
				testFile.createNewFile();//有路径才能创建文件

			FileWriter writer = new FileWriter(testFile);
			writer.write(re);
			writer.flush();
			writer.close();
			out.println("-------------success");;
			return fileName;
		}



	private String saveFile(MultipartFile file, String path, String folder) throws IOException {
		String fileName = file.getOriginalFilename();

		String localDir;
		localDir = (folder.equals(""))?(path+folder):(path+File.separator +folder);
		out.println(localDir);
		File dirFile = new File(localDir);
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}

		fileName = fileName.replaceAll("\\\\|/", "-");

		String uuid = UUID.randomUUID().toString()
				.replace("-", "");

		String realFileName = uuid + fileName;

		System.out.println("\n-------------------"+realFileName);

		String filePathAll = localDir + File.separator+realFileName;

		System.out.println("\n-------------------"+filePathAll);
		File realFile = new File(filePathAll);

		file.transferTo(realFile);
		out.println(folder+realFileName);
		return (folder.equals(""))?(folder +realFileName):(folder + File.separator+realFileName);
	}

	public FIleFolder saveFolder(MultipartFile[] files,HttpServletRequest request ) throws IOException {
		FIleFolder fIleFolder = new FIleFolder();
		String path = request.getServletContext().getRealPath("/upload");
		String[] virtualPaths = new String[files.length];

		String fileName = files[0].getOriginalFilename();
		String pattern = "\\\\|/";
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(fileName);
		boolean io = matcher.find();
		out.println(fileName);
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
}