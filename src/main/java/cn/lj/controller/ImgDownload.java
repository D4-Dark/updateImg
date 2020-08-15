package cn.lj.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@RequestMapping(value ="/updataImg", method = RequestMethod.POST)
	@ResponseBody
    public request updataImg(@PathVariable("file")MultipartFile image,HttpServletRequest request){
		
    	 
		Entity entity = new Entity();
		entity.setCarId(request.getParameter("carId"));
		entity.setShipId(request.getParameter("shipId"));
		entity.setMatId(request.getParameter("matId"));
		entity.setDate(request.getParameter("date"));
		entity.setType1(request.getParameter("type1"));
		entity.setType2(request.getParameter("type2"));
		
		
		String returnMsg;
		try {
			returnMsg = imgUpload.ImgUpdate(image, entity);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return new request("上传失败",500,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new request("上传失败",500,null);
		}
    	
    	return new request("上传成功",200,returnMsg);
    }
}
