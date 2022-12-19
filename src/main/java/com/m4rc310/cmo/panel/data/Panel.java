package com.m4rc310.cmo.panel.data;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

@Data
public class Panel {
	@GraphQLQuery(name = "id_painel")
	private String id;
	@GraphQLQuery(name = "ds_box")
	private String box;
	@GraphQLQuery(name = "ds_erro")
	private String error;
	@GraphQLQuery(name = "ds_mensagem")
	private String message;
	@GraphQLQuery(name = "senha")
	private Integer currentNumber;
	@GraphQLQuery(name = "in_alert")
	private Boolean alert;
	@GraphQLQuery(name = "cd_status")
	private Integer errorStatus;

}
