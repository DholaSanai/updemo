package com.backend.demo.feignclient.user;

import com.backend.demo.config.EntrataFeignConfiguration;
import com.backend.demo.dto.entrata.health.EntrataHealthCheckRequestBody;
import com.backend.demo.dto.entrata.property.GetEntrataPropertiesRequestBody;
import com.backend.demo.dto.entrata.property.ReceivingObject;
import com.backend.demo.dto.entrata.sendLeads.SendLeadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "entrataClient", url = "https://cardinal.entrata.com/api/v1", configuration = EntrataFeignConfiguration.class)
public interface EntrataClient {
    @PostMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    String healthCheck(@RequestBody EntrataHealthCheckRequestBody body);

    @PostMapping(value = "/properties", consumes = MediaType.APPLICATION_JSON_VALUE)
    ReceivingObject getProperties(GetEntrataPropertiesRequestBody getEntrataPropertiesRequestBody);

    @PostMapping(value = "/leads", consumes = MediaType.APPLICATION_JSON_VALUE)
    String getLeads(GetEntrataPropertiesRequestBody getEntrataPropertiesRequestBody);

    @PostMapping(value = "/leads", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    String sendLeads(SendLeadRequest sendLeadRequest);
}
