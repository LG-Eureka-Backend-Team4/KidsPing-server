package com.kidsworld.kidsping.domain.like.service;

import com.kidsworld.kidsping.domain.like.dto.request.DislikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.DislikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeMbtiRequest;

public interface LikeMbtiService {
    void like(LikeMbtiRequest likeMbtiRequest);

    void likeCancel(LikeCancelMbtiRequest likeCancelMbtiRequest);

    void dislike(DislikeMbtiRequest disLikeMbtiRequest);

    void dislikeCancel(DislikeCancelMbtiRequest dislikeCancelMbtiRequest);
}
