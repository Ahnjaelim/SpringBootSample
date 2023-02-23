package com.example.demo02.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo02.domain.Board;
import com.example.demo02.repository.search.BoardSearch;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

	@Query(value = "select now()", nativeQuery = true)
	String getTime();
	
	@EntityGraph(attributePaths = {"imageSet"})
	@Query("select b from Board b where b.bno =:bno")
	Optional<Board> findByIdWithImages(@Param("bno") Long bno);
}