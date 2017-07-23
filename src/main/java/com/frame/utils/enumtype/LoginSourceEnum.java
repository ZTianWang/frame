package com.frame.utils.enumtype;

/**
 * 登录来源枚举类 
 * 0:pc网页登录 
 * 1:安卓登录 
 * 2:ios登录 
 * 3:微信登录 
 * 4:wap登录
 */
public enum LoginSourceEnum {

	PC(1, "电脑端登录"), 
	ANDROID(2, "安卓端登录"),
	IOS(3, "苹果端登录"),
	WECHAT(4, "微信平台登录"), 
	WAP(5, "wap网页登录");

	private int code;// 编码
	private String desc;// 描述

	private LoginSourceEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int code() {
		return code;
	}

	 

	public String desc() {
		return desc;
	}

	 
}
