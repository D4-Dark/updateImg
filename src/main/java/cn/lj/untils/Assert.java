package cn.lj.untils;
/**
 * 断言验证
 * @author Administrator
 *
 */
public class Assert {
	private Assert() {}
		//判断是否
		public static void isValid(boolean valid,String message) {
			if(!valid)
				throw new IllegalArgumentException(message);
		}
		//判断对象为空
		public static void isNull(Object object,String message) {
			if(object==null) {
				throw new IllegalArgumentException(message);
			}
		}
		//判断字符串为空
		public static void isEmpty(String object,String message) {
			if(object==null||"".equals(object.trim())) {
				throw new IllegalArgumentException(message);
			}
		}
		//判断数组为空
		public static void isEmpty(Integer[]array,String message) {
			if(array==null||array.length==0) {
				throw new IllegalArgumentException(message);
			}
		}
		//判断为默认
		public static void isDefault (String defaultString,String object,String message) {
			if(object==defaultString) {
				throw new IllegalArgumentException(message);
			}
		}
}
