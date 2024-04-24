package com.backend.demo.controller.datamigrate;

import com.backend.demo.dto.datamigrate.DataMigrateDto;
import com.backend.demo.dto.datamigrate.DataMigrateDtoResponseBody;
import com.backend.demo.service.dataMigrate.DataMigrateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/chillow-datamigrate/api/v1/")
public class DataMigrateController {

    @Autowired
    DataMigrateService dataMigrateService;
    @PostMapping(value = "save-data")
    public DataMigrateDtoResponseBody saveData(@Valid @RequestBody DataMigrateDto dataMigrateDto){
        return dataMigrateService.saveData(dataMigrateDto);
    }
}
