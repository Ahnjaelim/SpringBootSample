package com.example.demo02.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo02.domain.Board;
import com.example.demo02.dto.BoardDTO;
import com.example.demo02.dto.BoardListAllDTO;
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
	
	// 게시글의 이미지와 댓글의 숫자까지 처리
	PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);
	
	default Board dtoToEntity(BoardDTO boardDTO) {
		Board board = Board.builder()
				.bno(boardDTO.getBno())
				.title(boardDTO.getTitle())
				.content(boardDTO.getContent())
				.writer(boardDTO.getWriter())
				.build();
		if(boardDTO.getFileNames() != null) {
			boardDTO.getFileNames().forEach(fileName -> {
				String[] arr = fileName.split("_");
				board.addImage(arr[0], arr[1]);
			});
		}
		return board;
	}
	
	default BoardDTO entityToDTO(Board board) {
		BoardDTO boardDTO = BoardDTO.builder()
				.bno(board.getBno())
				.title(board.getTitle())
				.content(board.getContent())
				.writer(board.getWriter())
				.regDate(board.getRegDate())
				.build();
		List<String> fileNames = board.getImageSet().stream().sorted().map(boardImage -> boardImage.getUuid()+"_"+boardImage.getFileName()).collect(Collectors.toList());
		boardDTO.setFileNames(fileNames);
		return boardDTO;
	}
}
