package com.example.demo02.service;

import com.example.demo02.dto.BoardDTO;
import com.example.demo02.dto.BoardListReplyCountDTO;
import com.example.demo02.dto.PageRequestDTO;
import com.example.demo02.dto.PageResponseDTO;

public interface BoardService {
	Long register(BoardDTO boardDTO);
	BoardDTO readOne(Long wr_id);
	void modify(BoardDTO boardDTO);
	void remove(Long wr_id);
	PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
	
	// 댓글수까지 조회
	PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
}
