package com.backend.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SwipeRequestBody {
    @NotNull
    private String userId;
    @NotNull
    private String shownUserId;
    @NotNull
    private Boolean isLiked;
    @NotNull
    private Boolean isSwipedRight;
    @NotNull
    private Boolean isSwipedLeft;
}
