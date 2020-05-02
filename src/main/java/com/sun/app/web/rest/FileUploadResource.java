package com.sun.app.web.rest;

import com.sun.app.service.FileService;
import com.sun.app.service.dto.PhotoDTO;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FileUploadResource {
    private final Logger log = LoggerFactory.getLogger(FileUploadResource.class);

    private final FileService fileService;

    private static final String ENTITY_NAME = "photo";

    @Autowired
    private ResourceLoader resourceLoader;

    public FileUploadResource(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @PostMapping("/upload/uploadOneFile")
    public ResponseEntity<PhotoDTO> uploadOneFileHandlerPOST(@RequestParam("file") List<MultipartFile> files) throws IOException, URISyntaxException {
        PhotoDTO photoDTO = this.fileService.upLoadImage(files);
        return ResponseEntity.created(new URI("/api/image/" + photoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, photoDTO.getId().toString()))
            .body(photoDTO);
    }

//    @PostMapping("/upload/uploadMultiFile")
//    public ResponseEntity<String> uploadMultiFileHandlerPOST(@RequestParam("file") List<MultipartFile> files) {
//        return ResponseEntity.ok().body(this.fileService.doUpload(files));
//    }

    @GetMapping("/getFile/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(this.fileService.getPhoto(id));
    }


//    @GetMapping("/getFile/images")
//    public Map<String, String> getImages(@RequestParam("paths") List<String> paths) {
//        return this.fileService.getImages(paths);
//    }
}
