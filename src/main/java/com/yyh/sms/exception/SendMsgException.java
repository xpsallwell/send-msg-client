package com.yyh.sms.exception;

public class SendMsgException extends RuntimeException {

	private static final long serialVersionUID = 1985609894L;

	private String code;

	private String message;

	private SendMsgExcepComponent sendMsgExcepComponent;

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

	public SendMsgException(String message){
		this.message = message;
	}

	public SendMsgException(SendMsgExcepComponent enumException){
		this.sendMsgExcepComponent = enumException;
		this.code = enumException.getCode();
		this.message = enumException.getMessage();
	}
	public SendMsgException(SendMsgExcepComponent enumException,Throwable cause){
		super(enumException.getMessage(),cause);
		this.sendMsgExcepComponent = enumException;
		this.code = enumException.getCode();
		this.message = enumException.getMessage();

	}
	public SendMsgException(String code, String message){
		 super(message);
		 this.code = code;
		 this.message = message;
	}

	public SendMsgExcepComponent getSendMsgExcepComponent() {
		return sendMsgExcepComponent;
	}

	public void setSendMsgExcepComponent(SendMsgExcepComponent sendMsgExcepComponent) {
		this.sendMsgExcepComponent = sendMsgExcepComponent;
	}
}
