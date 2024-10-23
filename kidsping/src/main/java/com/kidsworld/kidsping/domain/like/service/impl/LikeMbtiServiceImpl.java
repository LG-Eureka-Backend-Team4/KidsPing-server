package com.kidsworld.kidsping.domain.like.service.impl;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.entity.KidMbtiHistory;
import com.kidsworld.kidsping.domain.kid.repository.KidMbtiHistoryRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.like.dto.request.DisLikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.entity.LikeMbti;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import com.kidsworld.kidsping.domain.like.repository.LikeMbtiRepository;
import com.kidsworld.kidsping.domain.like.service.LikeMbtiService;
import com.kidsworld.kidsping.global.common.dto.MbtiScore;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import com.kidsworld.kidsping.global.util.MbtiCalculator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikeMbtiServiceImpl implements LikeMbtiService {

    private final LikeMbtiRepository likeMbtiRepository;
    private final BookRepository bookRepository;
    private final KidRepository kidRepository;
    private final KidMbtiHistoryRepository kidMbtiHistoryRepository;

    @Override
    public void like(LikeMbtiRequest likeMbtiRequest) {
        Kid kid = findKidByKidId(likeMbtiRequest.getKidId());
        Book book = findBookByBookId(likeMbtiRequest.getBookId());
        LikeMbti like = handleLikeOrDislike(kid, book, LikeStatus.LIKE);

        KidMbti kidMbti = kid.getKidMbti();
        MbtiStatus currentKidMbtiStatus = kidMbti.getMbtiStatus();

        MbtiScore mbtiScore = MbtiScore.from(kidMbti);
        mbtiScore.updateMbtiScore(book.getBookMbti(), like.getPreviousLikeStatus(), LikeStatus.LIKE);
        MbtiStatus updatedKidMbtiStatus = MbtiCalculator.determineMbtiType(mbtiScore);

        kidMbti.updateMbti(mbtiScore, updatedKidMbtiStatus);
        if (currentKidMbtiStatus != updatedKidMbtiStatus) {
            createKidMbtiHistory(kid, updatedKidMbtiStatus);
        }
    }

    @Override
    public void likeCancel(LikeCancelMbtiRequest likeCancelMbtiRequest) {
        Kid kid = findKidByKidId(likeCancelMbtiRequest.getKidId());
        Book book = findBookByBookId(likeCancelMbtiRequest.getBookId());
        LikeMbti like = findLikeMbtiByKidAndBook(kid, book);

        KidMbti kidMbti = kid.getKidMbti();
        MbtiStatus currentKidMbtiStatus = kidMbti.getMbtiStatus();

        MbtiScore mbtiScore = MbtiScore.from(kidMbti);
        mbtiScore.updateMbtiScore(book.getBookMbti(), like.getLikeStatus(), LikeStatus.CANCEL);
        MbtiStatus updatedKidMbtiStatus = MbtiCalculator.determineMbtiType(mbtiScore);

        kidMbti.updateMbti(mbtiScore, updatedKidMbtiStatus);
        if (currentKidMbtiStatus != updatedKidMbtiStatus) {
            createKidMbtiHistory(kid, updatedKidMbtiStatus);
        }
        likeMbtiRepository.delete(like);
    }

    @Override
    public void dislike(DisLikeMbtiRequest disLikeMbtiRequest) {
        Kid kid = findKidByKidId(disLikeMbtiRequest.getKidId());
        Book book = findBookByBookId(disLikeMbtiRequest.getBookId());
        LikeMbti disLike = handleLikeOrDislike(kid, book, LikeStatus.DISLIKE);

        KidMbti kidMbti = kid.getKidMbti();
        MbtiStatus currentKidMbtiStatus = kidMbti.getMbtiStatus();

        MbtiScore mbtiScore = MbtiScore.from(kidMbti);
        mbtiScore.updateMbtiScore(book.getBookMbti(), disLike.getPreviousLikeStatus(), LikeStatus.DISLIKE);
        MbtiStatus updatedKidMbtiStatus = MbtiCalculator.determineMbtiType(mbtiScore);

        kidMbti.updateMbti(mbtiScore, updatedKidMbtiStatus);
        if (currentKidMbtiStatus != updatedKidMbtiStatus) {
            createKidMbtiHistory(kid, updatedKidMbtiStatus);
        }
    }

    private LikeMbti findLikeMbtiByKidAndBook(Kid kid, Book book) {
        return likeMbtiRepository.findLikeMbtiByKidAndBook(kid, book)
                .orElseThrow(() -> new RuntimeException("취소할 좋아요가 없습니다."));
    }

    private Kid findKidByKidId(Long kidId) {
        return kidRepository.findKidWithMbti(kidId)
                .orElseThrow(() -> new RuntimeException("no kid"));
    }

    private Book findBookByBookId(Long bookId) {
        return bookRepository.findBookWithMbti(bookId)
                .orElseThrow(() -> new RuntimeException("no book"));
    }

    private LikeMbti createLike(Kid kid, Book book) {
        LikeMbti likeMbti = LikeMbti.builder()
                .likeStatus(LikeStatus.LIKE)
                .kid(kid)
                .book(book)
                .previousLikeStatus(LikeStatus.LIKE)
                .build();
        return likeMbtiRepository.save(likeMbti);
    }

    private LikeMbti createDisLike(Kid kid, Book book) {
        LikeMbti likeMbti = LikeMbti.builder()
                .likeStatus(LikeStatus.DISLIKE)
                .kid(kid)
                .book(book)
                .previousLikeStatus(LikeStatus.DISLIKE)
                .build();
        return likeMbtiRepository.save(likeMbti);
    }

    private LikeMbti handleLikeOrDislike(Kid kid, Book book, LikeStatus requestedStatus) {
        Optional<LikeMbti> existingLike = likeMbtiRepository.findByKidIdAndBookId(kid.getId(), book.getId());
        if (existingLike.isPresent()) {
            LikeMbti currentLike = existingLike.get();
            currentLike.savePreviousLikeStatus(currentLike.getLikeStatus());
            // 현재 상태와 요청 상태가 동일한 경우 예외 발생
            if (currentLike.getLikeStatus() == requestedStatus) {
                throw new RuntimeException("이미 좋아요 또는 싫어요를 했어요");
            }
            // 현재 상태를 요청 상태로 수정
            currentLike.changeLikeStatus(requestedStatus);
            return currentLike;
        }
        // 새로운 좋아요 또는 싫어요 생성
        return requestedStatus == LikeStatus.LIKE ? createLike(kid, book) : createDisLike(kid, book);
    }

    private void createKidMbtiHistory(Kid kid, MbtiStatus mbtiStatus) {
        KidMbtiHistory kidMbtiHistory = KidMbtiHistory.builder()
                .kid(kid)
                .mbtiStatus(mbtiStatus)
                .isDeleted(false)
                .build();
        kidMbtiHistoryRepository.save(kidMbtiHistory);
    }
}