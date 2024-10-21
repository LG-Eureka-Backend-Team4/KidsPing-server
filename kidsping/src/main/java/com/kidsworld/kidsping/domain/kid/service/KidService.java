package com.kidsworld.kidsping.domain.kid.service;

import com.kidsworld.kidsping.domain.kid.dto.request.KidMBTIDiagnosisRequest;

public interface KidService {
    void diagnoseKidMBTI(KidMBTIDiagnosisRequest diagnosisRequest);
}
