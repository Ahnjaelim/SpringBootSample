package com.example.demo02.service;

import com.example.demo02.dto.PageRequestDTO;
import com.example.demo02.dto.PageResponseDTO;
import com.example.demo02.dto.ReplyDTO;

public interface ReplyService {
	Long register(ReplyDTO replyDTO);
	ReplyDTO read(Long rno);
	void modify(ReplyDTO replyDTO);
	void remove(Long rno);
	PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
