package com.backend.demo.service.user;

import com.backend.demo.dto.evident.EvidentIdRequestBody;
import com.backend.demo.dto.user.*;
import com.backend.demo.dto.user.chillowUser.ChillowImageDto;
import com.backend.demo.dto.user.chillowUser.ChillowUserDto;
import com.backend.demo.dto.user.chillowUser.ChillowUserEditProfileDto;
import com.backend.demo.dto.user.chillowUser.ChillowUserProfile;
import com.backend.demo.entity.user.ChillowUser;
import com.backend.demo.model.ChillowUserDistance;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<ChillowUser> getAccountDetails(String phone);

    Optional<ChillowUser> findOneById(String id);

    ChillowUserDto loginWithGoogle(String token);

    ChillowUserDto loginWithApple(String token) throws UsernameNotFoundException;

    ChillowUserDto loginWithFacebook(String idToken);

    ChillowUserDto getUserById(String id);

    ChillowUserDto getAllUserMatchesWithDeleteFalse(String id);

    List<ChillowUserDto> getUserByIds(List<String> ids);

    List<ChillowUserDistance> getUsersNotInIdsOrderedByDistance(String userId, List<String> ids, Double longitude, Double latitude);

    boolean verifyUser(VerifyUserRequestBody body);

    List<MultipleUserVerifyResponse> verifyMultipleUsersByPhone(List<String> userlist);

    boolean sendSMSNotificationOfOtp(String phoneNumber);

    boolean verifyOtpByPhoneNumber(String phoneNumber, Integer pinCode);

    ChillowUserDto addImages(AddImageRequestBody body);

    ChillowUserEditProfileDto updateUser(ChillowUserEditProfileDto updatedUser) throws NotFoundException;

    ChillowUserDto saveNewUser(ChillowUserDto user);

    Boolean deleteImagesOfUser(String userId, ChillowImageDto image);

    String getUserProfileImage(String userId);

    Map<String, String> getProfileImagesByIds(List<String> ids);

    List<ChillowUserProfile> getUserWithProfileImage(List<String> phone);

    List<ChillowUserProfile> getUserByEmailWithProfileImage(List<String> emails);

     List<ChillowUserProfile> getUserProfileByIds(List<String> ids);

    List<AllUserChatProfiles> getAllChatUserProfiles(List<String> ids);

    boolean existsByUserId(String userId);

    GenerateLoginOTPResponseBody generateOTPOnLogin(GenerateLoginOTPRequestBody generateLoginOTPRequestBody);

    VerifyLoginOTPResponseBody verifyOTPOnLogin(VerifyLoginOTPRequestBody verifyLoginOTPRequestBody);

    ChillowUserDto userSignup(ChillowUserSignupDTO body) throws NotFoundException;

    boolean modifyStatus(String id) throws NotFoundException;

    boolean logout(String deviceId, String userId);

    ChillowUserDto getUserDetails(String id);

    Boolean updateUserEvidentStatus(EvidentIdRequestBody evidentIdRequestBody);
}
