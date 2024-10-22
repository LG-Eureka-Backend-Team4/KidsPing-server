package com.kidsworld.kidsping.domain.like.service;

import com.kidsworld.kidsping.domain.like.dto.request.LikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeMbtiRequest;

public interface LikeMbtiService {
    void like(LikeMbtiRequest likeMbtiRequest);

    void likeCancel(LikeCancelMbtiRequest likeCancelMbtiRequest);
}
