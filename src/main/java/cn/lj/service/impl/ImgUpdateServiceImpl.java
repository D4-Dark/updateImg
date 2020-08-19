package cn.lj.service.impl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;


import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
 


import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import cn.lj.pojo.Entity;
import cn.lj.service.ImgUpdateService;
import cn.lj.untils.Assert;

@Service
public class ImgUpdateServiceImpl implements ImgUpdateService{
	
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ImgUpdateServiceImpl.class);


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
		String name = file.getContentType();
		//截取后缀
		
		String Endname = name.substring(5+1, name.length());
		
		//System.out.println(Endname);
		
		/**
		 * 判断: 哪个有值用哪个id
		 * 设定文件名称和文件夹名称
		 * 
		 * */
		if(!(entity.getCarId().equals(""))) {
			fileName = entity.getCarId()+"."+Endname;
			filePath=getPath(entity.getType1(),entity.getType2(),entity.getDate(),entity.getCarId());
		}else if(!(entity.getMatId().equals(""))) {
			fileName = entity.getMatId()+"."+Endname;
			filePath=getPath(entity.getType1(),entity.getType2(),entity.getDate(),entity.getMatId());
		}else if(!(entity.getShipId().equals(""))) {
			fileName = entity.getShipId()+"."+Endname;
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
		log.info("保存成功");
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
			filePath = "D://WorkImage//"+name+"//"+type1+"//"+type2;
		}else {
			filePath = "D://WorkImage//"+name+"//"+date.replace(":","_")+"//"+type1+"//"+type2;
		}
		return filePath;
	}
	/**
	 * ftp设定文件夹名称
	 * @param type1 目录1
	 * @param type2 目录2
	 * @param date 日期
	 * @param name id
	 * @return
	 */
	private String getFtpPath(String type1,String type2,String date,String name) {
		String filePath = null;
		if(date==""||date==null) {
//			filePath = "/"+name+"/"+type1+"/"+type2;
			filePath = "/WorkImage/"+name+"/"+type1+"/"+type2;
		}else {
//			filePath = "/"+name+"/"+date.replace(":","_")+"/"+type1+"/"+type2;
			filePath = "/WorkImage/"+name+"/"+date.replace(":","_")+type1+"/"+type2;
		}
		return filePath;
	}
	/*开启连接*/
	public ChannelSftp connect(String host, int port, String username,
			String password) {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
		} catch (Exception e) {
 
		}
		return sftp;
	}

	/**
	 *  上传方法
	 * @param directory
	 * @param uploadFile
	 * @param sftp
	 */
	public void upload(String fileName, File file, ChannelSftp sftp) {
		try {
			sftp.cd("D:\\123");  //控制文件保存路径
			sftp.put(new FileInputStream(file), file.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	 
    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {
 
        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }
    
    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 删除本地临时文件
     * @param file
     */
    public static void delteTempFile(File file) {
    if (file != null) {
        File del = new File(file.toURI());
        del.delete();
    }
}

	@Override
	public void SFTPUpload(MultipartFile img, Entity entity) throws Exception {
		/*
		 * 解析文件地址
		 * */
		Assert.isNull(entity,"数据发送失败");
		Assert.isEmpty(entity.getType1(), "目录1不能为空,请选择目录");
		Assert.isEmpty(entity.getType2(), "目录2不能为空,请选择目录");
		Assert.isDefault("选择类型", entity.getType1(), "请选择目录1");
		Assert.isDefault("选择类型", entity.getType2(), "请选择目录2");
		
		String fileName = null;
		
		String name = img.getContentType();
		//截取后缀
		
		String Endname = name.substring(5+1, name.length());
		
		//System.out.println(Endname);
		
		/**
		 * 判断: 哪个有值用哪个id
		 * 设定文件名称和文件夹名称
		 * 
		 * */
		if(!(entity.getCarId().equals(""))) {
			fileName = entity.getCarId()+"."+Endname;
		}else if(!(entity.getMatId().equals(""))) {
			fileName = entity.getMatId()+"."+Endname;
		}else if(!(entity.getShipId().equals(""))) {
			fileName = entity.getShipId()+"."+Endname;
		}
		
		Assert.isNull(img,"图片接受失败");
		File multipartFileToFile = multipartFileToFile(img);
		/**
		 *  IP地址 端口号 用户名 密码
		 */
		upload(fileName,multipartFileToFile,connect("10.88.252.231",3380,"Administrator",
				"***123abc"));
		log.info("上传成功");
		
	}
	@Override
	public void FTPUpload(MultipartFile img, Entity entity) throws Exception {
		String fileName = null;
		String filePath = null;
		String name = img.getContentType();
		//截取后缀
		String Endname = name.substring(5+1, name.length());
//		System.out.println(Endname);
		/**
		 * 判断: 哪个有值用哪个id
		 * 设定文件名称和文件夹名称
		 * 
		 * */
		if(!(entity.getCarId().equals(""))) {
			fileName = entity.getCarId()+"."+Endname;
			filePath=getFtpPath(entity.getType1(),entity.getType2(),entity.getDate(),entity.getCarId());
		}else if(!(entity.getMatId().equals(""))) {
			fileName = entity.getMatId()+"."+Endname;
			filePath=getFtpPath(entity.getType1(),entity.getType2(),entity.getDate(),entity.getMatId());
		}else if(!(entity.getShipId().equals(""))) {
			fileName = entity.getShipId()+"."+Endname;
			filePath=getFtpPath(entity.getType1(),entity.getType2(),entity.getDate(),entity.getShipId());
		}
		
		File multipartFileToFile = multipartFileToFile(img);
		
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("utf-8");
		ftpClient.setConnectTimeout(10*1000);
        FileInputStream fis = null;
        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
        conf.setServerLanguageCode("zh");  
        try {
        	
            ftpClient.connect("10.88.252.231");
            ftpClient.login("administrator", "***123abc");
            
            int reply = ftpClient.getReplyCode();  
            
            // 以2开头的返回值就会为真  
           
            if (!FTPReply.isPositiveCompletion(reply)) {  
           
             ftpClient.disconnect();  
           
             System.out.println("连接服务器失败");  
           
                 return;  
           
            }  
            System.out.println("登陆服务器成功"); 
//            File srcFile = new File("D:\\bg.jpg");
            fis = new FileInputStream(multipartFileToFile);
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            ftpClient.sendCommand("OPTS UTF8", "ON");
//            ftpClient.makeDirectory(new String(filePath.getBytes("GBK"),"iso-8859-1"));
//            name1=new String(name1.getBytes("GBK"),"iso-8859-1");
            if (!ftpClient.changeWorkingDirectory(filePath)) {
                //如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath ="";
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) continue;
                    tempPath += "/" + dir;
                    if (!ftpClient.changeWorkingDirectory(tempPath)) {
                        if (!ftpClient.makeDirectory(tempPath)) {
                            return ;
                        } else {
                        	ftpClient.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            
            ftpClient.storeFile(fileName, fis);
           
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fis);
        	try {
				fis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        } 
		
	}	

	
}
