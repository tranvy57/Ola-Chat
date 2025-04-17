/*
 * @ (#) MediaController.java    1.0    03/04/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package vn.edu.iuh.fit.olachatbackend.controllers;/*
 * @description:
 * @author: Bao Thong
 * @date: 03/04/2025
 * @version: 1.0
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.olachatbackend.entities.File;
import vn.edu.iuh.fit.olachatbackend.exceptions.NotFoundException;
import vn.edu.iuh.fit.olachatbackend.repositories.FileRepository;
import vn.edu.iuh.fit.olachatbackend.services.CloudinaryService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {
    private final CloudinaryService cloudinaryService;
    private final FileRepository fileRepository;

    @Value("${DOWNLOAD_DIR}")
    private String downloadDir;

    public FileController(CloudinaryService cloudinaryService, FileRepository fileRepository) {
        this.cloudinaryService = cloudinaryService;
        this.fileRepository = fileRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "associatedIDMessageId", required = false) Long associatedIDMessageId) {
        try {
            File fileUpload = cloudinaryService.uploadFileAndSaveToDB(file, associatedIDMessageId);
            return ResponseEntity.ok(fileUpload);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    //delete file and remove from database
    @PostMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestParam("publicId") String publicId) {
        try {
            cloudinaryService.deleteFile(publicId);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Delete failed: " + e.getMessage());
        }
    }

    //download file
    @PostMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("publicId") String publicId) {
        try {
            byte[] fileData = cloudinaryService.downloadFile(publicId);
            File fileEntity = fileRepository.findByPublicId(publicId)
                    .orElseThrow(() -> new NotFoundException("File not found"));
            String fileType = fileEntity.getFileType();
            String fileExtension = fileType.substring(fileType.lastIndexOf('/') + 1);
            String fileName = publicId + "." + fileExtension;

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(Map.of(
                            "fileName", fileName,
                            "location", downloadDir + fileName,
                            "message", "Tải xuống thành công"
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Download failed: " + e.getMessage());
        }
    }

    @PostMapping("/upload/image")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file)
            throws IOException, IOException {
        return ResponseEntity.ok(cloudinaryService.uploadImage(file));
    }




}
