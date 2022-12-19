package com.m4rc310.cmo.panel.services;

import java.util.HashMap;
import java.util.Map;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m4rc310.cmo.panel.data.Message;
import com.m4rc310.cmo.panel.data.Panel;
import com.m4rc310.cmo.panel.utils.MMultiRegitry;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLSubscription;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Service
@GraphQLApi
public class CmoPanelService {

	private final MMultiRegitry<String, FluxSink<Panel>> subscribes = new MMultiRegitry<>();
	private final MMultiRegitry<String, FluxSink<Message>> messages = new MMultiRegitry<>();
	private final MMultiRegitry<String, FluxSink<String>> painelList = new MMultiRegitry<>();

	private final Map<String, Object> mapCache = new HashMap<>();

	@GraphQLQuery(name = "panel")
	public Panel getPanel(@GraphQLArgument(name = "painel_id") String panelId) {
		Panel panel = new Panel();
		panel.setId(panelId);
		panel.setCurrentNumber(12);
		return panel;
	}

	@GraphQLMutation(name = "chamar_senha")
	public int callPanelNumber(@GraphQLArgument(name = "painel_id") String sid,
			@GraphQLArgument(name = "json") String json) {

		if (subscribes.contains(sid)) {
			painelList.get(sid).forEach(subs -> subs.next(json));
			return 0;
		}
		return -1;
	}

	@GraphQLMutation(name = "chamar_senha2")
	public Panel callPanelNumber(@GraphQLArgument(name = "painel") Panel panel) {
		String id = panel.getId();
		if (subscribes.contains(id)) {
			subscribes.get(id).forEach(subs -> subs.next(panel));
			panel.setAlert(false);
			mapCache.put(id, panel);
			panel.setErrorStatus(0);
			panel.setMessage("Senha chamada com sucesso.");
			return panel;
		}

		String error = String.format("Painel '%s' não está na espera.", panel.getId());
		panel.setError(error);
		panel.setErrorStatus(-1);
		return panel;
	}

	public int callPanelNumber_(@GraphQLArgument(name = "painel") String panelId,
			@GraphQLArgument(name = "senha") int number) {

		Panel panel = new Panel();
		panel.setId(panelId);
		panel.setCurrentNumber(number);

		if (subscribes.contains(panelId)) {
			subscribes.get(panelId).forEach(subs -> subs.next(panel));
			return 0;
		}

		return -1;
	}

	@GraphQLMutation(name = "send_message")
	public String sendMessage(@GraphQLArgument(name = "ref") String ref, @GraphQLArgument(name = "text") String text) {

		Message message = new Message();
		message.setText(text);

		messages.get(ref).forEach(msg -> msg.next(message));

		return ("OK");
	}

	@GraphQLSubscription(name = "sub_mensagem")
	public Publisher<Message> subMessage(@GraphQLArgument(name = "ref") String ref) {

		return Flux.create(
				fspanel -> messages.add(ref,
						fspanel.onDispose(() -> messages.remove(ref, fspanel)).next(new Message())),
				FluxSink.OverflowStrategy.BUFFER);
	}

	// @GraphQLSubscription(name = "sub_chamar_senha2")
	public Publisher<String> subCallNewPanelNumber(@GraphQLArgument(name = "painel") String panelId)
			throws JsonProcessingException {

		Panel panel = new Panel();
		panel.setId(panelId);
		panel.setBox("---");
		panel.setCurrentNumber(0);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(panel);

		return Flux.create(
				fspanel -> painelList.add(panelId,
						fspanel.onDispose(() -> painelList.remove(panelId, fspanel)).next(json)),
				FluxSink.OverflowStrategy.BUFFER);
	}

	@GraphQLSubscription(name = "sub_chamar_senha")
	public Publisher<Panel> subCallNewPanelNumber_(@GraphQLArgument(name = "painel") String panelId) {

		Panel panel = new Panel();
		if (mapCache.containsKey(panelId)) {
			panel = (Panel) mapCache.get(panelId);
		} else {
			panel.setId(panelId);
			panel.setCurrentNumber(0);
			panel.setBox("--");
			panel.setAlert(false);
		}

		final Panel p = panel;
		return Flux.create(
				fspanel -> subscribes.add(panelId,
						fspanel.onDispose(() -> subscribes.remove(panelId, fspanel)).next(p)),
				FluxSink.OverflowStrategy.BUFFER);
	}

}
