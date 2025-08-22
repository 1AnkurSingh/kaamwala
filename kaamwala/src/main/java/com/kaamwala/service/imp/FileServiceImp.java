package com.kaamwala.service.imp;

import com.kaamwala.exception.BadApiRequest;
import com.kaamwala.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImp implements FileService {
    Logger logger= LoggerFactory.getLogger(FileServiceImp.class);
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        logger.info("File name :{}" , originalFilename);
        String fileName= UUID.randomUUID().toString();

        String extension =originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension =fileName + extension;

        String fullPathWithFIleName=path+fileNameWithExtension;

        if(extension.equalsIgnoreCase(".png")||extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase(".jpeg")){
            File folder = new File(path) ;
            if(!folder.exists()){

                // create the folder
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFIleName));
            return fileNameWithExtension;
        }else {
            throw new BadApiRequest("File With this "+extension+ "not allowed !!");


        }


    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath=path+File.separator+name;
        InputStream inputStream= new FileInputStream(fullPath);
        return inputStream;
    }
}
