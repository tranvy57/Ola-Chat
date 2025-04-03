package vn.edu.iuh.fit.olachatbackend.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.olachatbackend.entities.File;

import java.io.IOException;

@Service
public interface CloudinaryService {
    File uploadFileAndSaveToDB(MultipartFile file, Long associatedIDMessageId) throws IOException;
    //delete file and delete from database
    void deleteFile(String publicId) throws IOException;
    //download file
    byte[] downloadFile(String publicId) throws Exception;
}
