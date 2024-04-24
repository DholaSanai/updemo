package com.backend.demo.dto.hubspot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class HubspotRequestBody {
    private HubspotProperties properties;
}
