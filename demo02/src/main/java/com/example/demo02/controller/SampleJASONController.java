package com.example.demo02.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class SampleJASONController {

	@GetMapping("/helloArr")
	public String[] helloArr() {
		System.out.println("helloArr");
		return new String[] {"AAA","BBB","CCC"};
	}
}
