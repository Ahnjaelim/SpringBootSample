package com.example.demo02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo02.domain.Board;
import com.example.demo02.repository.search.BoardSearch;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
	
	@Query(value = "select now()", nativeQuery = true)
	String getTime();
	

}
