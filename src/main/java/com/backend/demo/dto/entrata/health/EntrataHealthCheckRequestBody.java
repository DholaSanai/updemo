package com.backend.demo.dto.entrata.health;

import com.backend.demo.dto.entrata.sharedClasses.Auth;
import com.backend.demo.dto.entrata.sharedClasses.Method;
import lombok.*;


@Setter
@Getter
@Builder
public class EntrataHealthCheckRequestBody {
    private Auth auth;
    private Integer requestId;
    private Method method;
}
