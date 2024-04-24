package com.backend.demo.firebase.swipe.firebaseModels;

import com.backend.demo.dto.user.chillowUser.ChillowImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FirebaseSwipeMatchModel {
    private Boolean isMatched;
    private Boolean isSwiped;
    private String matchedId;
    private String userId;
    private String matchedUserId;
    private String matchedUserName;
    private String user1;
    private String user2;
    private String isRoomOwner;
    private ChillowImageDto userImage;
    private ChillowImageDto matchedUserImage;
    private Boolean isConsumed;
}
