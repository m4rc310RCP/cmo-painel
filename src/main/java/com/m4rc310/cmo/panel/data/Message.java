package com.m4rc310.cmo.panel.data;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

@Data
public class Message {
	@GraphQLQuery(name = "text")
	private String text;
}
