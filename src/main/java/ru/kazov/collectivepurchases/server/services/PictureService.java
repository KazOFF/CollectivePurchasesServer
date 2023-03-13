package ru.kazov.collectivepurchases.server.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class PictureService {

    public String storePicture(MultipartFile file) {
        try {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
            String fileName = date + file.getOriginalFilename();
            String filePath = "static"+ File.separator +"images" + File.separator + fileName;
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(filePath));
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String storePicture(String urlString) {
        try {
            URL url = new URL(urlString);
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
            String fileName = FilenameUtils.getName(url.getPath());
            String filePath = "static"+ File.separator +"images" + File.separator + fileName;
            FileUtils.copyURLToFile(url, new File(filePath));
            return filePath.replace("\\", "/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
