package com.sun.app.service;

import com.sun.app.service.dto.PhotoDTO;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FileService {
    private final Logger log = LoggerFactory.getLogger(FileService.class);

    private final PhotoService photoService;

    public FileService(PhotoService photoService) {
        this.photoService = photoService;
    }

    public byte[] getImage(String path) {
        if(path == null) return new byte[0];
        log.debug("path________" + path);
        InputStream in = FileService.class.getClassLoader()
            .getResourceAsStream("assest/upload/"+ path);
        log.debug("in________" + in);
        if(in == null) return new byte[0];
        byte[] media = new byte[0];
        try {
            media = IOUtils.toByteArray(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return media;
    }

//    public Map<String, String> getImages(List<String> paths) {
//        Map<String, String> result = new HashMap<String, String>();
//        for (String path: paths) {
//            result.put(path, this.getImage(path));
//        }
//        return result;
//    }

    public PhotoDTO upLoadImage(List<MultipartFile> files) throws IOException{
        List<PhotoDTO> result = new ArrayList<PhotoDTO>();
        for (MultipartFile fileData : files) {
            PhotoDTO photoDTO = new PhotoDTO();
            String name = fileData.getOriginalFilename();
            System.out.println("Client File Name = " + name);
            if (name != null && name.length() > 0) {
                photoDTO.setImage(fileData.getBytes());
                photoDTO.setImageContentType(fileData.getContentType());
                photoDTO.setName(name);
                result.add(this.photoService.save(photoDTO));
            }
        }
        return result.get(0);
    }

    public byte[] getPhoto(Long id) {
        Optional<PhotoDTO> photoDTO = this.photoService.findOne(id);
        return photoDTO.get().getImage();
    }

    public String doUpload(List<MultipartFile> files) {
        File uploadRootDir = new File("src/main/resources/assest/upload");
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        List<File> uploadedFiles = new ArrayList<File>();
        List<String> failedFiles = new ArrayList<String>();

        for (MultipartFile fileData : files) {

            String name = fileData.getOriginalFilename();
            System.out.println("Client File Name = " + name);

            if (name != null && name.length() > 0) {
                try {
                    File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(fileData.getBytes());
                    stream.close();
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
