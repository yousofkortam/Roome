package com.booking.roome.service.Impl;

import com.booking.roome.service.UploadFileService;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFileServiceImpl implements UploadFileService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            return cloudinary.uploader()
                    .upload(
                            file.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString())
                    )
                    .get("url")
                    .toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
