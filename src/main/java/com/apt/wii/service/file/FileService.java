package com.apt.wii.service.file;

import com.apt.wii.service.dto.FileDetailsDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileDetailsDTO uploadFile(MultipartFile file);
    InputStreamResource downloadFile(Long fileDetailsId);
}
