package com.aadata.activemqPlugin.plugin;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;
import org.springframework.jdbc.core.JdbcTemplate;

public class MyAuthenticationPlugin implements BrokerPlugin {

	JdbcTemplate jdbcTemplate;// 注入JdbcTemplate

	public MyAuthenticationPlugin(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	public Broker installPlugin(Broker broker) throws Exception {
		return new ActivemqFilter(broker,jdbcTemplate);
	}

}
