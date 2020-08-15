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
		
		/*
		 * 提问: 用户如果传了多个名称要怎么办?  比如matId 和 carId 都有值 ?
		 * */
		
		/**
		 * 判断: 哪个有值用哪个id
		 * 设定文件名称和文件夹名称
		 * 
		 * */
		if(!(entity.getCarId().equals(""))) {
			fileName = entity.getCarId()+entity.getType1()+entity.getType2()+".jpg";
			filePath=getPath(entity.getType1(),entity.getType2(),entity.getDate(),entity.getCarId());
		}else if(!(entity.getMatId().equals(""))) {
			fileName = entity.getMatId()+entity.getType1()+entity.getType2()+".jpg";
			filePath=getPath(entity.getType1(),entity.getType2(),entity.getDate(),entity.getMatId());
		}else if(!(entity.getShipId().equals(""))) {
			fileName = entity.getShipId()+entity.getType1()+entity.getType2()+".jpg";
			filePath=getPath(entity.getType1(),entity.getType2(),entity.getDate(),entity.getShipId());
		}
		
		Assert.isNull(file,"图片接受失败");
		/*
		 * 创建文件
		 * */
		File imagePath = new File(filePath);
		File localFile = new File(filePath+"//"+fileName);
		if (!imagePath.exists()) {
            imagePath.mkdirs();
        }
		file.transferTo(localFile);
		
		/**
		 * 返回保存路径
		 */
		return filePath+"//"+fileName;
	}

	/**
	 * 设定文件夹名称
	 * @param type1 目录1
	 * @param type2 目录2
	 * @param date 日期
	 * @param name id
	 * @return
	 */
	private String getPath(String type1,String type2,String date,String name) {
		String filePath = null;
		if(date==""||date==null) {
			filePath = "D://WorkImage//"+type1+"//"+type2+"//"+name;
		}else {
			filePath = "D://WorkImage//"+type1+"//"+type2+"//"+date.replace(":","_")+"//"+name;
		}
		return filePath;
	}
	
}
