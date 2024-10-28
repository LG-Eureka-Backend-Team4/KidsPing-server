package com.kidsworld.kidsping.global.common.controller;

import com.kidsworld.kidsping.global.common.dto.DownloadFileResponse;
import com.kidsworld.kidsping.global.common.service.FileQueryService;
import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileQueryController {

    private final FileQueryService fileQueryService;

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadAttachFile(@RequestParam String storeFileName,
                                                       @RequestParam String originalFileName)
            throws MalformedURLException {

        DownloadFileResponse downloadFileResponse = fileQueryService.downloadAttachFile(storeFileName,
                originalFileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, downloadFileResponse.getContentDisposition())
                .body(downloadFileResponse.getResource());
    }
}