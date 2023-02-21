package com.example.demo02.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo02.domain.Board;
import com.example.demo02.dto.BoardDTO;
import com.example.demo02.dto.BoardListReplyCountDTO;
import com.example.demo02.dto.PageRequestDTO;
import com.example.demo02.dto.PageResponseDTO;
import com.example.demo02.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

	private final ModelMapper modelMapper;
	private final BoardRepository boardRepository;
	
	@Override
	public Long register(BoardDTO boardDTO) {
		Board board = modelMapper.map(boardDTO, Board.class);
		Long wr_id = boardRepository.save(board).getBno();
		return wr_id;
	}

	@Override
	public BoardDTO readOne(Long wr_id) {
		Optional<Board> result = boardRepository.findById(wr_id);
		Board board = result.orElseThrow();
		BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
		return boardDTO;
	}

	@Override
	public void modify(BoardDTO boardDTO) {
		Optional<Board> result = boardRepository.findById(boardDTO.getBno());
		Board board = result.orElseThrow();
		board.change(boardDTO.getTitle(), boardDTO.getContent());
		boardRepository.save(board);
	}

	@Override
	public void remove(Long wr_id) {
		boardRepository.deleteById(wr_id);
		
	}

	@Override
	public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
		String[] types = pageRequestDTO.getTypes();
		String keyword = pageRequestDTO.getKeyword();
		Pageable pageable = pageRequestDTO.getPageable("bno");
		Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
		
		List<BoardDTO> dtoList = result.getContent().stream().map(board -> modelMapper.map(board, BoardDTO.class))
				.collect(Collectors.toList());
		return PageResponseDTO.<BoardDTO>withAll()
				.pageRequestDTO(pageRequestDTO)
				.dtoList(dtoList)
				.total((int)result.getTotalElements())
				.build();
	}

	@Override
	public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
		String[] types = pageRequestDTO.getTypes();
		String keyword = pageRequestDTO.getKeyword();
		Pageable pageable = pageRequestDTO.getPageable("bno");
		Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);
		
		return PageResponseDTO.<BoardListReplyCountDTO>withAll()
				.pageRequestDTO(pageRequestDTO)
				.dtoList(result.getContent())
				.total((int)result.getTotalElements())
				.build();
	}

}
