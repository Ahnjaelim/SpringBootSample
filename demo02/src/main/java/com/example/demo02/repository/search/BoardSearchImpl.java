package com.example.demo02.repository.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.demo02.domain.Board;
import com.example.demo02.domain.QBoard;
import com.example.demo02.domain.QReply;
import com.example.demo02.dto.BoardImageDTO;
import com.example.demo02.dto.BoardListAllDTO;
import com.example.demo02.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;


public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

	public BoardSearchImpl() {
		super(Board.class);
	}
	
	@Override
	public Page<Board> search1(Pageable pageable) {
		
		QBoard board = QBoard.board;
		JPQLQuery<Board> query = from(board);
		
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.or(board.title.contains("1"));
		booleanBuilder.or(board.content.contains("1"));

		query.where(booleanBuilder);
		query.where(board.bno.gt(0L));
		
		// 페이징
		this.getQuerydsl().applyPagination(pageable, query);
	
		List<Board> list = query.fetch();
		long count = query.fetchCount();
		return null;
	}

	@Override
	public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
		QBoard board = QBoard.board;
		JPQLQuery<Board> query = from(board);
		
		if((types != null && types.length >0) && keyword !=null) {
			BooleanBuilder booleanBuilder = new BooleanBuilder();
			for(String type :  types) {
				switch(type) {
				case "t" :
					booleanBuilder.or(board.title.contains(keyword));
					break;
				case "c" :
					booleanBuilder.or(board.content.contains(keyword));
					break;
				case "w" :
					booleanBuilder.or(board.writer.contains(keyword));
					break;					
				}
			} // end of for
			query.where(booleanBuilder);
		} // end of if 
		
		query.where(board.bno.gt(0L));
		
		// paging
		this.getQuerydsl().applyPagination(pageable, query);
		
		List<Board> list = query.fetch();
		long count = query.fetchCount();
		
		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
		QBoard board = QBoard.board;
		QReply reply = QReply.reply;
		JPQLQuery<Board> query = from(board);
		query.leftJoin(reply).on(reply.board.eq(board));
		query.groupBy(board);
		
		if((types != null && types.length >0) && keyword !=null) {
			BooleanBuilder booleanBuilder = new BooleanBuilder();
			for(String type :  types) {
				switch(type) {
				case "t" :
					booleanBuilder.or(board.title.contains(keyword));
					break;
				case "c" :
					booleanBuilder.or(board.content.contains(keyword));
					break;
				case "w" :
					booleanBuilder.or(board.writer.contains(keyword));
					break;					
				}
			} // end of for
			query.where(booleanBuilder);
		} // end of if 
		
		query.where(board.bno.gt(0L));
		
		JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class, 
				board.bno,
				board.title,
				board.writer,
				board.regDate,
				reply.count().as("replyCount")
				));
		
		// paging
		this.getQuerydsl().applyPagination(pageable, query);
		
		List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
		
		long count = dtoQuery.fetchCount();
		
		return new PageImpl<>(dtoList, pageable, count);
	}

	@Override
	public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

		QBoard board = QBoard.board;
		QReply reply = QReply.reply;
		
		JPQLQuery<Board> boardJPQLQuery = from(board);
		boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); // left join
		
		// 검색 조건 추가
		if((types != null && types.length > 0) && keyword != null) {
			BooleanBuilder booleanBuilder = new BooleanBuilder();
			for(String type :  types) {
				switch(type) {
				case "t" :
					booleanBuilder.or(board.title.contains(keyword));
					break;
				case "c" :
					booleanBuilder.or(board.content.contains(keyword));
					break;
				case "w" :
					booleanBuilder.or(board.writer.contains(keyword));
					break;					
				}
			} // end of for
			boardJPQLQuery.where(booleanBuilder);
		}
		
		
		boardJPQLQuery.groupBy(board);
		
		getQuerydsl().applyPagination(pageable, boardJPQLQuery); // paging
		
		JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());
		
		List<Tuple> tupleList = tupleJPQLQuery.fetch();
		
		List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
			Board board1 = (Board) tuple.get(board);
			long replyCount = tuple.get(1, Long.class);
			BoardListAllDTO dto = BoardListAllDTO.builder()
					.bno(board1.getBno())
					.title(board1.getTitle())
					.writer(board1.getWriter())
					.regDate(board1.getRegDate())
					.replyCount(replyCount)
					.build();
			
			// BoardImage를 BoardImageDTO 처리할 부분
			List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
					.map(boardImage -> BoardImageDTO.builder()
							.uuid(boardImage.getUuid())
							.fileName(boardImage.getFileName())
							.ord(boardImage.getOrd())
							.build()
						).collect(Collectors.toList());
			dto.setBoardImages(imageDTOS);
			return dto;
		}).collect(Collectors.toList());
		
		long totalCount = boardJPQLQuery.fetchCount();
		
		return new PageImpl<>(dtoList, pageable, totalCount);
	}

}
