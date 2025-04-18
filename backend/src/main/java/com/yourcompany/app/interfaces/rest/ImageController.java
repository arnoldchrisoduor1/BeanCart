package com.yourcompany.app.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yourcompany.app.application.service.S3Service;

import java.net.URL;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final S3Service s3Service;

    public ImageController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/upload-url")
    public ResponseEntity<?> getImageUploadUrl() {
        try {
            URL uploadUrl = s3Service.generateUploadURL();
            return ResponseEntity.ok().body(new UploadUrlResponse(uploadUrl.toString()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage()));
        }
    }

    // Helper classes for response
    private static class UploadUrlResponse {
        private final String uploadURL;

        public UploadUrlResponse(String uploadURL) {
            this.uploadURL = uploadURL;
        }

        public String getUploadURL() {
            return uploadURL;
        }
    }

    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}