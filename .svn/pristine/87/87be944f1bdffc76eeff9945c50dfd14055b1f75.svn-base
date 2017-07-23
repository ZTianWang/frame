package com.frame.utils;

public class StringUtil {
	   public static final char UNDERLINE='_';
	   /**
	    * 转换字符串为sql语句时使用此方法
	    * @param param
	    * @return
	    */
	    public static String camel2underline(String param){
	        int len=param.length();
	        StringBuilder sb=new StringBuilder(len);
	        boolean flag=true;
	        for (int i = 0; i < len; i++) {
	            char c=param.charAt(i);
	            if(c=='{')flag=false;else if(c=='}')flag=true;
	            	  if (flag&&Character.isUpperCase(c)){
	  	                sb.append(UNDERLINE);
	  	                sb.append(Character.toLowerCase(c));
	  	            }else{
	  	                sb.append(c);
	  	            }
	          
	          
	        }
	        return sb.toString();
	    }
	    
	    /**
	     * 判断字符串是否为空
	     * @param str
	     * @return
	     */
	    public static  boolean isEmpty(String str){
	    	if(str==null||str.trim().equals("")){
	    		return true;
	    	}
	    	return false;
	    }
	    
	    /**
	     * 判断字符串是否不为空
	     * @param str
	     * @return
	     */
	    public static  boolean isNotEmpty(String str){
	    	 return !isEmpty(str);
	    }
	    public  static void main(String args[]){
	    	//System.out.println(StringUtil.isNotEmpty("11"));
	    	System.out.println(StringUtil.camel2underline("userName+password"));
	    }
	    /**
	     * 是否手机号
	     * @param cellPhone  手机号
	     * @return
	     */
	    public static boolean isCellPhone(String cellPhone){
	    	  return cellPhone.matches("^1[3|4|5|7|8][0-9]{9}$");
	    }
	    /**
	     * 是否邮箱
	     * @param email  邮箱
	     * @return
	     */
	    public static boolean isEmail(String email){
	    	  return email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
	    }
}
