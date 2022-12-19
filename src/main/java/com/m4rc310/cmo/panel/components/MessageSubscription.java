package com.m4rc310.cmo.panel.components;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import com.m4rc310.cmo.panel.data.Message;

import io.leangen.graphql.annotations.GraphQLSubscription;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;

@Service
@GraphQLApi
public class MessageSubscription {
	
	@GraphQLSubscription(name = "sub_mensagens", description = "Monitorar mensagens")
	public Publisher<Message> listenerMessages(String ref) {
		
		Message message = new Message();
		message.setText("");
		
//		Observable<Message> observable = Observable
//				.switchOnNext(m -> Observable.create(emitter -> emitter.onNext(message)))
//				.flatMap(m -> Observable.create(emitter -> emitter.onNext(message)));
		Observable<Message> observable = Observable.create(emitter ->{
			emitter.onNext(message);
		});
		
		return observable.toFlowable(BackpressureStrategy.BUFFER);
	}
}
