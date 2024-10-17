package com.booking.roome.service.uploadFile;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    String uploadFile(MultipartFile file);
}
