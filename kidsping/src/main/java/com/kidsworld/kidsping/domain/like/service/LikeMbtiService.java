package com.kidsworld.kidsping.domain.like.service;

import com.kidsworld.kidsping.domain.like.dto.response.LikeBookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface LikeMbtiService {
    void like(Long kidId, Long bookId);

    void likeCancel(Long kidId, Long bookId);

    void dislike(Long kidId, Long bookId);

    void dislikeCancel(Long kidId, Long bookId);

    Page<LikeBookResponse> getBooksLiked(Long kidId, Pageable pageable);

    // 재진단 장르점수 초기화
    @Transactional
    void resetMbtiLikesForKid(Long kidId);
}
