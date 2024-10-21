package com.kidsworld.kidsping.domain.kid.controller;

import com.kidsworld.kidsping.domain.kid.dto.request.KidMBTIDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kid")
public class KidController {

    private final KidService kidService;

    @PostMapping("/mbti/diagonosis")
    public void diagnoseKidMBTI(@RequestBody KidMBTIDiagnosisRequest diagnosisRequest) {
        kidService.diagnoseKidMBTI(diagnosisRequest);
    }
}