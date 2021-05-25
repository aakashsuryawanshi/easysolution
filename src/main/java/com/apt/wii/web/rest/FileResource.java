package com.apt.wii.web.rest;

import com.apt.wii.service.dto.FileDetailsDTO;
import com.apt.wii.service.file.FileService;
import com.apt.wii.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class FileResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    private static final String ENTITY_NAME = "file";

    private final FileService fileService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public FileResource(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/secure/file-upload")
    public ResponseEntity<FileDetailsDTO> uploadFile(@RequestParam("file") MultipartFile file) throws URISyntaxException {
        log.debug("REST request to upload File : {}", file.getName());
        FileDetailsDTO obj = fileService.uploadFile(file);
        return ResponseEntity
            .created(new URI("/api/file-upload/" + obj.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, obj.getId().toString()))
            .body(obj);
    }

    @GetMapping("/secure/file-download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long fileDetailId) throws URISyntaxException {
        log.debug("REST request to donwload File : {}", fileDetailId);
        InputStreamResource obj = fileService.downloadFile(fileDetailId);
        return ResponseEntity.ok().body(obj);
    }
}
