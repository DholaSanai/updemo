package com.backend.demo.dto.swipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwipeDto {
    private String id;
    private String userId;
    private String shownUserId;
    private Boolean isLiked;
    private Boolean isSwipedRight;
    private Boolean isSwipedLeft;
}
