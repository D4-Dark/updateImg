package cn.lj.service.impl;
import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.lj.pojo.Entity;
import cn.lj.service.ImgUpdateService;
import cn.lj.untils.Assert;

@Service
public class ImgUpdateServiceImpl implements ImgUpdateService{

	@Override
	public String ImgUpdate(MultipartFile file,Entity entity) throws IllegalStateException, IOException {
		
		/*
		 * 解析文件地址
		 * */
		Assert.isNull(entity,"数据发送失败");
		Assert.isEmpty(entity.getType1(), "目录1不能为空,请选择目录");
		Assert.isEmpty(entity.getType2(), "目录2不能为空,请选择目录");
		Assert.isDefault("选择类型", entity.getType1(), "请选择目录1");
		Assert.isDefault("选择类型", entity.getType2(), "请选择目录2");
		
		String fileName = null;
		String filePath = null;
		
		
		if(""!=entity.getCarId()) {
			fileName = entity.getCarId()+entity.getDate()+entity.getType1()+entity.getType2()+".jpg";
			filePath = "D://"+entity.getCarId()+"//"+entity.getDate()+"//"+entity.getType1()+"//"+entity.getType2();
		}else if(""!=entity.getMatId()) {
			fileName = entity.getMatId()+entity.getDate()+entity.getType1()+entity.getType2()+".jpg";
			filePath = "D://"+entity.getMatId()+"//"+entity.getDate()+"//"+entity.getType1()+"//"+entity.getType2();
		}else if(""!=entity.getShipId()) {
			fileName = entity.getShipId()+entity.getDate()+entity.getType1()+entity.getType2()+".jpg";
			filePath = "D://"+entity.getShipId()+"//"+entity.getDate()+"//"+entity.getType1()+"//"+entity.getType2();
		}
		
		Assert.isNull(file,"图片接受失败");
		
		File localFile = new File(fileName);
		File imagePath = new File(filePath);
		if (!imagePath.exists()) {
            imagePath.mkdirs();
        }
			file.transferTo(localFile);
		
		
		return filePath+"//"+fileName;
	}

	
	
	
}
