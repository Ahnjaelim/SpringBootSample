package com.example.demo02.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.demo02.domain.Board;
import com.example.demo02.domain.BoardImage;
import com.example.demo02.dto.BoardListAllDTO;
import com.example.demo02.dto.BoardListReplyCountDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class BoardRepositoryTest {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	// 기본 인서트 테스트
	// @Test
	public void testInsert() {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			Board board = Board.builder()
					.title("title..."+i)
					.content("content..."+i)
					.writer("user"+(i%10))
					.build();
			Board result = boardRepository.save(board);
			log.info("BNO : "+result.getBno());
		});
	}
	// @Test
	public void testSelect() {
		Long bno = 100L;
		Optional<Board> result = boardRepository.findById(bno);
		Board board = result.orElseThrow();
		log.info(board);
	}
	// @Test
	public void testUpdate() {
		Long bno = 100L;
		Optional<Board> result = boardRepository.findById(bno);
		Board board = result.orElseThrow();
		board.change("update...title 100", "update content 100");
		boardRepository.save(board);
	}
	// @Test
	public void testDelete() {
		Long bno = 1L;
		boardRepository.deleteById(bno);
	}
	// @Test
	public void testPaging() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		Page<Board> result = boardRepository.findAll(pageable);
		log.info("total count : "+result.getTotalElements());
		log.info("total pages : "+result.getTotalPages());
		log.info("page number : "+result.getNumber());
		log.info("page size : "+result.getSize());
		List<Board> boardList = result.getContent();
		boardList.forEach(board -> log.info(board));
	}
	// @Test
	public void testSearch1() {
		Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
		boardRepository.search1(pageable);
	}
	// @Test
	public void testSearchAll() {
		String[] types = {"t", "c", "w"};
		String keyword = "1";
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
		log.info(result.getTotalPages());
		log.info(result.getSize());
		log.info(result.getNumber());
		log.info(result.hasPrevious()+" : "+result.hasNext());
		result.getContent().forEach(board -> log.info(board));
	}
	
	// @Test
	public void testSearchReplyCount() {
		String[] types = {"t", "c", "w"};
		String keyword = "1";
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);
		log.info(result.getTotalPages());
		log.info(result.getSize());
		log.info(result.getNumber());
		log.info(result.hasPrevious()+" : "+result.hasNext());
		result.getContent().forEach(board -> log.info(board));;
	}
	
	// @Test
	public void testInsertWithImage() {
		Board board = Board.builder()
				.title("Image Test")
				.content("첨부파일 테스트")
				.writer("tester")
				.build();
		for(int i = 0; i < 3; i++) {
			board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
		}
		
		boardRepository.save(board);
	}
	
	//@Test
	public void testReadWithImages() {
		Optional<Board> result = boardRepository.findByIdWithImages(3L);
		Board board = result.orElseThrow();
		log.info(board);
		log.info("==========filelist==========");
		for(BoardImage boardImage : board.getImageSet()) {
			log.info(boardImage);
		}
	}
	
	// @Test
	public void testModifyImages() {	
		Optional<Board> result = boardRepository.findByIdWithImages(3L);
		Board board = result.orElseThrow();
		board.clearImage();
		for(int i=0; i<2; i++) {
			board.addImage(UUID.randomUUID().toString(), "updatefile"+i+".jpg");
		}
		boardRepository.save(board);
	}
	
	//@Test
	//@Transactional
	//@Commit
	public void testRemoveAll() {
		
		Long bno = 1L;
		replyRepository.deleteByBoard_Bno(bno);
		boardRepository.deleteById(bno);
	}
	
	// @Test
	public void testInsertAll() {
		for(int i=1; i<=100; i++) {
			Board board = Board.builder()
					.title("Title..."+i)
					.content("Content..."+i)
					.writer("writer..."+i)
					.build();
			for(int j=0; j<3; j++) {
				if(i%5==0) {
					continue;
				}
				board.addImage(UUID.randomUUID().toString(), i+"file"+j+".jpg");
			}
			boardRepository.save(board);
		}
	}
	
	@Transactional
	@Test
	public void testSearchImageReplyCount() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		// boardRepository.searchWithAll(null, null, pageable);
		Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);
		log.info("==============================");
		log.info(result.getTotalElements());
		result.getContent().forEach(boardListAllDTO -> log.info(boardListAllDTO));
	}
	
}
