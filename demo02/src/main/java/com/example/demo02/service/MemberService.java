package com.example.demo02.service;

import com.example.demo02.dto.MemberJoinDTO;

public interface MemberService {

	static class MidExistException extends Exception{
		
	}
	
	void join(MemberJoinDTO memberJoinDTO) throws MidExistException;
}
