package cn.lj.pojo;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.experimental.Accessors;
/**
 * 
 * @author Administrator
 *
 */
@Data
@Accessors(chain = true)
public class Entity implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -3221973313826823558L;
	
	
	/**
	 * 
	 */
	private String shipId; //船号
	private String matId; //钢卷号
	private String carId; //车号
	private String date; //日期
	private String type1; //地址1
	private String type2; //地址2
}
