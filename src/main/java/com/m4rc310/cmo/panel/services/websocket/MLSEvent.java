package com.m4rc310.cmo.panel.services.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MLSEvent {
	private String code;
	
	public MLSEvent(String code) {
		this.code = code;
	}

	public MLSEvent() {
	}


	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
