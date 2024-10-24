package com.kidsworld.kidsping.domain.like.service.impl;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.genre.service.GenreScoreService;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.like.entity.LikeGenre;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import com.kidsworld.kidsping.domain.like.repository.LikeGenreRepository;
import com.kidsworld.kidsping.domain.like.service.LikeGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeGenreServiceImpl implements LikeGenreService {

    private final LikeGenreRepository likeGenreRepository;
    private final KidRepository kidRepository;
    private final BookRepository bookRepository;
    private final GenreScoreService genreScoreService;



    // 좋아요 처리
    @Override
    public void like(Long kidId, Long bookId) {
        Kid kid = findKidByKidId(kidId);
        Book book = findBookById(bookId);

        LikeGenre like = handleLikeOrDislike(kid, book, LikeStatus.LIKE);

        genreScoreService.updateScore(kid, book.getGenre(), like.getPreviousLikeStatus(), LikeStatus.LIKE);
    }

    // 좋아요 취소 처리
    @Override
    public void likeCancel(Long kidId, Long bookId) {
        Kid kid = findKidByKidId(kidId);
        Book book = findBookById(bookId);
        LikeGenre like = findLikeGenreByKidAndBook(kid, book);

        if (like.getLikeStatus() == LikeStatus.LIKE) {
            genreScoreService.updateScore(kid, book.getGenre(), LikeStatus.LIKE, LikeStatus.CANCEL);
            likeGenreRepository.delete(like);
        }
    }

    // 싫어요 처리
    @Override
    public void dislike(Long kidId, Long bookId) {
        Kid kid = findKidByKidId(kidId);
        Book book = findBookById(bookId);
        LikeGenre dislike = handleLikeOrDislike(kid, book, LikeStatus.DISLIKE);

        genreScoreService.updateScore(kid, book.getGenre(), dislike.getPreviousLikeStatus(), LikeStatus.DISLIKE);
    }

    // 싫어요 취소 처리
    @Override
    public void dislikeCancel(Long kidId, Long bookId) {
        Kid kid = findKidByKidId(kidId);
        Book book = findBookById(bookId);
        LikeGenre dislike = findLikeGenreByKidAndBook(kid, book);

        if (dislike.getLikeStatus() == LikeStatus.DISLIKE) {
            genreScoreService.updateScore(kid, book.getGenre(), LikeStatus.DISLIKE, LikeStatus.CANCEL);
            likeGenreRepository.delete(dislike);
        }
    }

    private LikeGenre createLike(Kid kid, Book book) {
        LikeGenre likeGenre = LikeGenre.builder()
                .likeStatus(LikeStatus.LIKE)
                .kid(kid)
                .book(book)
                .previousLikeStatus(LikeStatus.LIKE)
                .build();
        return likeGenreRepository.save(likeGenre);
    }

    private LikeGenre createDisLike(Kid kid, Book book) {
        LikeGenre likeGenre = LikeGenre.builder()
                .likeStatus(LikeStatus.DISLIKE)
                .kid(kid)
                .book(book)
                .previousLikeStatus(LikeStatus.DISLIKE)
                .build();
        return likeGenreRepository.save(likeGenre);
    }

    private LikeGenre handleLikeOrDislike(Kid kid, Book book, LikeStatus requestedStatus) {
        Optional<LikeGenre> existingLike = likeGenreRepository.findByKidIdAndBookId(kid.getId(), book.getId());
        if (existingLike.isPresent()) {
            LikeGenre currentLike = existingLike.get();
            currentLike.savePreviousLikeStatus(currentLike.getLikeStatus());
            // 요청한 상태가 현재 상태와 같다면 예외를 발생시킴
            if (currentLike.getLikeStatus() == requestedStatus) {
                throw new RuntimeException("이미 이 상태입니다.");
            }
            // 현재 상태를 요청 상태로 변경
            currentLike.changeLikeStatus(requestedStatus);
            return currentLike;
        }
        // 요청한 상태에 따라 새로운 LikeGenre 생성
        return requestedStatus == LikeStatus.LIKE ? createLike(kid, book) : createDisLike(kid, book);
    }


    private LikeGenre findLikeGenreByKidAndBook(Kid kid, Book book) {
        return likeGenreRepository.findByKidIdAndBookId(kid.getId(), book.getId())
                .orElseThrow(() -> new RuntimeException("해당 좋아요/싫어요가 없습니다."));
    }

    private Kid findKidByKidId(Long kidId) {
        return kidRepository.findById(kidId)
                .orElseThrow(() -> new RuntimeException("해당 사용자가 없습니다."));
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("해당 도서가 없습니다."));
    }
}
