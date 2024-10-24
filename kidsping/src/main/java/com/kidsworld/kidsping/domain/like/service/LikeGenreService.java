package com.kidsworld.kidsping.domain.like.service;

public interface LikeGenreService {

    void like(Long kidId, Long bookId);
    void likeCancel(Long kidId, Long bookId);
    void dislike(Long kidId, Long bookId);
    void dislikeCancel(Long kidId, Long bookId);
}
