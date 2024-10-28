package com.kidsworld.kidsping.infra.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.custom.FileException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileDownload {

    @Value("${file.dir}")
    private String fileDir;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public void validateFileExistsAtUrl(String resourcePath) {

        if (!amazonS3Client.doesObjectExist(bucketName, resourcePath)) {
            throw new FileException(ExceptionCode.FILE_NOT_FOUND);
        }
    }
}
