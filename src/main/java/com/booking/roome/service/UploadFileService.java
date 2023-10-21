package com.booking.roome.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    public String uploadFile(MultipartFile file);
}
