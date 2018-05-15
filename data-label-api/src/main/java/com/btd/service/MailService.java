package com.btd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.btd.rest.ApiConstant;

@Service
@Transactional
public class MailService {

	@Autowired
	private JavaMailSender jms;

	public void send(String to, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setFrom(ApiConstant.ADMIN_EMAIL);
		message.setSubject(ApiConstant.REGISTRATION_SUBJECT);
		message.setText(text);
		jms.send(message);
	}
}
