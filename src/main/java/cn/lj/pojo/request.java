package cn.lj.pojo;

import java.io.Serializable;

import lombok.Data;

@Data
public class request implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String msg;
	private Integer status;
	private Object obj;
	/**
	 * 成功构造方法
	 * @param obj
	 */
	public request(Object obj) {
		super();
		this.msg = "OK";
		this.status = 200;
		this.obj = obj;
	}
	/**
	 * 失败构造方法
	 * @param msg
	 */
	public request(String msg) {
		super();
		this.msg = msg;
		this.status = 500;
		//this.obj = obj;
	}
	public request(String msg, Integer status, Object obj) {
		super();
		this.msg = msg;
		this.status = status;
		this.obj = obj;
	}
	
	

}
