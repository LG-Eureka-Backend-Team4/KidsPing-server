package com.kidsworld.kidsping.domain.like.service.impl;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.entity.KidMbtiHistory;
import com.kidsworld.kidsping.domain.kid.repository.KidMbtiHistoryRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.like.dto.request.LikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.entity.LikeMbti;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import com.kidsworld.kidsping.domain.like.repository.LikeMbtiRepository;
import com.kidsworld.kidsping.domain.like.service.LikeMbtiService;
import com.kidsworld.kidsping.global.common.dto.MbtiScore;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import com.kidsworld.kidsping.global.util.MbtiCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeMbtiServiceImpl implements LikeMbtiService {

    private final LikeMbtiRepository likeMbtiRepository;
    private final BookRepository bookRepository;
    private final KidRepository kidRepository;
    private final KidMbtiHistoryRepository kidMbtiHistoryRepository;

    @Transactional
    @Override
    public void like(LikeMbtiRequest likeMbtiRequest) {
        Kid kid = findKidByKidId(likeMbtiRequest.getKidId());
        Book book = findBookByBookId(likeMbtiRequest.getBookId());
        checkIfAlreadyLiked(kid, book);
        LikeMbti like = createLike(kid, book);

        KidMbti kidMbti = kid.getKidMbti();
        MbtiStatus currentKidMbtiStatus = kidMbti.getMbtiStatus();

        MbtiScore mbtiScore = MbtiScore.from(kidMbti);
        mbtiScore.updateMbtiScore(book.getBookMbti(), like.getLikeStatus(), LikeStatus.LIKE);
        MbtiStatus updatedKidMbtiStatus = MbtiCalculator.determineMbtiType(mbtiScore);

        kidMbti.updateMbti(mbtiScore, updatedKidMbtiStatus);
        if (currentKidMbtiStatus != updatedKidMbtiStatus) {
            createKidMBTIHistory(kid, updatedKidMbtiStatus);
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
            createKidMBTIHistory(kid, updatedKidMbtiStatus);
        }
        likeMbtiRepository.delete(like);
    }

    private LikeMbti findLikeMbtiByKidAndBook(Kid kid, Book book) {
        return likeMbtiRepository.findLikeMbtiByKidAndBook(kid, book)
                .orElseThrow(() -> new RuntimeException("취소할 좋아요가 없습니다."));
    }

    private Kid findKidByKidId(Long kidId) {
        return kidRepository.findKidWithMbtiByKidId(kidId)
                .orElseThrow(() -> new RuntimeException("no kid"));
    }

    private Book findBookByBookId(Long bookId) {
        return bookRepository.findBookWithMbtiByBookId(bookId)
                .orElseThrow(() -> new RuntimeException("no book"));
    }

    private LikeMbti createLike(Kid kid, Book book) {
        LikeMbti likeMbti = LikeMbti.builder()
                .likeStatus(LikeStatus.LIKE)
                .kid(kid)
                .book(book)
                .build();
        return likeMbtiRepository.save(likeMbti);
    }

    private void checkIfAlreadyLiked(Kid kid, Book book) {
        if (likeMbtiRepository.findByKidIdAndBookId(kid.getId(), book.getId())
                .isPresent()) {

            throw new RuntimeException("이미 좋아요 했어요");
        }
    }

    private void createKidMBTIHistory(Kid kid, MbtiStatus mbtiStatus) {
        KidMbtiHistory kidMbtiHistory = KidMbtiHistory.builder()
                .kid(kid)
                .mbtiStatus(mbtiStatus)
                .isDeleted(false)
                .build();
        kidMbtiHistoryRepository.save(kidMbtiHistory);
    }
}
