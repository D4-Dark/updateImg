package cn.lj.controller;

import java.io.IOException;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.lj.pojo.Entity;
import cn.lj.pojo.request;
import cn.lj.service.ImgUpdateService;

@RestController
@RequestMapping("/img")
public class ImgDownload {

	@Autowired
	ImgUpdateService imgUpload;
	
	@RequestMapping(name ="/updataImg", method = RequestMethod.POST)
	@ResponseBody
    public request updataImg(@RequestParam("file")MultipartFile image,@RequestParam("entity") Entity entity){
    	
		String returnMsg;
		try {
			returnMsg = imgUpload.ImgUpdate(image, entity);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return new request("上传失败",500,null);
		}
    	
    	return new request("上传成功",200,returnMsg);
    }
}
