package com.kidsworld.kidsping.global.cache;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class CachedPage<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CachedPage(@JsonProperty("content") List<T> content,
                      @JsonProperty("number") int page,
                      @JsonProperty("size") int size,
                      @JsonProperty("totalElements") long total) {
        super(content, PageRequest.of(page, size), total);
    }

    public CachedPage(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }
}