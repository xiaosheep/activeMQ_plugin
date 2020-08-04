package com.aadata.activemqPlugin.plugin;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.ConsumerBrokerExchange;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.Message;
import org.apache.activemq.command.MessageAck;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;

public class ActivemqFilter extends BrokerFilter {

	private JdbcTemplate jdbcTemplate;

	// 用户 这里是封装成实体对象
	private User user;

	public ActivemqFilter(Broker next, JdbcTemplate jdbcTemplate) {
		super(next);
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//发送消息
	public void send(ProducerBrokerExchange producerExchange, Message messageSend) throws Exception {
		// 判断是否经过创建连接
		if (this.getUser() == null) {
			throw new SecurityException("请先去登录");
		}

		// 获得发送时传递过来的对列名称
		String physicalName = messageSend.getDestination().getPhysicalName();
		System.out.println(physicalName);
		if (StrUtil.isBlank(physicalName)) {
			throw new SecurityException("请输入队列名称");
		}

		super.send(producerExchange, messageSend);
	}
	
	//接收消息
	 public void acknowledge(ConsumerBrokerExchange consumerExchange, MessageAck ack) throws Exception {
		// 判断是否经过创建连接
		if (this.getUser() == null) {
			throw new SecurityException("请先去登录");
		}
		 //判断队列名是否为空
        String physicalName = ack.getDestination().getPhysicalName();
        if(StrUtil.isBlank(physicalName)){
            throw new SecurityException("请输入队列名称");
        }
		
        super.acknowledge(consumerExchange, ack);
	 }
	

	/**
	 * 创建链接的时候进行校验
	 * 
	 * @param context
	 * @param info
	 * @throws Exception
	 */
	@Override
	public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
		// 获得连接是对方传的用户名和密码
		String userName = info.getUserName().trim();
		String password = info.getPassword().trim();
		// 用户校验
		auth(userName, password);
		// 创建连接
		super.addConnection(context, info);
	}

	/**
	 * 用户校验 具体实现需要根据需求来进行编写 请不要直接复制粘贴
	 * 
	 * @param userName
	 * @param password
	 */
	public void auth(String userName, String password) {
		// 如果用户名密码为空
		if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
			throw new SecurityException("用户名或密码不能为空");
		}

//        //进行数据库查询
        String sql = "select a.username,a.password from sys_user a where a.username=? and a.status=1";
//        List<User> users  = jdbcTemplate.query(sql, new UserMapper(), userName, password);
        User user = jdbcTemplate.queryForObject(sql, new Object[]{DESUtil.encryptHY(userName)}, new BeanPropertyRowMapper<User>(User.class));
        if(user ==null) {
        	 throw new SecurityException("没有该用户名");
        }
        if(!BCrypt.checkpw(password,user.getPassword())) {
        	 throw new SecurityException("密码错误");
        }
        
		this.setUser(user);
		// 校验用户的连接权限 校验用户的时间段 校验用户的状态 这里需要自己来编写
//        ...
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
