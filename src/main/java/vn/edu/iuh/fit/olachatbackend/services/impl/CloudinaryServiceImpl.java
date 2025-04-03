/*
 * @ (#) CloudinaryServiceImpl.java    1.0    03/04/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package vn.edu.iuh.fit.olachatbackend.services.impl;/*
 * @description:
 * @author: Bao Thong
 * @date: 03/04/2025
 * @version: 1.0
 */

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.olachatbackend.entities.File;
import vn.edu.iuh.fit.olachatbackend.entities.User;
import vn.edu.iuh.fit.olachatbackend.exceptions.NotFoundException;
import vn.edu.iuh.fit.olachatbackend.repositories.FileRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.UserRepository;
import vn.edu.iuh.fit.olachatbackend.services.CloudinaryService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    @Value("${DOWNLOAD_DIR}")
    private String downloadDir;

    private final Cloudinary cloudinary;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public CloudinaryServiceImpl(Cloudinary cloudinary, FileRepository fileRepository, UserRepository userRepository) {
        this.cloudinary = cloudinary;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public File uploadFileAndSaveToDB(MultipartFile file, Long associatedIDMessageId) throws IOException {
        var context = SecurityContextHolder.getContext();
        String currentUsername = context.getAuthentication().getName();

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng này"));
        Map<?, ?> uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        String url = uploadResult.get("secure_url").toString();
        String publicId = uploadResult.get("public_id").toString();
        File fileUpload = File.builder()
                .fileUrl(url)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .uploadedAt(LocalDateTime.now())
                .uploadedBy(user)
                .associatedIDMessageId(associatedIDMessageId)
                .publicId(publicId) // Add publicId to the File entity
                .build();
        fileRepository.save(fileUpload);
        return fileUpload;
    }

    //delete file and delete from database
    @Override
    public void deleteFile(String publicId) throws IOException {
        // Delete the file from Cloudinary
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        // Find the file in the database and delete it
        File file = fileRepository.findByPublicId(publicId).orElseThrow(() -> new NotFoundException("File not found"));
        fileRepository.delete(file);
    }

    //download file
    @Override
    public byte[] downloadFile(String publicId) throws Exception {
        // Retrieve the file entity from the database
        File fileEntity = fileRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("File not found"));

        // Get the file URL from Cloudinary
        Map<?, ?> resourceResult = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
        String fileUrl = resourceResult.get("secure_url").toString();

        // Determine the file extension based on the file type
        String fileType = fileEntity.getFileType();
        String fileExtension = fileType.substring(fileType.lastIndexOf('/') + 1);

        // Download the file using HttpURLConnection
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream()) {
            byte[] fileData = inputStream.readAllBytes();

            // Save the file to the src/main/resources/save directory with the correct extension
            String saveDirectory = downloadDir;
            java.io.File saveDir = new java.io.File(saveDirectory);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            try (FileOutputStream fos = new FileOutputStream(saveDirectory + publicId + "." + fileExtension)) {
                fos.write(fileData);
            }

            return fileData;
        }
    }
}
