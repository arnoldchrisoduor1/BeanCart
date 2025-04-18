package com.yourcompany.app.application.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;

@Service
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public URL generateUploadURL() {
        // Generate unique image name
        String imageName = generateImageName();
        
        // Set expiration time (1 second = 1000 milliseconds)
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime() + 1000 * 1000; // 1000 seconds
        expiration.setTime(expTimeMillis);

        // Generate the presigned URL
        GeneratePresignedUrlRequest generatePresignedUrlRequest = 
                new GeneratePresignedUrlRequest(bucketName, imageName)
                        .withMethod(com.amazonaws.HttpMethod.PUT)
                        .withExpiration(expiration)
                        .withContentType("image/jpeg");

        return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    private String generateImageName() {
        String nanoId = NanoIdUtils.randomNanoId();
        long timestamp = System.currentTimeMillis();
        return String.format("%s-%d.jpeg", nanoId, timestamp);
    }
}