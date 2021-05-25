package com.apt.wii.service.file.impl;

import com.apt.wii.service.FileDetailsService;
import com.apt.wii.service.FileMetaDataService;
import com.apt.wii.service.dto.FileDetailsDTO;
import com.apt.wii.service.dto.FileMetaDataDTO;
import com.apt.wii.service.file.FileService;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileSystemServiceImpl.class);

    @Value("${wii.folder_path}")
    private String folderPath;

    private final FileDetailsService fileDetailsService;

    private final FileMetaDataService fileMetaDataService;

    public FileSystemServiceImpl(FileDetailsService fileDetailsService, FileMetaDataService fileMetaDataService) {
        this.fileDetailsService = fileDetailsService;
        this.fileMetaDataService = fileMetaDataService;
    }

    @Override
    public FileDetailsDTO uploadFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String postfix = "_" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            String filename = file.getOriginalFilename();
            filename = filename.substring(0, filename.lastIndexOf(".")) + postfix + filename.substring(filename.lastIndexOf("."));
            try (FileOutputStream fos = new FileOutputStream(folderPath + filename)) {
                fos.write(bytes);
            }
            FileDetailsDTO data = new FileDetailsDTO();
            data.setSourceName(file.getOriginalFilename());
            data.setDestination("FILE_SYSTEM");
            data.setDestinationName(filename);
            data = fileDetailsService.save(data);
            FileMetaDataDTO metadata = new FileMetaDataDTO();
            metadata.setKey("filePath");
            metadata.setValue(folderPath + filename);
            metadata.setFileDetails(data);
            metadata = fileMetaDataService.save(metadata);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception while uploading file {}", e.getMessage());
        }
        return null;
    }

    @Override
    public InputStreamResource downloadFile(Long fileDetailsId) {
        List<FileMetaDataDTO> fileMetaDataDTOList = fileMetaDataService.findByFileDetail(fileDetailsId);
        String filePath = null;
        for (FileMetaDataDTO fileMetaDataDTO : fileMetaDataDTOList) {
            if (fileMetaDataDTO.getKey().equalsIgnoreCase("filePath")) {
                filePath = fileMetaDataDTO.getValue();
                break;
            }
        }
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(filePath));
            return resource;
        } catch (Exception e) {
            log.error("Exception while downloading exception {}", e.getMessage());
        }
        return null;
    }
}
