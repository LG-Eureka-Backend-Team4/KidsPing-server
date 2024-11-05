package com.kidsworld.kidsping.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.UrlResource;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DownloadFileResponse {

    UrlResource resource;
    String contentDisposition;
}
