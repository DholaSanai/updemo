package com.backend.demo.controller.backendversion;

import com.backend.demo.dto.backendVersion.BackendVersionRequestBody;
import com.backend.demo.dto.backendVersion.BackendVersionResponseBody;
import com.backend.demo.dto.backendVersion.OldPaginationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("chillow-users/api/v1/server-version/")
public class BackendVersionController {

    @Autowired
    com.backend.demo.service.backendVersionService.BackendVersionService backendVersionService;

    @GetMapping(value = "get-version", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> returnLatestVersion() {

        BackendVersionResponseBody backendVersionResponseBody = backendVersionService.returnLatestVersion();
        if (nonNull(backendVersionResponseBody)) {
            return new ResponseEntity<>(backendVersionResponseBody, HttpStatus.OK);
        }
        return new ResponseEntity<>("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "get-version-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public OldPaginationDTO<BackendVersionResponseBody> returnVersionList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit) {
        return backendVersionService.returnVersionList(page, limit);
    }

    @PostMapping(value = "add-version", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BackendVersionResponseBody addBackendVersion(@RequestBody BackendVersionRequestBody backendVersionRequestBody) {
        return backendVersionService.addBackendVersion(backendVersionRequestBody);
    }

    @PutMapping(value = "update-maintenance-check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BackendVersionResponseBody updateBackendVersion(@RequestBody BackendVersionRequestBody backendVersionRequestBody) {
        return backendVersionService.updateBackendVersion(backendVersionRequestBody);
    }

    @DeleteMapping(value = "delete-version/{version}")
    public Boolean deleteBackendVersion(@PathVariable("version") UUID version) {
        return backendVersionService.deleteBackendVersion(version);
    }
}
