package com.kidsworld.kidsping.domain.genre.service.impl;

import com.kidsworld.kidsping.domain.genre.dto.response.TopGenreResponse;
import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.domain.genre.entity.GenreScore;
import com.kidsworld.kidsping.domain.genre.repository.GenreRepository;
import com.kidsworld.kidsping.domain.genre.repository.GenreScoreRepository;
import com.kidsworld.kidsping.domain.genre.service.GenreScoreService;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreScoreServiceImpl implements GenreScoreService {

    private final GenreScoreRepository genreScoreRepository;
    private final KidRepository kidRepository;
    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public void addGenreScore(Long kidId, List<Long> genreIds) {
        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_KID));

        List<Genre> allGenres = genreRepository.findAll();
        for (Genre genre : allGenres) {
            GenreScore genreScore = genreScoreRepository.findByKidAndGenre(kid, genre)
                    .orElseGet(() -> GenreScore.builder()
                            .kid(kid)
                            .genre(genre)
                            .score(0)
                            .isDeleted(false)
                            .build());
            genreScoreRepository.save(genreScore);
        }

        for (Long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_GENRE));

            GenreScore genreScore = genreScoreRepository.findByKidAndGenre(kid, genre)
                    .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_GENRE));

            genreScore.updateScore(5);
            genreScoreRepository.save(genreScore);
        }
    }

    @Override
    public void updateScore(Kid kid, Genre genre, LikeStatus previousStatus, LikeStatus newStatus) {
        GenreScore genreScore = genreScoreRepository.findByKidAndGenre(kid, genre)
                .orElseGet(() -> GenreScore.builder()
                        .kid(kid)
                        .genre(genre)
                        .score(0)
                        .isDeleted(false)
                        .build());

        int scoreChange = calculateScoreChange(previousStatus, newStatus);
        genreScore.updateScore(scoreChange);

        genreScoreRepository.save(genreScore);
    }

    private int calculateScoreChange(LikeStatus previousStatus, LikeStatus newStatus) {
        if (previousStatus == LikeStatus.LIKE && newStatus == LikeStatus.DISLIKE) {
            return -4;  // 좋아요에서 싫어요로: -4점
        } else if (previousStatus == LikeStatus.DISLIKE && newStatus == LikeStatus.LIKE) {
            return +4;  // 싫어요에서 좋아요로: +4점
        } else if (newStatus == LikeStatus.LIKE) {
            return +2;  // 처음 좋아요: +2점
        }else if (newStatus == LikeStatus.DISLIKE) {
            return -2;  // 처음 싫어요: -2점
        } else if (newStatus == LikeStatus.CANCEL && previousStatus == LikeStatus.LIKE) {
            return -2;  // 좋아요 취소: -2점
        } else if (newStatus == LikeStatus.CANCEL && previousStatus == LikeStatus.DISLIKE) {
            return +2;  // 싫어요 취소: +2점
        }
        return 0;
    }
  
    @Transactional(readOnly = true)
    public TopGenreResponse getTopGenre(Long kidId) {
        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_KID));

        GenreScore topGenreScore = genreScoreRepository.findTopGenreByKidId(kidId)
                .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_GENRE, "아이의 장르 점수가 존재하지 않습니다"));

        return TopGenreResponse.from(topGenreScore.getGenre(), topGenreScore);
    }

    // 재진단 장르점수 초기화
    @Override
    @Transactional
    public void resetGenreScoreForKid(Long kidId) {
        List<Long> genreScoreIds = genreScoreRepository.findGenreScoreIdsByKidId(kidId);
        genreScoreRepository.deleteGenreScores(genreScoreIds);
    }

}
