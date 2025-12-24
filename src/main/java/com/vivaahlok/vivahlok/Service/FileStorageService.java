package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    public String uploadFile(MultipartFile file, String subDirectory) {
        try {
            if (file.isEmpty()) {
                throw new BadRequestException("File is empty");
            }
            
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String newFilename = UUID.randomUUID().toString() + extension;
            
            Path uploadPath = Paths.get(uploadDir, subDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            return "/uploads/" + subDirectory + "/" + newFilename;
            
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            throw new BadRequestException("Failed to upload file");
        }
    }
    
    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl != null && fileUrl.startsWith("/uploads/")) {
                String relativePath = fileUrl.substring("/uploads/".length());
                Path filePath = Paths.get(uploadDir, relativePath);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            log.error("Error deleting file: {}", e.getMessage());
        }
    }
}
