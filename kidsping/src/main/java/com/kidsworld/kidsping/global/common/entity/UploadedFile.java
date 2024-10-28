package com.kidsworld.kidsping.global.common.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class UploadedFile {

    private String fileUrl;
    private String uploadedFileName;
    private String serverFileName;

    public UploadedFile(String uploadedFileName, String serverFileName) {
        this.uploadedFileName = uploadedFileName;
        this.serverFileName = serverFileName;
    }
}