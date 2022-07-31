//package com.onlineshop.hishop.message;
//
//import com.onlineshop.hishop.exception.HiShopException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.MessageListener;
//import javax.jms.TextMessage;
//@Component
//public class MyMessageListener implements MessageListener {
//
//	private final static Logger log= LoggerFactory.getLogger(MyMessageListener.class);
//
//	@Override
//	public void onMessage(Message message) {
//
//		//取消息内容
//		TextMessage textMessage = (TextMessage) message;
//		try {
//			String text = textMessage.getText();
//			log.info(text);
//		} catch (JMSException e) {
//			e.printStackTrace();
//			throw new HiShopException("监听消息失败");
//		}
//	}
//
//}
