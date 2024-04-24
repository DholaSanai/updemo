package com.backend.demo.service.property;


import com.backend.demo.entity.property.ListedProperty;
import com.backend.demo.repository.property.ListedPropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class SchedulerService {
    @Autowired
    private ListedPropertyRepository listedPropertyRepository;

    private final int days = 60;

    @Scheduled(cron = "58 59 11 * * ?")
    public void deleteListedProperties() {
        try {
            log.info("Property listings older than " + days + " days removal service running");
            List<ListedProperty> listedProperties =
                    listedPropertyRepository.findAllByIsVerifiedFalseAndIsAddedByAdminFalse();

            LocalDate minus60days = LocalDate.now().minusDays(days);

            for (ListedProperty eachProperty : listedProperties) {
                if (eachProperty.getDateAdded() != null && (eachProperty.getDateAdded().isBefore(minus60days)
                        || eachProperty.getDateAdded().equals(minus60days))) {
                    eachProperty.setIsListingDeleted(true);
                }
            }
            listedPropertyRepository.saveAll(listedProperties);
            log.info("removal service successfully completed");
        } catch (Exception e) {
            log.error("Scheduler service error", e);
        }
    }
}
