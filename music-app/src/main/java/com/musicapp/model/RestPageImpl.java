package com.musicapp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestPageImpl<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestPageImpl(
            @JsonProperty("content") List<T> content,
            @JsonProperty("page") PageMetadata page) {
        super(content, PageRequest.of(
                        page != null ? page.number() : 0,
                        page != null ? page.size() : 10),
                page != null ? page.totalElements() : 0);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PageMetadata(
            @JsonProperty("size") int size,
            @JsonProperty("number") int number,
            @JsonProperty("totalElements") long totalElements,
            @JsonProperty("totalPages") int totalPages) {}
}