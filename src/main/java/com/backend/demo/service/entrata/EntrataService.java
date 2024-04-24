package com.backend.demo.service.entrata;

import com.backend.demo.dto.entrata.health.EntrataHealthCheckRequestBody;
import com.backend.demo.dto.entrata.property.GetEntrataPropertiesRequestBody;
import com.backend.demo.dto.entrata.property.ReceivingObject;
import com.backend.demo.dto.entrata.sendLeads.SendLeadRequest;
import com.backend.demo.dto.entrata.sharedClasses.Auth;
import com.backend.demo.dto.entrata.sharedClasses.Method;
import com.backend.demo.dto.entrata.sharedClasses.Params;
import com.backend.demo.feignclient.user.EntrataClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EntrataService {

    @Autowired
    EntrataClient entrataClient;

    public String checkHealthStatusEntrata() {
        EntrataHealthCheckRequestBody requestBody = EntrataHealthCheckRequestBody.builder().auth(new Auth("basic")).
                requestId(15).method(new Method("getStatus", "r1", null)).build();

        return entrataClient.healthCheck(requestBody);
    }

    public ReceivingObject getPropertiesfromEntrata() {
        try {
            GetEntrataPropertiesRequestBody getEntrataPropertiesRequestBody =
                    GetEntrataPropertiesRequestBody.builder().auth(new Auth("basic")).
                            requestId(15).method(new Method("getProperties", "r1",
                                            new Params(0, null, null,
                                                    null, null)
                                    )
                            )
                            .build();
            return entrataClient.getProperties(getEntrataPropertiesRequestBody);
        }catch(Exception e){
            log.error(e.toString());
        }
        return null;
    }

    public String leadSource(Integer entrataPropertyId) {
        try {
            GetEntrataPropertiesRequestBody getEntrataPropertiesRequestBody =
                    GetEntrataPropertiesRequestBody.builder().auth(new Auth("basic")).
                            requestId(15).method(new Method("getLeadPickLists", "r2",
                                            new Params(0, entrataPropertyId.toString(), null, null, null)
                                    )
                            )
                            .build();
            return entrataClient.getLeads(getEntrataPropertiesRequestBody);
        }catch(Exception e){
            log.error(e.toString());
        }
        return null;
    }

    public String sendLeads(SendLeadRequest sendLeadRequest) {
        try {
            return entrataClient.sendLeads(sendLeadRequest);
        }catch(Exception e){
            log.error(e.toString());
        }
        return null;
    }
}

