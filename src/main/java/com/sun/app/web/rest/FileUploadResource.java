package com.sun.app.web.rest;

import com.sun.app.domain.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FileUploadResource {
    private final Logger log = LoggerFactory.getLogger(FileUploadResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @PostMapping("/upload/uploadOneFile")
    public String uploadOneFileHandlerPOST(@RequestParam("file") List<MultipartFile> files) {
        return this.doUpload(files);
    }

    @PostMapping("upload/uploadMultiFile")
    public String uploadMultiFileHandlerPOST(@RequestParam("file") List<MultipartFile> files) {
        return this.doUpload(files);
    }

    private String doUpload(List<MultipartFile> files) {

        // Thư mục gốc upload file.

        File uploadRootDir = new File("assest/upload");
        // Tạo thư mục gốc upload nếu nó không tồn tại.
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        //
        List<File> uploadedFiles = new ArrayList<File>();
        List<String> failedFiles = new ArrayList<String>();

        for (MultipartFile fileData : files) {

            // Tên file gốc tại Client.
            String name = fileData.getOriginalFilename();
            System.out.println("Client File Name = " + name);

            if (name != null && name.length() > 0) {
                try {
                    // Tạo file tại Server.
                    File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(fileData.getBytes());
                    stream.close();
                    //
                    uploadedFiles.add(serverFile);
                    System.out.println("Write file: " + serverFile);
                } catch (Exception e) {
                    System.out.println("Error Write file: " + name);
                    failedFiles.add(name);
                }
            }
        }
        return "{'message': 'success'}";
    }
}
