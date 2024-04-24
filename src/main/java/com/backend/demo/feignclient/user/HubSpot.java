package com.backend.demo.feignclient.user;

import com.backend.demo.config.FeignLoggingConfiguration;
import com.backend.demo.dto.hubspot.HubspotRequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://api.hubapi.com/crm/v3/objects/contacts", name = "Hubspot", configuration = FeignLoggingConfiguration.class)
public interface HubSpot {
    @PostMapping()
    void postHubspotData(@RequestHeader HttpHeaders headers, HubspotRequestBody hubspotProperties);

}
