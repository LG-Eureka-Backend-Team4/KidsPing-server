package com.kidsworld.kidsping.domain.kid.service;


import com.kidsworld.kidsping.domain.kid.dto.request.KidMbtiDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.UpdateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.CreateKidResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.DeleteKidResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.GetKidMbtiHistoryResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.GetKidMbtiResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.GetKidResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.UpdateKidResponse;
import com.kidsworld.kidsping.domain.kid.entity.KidBadgeAwarded;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface KidService {

    CreateKidResponse createKid(String request, MultipartFile profileImage);

    GetKidResponse getKid(Long kidId);

    UpdateKidResponse updateKid(Long kidId, UpdateKidRequest request, MultipartFile profileImage);

    DeleteKidResponse deleteKid(Long kidId);

    List<GetKidMbtiHistoryResponse> getKidMbtiHistory(Long kidId);

    void diagnoseKidMbti(KidMbtiDiagnosisRequest diagnosisRequest);

    List<KidBadgeAwarded> getAwardedBadges(Long kidId);

    void deleteExpiredKid();

    GetKidMbtiResponse getKidMbti(Long kidId);
}