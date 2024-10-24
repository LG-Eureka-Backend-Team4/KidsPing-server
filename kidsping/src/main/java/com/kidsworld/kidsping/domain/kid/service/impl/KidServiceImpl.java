package com.kidsworld.kidsping.domain.kid.service.impl;


import com.kidsworld.kidsping.domain.kid.dto.response.*;
import com.kidsworld.kidsping.domain.kid.exception.MaxKidLimitReachedException;
import com.kidsworld.kidsping.domain.kid.exception.NotFoundKidException;
import com.kidsworld.kidsping.domain.user.exception.UnauthorizedUserException;
import com.kidsworld.kidsping.domain.kid.dto.request.CreateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.KidMbtiDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.UpdateKidRequest;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.entity.KidMbtiHistory;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.kid.repository.KidMbtiHistoryRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidMbtiRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.domain.question.entity.MbtiAnswer;
import com.kidsworld.kidsping.domain.question.repository.MbtiAnswerRepository;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import com.kidsworld.kidsping.global.common.dto.MbtiScore;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import com.kidsworld.kidsping.global.util.MbtiCalculator;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KidServiceImpl implements KidService {

    private final KidRepository kidRepository;
    private final MbtiAnswerRepository mbtiAnswerRepository;
    private final UserRepository userRepository;
    private final KidMbtiRepository kidMBTIRepository;
    private final KidMbtiHistoryRepository kidMBTIHistoryRepository;


    /*
    자녀 프로필 생성
    */
    @Override
    @Transactional
    public CreateKidResponse createKid(CreateKidRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UnauthorizedUserException::new);

        long userKidCount = kidRepository.countByUserId(user.getId());
        if (userKidCount >= 5) {
            throw new MaxKidLimitReachedException();
        }

        Kid kid = Kid.builder()
                .gender(Gender.valueOf(request.getGender()))
                .name(request.getKidName())
                .birth(LocalDate.parse(request.getBirth()))
                .isDeleted(false)
                .user(user)
                .build();

        Kid savedKid = kidRepository.save(kid);

        return CreateKidResponse.from(savedKid);
    }

    /*
    자녀 프로필 조회
    */
    @Override
    public GetKidResponse getKid(Long kidId) {

        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(NotFoundKidException::new);

        return new GetKidResponse(kid);
    }

    /*
    자녀 프로필 수정
    */
    @Override
    @Transactional
    public UpdateKidResponse updateKid(Long kidId, UpdateKidRequest request) {
        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(NotFoundKidException::new);

        kid.update(
                Gender.valueOf(request.getGender()),
                request.getKidName(),
                LocalDate.parse(request.getBirth())
        );

        return UpdateKidResponse.from(kid);
    }

    /*
    자녀 프로필 삭제
    */
    @Override
    @Transactional
    public DeleteKidResponse deleteKid(Long kidId) {
        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(NotFoundKidException::new);
        kidRepository.delete(kid);
        return new DeleteKidResponse(kidId);
    }


    /*
    자녀 성향 진단
    */
    @Transactional
    @Override
    public void diagnoseKidMbti(KidMbtiDiagnosisRequest diagnosisRequest) {
        Kid kid = findKidById(diagnosisRequest.getUserId());
        saveMbtiResponse(diagnosisRequest, kid);

        MbtiScore mbtiScore = MbtiScore.from(diagnosisRequest);
        MbtiStatus mbtiStatus = MbtiCalculator.determineMbtiType(mbtiScore);
        updateOrCreateKidMbti(kid, diagnosisRequest, mbtiStatus);

        saveKidMbtiHistory(kid, mbtiStatus);
    }

    private Kid findKidById(Long userId) {
        return kidRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("no kid"));
    }

    private void saveMbtiResponse(KidMbtiDiagnosisRequest diagnosisRequest, Kid kid) {
        MbtiAnswer mbtiAnswer = KidMbtiDiagnosisRequest.getMBTIResponse(diagnosisRequest, kid);
        mbtiAnswerRepository.save(mbtiAnswer);
    }

    private void updateOrCreateKidMbti(Kid kid, KidMbtiDiagnosisRequest diagnosisRequest, MbtiStatus mbtiStatus) {
        KidMbti kidMbti = kid.getKidMbti();
        if (kidMbti == null) {
            kidMbti = createKidMbti(diagnosisRequest, mbtiStatus);
        } else {
            updateKidMbti(kidMbti, diagnosisRequest, mbtiStatus);
        }
        kid.updateKidMbti(kidMbti);
    }

    private KidMbti createKidMbti(KidMbtiDiagnosisRequest diagnosisRequest, MbtiStatus mbtiStatus) {
        KidMbti kidMbti = KidMbti.builder()
                .eScore(diagnosisRequest.getExtraversionScore())
                .iScore(diagnosisRequest.getIntroversionScore())
                .sScore(diagnosisRequest.getSensingScore())
                .nScore(diagnosisRequest.getIntuitionScore())
                .fScore(diagnosisRequest.getFeelingScore())
                .tScore(diagnosisRequest.getThinkingScore())
                .jScore(diagnosisRequest.getJudgingScore())
                .pScore(diagnosisRequest.getPerceivingScore())
                .mbtiStatus(mbtiStatus)
                .build();
        return kidMBTIRepository.save(kidMbti);
    }

    private void updateKidMbti(KidMbti kidMbti, KidMbtiDiagnosisRequest diagnosisRequest, MbtiStatus mbtiStatus) {
        kidMbti.updateMbtiScore(
                diagnosisRequest.getExtraversionScore(),
                diagnosisRequest.getIntroversionScore(),
                diagnosisRequest.getSensingScore(),
                diagnosisRequest.getIntuitionScore(),
                diagnosisRequest.getThinkingScore(),
                diagnosisRequest.getFeelingScore(),
                diagnosisRequest.getJudgingScore(),
                diagnosisRequest.getPerceivingScore(),
                mbtiStatus
        );
    }

    private void saveKidMbtiHistory(Kid kid, MbtiStatus mbtiStatus) {
        KidMbtiHistory kidMbtiHistory = KidMbtiHistory.builder()
                .kid(kid)
                .mbtiStatus(mbtiStatus)
                .isDeleted(false)
                .build();
        kidMBTIHistoryRepository.save(kidMbtiHistory);
    }



    /*
    자녀 성향 히스토리 조회
    */
    @Override
    public List<GetKidMbtiHistoryResponse> getKidMbtiHistory(Long kidId) {
        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(NotFoundKidException::new);

        List<KidMbtiHistory> histories = kidMBTIHistoryRepository.findActiveHistories(kid);

        return histories.stream()
                .map(GetKidMbtiHistoryResponse::new)
                .collect(Collectors.toList());
    }


}