package com.backend.demo.dto.user.unsubscribeuserrequestbody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UnsubscribeUserRequestBody {
    @NotNull
    private String userId;
}
