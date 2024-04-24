package com.backend.demo.dto.entrata.property;

import com.backend.demo.dto.entrata.sharedClasses.Auth;
import com.backend.demo.dto.entrata.sharedClasses.Method;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class GetEntrataPropertiesRequestBody {
    private Auth auth;
    private Integer requestId;
    private Method method;
}
