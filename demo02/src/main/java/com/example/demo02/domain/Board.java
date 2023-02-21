package com.example.demo02.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "bootboard")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Board extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bno;

	@Column(length = 500, nullable = false)
	private	String title;
	
	@Column(length = 2000, nullable = false)
	private	String content;

	@Column(length = 50, nullable = false)
	private	String writer;
	
	public void change(String title, String content) {
		this.title = title;
		this.content = content; 
	}
}
