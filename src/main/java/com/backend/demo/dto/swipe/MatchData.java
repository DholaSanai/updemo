package com.backend.demo.dto.swipe;

public interface MatchData {
    String getId();

    String getUserId();

    String getMatchedUserId();

    String getUserChat();
    Integer getUnreadMessages();

    String getRoomOwner();

    String getEmail();

    String getName();

    String getProfileImage();

    String getUpdatedAt();
}
