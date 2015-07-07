package com.ogzcm.dto;

import com.ogzcm.enums.ResponseType;

public class SimpleResponse {

	private String type;
	private String message;

	public SimpleResponse() {
		super();
		type = ResponseType.SUCCESS.getText();
		message = "It has been successfully done.";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
