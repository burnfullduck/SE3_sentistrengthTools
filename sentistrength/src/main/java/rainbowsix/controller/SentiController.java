package rainbowsix.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rainbowsix.service.SentiService;

import java.io.*;

@RestController
@CrossOrigin(origins = "*")
public class SentiController {
	@Autowired
	SentiService sentiService;

	@GetMapping("/senti-strength/test")
	public String getSentiResponse(@RequestParam(value = "input", defaultValue = "i+do+not+love+you") String input) {
		input = "sentidata ./src/main/dict/ text " + input + " noNegators explain";
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		rainbowsix.ss.SentiStrength.main(input.split(" "));
		return outContent.toString().replaceAll("\\s+$", "");
	}

	@PostMapping("/senti-strength/text")
	public String getSentiTextResponse(@RequestParam("text") String text, @RequestParam("para") String para){
		return sentiService.processText(text,para);
	}
	@PostMapping("/senti-strength/file")
	public String getSentiFileResponse(@RequestParam("file") MultipartFile file, @RequestParam("para") String para, HttpServletRequest request) throws IOException{
		return sentiService.processFile(file, para, request);
	}


	@PostMapping(value = "/senti-strength/fileFolder")
	public String[] getSentiFileFolderResponse(@RequestParam("files") MultipartFile[] files,@RequestParam("para") String para, HttpServletRequest request) throws IOException{
		return sentiService.processFileFolder(files,para,request);
	}


	@GetMapping("/senti-strength/downloadFile")
	public Object downloadFile(HttpServletResponse response, HttpServletRequest request, String filePath) throws IOException{
		return sentiService.downloadFileByName(response,request,filePath);
	}

}