package com.kidsworld.kidsping.domain.kid.service;

import com.kidsworld.kidsping.domain.kid.dto.request.CreateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.KidMbtiDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.UpdateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface KidService {

    CreateKidResponse createKid(String request, MultipartFile profileImage);
    GetKidResponse getKid(Long kidId);

    UpdateKidResponse updateKid(Long kidId, UpdateKidRequest request, MultipartFile profileImage);

    DeleteKidResponse deleteKid(Long kidId);

    List<GetKidMbtiHistoryResponse> getKidMbtiHistory(Long kidId);


    void diagnoseKidMbti(KidMbtiDiagnosisRequest diagnosisRequest);
}