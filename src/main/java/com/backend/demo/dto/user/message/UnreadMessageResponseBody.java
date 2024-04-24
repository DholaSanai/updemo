package com.backend.demo.dto.user.message;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UnreadMessageResponseBody extends UnreadMessageRequestBody {
    private Integer unreadMessageCount;
}
