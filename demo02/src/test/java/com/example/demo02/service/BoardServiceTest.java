package com.example.demo02.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo02.dto.BoardDTO;
import com.example.demo02.dto.BoardImageDTO;
import com.example.demo02.dto.BoardListAllDTO;
import com.example.demo02.dto.PageRequestDTO;
import com.example.demo02.dto.PageResponseDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class BoardServiceTest {

	@Autowired
	BoardService boardService;
	
	// @Test
	public void testRegisterWithImages() {
		log.info(boardService.getClass().getName());
		BoardDTO boardDTO = BoardDTO.builder()
				.title("File...Sample Title...")
				.content("Sample Content...")
				.writer("user00")
				.build();
		List<String> templist = new ArrayList<>();
		templist.add(UUID.randomUUID()+"_aaa.jpg");
		templist.add(UUID.randomUUID()+"_bbb.jpg");
		templist.add(UUID.randomUUID()+"_ccc.jpg");
		boardDTO.setFileNames(templist);
		Long bno = boardService.register(boardDTO);
		log.info("bno: " + bno);
	}
	
	// @Test
	public void testReadAll() {
		Long bno = 100L;
		BoardDTO boardDTO = boardService.readOne(bno);
		log.info(boardDTO);
		for(String fileName : boardDTO.getFileNames()) {
			log.info(fileName);
			
		}
	}
	
	// @Test
	public void testModify() {
		// 변경에 필요한 데이터
		BoardDTO boardDTO = BoardDTO.builder()
				.bno(100L)
				.title("Updated.....101")
				.content("Updated content 101...")
				.build();
		// 첨부파일 하나 추가
		List<String> templist = new ArrayList<>();
		templist.add(UUID.randomUUID()+"_zzz.jpg");
		
		boardDTO.setFileNames(templist);
		boardService.modify(boardDTO);
		
	}
	
	// @Test
	public void testRemoveAll() {
		Long bno = 1L;
		boardService.remove(bno);
	}
	
	@Test
	public void testListWithAll() {
		PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
				.page(1)
				.size(10)
				.build();
		PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);
		List<BoardListAllDTO> dtoList = responseDTO.getDtoList();
		dtoList.forEach(boardListAllDTO -> {
			log.info(boardListAllDTO.getBno()+":"+boardListAllDTO.getTitle());
			if(boardListAllDTO.getBoardImages() != null) {
				for(BoardImageDTO boardImage : boardListAllDTO.getBoardImages()) {
					log.info(boardImage);
					
				}
				
			}
			log.info("==============================");
		});
	}
}
