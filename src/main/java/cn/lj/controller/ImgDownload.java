package cn.lj.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.lj.pojo.Entity;
import cn.lj.pojo.request;
import cn.lj.service.ImgUpdateService;

@RestController
@RequestMapping("/img")
public class ImgDownload {

	@Autowired
	ImgUpdateService imgUpload;
	/**
	 * 传到本地方法
	 * @param img
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/updataImg", method = RequestMethod.POST)
	@ResponseBody
    public request updataImg(@RequestParam("file")MultipartFile img,HttpServletRequest request){
		
    	 
		Entity entity = new Entity();
		entity.setCarId(request.getParameter("carId"));
		entity.setShipId(request.getParameter("shipId"));
		entity.setMatId(request.getParameter("matId"));
		entity.setDate(request.getParameter("date"));
		entity.setType1(request.getParameter("type1"));
		entity.setType2(request.getParameter("type2"));
		//MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        //对应前端的upload的name参数"file"
        //MultipartFile file = req.getFile("file");
		System.out.println(img.toString());
		String returnMsg;
		try {
			returnMsg = imgUpload.ImgUpdate(img, entity);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return new request("上传失败",500,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new request("上传失败",500,null);
		}
    	
    	return new request("上传成功",200,null);
    }
	
	/**
	 * 传到服务器方法
	 * @param img
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/updataImgSFTP", method = RequestMethod.POST)
	@ResponseBody
	public request updataImgSFTP(@RequestParam("file")MultipartFile img,HttpServletRequest request){
		
		Entity entity = new Entity();
		entity.setCarId(request.getParameter("carId"));
		entity.setShipId(request.getParameter("shipId"));
		entity.setMatId(request.getParameter("matId"));
		entity.setDate(request.getParameter("date"));
		entity.setType1(request.getParameter("type1"));
		entity.setType2(request.getParameter("type2"));
		
		try {
			imgUpload.SFTPUpload(img,entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new request("上传成功",200,null);
	}
}
