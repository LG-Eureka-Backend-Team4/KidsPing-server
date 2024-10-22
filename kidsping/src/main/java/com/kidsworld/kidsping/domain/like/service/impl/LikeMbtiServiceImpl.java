package com.kidsworld.kidsping.domain.like.service.impl;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMBTI;
import com.kidsworld.kidsping.domain.kid.entity.KidMBTIHistory;
import com.kidsworld.kidsping.domain.kid.repository.KidMBTIHistoryRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
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
    private final KidMBTIHistoryRepository kidMBTIHistoryRepository;

    @Transactional
    @Override
    public void like(LikeMbtiRequest likeMbtiRequest) {
        Book book = findBookByBookId(likeMbtiRequest);
        Kid kid = findKidByKidId(likeMbtiRequest);
        checkIfAlreadyLiked(kid, book);
        LikeMbti like = createLike(kid, book);

        KidMBTI kidMbti = kid.getKidMbti();
        MbtiStatus currentKidMbtiStatus = kidMbti.getMbtiStatus();

        MbtiScore mbtiScore = MbtiScore.from(kidMbti);
        mbtiScore.updateMbtiScore(book.getBookMBTI(), like.getLikeStatus(), LikeStatus.LIKE);
        MbtiStatus updatedKidMbtiStatus = MbtiCalculator.determineMbtiType(mbtiScore);

        kidMbti.updateMbti(mbtiScore, updatedKidMbtiStatus);
        if (!currentKidMbtiStatus.equals(updatedKidMbtiStatus)) {
            createKidMBTIHistory(kid, updatedKidMbtiStatus);
        }
    }

    private Kid findKidByKidId(LikeMbtiRequest likeMbtiRequest) {
        return kidRepository.findKidWithMbtiByKidId(likeMbtiRequest.getKidId())
                .orElseThrow(() -> new RuntimeException("no kid"));
    }

    private Book findBookByBookId(LikeMbtiRequest likeMbtiRequest) {
        return bookRepository.findBookWithMbtiByBookId(likeMbtiRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("no  book"));
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
        KidMBTIHistory kidMbtiHistory = KidMBTIHistory.builder()
                .kid(kid)
                .mbtiStatus(mbtiStatus)
                .isDeleted(false)
                .build();
        kidMBTIHistoryRepository.save(kidMbtiHistory);
    }
}
