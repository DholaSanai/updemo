package com.backend.demo.dto.entrata.sendLeads;

import com.backend.demo.dto.entrata.sharedClasses.Auth;
import com.backend.demo.dto.entrata.sharedClasses.Method;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SendLeadRequest {
    private Auth auth;
    private String requestId;
    private Method method;
}
