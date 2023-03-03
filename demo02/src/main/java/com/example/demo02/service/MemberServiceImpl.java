package com.example.demo02.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo02.domain.Member;
import com.example.demo02.domain.MemberRole;
import com.example.demo02.dto.MemberJoinDTO;
import com.example.demo02.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final ModelMapper modelMapper;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {
		
		String mid = memberJoinDTO.getMid();
		boolean exist = memberRepository.existsById(mid);
		if(exist) {
			throw new MidExistException();
		}
		
		Member member = modelMapper.map(memberJoinDTO, Member.class);
		member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
		member.addRole(MemberRole.USER);
		
		log.info("==============================");
		log.info(member);
		log.info(member.getRoleSet());
		
		memberRepository.save(member);
		
	}

}
