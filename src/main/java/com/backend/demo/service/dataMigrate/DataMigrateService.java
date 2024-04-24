package com.backend.demo.service.dataMigrate;

import com.backend.demo.dto.datamigrate.DataMigrateDto;
import com.backend.demo.dto.datamigrate.DataMigrateDtoResponseBody;
import com.backend.demo.dto.datamigrate.ListingPropertyDataMigrateDto;
import com.backend.demo.dto.property.PropertyAmenitiesDto;
import com.backend.demo.dto.property.PropertyImageDto;
import com.backend.demo.dto.property.PropertyLocationDto;
import com.backend.demo.dto.user.chillowUser.*;
import com.backend.demo.entity.property.ListedProperty;
import com.backend.demo.entity.property.ListedPropertyAmenities;
import com.backend.demo.entity.property.ListedPropertyImage;
import com.backend.demo.entity.property.ListedPropertyLocation;
import com.backend.demo.entity.user.*;
import com.backend.demo.repository.property.ListedPropertyRepository;
import com.backend.demo.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class DataMigrateService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ListedPropertyRepository listedPropertyRepository;

    public DataMigrateDtoResponseBody saveData(DataMigrateDto dataMigrateDto) {
        log.info("importing user");
        //NOT EVERY USER WILL BE LOOKING FOR PLACES ON SIGNUP. MANAGE WITH IF ELSE AND WANT-TO FIELD
        if (dataMigrateDto != null && (dataMigrateDto.getId() != null || !dataMigrateDto.getId().equals(""))
                && (dataMigrateDto.getLocation().getAddress() != null &&
                !dataMigrateDto.getLocation().getAddress().equals("")) &&
                (dataMigrateDto.getLocation().getLatitude() != null &&
                        !dataMigrateDto.getLocation().getLatitude().equals("")) &&
                (dataMigrateDto.getLocation().getLongitude() != null
                        && !dataMigrateDto.getLocation().getLongitude().equals(""))) {

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);

            ChillowUser user = modelMapper.map(dataMigrateDto, ChillowUser.class);
//            String userId = UUID.randomUUID().toString();
            user.setId(dataMigrateDto.getId()); //GET OLD USER ID
            user.setIsAdmin(false);
            user.setUserExpired(true);
            user.setNewUser(false);
            user.setInitialLogin(false);

            if (!dataMigrateDto.getNumber().equals("")) {
                boolean existingUserWithNumber = userRepository.existsByNumber(dataMigrateDto.getNumber());
                if (existingUserWithNumber) {
                    log.warn("Phone number " + dataMigrateDto.getNumber() + " is already in db");
                    log.info("user name" + dataMigrateDto.getName());
                    return null;
                }
            } else {
                user.setNumber(dataMigrateDto.getEmail());
            }

            user.getLocation().setId(UUID.randomUUID().toString());
            user.getLocation().setUser(user);

            List<ChillowImageDto> chillowUserImages = dataMigrateDto.getChillowUserImages();
            List<ChillowUserImage> userImageList = new ArrayList<>();

            for (ChillowImageDto eachImage : chillowUserImages) {
                ChillowUserImage chillowUserImage =
                        new ChillowUserImage(UUID.randomUUID().toString(), eachImage.getSequence(), eachImage.getFile(),
                                eachImage.getIsDeleted(), user);
                userImageList.add(chillowUserImage);
            }
            user.setChillowUserImages(userImageList);
            //LAST LOGIN HARD-CODED SO THAT USER IS TAKEN TO EDIT PROFILE ON START-UP
            user.setLastLogin(LocalDate.now().minusDays(40));

            ChillowUserInterests interests = modelMapper.map(dataMigrateDto.getChillowUserInterests(), ChillowUserInterests.class);
            interests.setId(UUID.randomUUID().toString());
            interests.setUser(user);
            user.setChillowUserInterests(interests);

            if (dataMigrateDto.getChillowUserVerify() != null) {
                ModelMapper mappa = new ModelMapper();
                mappa.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                ChillowUserVerify verify = mappa.map(dataMigrateDto.getChillowUserVerify(), ChillowUserVerify.class);
                verify.setId(UUID.randomUUID().toString());
                verify.setUser(user);
                user.setChillowUserVerify(verify);

            } else {
                ChillowUserVerify verify = new ChillowUserVerify();
                verify.setId(UUID.randomUUID().toString());
                verify.setUser(user);
                user.setChillowUserVerify(verify);
            }
            ChillowUserPreferences preferences = modelMapper.map(dataMigrateDto.getChillowUserPreferences(), ChillowUserPreferences.class);
            preferences.setId(UUID.randomUUID().toString());
            preferences.setUser(user);
            user.setChillowUserPreferences(preferences);
            List<ListingPropertyDataMigrateDto> listedProperties = dataMigrateDto.getListedProperties();
            if (!CollectionUtils.isEmpty(listedProperties)) {

                for (ListingPropertyDataMigrateDto eachProperty : listedProperties) {
                    if (eachProperty.getBuilding() == null || eachProperty.getBuilding().equals("")) {

                    } else {
                        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                        ListedProperty listedProperty = modelMapper.map(eachProperty, ListedProperty.class);
                        listedProperty.setId(UUID.randomUUID().toString());
                        if(eachProperty.getBuilding().equals("Apartment")) {
                            listedProperty.setBuildingType(0);
                        }
                        if(eachProperty.getBuilding().equals("Townhouse")) {
                            listedProperty.setBuildingType(1);
                        }
                        if(eachProperty.getBuilding().equals("House")) {
                            listedProperty.setBuildingType(2);
                        }
                        if(eachProperty.getBuilding().equals("Room") || eachProperty.getBuilding().equals("Condo")) {
                            listedProperty.setBuildingType(3);
                        }
                        ListedPropertyLocation location = modelMapper.map(eachProperty.getLocation(), ListedPropertyLocation.class);
                        location.setId(UUID.randomUUID().toString());
                        location.setListedProperty(listedProperty);
                        listedProperty.setLocation(location);


                        ListedPropertyAmenities amenities = modelMapper.map(eachProperty.getAmenities(), ListedPropertyAmenities.class);
                        amenities.setId(UUID.randomUUID().toString());
                        amenities.setListedProperty(listedProperty);
                        listedProperty.setListedPropertyAmenities(amenities);


                        List<ListedPropertyImage> listedPropertyImages = new ArrayList<>();
                        List<PropertyImageDto> images = eachProperty.getImages();
                        for (PropertyImageDto eachImage : images) {
                            ListedPropertyImage listedPropertyImage = modelMapper.map(eachImage, ListedPropertyImage.class);
                            listedPropertyImage.setId(UUID.randomUUID().toString());
                            listedPropertyImage.setListedProperty(listedProperty);
                            listedPropertyImages.add(listedPropertyImage);
                        }
                        listedProperty.setListedPropertyImages(listedPropertyImages);
                        listedProperty.setOwnerId(user.getId());
                        listedProperty.setIsVerified(false);
                        listedProperty.setIsAddedByAdmin(false);
                        listedProperty.setOwnerName(user.getName());
                        listedPropertyRepository.save(listedProperty);
                    }
                }
            }
            userRepository.save(user);


            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            Optional<ChillowUser> newUser = userRepository.findById(user.getId());
            if (newUser.isPresent()) {
                ChillowUser updatedUser = newUser.get();

                DataMigrateDtoResponseBody dataMigrateDtoResponseBody = modelMapper.map(updatedUser, DataMigrateDtoResponseBody.class);

                ChillowUserInterestsDto userInterests = modelMapper.map(updatedUser.getChillowUserInterests(), ChillowUserInterestsDto.class);
                dataMigrateDtoResponseBody.setChillowUserInterests(userInterests);

                ChillowUserPreferencesDto userpreferences = modelMapper.map(updatedUser.getChillowUserPreferences(), ChillowUserPreferencesDto.class);
                dataMigrateDtoResponseBody.setChillowUserPreferences(userpreferences);


                ChillowUserVerifyDto userVerify = modelMapper.map(updatedUser.getChillowUserVerify(), ChillowUserVerifyDto.class);
                dataMigrateDtoResponseBody.setChillowUserVerify(userVerify);


                ChillowUserLocationDto userLocation = modelMapper.map(updatedUser.getLocation(), ChillowUserLocationDto.class);
                dataMigrateDtoResponseBody.setLocation(userLocation);


                if (!CollectionUtils.isEmpty(updatedUser.getChillowUserImages())) {

                    List<ChillowUserImage> images = updatedUser.getChillowUserImages();
                    List<ChillowImageDto> newImageList = new ArrayList<>();
                    for (ChillowUserImage eachImage : images) {
                        ChillowImageDto image = modelMapper.map(eachImage, ChillowImageDto.class);
                        newImageList.add(image);
                    }
                    dataMigrateDtoResponseBody.setChillowUserImages(newImageList);
                }

                //GET ALL PROPERTIES AND SET TO THE DTO
                List<ListedProperty> propertyList = listedPropertyRepository.findAllByOwnerId(updatedUser.getId());
                List<ListingPropertyDataMigrateDto> userProperties = new ArrayList<>();
                for (ListedProperty eachProperty : propertyList) {
                    ListingPropertyDataMigrateDto listingPropertyDataMigrateDto =
                            modelMapper.map(eachProperty, ListingPropertyDataMigrateDto.class);

                    listingPropertyDataMigrateDto.setLocation(
                            modelMapper.map(eachProperty.getLocation(), PropertyLocationDto.class));

                    listingPropertyDataMigrateDto.setAmenities(
                            modelMapper.map(eachProperty.getListedPropertyAmenities(), PropertyAmenitiesDto.class));

                    listingPropertyDataMigrateDto.setImages(
                            modelMapper.map(eachProperty.getListedPropertyImages(), new TypeToken<List<PropertyImageDto>>() {
                            }.getType()));

                    userProperties.add(listingPropertyDataMigrateDto);
                }
                dataMigrateDtoResponseBody.setListedProperties(userProperties);
                log.info("user import success");
                return dataMigrateDtoResponseBody;
            }
            //user not saved
        }
        log.info("User location object is in-complete! Unable to save user  " + dataMigrateDto.getName());
        return null;
    }

}
