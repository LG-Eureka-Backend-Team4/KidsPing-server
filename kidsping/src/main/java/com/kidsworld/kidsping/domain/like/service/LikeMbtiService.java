package com.kidsworld.kidsping.domain.like.service;

import com.kidsworld.kidsping.domain.like.dto.request.DislikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.DislikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.response.LikeBookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeMbtiService {
    void like(LikeMbtiRequest likeMbtiRequest);

    void likeCancel(LikeCancelMbtiRequest likeCancelMbtiRequest);

    void dislike(DislikeMbtiRequest disLikeMbtiRequest);

    void dislikeCancel(DislikeCancelMbtiRequest dislikeCancelMbtiRequest);

    Page<LikeBookResponse> getBooksLiked(Long kidId, Pageable pageable);
}
