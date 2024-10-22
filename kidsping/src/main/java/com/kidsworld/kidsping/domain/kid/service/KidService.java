package com.kidsworld.kidsping.domain.kid.service;

import com.kidsworld.kidsping.domain.kid.dto.request.KidCreateRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.KidUpdateRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.KidCreateResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidDeleteResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidGetResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidUpdateResponse;
import com.kidsworld.kidsping.domain.kid.dto.request.KidMBTIDiagnosisRequest;

public interface KidService {

    KidCreateResponse createKid(KidCreateRequest request);
    KidGetResponse getKid(Long kidId);
    KidUpdateResponse updateKid(Long kidId, KidUpdateRequest request);
    KidDeleteResponse deleteKid(Long kidId);
    void diagnoseKidMBTI(KidMBTIDiagnosisRequest diagnosisRequest);
}