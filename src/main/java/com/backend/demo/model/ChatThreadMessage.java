package com.backend.demo.model;

import java.time.LocalDateTime;

public interface ChatThreadMessage {
    String getId();

    String getUserChat();

    String getMessageHtml();

    String getSenderId();

    LocalDateTime getTimestamp();

    Boolean getIsReadByUser1();

    Boolean getIsReadByUser2();

    String getName();

    String getImage();
}
