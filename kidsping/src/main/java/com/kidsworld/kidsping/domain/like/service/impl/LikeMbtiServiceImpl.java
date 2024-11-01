package com.kidsworld.kidsping.domain.like.service.impl;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.entity.KidMbtiHistory;
import com.kidsworld.kidsping.domain.kid.repository.KidMbtiHistoryRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.like.dto.request.DislikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.DislikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.response.LikeBookResponse;
import com.kidsworld.kidsping.domain.like.entity.LikeMbti;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import com.kidsworld.kidsping.domain.like.exception.EmpathyStatusConflictException;
import com.kidsworld.kidsping.domain.like.repository.LikeMbtiRepository;
import com.kidsworld.kidsping.domain.like.service.LikeMbtiService;
import com.kidsworld.kidsping.global.common.entity.MbtiScore;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.custom.NotFoundException;
import com.kidsworld.kidsping.global.util.MbtiCalculator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    @Override
    public Page<LikeBookResponse> getBooksLiked(Long kidId, Pageable pageable) {
        Page<LikeMbti> bookPage = likeMbtiRepository.findBooksByLikeMbtiId(kidId, pageable, LikeStatus.LIKE);
        return bookPage.map(likeMbti -> LikeBookResponse.from(likeMbti.getBook()));
    }

    /**
     * 자녀가 도서를 좋아요 하는 메서드
     *
     * @param likeMbtiRequest 자녀의 id와 좋아요할 도서의 id 데이터를 담고 있는 객체
     */
    @Override
    public void like(LikeMbtiRequest likeMbtiRequest) {
        Kid kid = findKidByKidId(likeMbtiRequest.getKidId());
        Book book = findBookByBookId(likeMbtiRequest.getBookId());
        LikeMbti like = handleLikeOrDislike(kid, book, LikeStatus.LIKE);

        updateKidMbtiScoreAndStatus(kid, book, like, LikeStatus.LIKE);
    }

    /**
     * 자녀가 도서 좋아요를 취소 하는 메서드
     *
     * @param likeCancelMbtiRequest 자녀의 id와 좋아요할 도서의 id 데이터를 담고 있는 객체
     */
    @Override
    public void likeCancel(LikeCancelMbtiRequest likeCancelMbtiRequest) {
        Kid kid = findKidByKidId(likeCancelMbtiRequest.getKidId());
        Book book = findBookByBookId(likeCancelMbtiRequest.getBookId());
        LikeMbti like = findLikeMbtiByKidAndBook(kid, book);

        cancelLikeAndUpdateKidMbti(kid, like, LikeStatus.LIKE, book);
    }

    /**
     * 자녀가 도서 싫어요를 하는 메서드
     *
     * @param disLikeMbtiRequest 자녀의 id와 좋아요할 도서의 id 데이터를 담고 있는 객체
     */
    @Override
    public void dislike(DislikeMbtiRequest disLikeMbtiRequest) {
        Kid kid = findKidByKidId(disLikeMbtiRequest.getKidId());
        Book book = findBookByBookId(disLikeMbtiRequest.getBookId());
        LikeMbti disLike = handleLikeOrDislike(kid, book, LikeStatus.DISLIKE);

        updateKidMbtiScoreAndStatus(kid, book, disLike, LikeStatus.DISLIKE);
    }

    /**
     * 자녀가 도서 싫어요를 취소 하는 메서드
     *
     * @param dislikeCancelMbtiRequest 자녀의 id와 좋아요할 도서의 id 데이터를 담고 있는 객체
     */
    @Override
    public void dislikeCancel(DislikeCancelMbtiRequest dislikeCancelMbtiRequest) {
        Kid kid = findKidByKidId(dislikeCancelMbtiRequest.getKidId());
        Book book = findBookByBookId(dislikeCancelMbtiRequest.getBookId());
        LikeMbti like = findLikeMbtiByKidAndBook(kid, book);

        cancelLikeAndUpdateKidMbti(kid, like, LikeStatus.DISLIKE, book);
    }

    /**
     * 자녀가 공감한 상태와 예상 공감 상태가 같으면 공감을 취소하고 공감 취소로 인한 점수와 MBTI 상태를 업데이트하고 자녀의 MBTI가 바뀌면 MBTI 히스토리를 생성하는 메서드
     *
     * @param kid                자녀 엔티티
     * @param currentLikeMbti    자녀가 공감한 좋아요, 싫어요 엔티티
     * @param expectedLikeStatus 자녀가 어떤 공감을 했는지에 대한 정보를 담고 있는 객체(좋아요인지, 싫어요인지)
     * @param book               도서 엔티티
     */
    private void cancelLikeAndUpdateKidMbti(Kid kid, LikeMbti currentLikeMbti, LikeStatus expectedLikeStatus,
                                            Book book) {
        KidMbti currentKidMbti = kid.getKidMbti();
        MbtiStatus currentKidMbtiStatus = currentKidMbti.getMbtiStatus();

        MbtiScore kidMbtiScore = MbtiScore.from(currentKidMbti);
        if (currentLikeMbti.getLikeStatus() == expectedLikeStatus) {
            kidMbtiScore.updateMbtiScore(book.getBookMbti(), currentLikeMbti.getLikeStatus(), LikeStatus.CANCEL);
            MbtiStatus updatedKidMbtiStatus = MbtiCalculator.determineMbtiType(kidMbtiScore);

            currentKidMbti.updateMbti(kidMbtiScore, updatedKidMbtiStatus);
            updateKidMbtiHistoryIfChanged(currentKidMbtiStatus, updatedKidMbtiStatus, kid);
            likeMbtiRepository.delete(currentLikeMbti);
        }
    }

    /**
     * 자녀가 좋아요 혹은 싫어요한 도서의 MBTI를 바탕으로 자녀의 MBTI 점수와 MBTI 상태를 변경하는 메서드
     *
     * @param kid                자녀 엔티티
     * @param book               도서 엔티티
     * @param likeMbti           자녀가 이전에 공감한 정보를 담고 있는 엔티티
     * @param expectedLikeStatus 자녀의 변경된 MBTI 상태 정보를 담고 있는 객체
     */
    private void updateKidMbtiScoreAndStatus(Kid kid, Book book, LikeMbti likeMbti, LikeStatus expectedLikeStatus) {
        KidMbti kidMbti = kid.getKidMbti();
        MbtiStatus currentKidMbtiStatus = kidMbti.getMbtiStatus();

        MbtiScore currentMbtiScore = MbtiScore.from(kidMbti);
        currentMbtiScore.updateMbtiScore(book.getBookMbti(), likeMbti.getPreviousLikeStatus(), expectedLikeStatus);
        MbtiStatus updatedKidMbtiStatus = MbtiCalculator.determineMbtiType(currentMbtiScore);

        kidMbti.updateMbti(currentMbtiScore, updatedKidMbtiStatus);
        updateKidMbtiHistoryIfChanged(currentKidMbtiStatus, updatedKidMbtiStatus, kid);
    }

    /**
     * 자녀의 MBTI가 바뀌면 MBTI 히스토리를 생성하는 메서드
     *
     * @param currentKidMbtiStatus 자녀의 현재 MBTI 상태 정보를 담고 있는 객체
     * @param updatedKidMbtiStatus 자녀의 변경된 MBTI 상태 정보를 담고 있는 객체
     * @param kid                  자녀 엔티티
     */
    private void updateKidMbtiHistoryIfChanged(MbtiStatus currentKidMbtiStatus, MbtiStatus updatedKidMbtiStatus,
                                               Kid kid) {
        if (currentKidMbtiStatus != updatedKidMbtiStatus) {
            createKidMbtiHistory(kid, updatedKidMbtiStatus);
        }
    }

    /**
     * 기존 자녀의 공감이 존재하면 자녀의 공감 상태를 변경하고 공감이 없으면 새로운 공감을 만드는 메서드
     *
     * @param kid             자녀 엔티티
     * @param book            도서 엔티티
     * @param requestedStatus 자녀가 현재 공감한 정보를 담고 있는 객체
     */
    private LikeMbti handleLikeOrDislike(Kid kid, Book book, LikeStatus requestedStatus) {
        Optional<LikeMbti> existingLike = likeMbtiRepository.findByKidIdAndBookId(kid.getId(), book.getId());
        if (existingLike.isPresent()) {
            LikeMbti currentLike = existingLike.get();
            currentLike.savePreviousLikeStatus(currentLike.getLikeStatus());
            // 자녀의 현재 공감 상태와 요청 공감 상태가 동일한 경우 예외 발생
            if (currentLike.getLikeStatus() == requestedStatus) {
                throw new EmpathyStatusConflictException(ExceptionCode.DUPLICATE_EMPATHY_STATUS);
            }
            // 현재 공감 상태를 요청 공감 상태로 변경
            currentLike.changeLikeStatus(requestedStatus);
            return currentLike;
        }
        // 공감한적이 없으면 새로운 좋아요 또는 싫어요 생성
        return requestedStatus == LikeStatus.LIKE ? createLike(kid, book) : createDisLike(kid, book);
    }

    /**
     * 자녀 엔티티를 조회하는 메서드
     *
     * @param kidId 자녀 엔티티의 id 값
     */
    private Kid findKidByKidId(Long kidId) {
        return kidRepository.findKidWithMbtiByKidId(kidId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_KID_WITH_MBTI));
    }

    /**
     * 도서 엔티티 조회하는 메서드
     *
     * @param bookId 도서 엔티티의 id 값
     */
    private Book findBookByBookId(Long bookId) {
        return bookRepository.findBookWithMbtiByBookId(bookId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_BOOK_WITH_MBTI));
    }

    /**
     * 도서에 대한 공감 정보를 조회하는 메서드
     *
     * @param kid  자녀 엔티티
     * @param book 도서 엔티티
     */
    private LikeMbti findLikeMbtiByKidAndBook(Kid kid, Book book) {
        return likeMbtiRepository.findLikeMbtiByKidAndBook(kid, book)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_EMPATHY));
    }

    /**
     * 좋아요를 생성하는 메서드
     *
     * @param kid  자녀 엔티티
     * @param book 도서 엔티티
     */
    private LikeMbti createLike(Kid kid, Book book) {
        LikeMbti likeMbti = LikeMbti.builder()
                .likeStatus(LikeStatus.LIKE)
                .kid(kid)
                .book(book)
                .previousLikeStatus(LikeStatus.LIKE)
                .build();
        return likeMbtiRepository.save(likeMbti);
    }

    /**
     * 싫어요를 생성하는 메서드
     *
     * @param kid  자녀 엔티티
     * @param book 도서 엔티티
     */
    private LikeMbti createDisLike(Kid kid, Book book) {
        LikeMbti likeMbti = LikeMbti.builder()
                .likeStatus(LikeStatus.DISLIKE)
                .kid(kid)
                .book(book)
                .previousLikeStatus(LikeStatus.DISLIKE)
                .build();
        return likeMbtiRepository.save(likeMbti);
    }

    /**
     * 자녀의 MBTI 히스토리를 생성하는 메서드
     *
     * @param kid        자녀 엔티티
     * @param mbtiStatus 자녀 mbti 상태 데이터를 담고 있는 객체
     */
    private void createKidMbtiHistory(Kid kid, MbtiStatus mbtiStatus) {
        KidMbtiHistory kidMbtiHistory = KidMbtiHistory.builder()
                .kid(kid)
                .mbtiStatus(mbtiStatus)
                .isDeleted(false)
                .build();
        kidMbtiHistoryRepository.save(kidMbtiHistory);
    }

    // 재진단 장르점수 초기화
    @Override
    @Transactional
    public void resetMbtiLikesForKid(Long kidId) {
        likeMbtiRepository.deleteLikeMbtisByKidId(kidId);
    }
}
