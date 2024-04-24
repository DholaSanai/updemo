package com.backend.demo.dto.user.broadcast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BroadcastRequestBody {
    private String message;
    private Object data;
}
