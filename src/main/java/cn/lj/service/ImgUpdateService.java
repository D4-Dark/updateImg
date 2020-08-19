package cn.lj.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import cn.lj.pojo.Entity;

public interface ImgUpdateService {

	/**
	 * 图片本地上传
	 * @param entity
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public String ImgUpdate(MultipartFile image,Entity entity) throws IllegalStateException, IOException;
	
	/**
	 * 图片服务器上传
	 * @param img
	 * @param entity
	 */
	public void SFTPUpload(MultipartFile img, Entity entity) throws Exception ; 

	public void FTPUpload(MultipartFile img,Entity entity) throws Exception;
	
}
