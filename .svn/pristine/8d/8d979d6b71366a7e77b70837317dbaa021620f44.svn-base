package com.frame.utils.exception;

import com.frame.utils.HttpCode;

public class FrameException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private int statusCode = HttpCode.FAILURE;

	public FrameException() {
		super();
	}

	public FrameException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public FrameException(String message,int statusCode) {
		super(message);
		this.statusCode=statusCode;
	}

	public FrameException(String message) {
		super(message);
	}

	public FrameException(Throwable cause) {
		super(cause);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
