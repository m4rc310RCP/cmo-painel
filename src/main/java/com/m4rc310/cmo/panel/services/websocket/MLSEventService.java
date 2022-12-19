package com.m4rc310.cmo.panel.services.websocket;

public interface MLSEventService {
	void sendToken(String token);
	void sendCode(String destination,  String code);
}
