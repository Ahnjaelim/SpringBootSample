package com.example.demo02.dto.upload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadFileDTO {
	private List<MultipartFile> files;
}
