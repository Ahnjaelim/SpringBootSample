package com.example.demo02.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo02.dto.ReplyDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ReplyServiceTest {
	
	@Autowired
	private ReplyService replyService;
	
	@Test
	public void testRegister() {
		ReplyDTO replyDTO = ReplyDTO.builder()
				.replyText("ReplyDTO Text")
				.replyer("replyer")
				.bno(100L)
				.build();
		log.info(replyService.register(replyDTO));
	}
}
