package com.backend.demo.service.backendVersion;

import com.backend.demo.dto.backendVersion.BackendVersionRequestBody;
import com.backend.demo.dto.backendVersion.BackendVersionResponseBody;
import com.backend.demo.dto.backendVersion.OldPaginationDTO;
import com.backend.demo.entity.version.BackendVersion;
import com.backend.demo.repository.version.BackendVersionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class BackendVersionService {

    @Autowired
    BackendVersionRepository backendVersionRepository;
    ModelMapper modelMapper = new ModelMapper();

    public BackendVersionResponseBody returnLatestVersion() {

        BackendVersion backendVersion = backendVersionRepository.findFirstByOrderByCreatedAtDesc();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper.map(backendVersion, BackendVersionResponseBody.class);

    }

    public BackendVersionResponseBody addBackendVersion(BackendVersionRequestBody backendVersionRequestBody) {
        BackendVersion backendVersion = new BackendVersion(UUID.randomUUID(), backendVersionRequestBody.getServerVersion(), backendVersionRequestBody.getMinimumMobileVersion(), backendVersionRequestBody.getIsUnderMaintenance(), Instant.now(), null);
        backendVersionRepository.save(backendVersion);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper.map(backendVersion, BackendVersionResponseBody.class);
    }

    public OldPaginationDTO<BackendVersionResponseBody> returnVersionList(int page, int limit) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        Pageable pageable = PageRequest.of(page, limit);
        Page<BackendVersion> backendVersionPage = backendVersionRepository.findAllByOrderByCreatedAtDesc(pageable);
        if (nonNull(backendVersionPage)) {
            List<BackendVersionResponseBody> backendVersionResponseBodyList = backendVersionPage.stream().map(eachVersion -> modelMapper.map(eachVersion, BackendVersionResponseBody.class)).collect(Collectors.toList());
            return new OldPaginationDTO<>(backendVersionResponseBodyList, page, backendVersionPage.getTotalPages(), backendVersionPage.getTotalElements());
        }
        return new OldPaginationDTO<>(Collections.emptyList(), page, 0, null);
    }

    public Boolean deleteBackendVersion(UUID version) {
        Optional<BackendVersion> backendVersion = backendVersionRepository.findById(version);
        if (backendVersion.isPresent()) {
            BackendVersion serverVersion = backendVersion.get();
            backendVersionRepository.deleteById(serverVersion.getId());
            return true;
        }
        return false;
    }

    public BackendVersionResponseBody updateBackendVersion(BackendVersionRequestBody backendVersionRequestBody) {

        Optional<BackendVersion> version = backendVersionRepository.findById(backendVersionRequestBody.getId());
        if (version.isPresent()) {
            BackendVersion backendVersion = version.get();
            backendVersion.setIsUnderMaintenance(backendVersionRequestBody.getIsUnderMaintenance());
            backendVersionRepository.save(backendVersion);
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            return modelMapper.map(backendVersion, BackendVersionResponseBody.class);
        }
        return new BackendVersionResponseBody();
    }
}
