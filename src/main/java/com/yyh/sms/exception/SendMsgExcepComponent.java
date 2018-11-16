/*
 * Project Name:mms-aop 
 * File Name: DefaultExcepComponent.java 
 * Package Name:com.yaoyaohao.mms.exceptions
 * Date:2015-9-17下午4:39:33 
 * Copyright (c) 2015, xiongps All Rights Reserved. 
 * 药药好（杭州）网络科技有限公司
*/  

package com.yyh.sms.exception;


/**
 *
 * @author xiongps
 * @version 2017-12-22 上午11:31:25
 * @since JDK1.7
 */
public enum SendMsgExcepComponent {

	//公共可预见性错误码
	SEND_SMS_ERROR("M1000","发送短信时出现错误","发送内容不能为空"),
	SEND_RECEIVER_NOT_EMPTY("M1001","接收人不能为空","接收人不能为空"),
	SEND_CONTENT_NOT_EMPTY("M1002","发送内容不能为空","发送内容不能为空"),
	SEND_RECEIVER_CONTENT_NOT_EMPTY("M1003","接收人与发送内容不能为空","接收人与发送内容不能为空"),
	CURR_NOT_SUPPORT_FREEMARKER("M1004","暂时不支持FREEMARKER格式","暂时不支持FREEMARKER格式"),
	TEMPLATE_TYPE_NOT_NULL("M1005","模板类型TemplateType不能为空","模板类型TemplateType不能为空"),
	SEND_TEMPLATE_CODE_NOT_EMPTY("M1006","模板code不能为空","发送内容不能为空");

	private String code;
	private String message;
	private String description;

	private SendMsgExcepComponent(String message) {
		this.code = "0000";
		this.message = message;
	}
	private SendMsgExcepComponent(String code, String message) {
		this.code = code;
		this.message = message;
		this.description = description;
	}
	private SendMsgExcepComponent(String code, String message, String description) {
		this.code = code;
		this.message = message;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
