package cn.lj.service;

import java.io.IOException;

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
	
}
