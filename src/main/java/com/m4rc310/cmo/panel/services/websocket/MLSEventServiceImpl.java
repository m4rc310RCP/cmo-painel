package com.m4rc310.cmo.panel.services.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Service
public class MLSEventServiceImpl implements MLSEventService {

	@Autowired
	private SimpMessagingTemplate service;

	@Override
	public void sendToken(String token) {
		sendCode("/topics/event", token);
	}

	@Override
	public void sendCode(String destination, String code) {
		MLSEvent event = new MLSEvent();
		event.setCode(code);
		service.convertAndSend(destination,
				JSON.toJSONString(event, SerializerFeature.BrowserCompatible));
	}

}
