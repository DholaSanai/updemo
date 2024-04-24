package com.backend.demo.service.admin;

import com.backend.demo.dto.admin.AdminSignupRequestBody;
import com.backend.demo.dto.user.chillowUser.ChillowUserDto;
import com.backend.demo.entity.user.*;
import com.backend.demo.repository.user.UserRepository;
import com.backend.demo.security.JWTauth;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTauth jwTauth;

    public ChillowUserDto createUser(AdminSignupRequestBody adminSignupRequestBody) {

        String userId = UUID.randomUUID().toString();

        ChillowUser newUser = new ChillowUser(userId, false, true, LocalDate.now(), true,
                false, true,
                adminSignupRequestBody.getEmail(), StringUtils.EMPTY, StringUtils.EMPTY, adminSignupRequestBody.getName(), LocalDate.now(),
                adminSignupRequestBody.getNumber(), jwTauth.generateToken(userId), StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, false,
                null, null, null, null, Collections.EMPTY_LIST,
                Collections.EMPTY_LIST, StringUtils.EMPTY, null, null, null, null,
                false, Instant.now(), Instant.now());

        ChillowUserInterests interests = new ChillowUserInterests(UUID.randomUUID().toString(), false,
                false, false, false, false
                , false, false, false, false, false, false,
                false, false, null);

        ChillowUserVerify verifyUser = new ChillowUserVerify(UUID.randomUUID().toString(), true,
                false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null);

        ChillowUserPreferences preferences = new ChillowUserPreferences(UUID.randomUUID().toString(),
                null, null, null, null, null, null, null,
                null, null, null, null, null, null);

        ChillowUserLocation location = new ChillowUserLocation(UUID.randomUUID().toString(), StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, 1.1d, 1.1d, null);

        location.setUser(newUser);
        interests.setUser(newUser);
        preferences.setUser(newUser);
        verifyUser.setUser(newUser);
        newUser.setLocation(location);
        newUser.setChillowUserPreferences(preferences);
        newUser.setChillowUserVerify(verifyUser);
        newUser.setChillowUserInterests(interests);
        userRepository.save(newUser);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper.map(newUser, ChillowUserDto.class);

    }
}
