package RainbowSix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static java.lang.System.err;
import static java.lang.System.out;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class MainController {

	@Autowired
	private RestTemplate restTemplate;
	@GetMapping("/controller/test")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public String getSentiResponse() {
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String s = "This is Controller, Current time: ";
		return s + dateTime.format(formatter);
	}

	@PostMapping("/controller/text")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public String getSentiTextResponse(HttpServletRequest request){
		String url = request.getRequestURL().toString();
		url = url.replace("8082","8081");
		url = url.replace("controller","senti-strength");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType((MediaType.parseMediaType(request.getContentType())));
		MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
		request.getParameterMap().forEach((key, value) -> map.put(key, Arrays.stream(value).toList()));
		// 组装请求体
		HttpEntity<MultiValueMap<String, String>> req =
				new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(
				url,req, String.class);
		out.println(response.getBody());
		return response.getBody();
	}
	@PostMapping(value= "/controller/file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@CrossOrigin(origins = "*", maxAge = 3600)
	public String getSentiFileResponse(HttpServletRequest request,MultipartFile file) throws IOException {
		String url = request.getRequestURL().toString();
		url = url.replace("8082","8081");
		url = url.replace("controller","senti-strength");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType((MediaType.parseMediaType(request.getContentType())));
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
		try {
			parts.add("file",request.getPart("file") );  // 添加文件
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
		parts.add("para", request.getParameter("para"));  // 添加字符串
		parts.forEach((key,value)->err.println(key+": "+ value.get(0)));
		// 组装请求体
		HttpEntity<MultiValueMap<String, Object>> req =
				new HttpEntity<>(parts,headers);
		ResponseEntity<String> response = restTemplate.postForEntity(
				url,req, String.class);
		out.println(response.getBody());
		return response.getBody();
	}


	@PostMapping(value = "/controller/fileFolder")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public String[] getSentiFileFolderResponse(HttpServletRequest request) throws IOException{
		String url = request.getRequestURL().toString();
		url = url.replace("8082","8081");
		url = url.replace("controller","senti-strength");
		ResponseEntity<String[]> response = restTemplate.postForEntity(
				url, request,String[].class);
		out.println(Arrays.toString(response.getBody()));
		return response.getBody();
	}


	@GetMapping("/controller/downloadFile")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Object downloadFile(HttpServletRequest request) throws IOException{
		String url = request.getRequestURL().toString();
		url = url.replace("8082","8081");
		url = url.replace("controller","senti-strength");
		ResponseEntity<Object> response = restTemplate.getForEntity(
				url, Object.class);
		out.println(response.getBody());
		return response.getBody();
	}

	@GetMapping("/controller/translate")
	@CrossOrigin
	public String translate(String url){
		ResponseEntity<String> response = restTemplate.getForEntity(url,String.class);
		return response.getBody();
	}
}