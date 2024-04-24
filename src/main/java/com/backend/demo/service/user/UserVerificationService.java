package com.backend.demo.service.user;

import com.backend.demo.dto.user.chillowUser.ChillowUserVerifyDto;
import com.backend.demo.entity.user.ChillowUser;
import com.backend.demo.exceptions.UserNotFoundException;

import com.backend.demo.repository.user.ChillowUserVerifyRepository;
import com.backend.demo.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserVerificationService {

//    @Autowired
//    private EvidentIdClient evidentIdClient;

    @Autowired
    private ChillowUserVerifyRepository chillowUserVerifyRepository;

    @Autowired
    private UserRepository userRepository;

    public String receiveVerification(String rpRequestId) {
//        EvidentIdRequestVerifyResponse verificationStatus;
//        try {
//            verificationStatus = evidentIdClient.requestVerifyInfo(rpRequestId);
//        } catch (FeignException exception) {
//            log.error("Error occured at Evident Id API server ", exception);
//            throw new VerificationError("Some error occurred from Evident API");
//        }
//        if (verificationStatus.equals("Background Check")) {
//            Optional<ChillowUserVerify> userVerify = chillowUserVerifyRepository.findByBackgroundVerifiedIdToken(rpRequestId);
//            if (userVerify.isPresent()) {
//                userVerify.get().setBackgroundVerified(true);
//                chillowUserVerifyRepository.save(userVerify.get());
//                log.info("back ground verified");
//                return "Background Check Verified";
//            }
//        } else if (verificationStatus.equals("Identity Verification")) {
//            Optional<ChillowUserVerify> userVerify = chillowUserVerifyRepository.findByVerifiedIdToken(rpRequestId);
//            if (userVerify.isPresent()) {
//                userVerify.get().setIdVerified(true);
//                chillowUserVerifyRepository.save(userVerify.get());
//                log.info("back ground verified");
//                return "Background Check Verified";
//            }
//        }
//        throw new VerificationError("Some error occurred while verifying");
        return null;
    }

    public ChillowUserVerifyDto setBackgroundVerificationToken(String userId, String token) {
//        Optional<ChillowUser> existingUser = userRepository.findById(userId);
//        if (existingUser.isPresent()) {
//            existingUser.get().getChillowUserVerify().setBackgroundVerifiedIdToken(token);
//            userRepository.save(existingUser.get());
//            ModelMapper modelMapper = new ModelMapper();
//            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
//            return modelMapper.map(existingUser.get().getChillowUserVerify(), ChillowUserVerifyDto.class);
//        }
//        throw new UserNotFoundException("User does not exist with the provided id");
        return null;
    }

    public ChillowUserVerifyDto setIdVerificationToken(String userId, String token) {
        Optional<ChillowUser> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            existingUser.get().getChillowUserVerify().setVerifiedIdToken(token);
            userRepository.save(existingUser.get());
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            return modelMapper.map(existingUser.get().getChillowUserVerify(), ChillowUserVerifyDto.class);
        }
        throw new UserNotFoundException("User does not exist with the provided id");
    }

}
