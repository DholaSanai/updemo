package com.backend.demo.service.property;

import com.backend.demo.components.RoomQueueConsumer;
import com.backend.demo.dto.entrata.admin.SendLeadRequestBody;
import com.backend.demo.dto.entrata.property.Property;
import com.backend.demo.dto.entrata.property.ReceivingObject;
import com.backend.demo.dto.entrata.sendLeads.*;
import com.backend.demo.dto.entrata.sharedClasses.Auth;
import com.backend.demo.dto.entrata.sharedClasses.Method;
import com.backend.demo.dto.entrata.sharedClasses.Params;
import com.backend.demo.dto.property.*;
import com.backend.demo.dto.property.featured.FeaturedPropertyPartnerResponseBody;
import com.backend.demo.entity.property.*;
import com.backend.demo.entity.suggestion.ComplexNameSuggestions;
import com.backend.demo.entity.swipe.Swipe;
import com.backend.demo.entity.user.ChillowUser;
import com.backend.demo.entity.user.ChillowUserImage;
import com.backend.demo.entity.user.UserPropertyJunctionTable;
import com.backend.demo.enums.property.PropertyType;
import com.backend.demo.exceptions.UserNotFoundException;
import com.backend.demo.repository.property.*;
import com.backend.demo.repository.suggestions.ComplexNameSuggestionsRepository;
import com.backend.demo.repository.swipe.MatchRepository;
import com.backend.demo.repository.swipe.SwipeRepository;
import com.backend.demo.repository.user.ChillowUserImagesRepository;
import com.backend.demo.repository.user.ChillowUserVerifyRepository;
import com.backend.demo.repository.user.UserRepository;
import com.backend.demo.service.entrata.EntrataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PropertyService {
    @Autowired
    ChillowUserImagesRepository chillowUserImagesRepository;
    @Autowired
    RoomQueueConsumer roomQueueConsumer;
    @Autowired
    MatchRepository matchRepository;
    @Autowired
    SwipeRepository swipeRepository;
    @Autowired
    ComplexNameSuggestionsRepository complexNameSuggestionsRepository;
    @Autowired
    ListedPropertyLocationRepository listedPropertyLocationRepository;
    @Autowired
    ExternalPropertyRepository externalPropertyRepository;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Autowired
    private PropertyPartnerContactDetailRepository propertyPartnerContactDetailRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ChillowUserVerifyRepository chillowUserVerifyRepository;
    @Autowired
    private UserPrefferedPropertyPartnerRepository userPrefferedPropertyPartnerRepository;
    @Autowired
    private PropertyPartnerRepository propertyPartnerRepository;
    @Autowired
    private ListedPropertyRepository listedPropertyRepository;
    @Autowired
    private UserPropertyJunctionTableRepository userPropertyJunctionTableRepository;
    @Autowired
    private PropertyPartnerCategoryRepository propertyPartnerCategoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PropertyPartnerLocationRepository propertyPartnerLocationRepository;
    @Autowired
    private PropertyPartnerAmenitiesRepository propertyPartnerAmenitiesRepository;
    @Autowired
    private ListedPropertyImagesRepository listedPropertyImagesRepository;
    @Autowired
    private PropertyPartnerCategoryJunctionTableRepository propertyPartnerCategoryJunctionTableRepository;
    @Autowired
    private EntrataService entrataService;

    public AddListedPropertyResponseBody addProperty(AddListedPropertyRequestBody addListedPropertyRequestBody) {
        Optional<ChillowUser> existingUser = userRepository.findById(addListedPropertyRequestBody.getOwnerId());
        if (existingUser.isPresent()) {
            ChillowUser user = existingUser.get();
            ListedProperty listedProperty = new ListedProperty();
            listedProperty.setId(UUID.randomUUID().toString());
            if (user.getIsAdmin() != null && user.getIsAdmin()) {
                listedProperty.setIsAddedByAdmin(true);
                listedProperty.setIsVerified(true);
            } else {
                listedProperty.setIsAddedByAdmin(false);
                listedProperty.setIsVerified(false);
            }
            listedProperty.setIsListingDeleted(false);
            listedProperty.setDateAdded(LocalDate.now());
            listedProperty.setOwnerId(user.getId());
            listedProperty.setOwnerName(user.getName());
            listedProperty.setBuildingType(addListedPropertyRequestBody.getBuildingType());
            listedProperty.setIsStudentHousing(addListedPropertyRequestBody.getIsStudentHousing());
            listedProperty.setIsCoLiving(addListedPropertyRequestBody.getIsCoLiving());

            if (addListedPropertyRequestBody.getPartnerId() != null && user.getIsAdmin() != null && user.getIsAdmin()) {
                Optional<PropertyPartner> existingPropertyPartner = propertyPartnerRepository.
                        findById(addListedPropertyRequestBody.getPartnerId());
                if (existingPropertyPartner.isPresent()) {
                    listedProperty.setPropertyPartnerId(existingPropertyPartner.get().getId());
                    listedProperty.setComplexName(existingPropertyPartner.get().getComplexName());
                } else {
                    listedProperty.setPropertyPartnerId(null);
                    listedProperty.setComplexName(addListedPropertyRequestBody.getComplexName());
                }
            } else {
                listedProperty.setPropertyPartnerId(null);
                listedProperty.setComplexName(addListedPropertyRequestBody.getComplexName());
            }
            listedProperty.setMonthlyRent(addListedPropertyRequestBody.getMonthlyRent());
            listedProperty.setIsUtilitiesIncluded(addListedPropertyRequestBody.getIsUtilitiesIncluded());
            listedProperty.setWhenAvailable(addListedPropertyRequestBody.getWhenAvailable());
            listedProperty.setLeaseTerm(addListedPropertyRequestBody.getLeaseTerm());
            listedProperty.setPropertySizeSqFt(addListedPropertyRequestBody.getPropertySizeSqFt());
            listedProperty.setBedrooms(addListedPropertyRequestBody.getBedrooms());
            listedProperty.setBathrooms(addListedPropertyRequestBody.getBathrooms());
            listedProperty.setFemaleRoommates(addListedPropertyRequestBody.getFemaleRoommates());
            listedProperty.setMaleRoommates(addListedPropertyRequestBody.getMaleRoommates());
            listedProperty.setIsCurrentlyLivingHere(addListedPropertyRequestBody.getIsCurrentlyLivingHere());
            listedProperty.setParkingFee(addListedPropertyRequestBody.getParkingFee());
            listedProperty.setRoomDescription(addListedPropertyRequestBody.getRoomDescription());

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            PropertyLocationDto location = addListedPropertyRequestBody.getLocation();
            ListedPropertyLocation listedPropertyLocation = new ListedPropertyLocation(UUID.randomUUID().toString(),
                    location.getAddress(), location.getCity(),
                    location.getCountry(), location.getState(), location.getLongitude(), location.getLatitude(),
                    Instant.now(), Instant.now(), listedProperty);

            PropertyAmenitiesDto amenities = addListedPropertyRequestBody.getAmenities();
            ListedPropertyAmenities listedPropertyAmenities = new ListedPropertyAmenities(UUID.randomUUID().toString(),
                    amenities.isParking(), amenities.isPrivateEntrance(), amenities.isGym(), amenities.isAdaAccessible(),
                    amenities.isDoorMan(), amenities.isPool(), amenities.isPrivateBathroom(), amenities.isWifi(), amenities.isAc(),
                    amenities.isPrivateLaundry(), amenities.isFurnished(), amenities.isPrivateCloset(), listedProperty);
            listedProperty.setPets(addListedPropertyRequestBody.getPets());
            listedProperty.setSmoke(addListedPropertyRequestBody.getSmoke());
            List<PropertyImageDto> imageList = addListedPropertyRequestBody.getImages();
            List<ListedPropertyImage> listedPropertyImagesList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(imageList)) {
                for (PropertyImageDto eachImage : imageList) {
                    ListedPropertyImage listedPropertyImage = new ListedPropertyImage(
                            UUID.randomUUID().toString(), eachImage.getSequence(), eachImage.getFile(),
                            false, listedProperty);
                    listedPropertyImagesList.add(listedPropertyImage);
                }
                listedProperty.setListedPropertyImages(listedPropertyImagesList);
            } else {
                listedProperty.setListedPropertyImages(null);
            }
            listedProperty.setIsUpdateRequired(false);
            listedProperty.setCreatedAt(Instant.now());
            listedProperty.setUpdatedAt(Instant.now());
            listedProperty.setLocation(listedPropertyLocation);
            listedProperty.setListedPropertyAmenities(listedPropertyAmenities);

            listedPropertyRepository.save(listedProperty);
            roomQueueConsumer.processNotificationDispatchToUsers(listedProperty, user.getId());

            UserPropertyJunctionTable userPropertyJunctionTable = new UserPropertyJunctionTable(UUID.randomUUID().toString(),
                    user.getId(), listedProperty.getId(), false, Instant.now(), Instant.now());
            userPropertyJunctionTableRepository.save(userPropertyJunctionTable);
            AddListedPropertyResponseBody responseBody = modelMapper.map(listedProperty, AddListedPropertyResponseBody.class);
            responseBody.setOwnerName(user.getName());
            if (!user.getChillowUserImages().isEmpty()) {
                responseBody.setOwnerImage(user.getChillowUserImages().get(0).getFile());
            }
            //TODO: FIX SUGGESTIONS LATER
            if (!user.getIsAdmin()) {
                if (listedProperty.getComplexName() != null && !listedProperty.getComplexName().equals("")) {
                    try {
                        saveComplexNameInSuggestions(addListedPropertyRequestBody.getComplexName());
                    } catch (RuntimeException e) {
                        log.error("unable to save complex name", e);
                    }
                }
            }
            //TODO: FIX SUGGESTIONS LATER
            return responseBody;
        }
        return null;
    }

    //TODO: FIX SUGGESTIONS LATER
    @Async
    private void saveComplexNameInSuggestions(String complexName) {
        ComplexNameSuggestions complexNameSuggestions = complexNameSuggestionsRepository.findByComplexName(complexName);
        if (complexNameSuggestions == null) {
            ComplexNameSuggestions name = new ComplexNameSuggestions(UUID.randomUUID().toString(), complexName, Instant.now());
            complexNameSuggestionsRepository.save(name);
        }
    }

    //TODO: FIX SUGGESTIONS LATER
    public PropertyPartnerCategoryResponseBody addPropertyPartnerCategory(PropertyPartnerCategoryRequestBody propertyPartnerCategoryRequestBody) {
        PropertyPartnerCategory propertyPartnerCategory = new PropertyPartnerCategory(UUID.randomUUID().toString(),
                propertyPartnerCategoryRequestBody.getTypeName(), propertyPartnerCategoryRequestBody.getDescription(), false,
                Instant.now(), Instant.now());
        propertyPartnerCategoryRepository.save(propertyPartnerCategory);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(propertyPartnerCategory, PropertyPartnerCategoryResponseBody.class);
    }

    public PropertyPartnerCategoryResponseBody updatePropertyPartnerCategory(PropertyPartnerCategoryRequestBody propertyPartnerCategoryRequestBody) throws NotFoundException {
        Optional<PropertyPartnerCategory> propertyPartnerCategory = propertyPartnerCategoryRepository.findById(propertyPartnerCategoryRequestBody.getId());
        if (propertyPartnerCategory.isPresent()) {
            propertyPartnerCategory.get().setTypeName(propertyPartnerCategoryRequestBody.getTypeName());
            propertyPartnerCategory.get().setDescription(propertyPartnerCategoryRequestBody.getDescription());
            propertyPartnerCategory.get().setCategoryDeleted(propertyPartnerCategoryRequestBody.isCategoryDeleted());
            propertyPartnerCategoryRepository.save(propertyPartnerCategory.get());
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
            return modelMapper.map(propertyPartnerCategory, PropertyPartnerCategoryResponseBody.class);
        }
        throw new NotFoundException("property partner not found against this category id!");
    }

    public Boolean deleteCategory(String id) throws NotFoundException {
        Optional<PropertyPartnerCategory> propertyPartnerCategory = propertyPartnerCategoryRepository.findById(id);
        if (propertyPartnerCategory.isPresent()) {
            propertyPartnerCategory.get().setCategoryDeleted(true);
            propertyPartnerCategoryRepository.save(propertyPartnerCategory.get());
            return true;
        }
        throw new NotFoundException("property partner not found against this category id!");
    }

    public List<PropertyPartnerCategoryResponseBody> getAllPropertyPartnerCategories() {
        List<PropertyPartnerCategory> propertyPartnerCategories = propertyPartnerCategoryRepository.findAll();
        if (CollectionUtils.isEmpty(propertyPartnerCategories)) {
            return Collections.emptyList();
        } else {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
            List<PropertyPartnerCategoryResponseBody> propertyPartnerCategoryResponseBody
                    = new ArrayList<>();
            for (PropertyPartnerCategory eachCategory : propertyPartnerCategories) {
                propertyPartnerCategoryResponseBody.add(modelMapper.map(eachCategory, PropertyPartnerCategoryResponseBody.class));
            }
            return propertyPartnerCategoryResponseBody;
        }
    }

    public PropertyPartnerResponseBody addPropertyPartner(PropertyPartnerRequestBody propertyPartnerRequestBody) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        PropertyPartner propertyPartner = new PropertyPartner();
        propertyPartner.setId(UUID.randomUUID().toString());
        propertyPartner.setComplexName(propertyPartnerRequestBody.getComplexName());
        propertyPartner.setDescription(propertyPartnerRequestBody.getDescription());
        propertyPartner.setMaxPrice(propertyPartnerRequestBody.getMaxPrice());
        propertyPartner.setMinPrice(propertyPartnerRequestBody.getMinPrice());
        propertyPartner.setParkingFee(propertyPartnerRequestBody.getParkingFee());

        PropertyPartnerLocation propertyPartnerLocation = modelMapper.map(propertyPartnerRequestBody.getLocation(), PropertyPartnerLocation.class);
        propertyPartnerLocation.setId(UUID.randomUUID().toString());
        propertyPartnerLocation.setPropertyPartner(propertyPartner);
        propertyPartner.setLocation(propertyPartnerLocation);

        PropertyPartnerAmenities propertyPartnerAmenities = modelMapper.map(propertyPartnerRequestBody.getAmenities(), PropertyPartnerAmenities.class);
        propertyPartnerAmenities.setId(UUID.randomUUID().toString());
        propertyPartnerAmenities.setPropertyPartner(propertyPartner);
        propertyPartner.setPropertyPartnerAmenities(propertyPartnerAmenities);

        List<PropertyImageDto> images = propertyPartnerRequestBody.getImage();
        List<PropertyPartnerImage> propertyPartnerImages = new ArrayList<>();

        for (PropertyImageDto eachImage : images) {
            PropertyPartnerImage propertyPartnerImage = new PropertyPartnerImage(
                    UUID.randomUUID().toString(), eachImage.getSequence(), eachImage.getFile(), false,
                    propertyPartner);
            propertyPartnerImages.add(propertyPartnerImage);
        }
        propertyPartner.setListedPropertyImages(propertyPartnerImages);
        propertyPartner.setPartnerPropertyDeleted(propertyPartnerRequestBody.isPartnerPropertyDeleted());
        //PREFERENCES
        propertyPartner.setPets(propertyPartnerRequestBody.getPets());
        propertyPartner.setSmoke(propertyPartnerRequestBody.getSmoke());
        //PREFERENCES
        propertyPartner.setCreatedAt(Instant.now());
        propertyPartner.setUpdatedAt(Instant.now());

//        propertyPartnerAmenitiesRepository.save(propertyPartnerAmenities);
//        propertyPartnerLocationRepository.save(propertyPartnerLocation);
        propertyPartnerRepository.save(propertyPartner);

        PropertyPartnerContactDetail propertyPartnerContactDetail = new PropertyPartnerContactDetail();
        propertyPartnerContactDetail.setId(UUID.randomUUID().toString());
        propertyPartnerContactDetail.setPropertyPartner(propertyPartner.getId());
        propertyPartnerContactDetail.setEmail(propertyPartnerRequestBody.getEmail());
        propertyPartnerContactDetail.setCreatedAt(LocalDateTime.now());
        propertyPartnerContactDetail.setUpdatedAt(LocalDateTime.now());
        propertyPartnerContactDetailRepository.save(propertyPartnerContactDetail);

        List<PropertyPartnerCategory> propertyPartnerCategories = propertyPartnerCategoryRepository.findAllByIdIn(propertyPartnerRequestBody.getPropertyPartnerCategories());
        for (PropertyPartnerCategory eachPropertyCategory : propertyPartnerCategories) {
            PropertyPartnerCategoryJunctionTable propertyPartnerCategoryJunctionTable = new PropertyPartnerCategoryJunctionTable(
                    UUID.randomUUID().toString(), eachPropertyCategory.getId(), propertyPartner.getId(), false, Instant.now(), Instant.now());
            propertyPartnerCategoryJunctionTableRepository.save(propertyPartnerCategoryJunctionTable);
        }
        PropertyPartnerResponseBody propertyPartnerResponseBody = modelMapper.map(propertyPartner, PropertyPartnerResponseBody.class);
        List<PropertyImageDto> propertyImageDto = modelMapper.map(propertyPartner.getListedPropertyImages(), new TypeToken<List<PropertyImageDto>>() {
        }.getType());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        PropertyLocationDto propertyLocationDto = modelMapper.map(propertyPartner.getLocation(), PropertyLocationDto.class);

        PropertyAmenitiesDto propertyAmenitiesDto = modelMapper.map(propertyPartner.getPropertyPartnerAmenities(), PropertyAmenitiesDto.class);

        propertyPartnerResponseBody.setImage(propertyImageDto);
        propertyPartnerResponseBody.setLocation(propertyLocationDto);
        propertyPartnerResponseBody.setAmenities(propertyAmenitiesDto);
        return propertyPartnerResponseBody;

    }

    public List<SuggestionsResponseBody> getPropertyPartnerSuggestionsForUsers(String complexName, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<ComplexNameSuggestions> propertyNames =
                complexNameSuggestionsRepository.findByComplexNameIgnoreCaseContaining(complexName, pageable);
        return modelMapper.map(propertyNames, new TypeToken<List<SuggestionsResponseBody>>() {
        }.getType());
    }

    public List<SuggestionsResponseBody> getPropertyPartnerSuggestionsForAdmin(String complexName, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<PropertyPartner> propertyNames =
                propertyPartnerRepository.findByComplexNameIgnoreCaseContaining(complexName, pageable);
        return modelMapper.map(propertyNames, new TypeToken<List<SuggestionsResponseBody>>() {
        }.getType());
    }


    public List<ListedPropertyResponseBody> getPropertyListing(String userId, Integer page, Integer limit, String valueToSearch) {
        Pageable pageable = PageRequest.of(page, limit);
        Optional<ChillowUser> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UserNotFoundException("Invalid user id!");
        }
        List<ListedProperty> listedProperties = null;
        if (StringUtils.isEmpty(valueToSearch)) {
            listedProperties = listedPropertyRepository.findAllByOwnerIdAndIsListingDeletedFalse(userId, pageable);
        } else {
            listedProperties = listedPropertyRepository.findByOwnerOrByLocationOfListing(userId, valueToSearch, valueToSearch, pageable);
        }
        if (!listedProperties.isEmpty()) {
            List<ListedPropertyResponseBody> listedPropertyResponseBodies = new ArrayList<>();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

            for (ListedProperty eachProperty : listedProperties) {
                ListedPropertyResponseBody listedPropertyResponseBody = modelMapper.map(eachProperty, ListedPropertyResponseBody.class);
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
                PropertyLocationDto propertyLocationDto = modelMapper.map(eachProperty.getLocation(), PropertyLocationDto.class);
                PropertyAmenitiesDto propertyAmenitiesDto = modelMapper.map(eachProperty.getListedPropertyAmenities(), PropertyAmenitiesDto.class);
                List<PropertyImageDto> propertyImageDto = modelMapper.map(eachProperty.getListedPropertyImages(), new TypeToken<List<PropertyImageDto>>() {
                }.getType());
                listedPropertyResponseBody.setLocation(propertyLocationDto);
                listedPropertyResponseBody.setAmenities(propertyAmenitiesDto);
                listedPropertyResponseBody.setImages(propertyImageDto);
                listedPropertyResponseBodies.add(listedPropertyResponseBody);
            }
            return listedPropertyResponseBodies;
        }
        return Collections.emptyList();
    }

    public PropertyPartnerWithCategories getPartneredPropertiesAccordingUsingLongitudeAndLatitude(Double longitude, Double latitude) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        List<PropertyPartnersByDistance> propertyPartnersByDistanceList = propertyPartnerRepository.findPropertyPartnersByDistance(longitude, latitude);

        List<PropertyPartnerResponseBody> propertyPartnerResponseBodies = new ArrayList<>();
        PropertyPartnerWithCategories propertyPartnerWithCategories = new PropertyPartnerWithCategories();

        List<PropertyPartnerResponseBody> generalLivingList = new ArrayList<>();
        List<PropertyPartnerResponseBody> coLivingList = new ArrayList<>();
        List<PropertyPartnerResponseBody> offCampusList = new ArrayList<>();

        //check junction table against propertyId in forloop and add to lists
        for (PropertyPartnersByDistance each : propertyPartnersByDistanceList) {

            PropertyPartnerResponseBody propertyPartnerResponseBody = modelMapper.map(each, PropertyPartnerResponseBody.class);
            Optional<PropertyPartner> propertyPartner = propertyPartnerRepository.findById(propertyPartnerResponseBody.getId());

            propertyPartnerResponseBody.setLocation(modelMapper.map(propertyPartner.get().getLocation(), PropertyLocationDto.class));
            propertyPartnerResponseBody.setAmenities(modelMapper.map(propertyPartner.get().getPropertyPartnerAmenities(), PropertyAmenitiesDto.class));
            propertyPartnerResponseBody.setImage(
                    modelMapper.map(propertyPartner.get().getListedPropertyImages(), new TypeToken<List<PropertyImageDto>>() {
                    }.getType())
            );
            List<PropertyPartnerCategoryJunctionTable> categoryJunctionTableList = propertyPartnerCategoryJunctionTableRepository.findAllByPropertyPartnerId(propertyPartner.get().getId());
            for (PropertyPartnerCategoryJunctionTable eachCategory : categoryJunctionTableList) {
                Optional<PropertyPartnerCategory> propertyPartnerCategory = propertyPartnerCategoryRepository.findById(eachCategory.getPropertyCategoryId());
                if (propertyPartnerCategory.isPresent()) {
                    PropertyPartnerCategory partnerCategory = propertyPartnerCategory.get();
                    if (partnerCategory.getTypeName().equals("General Living")) {
                        generalLivingList.add(propertyPartnerResponseBody);
                    }
                    if (partnerCategory.getTypeName().equals("Co-living")) {
                        coLivingList.add(propertyPartnerResponseBody);
                    }
                    if (partnerCategory.getTypeName().equals("Off-campus Housing")) {
                        offCampusList.add(propertyPartnerResponseBody);
                    }
                }
            }
        }
        propertyPartnerWithCategories.setGeneralLivingList(generalLivingList);
        propertyPartnerWithCategories.setCoLivingList(coLivingList);
        propertyPartnerWithCategories.setOffCampusList(offCampusList);
        return propertyPartnerWithCategories;
    }

    public ListedPropertyResponseBody updateProperty(UpdateListedPropertyRequestBody updateListedPropertyRequestBody) {
        Optional<ListedProperty> listedProperty = listedPropertyRepository.findById(updateListedPropertyRequestBody.getId());
        if (listedProperty.isPresent()) {
            ListedProperty presentListedProperty = listedProperty.get();

            if (updateListedPropertyRequestBody.getPartnerId() != null) {
                Optional<PropertyPartner> existingPropertyPartner = propertyPartnerRepository.findById(updateListedPropertyRequestBody.getPartnerId());
                if (existingPropertyPartner.isPresent()) {
                    presentListedProperty.setPropertyPartnerId(existingPropertyPartner.get().getId());
                    presentListedProperty.setComplexName(existingPropertyPartner.get().getComplexName());
                } else {
                    presentListedProperty.setPropertyPartnerId(null);
                    presentListedProperty.setComplexName(updateListedPropertyRequestBody.getComplexName());
                }
            } else {
                presentListedProperty.setPropertyPartnerId(null);
                presentListedProperty.setComplexName(updateListedPropertyRequestBody.getComplexName());
            }
            Optional<ChillowUser> user = userRepository.findById(presentListedProperty.getOwnerId());
            if (user.isPresent()) {
                if (user.get().getIsAdmin()) {
                    presentListedProperty.setIsAddedByAdmin(true);
                    presentListedProperty.setIsVerified(true);
                } else {
                    presentListedProperty.setIsAddedByAdmin(false);
                    presentListedProperty.setIsVerified(false);
                }
            }

            presentListedProperty.setBuildingType(updateListedPropertyRequestBody.getBuildingType());
            presentListedProperty.setIsStudentHousing(updateListedPropertyRequestBody.getIsStudentHousing());
            presentListedProperty.setIsCoLiving(updateListedPropertyRequestBody.getIsCoLiving());
            presentListedProperty.setMonthlyRent(updateListedPropertyRequestBody.getMonthlyRent());
            presentListedProperty.setIsUtilitiesIncluded(updateListedPropertyRequestBody.getIsUtilitiesIncluded());
            presentListedProperty.setWhenAvailable(updateListedPropertyRequestBody.getWhenAvailable());
            presentListedProperty.setLeaseTerm(updateListedPropertyRequestBody.getLeaseTerm());
            presentListedProperty.setPropertySizeSqFt(updateListedPropertyRequestBody.getPropertySizeSqFt());
            presentListedProperty.setBedrooms(updateListedPropertyRequestBody.getBedrooms());
            presentListedProperty.setBathrooms(updateListedPropertyRequestBody.getBathrooms());
            presentListedProperty.setFemaleRoommates(updateListedPropertyRequestBody.getFemaleRoommates());
            presentListedProperty.setMaleRoommates(updateListedPropertyRequestBody.getMaleRoommates());
            presentListedProperty.setIsCurrentlyLivingHere(updateListedPropertyRequestBody.getIsCurrentlyLivingHere());
            presentListedProperty.setParkingFee(updateListedPropertyRequestBody.getParkingFee());
            presentListedProperty.setRoomDescription(updateListedPropertyRequestBody.getRoomDescription());
            presentListedProperty.setPets(updateListedPropertyRequestBody.getPets());
            presentListedProperty.setSmoke(updateListedPropertyRequestBody.getSmoke());

            PropertyLocationDto locationDto = updateListedPropertyRequestBody.getLocation();
            presentListedProperty.getLocation().setAddress(locationDto.getAddress());
            presentListedProperty.getLocation().setCity(locationDto.getCity());
            presentListedProperty.getLocation().setCountry(locationDto.getCountry());
            presentListedProperty.getLocation().setState(locationDto.getState());
            presentListedProperty.getLocation().setLongitude(locationDto.getLongitude());
            presentListedProperty.getLocation().setLatitude(locationDto.getLatitude());

            PropertyAmenitiesDto amenitiesDto = updateListedPropertyRequestBody.getAmenities();
            presentListedProperty.getListedPropertyAmenities().setParking(amenitiesDto.isParking());
            presentListedProperty.getListedPropertyAmenities().setPrivateEntrance(amenitiesDto.isPrivateEntrance());
            presentListedProperty.getListedPropertyAmenities().setGym(amenitiesDto.isGym());
            presentListedProperty.getListedPropertyAmenities().setAdaAccessible(amenitiesDto.isAdaAccessible());
            presentListedProperty.getListedPropertyAmenities().setDoorMan(amenitiesDto.isDoorMan());
            presentListedProperty.getListedPropertyAmenities().setPool(amenitiesDto.isPool());
            presentListedProperty.getListedPropertyAmenities().setPrivateBathroom(amenitiesDto.isPrivateBathroom());
            presentListedProperty.getListedPropertyAmenities().setWifi(amenitiesDto.isWifi());
            presentListedProperty.getListedPropertyAmenities().setAc(amenitiesDto.isAc());
            presentListedProperty.getListedPropertyAmenities().setPrivateLaundry(amenitiesDto.isPrivateLaundry());
            presentListedProperty.getListedPropertyAmenities().setFurnished(amenitiesDto.isFurnished());
            presentListedProperty.getListedPropertyAmenities().setPrivateCloset(amenitiesDto.isPrivateCloset());

            List<ListedPropertyImage> oldImages = listedPropertyImagesRepository.findAllByListedProperty(presentListedProperty);
            listedPropertyImagesRepository.deleteAll(oldImages);

            List<PropertyImageDto> newImages = updateListedPropertyRequestBody.getImages();
            List<ListedPropertyImage> imageList = new ArrayList<>();

            for (PropertyImageDto eachImage : newImages) {
                ListedPropertyImage listedPropertyImage = new ListedPropertyImage(
                        UUID.randomUUID().toString(), eachImage.getSequence(),
                        eachImage.getFile(), false, presentListedProperty);
                imageList.add(listedPropertyImage);
            }
            presentListedProperty.setIsUpdateRequired(false);
            presentListedProperty.setListedPropertyImages(imageList);
            listedPropertyRepository.save(presentListedProperty);


            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);


            ListedPropertyResponseBody listedPropertyResponseBody = modelMapper.map(presentListedProperty, ListedPropertyResponseBody.class);
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
            PropertyLocationDto propertyLocationDto = modelMapper.map(presentListedProperty.getLocation(), PropertyLocationDto.class);
            PropertyAmenitiesDto propertyAmenitiesDto = modelMapper.map(presentListedProperty.getListedPropertyAmenities(), PropertyAmenitiesDto.class);
            List<PropertyImageDto> propertyImageDto = modelMapper.map(presentListedProperty.getListedPropertyImages(), new TypeToken<List<PropertyImageDto>>() {
            }.getType());
            listedPropertyResponseBody.setLocation(propertyLocationDto);
            listedPropertyResponseBody.setAmenities(propertyAmenitiesDto);
            listedPropertyResponseBody.setImages(propertyImageDto);

            return listedPropertyResponseBody;

        }
        return null;
    }

    public GetPropertiesWithPaginationDto getAllPropertyListings(int page, int size, String userId, int radius, Double longitude, Double latitude, Integer minRent,
                                                                 Integer maxRent, Boolean isColiving, Boolean isStudentHousing, Integer room,
                                                                 Integer apartment, Integer townhouse, Integer house) {

        Pageable pageable = PageRequest.of(page, size);

        List<Integer> buildingType = new ArrayList<>();

        if (apartment == 0) {
            buildingType.add(apartment);
        }
        if (townhouse == 1) {
            buildingType.add(townhouse);
        }
        if (house == 2) {
            buildingType.add(house);
        }
        if (room == 3) {
            buildingType.add(room);
        }
        if (room == 10 && house == 10 && townhouse == 10 && apartment == 10) {
            buildingType.add(0);
            buildingType.add(1);
            buildingType.add(2);
            buildingType.add(3);
        }

        List<ListedPropertyWithPagination> listedPropertyIds = new ArrayList<>();

        if (isColiving == null && isStudentHousing == null) {
            listedPropertyIds = listedPropertyRepository.findAllByParamsWithPaginationAndCoLivingIgnoredAndStudentHousingIgnored(userId, longitude, latitude, buildingType, minRent, maxRent, radius, pageable);
        } else if (isColiving == null && isStudentHousing) {
            listedPropertyIds = listedPropertyRepository.findAllByParamsWithPaginationAndCoLivingIgnoredAndStudentHousingTrue(userId, longitude, latitude, buildingType, minRent, maxRent, radius, pageable);
        } else if (isColiving == null && !isStudentHousing) {
            listedPropertyIds = listedPropertyRepository.findAllByParamsWithPaginationAndCoLivingIgnoredAndStudentHousingFalse(userId, longitude, latitude, buildingType, minRent, maxRent, radius, pageable);
        } else if (isColiving && isStudentHousing == null) {
            listedPropertyIds = listedPropertyRepository.findAllByParamsWithPaginationAndCoLivingTrueAndStudentHousingIgnored(userId, longitude, latitude, buildingType, minRent, maxRent, radius, pageable);
        } else if (!isColiving && isStudentHousing == null) {
            listedPropertyIds = listedPropertyRepository.findAllByParamsWithPaginationAndCoLivingFalseAndStudentHousingIgnored(userId, longitude, latitude, buildingType, minRent, maxRent, radius, pageable);
        } else if (!isColiving && !isStudentHousing) {
            listedPropertyIds = listedPropertyRepository.findAllByParamsWithPaginationAndCoLivingFalseAndStudentHousingFalse(userId, longitude, latitude, buildingType, minRent, maxRent, radius, pageable);
        } else if (!isColiving && isStudentHousing) {
            listedPropertyIds = listedPropertyRepository.findAllByParamsWithPaginationAndCoLivingFalseAndStudentHousingTrue(userId, longitude, latitude, buildingType, minRent, maxRent, radius, pageable);
        } else if (isColiving && !isStudentHousing) {
            listedPropertyIds = listedPropertyRepository.findAllByParamsWithPaginationAndCoLivingTrueAndStudentHousingFalse(userId, longitude, latitude, buildingType, minRent, maxRent, radius, pageable);
        } else if (isColiving && isStudentHousing) {
            listedPropertyIds = listedPropertyRepository.findAllByParamsWithPaginationAndCoLivingTrueAndStudentHousingTrue(userId, longitude, latitude, buildingType, minRent, maxRent, radius, pageable);
        }
//        listedPropertyRepository.findAllByParamsWithPagination(userId, longitude, latitude, buildingType, minRent, maxRent, isColivingTrue, isColivingFalse, studentHousingTrue, studentHousingFalse, 25, pageable);
        if (!listedPropertyIds.isEmpty()) {

            List<String> ids = new ArrayList<>();
            List<Double> distances = new ArrayList<>();
            for (ListedPropertyWithPagination each : listedPropertyIds) {
                ids.add(each.getListed_Property_Id());
                distances.add(each.getDistance());
            }
            List<ListedProperty> listedProperties = new ArrayList<>();
            for (String eachId : ids) {
                Optional<ListedProperty> listedProperty = listedPropertyRepository.findById(eachId);
                if (listedProperty.isPresent()) {
                    listedProperties.add(listedProperty.get());
                }
            }


            GetPropertiesWithPaginationDto getPropertiesWithPaginationDto = new GetPropertiesWithPaginationDto();
            getPropertiesWithPaginationDto.setPage(page);

            List<GetListedPropertiesResponseBody> properties = new ArrayList<>();

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

            if (!listedProperties.isEmpty()) {
                Integer iteration = 0;
                for (ListedProperty eachProperty : listedProperties) {

                    GetListedPropertiesResponseBody getListedPropertiesResponseBody =
                            modelMapper.map(eachProperty, GetListedPropertiesResponseBody.class);

                    PropertyLocationDto propertyLocationDto =
                            modelMapper.map(eachProperty.getLocation(), PropertyLocationDto.class);

                    PropertyAmenitiesDto propertyAmenitiesDto =
                            modelMapper.map(eachProperty.getListedPropertyAmenities(), PropertyAmenitiesDto.class);

                    List<PropertyImageDto> propertyImageDtoList =
                            modelMapper.map(eachProperty.getListedPropertyImages(), new TypeToken<List<PropertyImageDto>>() {
                            }.getType());

                    getListedPropertiesResponseBody.setImages(propertyImageDtoList);
                    getListedPropertiesResponseBody.setAmenities(propertyAmenitiesDto);
                    getListedPropertiesResponseBody.setLocation(propertyLocationDto);

                    Optional<ChillowUser> user = userRepository.findById(eachProperty.getOwnerId());
                    String userImage = null;
                    List<ChillowUserImage> images = user.get().getChillowUserImages();
                    for (ChillowUserImage eachImage : images) {
                        if (!eachImage.getIsDeleted() && eachImage.getSequence() == 0) {
                            userImage = eachImage.getFile();
                            break;
                        }
                    }
                    getListedPropertiesResponseBody.setOwnerImage(userImage);
                    getListedPropertiesResponseBody.setDistance(distances.get(iteration));
                    properties.add(getListedPropertiesResponseBody);
                    iteration++;
                }
                getPropertiesWithPaginationDto.setProperties(properties);
                getPropertiesWithPaginationDto.setStatus(200);
                return getPropertiesWithPaginationDto;
            }
        }
        return new GetPropertiesWithPaginationDto(Collections.EMPTY_LIST, 0, 0, 0, 204); //COMMENT OUT
    }

    public ListedPropertyResponseBody getListedPropertyById(String id) throws NotFoundException {
        Optional<ListedProperty> listedProperty = listedPropertyRepository.findById(id);
        if (listedProperty.isPresent() && !listedProperty.get().getIsListingDeleted()) {
            ListedProperty currentListedProperty = listedProperty.get();

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

            ListedPropertyResponseBody listedPropertyResponseBody =
                    modelMapper.map(currentListedProperty, ListedPropertyResponseBody.class);

            PropertyLocationDto propertyLocationDto =
                    modelMapper.map(currentListedProperty.getLocation(), PropertyLocationDto.class);

            List<PropertyImageDto> propertyImageDtoList =
                    modelMapper.map(currentListedProperty.getListedPropertyImages(),
                            new TypeToken<List<PropertyImageDto>>() {
                            }.getType());

            PropertyAmenitiesDto propertyAmenitiesDto =
                    modelMapper.map(currentListedProperty.getListedPropertyAmenities(), PropertyAmenitiesDto.class);

            listedPropertyResponseBody.setAmenities(propertyAmenitiesDto);
            listedPropertyResponseBody.setImages(propertyImageDtoList);
            listedPropertyResponseBody.setLocation(propertyLocationDto);

            Optional<ChillowUser> user = userRepository.findById(currentListedProperty.getOwnerId());
            if (user.isPresent()) {
                listedPropertyResponseBody.setOwnerImage(user.get().getChillowUserImages().get(0).getFile());
            } else {
                throw new UserNotFoundException("User not found");
            }
            return listedPropertyResponseBody;
        }
        return null;
    }

    public Boolean deleteProperty(String id) {
        Optional<ListedProperty> listedProperty = listedPropertyRepository.findById(id);
        Boolean isPresent = listedProperty.isPresent();
        if (isPresent) {
            listedProperty.get().setIsListingDeleted(true);
            listedPropertyRepository.save(listedProperty.get());
        }
        return isPresent;
    }

    public Boolean checkExistingPropertyOnLocation(Double longitude, Double latitude) {
        List<ListedPropertyLocation> listedPropertyLocation = listedPropertyLocationRepository.findAllByLongitudeAndLatitude(longitude, latitude);
        Boolean isNotDeleted = true;
        if (!listedPropertyLocation.isEmpty()) {
            for (ListedPropertyLocation eachPropertyLocation : listedPropertyLocation) {
                if (!eachPropertyLocation.getListedProperty().getIsListingDeleted()) {
                    isNotDeleted = true;
                    break;
                } else {
                    isNotDeleted = false;
                }
            }
            return isNotDeleted;
        }
        return false;
    }

    public List<ListedPropertyResponseBody> getFeaturedPartners(String propertyPartnerId, Double longitude, Double latitude) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STANDARD);
        FeaturedPropertyPartnerResponseBody featuredPropertyPartnerResponseBody =
                new FeaturedPropertyPartnerResponseBody();

        Optional<PropertyPartner> propertyPartner = propertyPartnerRepository.findById(propertyPartnerId);

        //GET ID OF LISTINGS
        List<ListedPropertyWithPagination> listedPropertyIds =
                propertyPartnerRepository.findListingsByRadius(longitude, latitude, propertyPartner.get().getId());

        List<String> ids = new ArrayList<>();

        for (ListedPropertyWithPagination eachId : listedPropertyIds) {
            ids.add(eachId.getListed_Property_Id());
        }

        List<ListedProperty> listedPropertyList = listedPropertyRepository.findAllByIdIn(ids);
        List<ListedPropertyResponseBody> listedPropertyResponseBodies = modelMapper.map(listedPropertyList, new TypeToken<List<ListedPropertyResponseBody>>() {
        }.getType());
        Optional<ChillowUser> user = userRepository.findById("72eae5e1-b844-4a5e-8f49-1ab6deb55011");
        String ownerImage = user.get().getChillowUserImages().get(0).getFile();
        for (ListedPropertyResponseBody each : listedPropertyResponseBodies) {
            each.setOwnerImage(ownerImage);
        }
        return listedPropertyResponseBodies;
    }

    public List<AdminPropertyPartnerResponseBody> getFeaturedPartnersForAdmin() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STANDARD);

        List<PropertyPartner> propertyPartnerList = propertyPartnerRepository.findAll();

        return modelMapper.map(propertyPartnerList, new TypeToken<List<AdminPropertyPartnerResponseBody>>() {
        }.getType());
    }

    public List<ListedPropertyResponseBody> getFeaturedPartnerListingsWithoutRadius(String propertyPartnerId) {
        List<ListedProperty> listedPropertyList = listedPropertyRepository.findAllByPropertyPartnerId(propertyPartnerId);
        if (!listedPropertyList.isEmpty()) {
            Optional<ChillowUser> user = userRepository.findById("72eae5e1-b844-4a5e-8f49-1ab6deb55011");
            String ownerImage = user.get().getChillowUserImages().get(0).getFile();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            List<ListedPropertyResponseBody> listedPropertyResponseBodyList = listedPropertyList.stream().map(each ->
                    modelMapper.map(each, ListedPropertyResponseBody.class)).collect(Collectors.toList());
            listedPropertyResponseBodyList.forEach(each -> each.setOwnerImage(ownerImage));
            return listedPropertyResponseBodyList;
        }
        return Collections.EMPTY_LIST;
    }

    public PropertyPartnerResponseBody getPropertyPartnerData(String propertyPartnerId) {
        Optional<PropertyPartner> propertyPartner = propertyPartnerRepository.findById(propertyPartnerId);
        if (propertyPartner.isPresent()) {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            PropertyPartnerResponseBody propertyPartnerResponseBody = modelMapper.map(propertyPartner.get(), PropertyPartnerResponseBody.class);

            List<PropertyImageDto> image = modelMapper.map(propertyPartner.get().getListedPropertyImages(), new TypeToken<List<PropertyImageDto>>() {
            }.getType());
            propertyPartnerResponseBody.setImage(image);
            return propertyPartnerResponseBody;
        }
        return null;
    }

    public Boolean postToEntrataFromBackend(String userId, List<String> propertyPartnerId) throws NotFoundException, JsonProcessingException {
        try {
            ReceivingObject getProperties = entrataService.getPropertiesfromEntrata();
            if (!propertyPartnerId.isEmpty()) {
                if (getProperties.getResponse().getPropertyResult() != null) {
                    for (String eachId : propertyPartnerId) {
                        Integer entrataPropertyId = extractPropertyId(getProperties, eachId);
                        if (entrataPropertyId != null) {
                            String leads = entrataService.leadSource(entrataPropertyId);
                            Integer leadsId = extractLeadsId(leads, entrataPropertyId.toString());
                            sendLeadsFromBackend(userId, leadsId, entrataPropertyId.toString());
                        }
                    }
                }
                return true;
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return false;
    }

    private void sendLeadsFromBackend(String userId, Integer leadsId, String entrataPropertyId) throws JsonProcessingException {
        Optional<ChillowUser> optionalChillowUser = userRepository.findById(userId);
        if (optionalChillowUser.isPresent()) {
            ChillowUser chillowUser = optionalChillowUser.get();
            SendLeadRequest sendLeadRequest = new SendLeadRequest();

            ZonedDateTime mountainTime = ZonedDateTime.now(ZoneId.of("America/Denver"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss");
            String formattedTime = mountainTime.format(formatter);

            String[] nameArr = chillowUser.getName() != null ? chillowUser.getName().split(" ") : new String[0];
            String firstName;
            String lastName;
            if (nameArr.length == 0) {
                firstName = "";
                lastName = "";
            } else if (nameArr.length == 1) {
                firstName = nameArr[0];
                lastName = "";
            } else {
                firstName = nameArr[0];
                lastName = nameArr[1];
            }

//            String[] nameArr = chillowUser.getName().split(" ");
//            String firstName;
//            String lastName;
//            if(nameArr[0] == null){
//                firstName ="";
//            }else{
//                firstName = nameArr[0];
//            }
//            if(nameArr.length == 2){
//                lastName ="";
//            }else{
//                lastName = nameArr[1];
//            }
            sendLeadRequest.setAuth(new Auth(
                    "basic"
            ));
            sendLeadRequest.setRequestId("15");
            sendLeadRequest.setMethod(new Method("sendLeads", null,
                    new Params(0,
                            entrataPropertyId, "1", "0",
                            new Prospects(
                                    new Prospect(
                                            new LeadSource(leadsId.toString(), null),
                                            formattedTime, null,
                                            new Customers(
                                                    new Customer(
                                                            new Name(
                                                                    firstName, lastName, null, null, null, null),
                                                            null, null, new Phone(
                                                            chillowUser.getNumber(), chillowUser.getNumber(), null, null),
                                                            chillowUser.getEmail(), null)
                                            ), null, null)
                            )
                    )));
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(sendLeadRequest);
//            System.out.print(json);
            String response = entrataService.sendLeads(sendLeadRequest);
//            System.out.println(response);
            return;
        }
        throw new UserNotFoundException("invalid user id!");
    }

    public Boolean postToEntrataFromFrontend(SendLeadRequestBody sendLeadRequestBody) throws NotFoundException, JsonProcessingException {
        try {
            ReceivingObject getProperties = entrataService.getPropertiesfromEntrata();
            Integer entrataPropertyId = extractPropertyId(getProperties, sendLeadRequestBody.getPropertyPartnerId());
            if (entrataPropertyId != null) {
                String leads = entrataService.leadSource(entrataPropertyId);
                if (leads != null || !leads.equals("")) {
                    Integer leadsId = extractLeadsId(leads, entrataPropertyId.toString());
                    if (leadsId != null) {
                        sendLeadsFromFrontend(sendLeadRequestBody, leadsId, entrataPropertyId.toString());
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return false;
    }

    private void sendLeadsFromFrontend(SendLeadRequestBody sendLeadRequestBody, Integer leadsId, String entrataPropertyId) throws JsonProcessingException {
        try {
            SendLeadRequest sendLeadRequest = new SendLeadRequest();

            ZonedDateTime mountainTime = ZonedDateTime.now(ZoneId.of("America/Denver"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss");
            String formattedTime = mountainTime.format(formatter);

            sendLeadRequest.setAuth(new Auth(
                    "basic"
            ));
            sendLeadRequest.setRequestId("15");
            sendLeadRequest.setMethod(new Method("sendLeads", null,
                    new Params(0,
                            entrataPropertyId, "1", "0",
                            new Prospects(
                                    new Prospect(
                                            new LeadSource(leadsId.toString(), null),
                                            formattedTime, null,
                                            new Customers(
                                                    new Customer(
                                                            new Name(
                                                                    sendLeadRequestBody.getFirstName(), sendLeadRequestBody.getLastName(), null, null, null, null),
                                                            null, null, new Phone(
                                                            sendLeadRequestBody.getNumber(), sendLeadRequestBody.getNumber(), null, null),
                                                            sendLeadRequestBody.getEmail(), null)
                                            ), null, null)
                            )
                    )));
            entrataService.sendLeads(sendLeadRequest);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    private Integer extractLeadsId(String leads, String entrataPropertyId) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(leads);

        JsonNode leadSourceNode = rootNode.at("/response/result/property/" + entrataPropertyId + "/leadSources/leadSource");
        if (!leadSourceNode.isEmpty()) {
            for (JsonNode eachNode : leadSourceNode) {
                String name = eachNode.at("/@attributes/name").asText();
                if (name.equals("Chillow")) {
                    return eachNode.at("/@attributes/id").asInt();
                }
            }
        }
        return null;
    }

    private Integer extractPropertyId(ReceivingObject getProperties, String propertyPartnerId) {
        Optional<PropertyPartner> propertyPartner = propertyPartnerRepository.findById(propertyPartnerId);
        if (propertyPartner.isPresent()) {
            List<Property> propertyList = getProperties.getResponse().getPropertyResult().getPhysicalProperty().getProperties();
            for (Property each : propertyList) {
                String incomingName = each.getMarketingName();
                String complexName = propertyPartner.get().getComplexName();

                if (incomingName.equals(complexName) || complexName.equals(incomingName.concat(" ")) ||
                        incomingName.equals(complexName.concat(" "))) {
                    return each.getPropertyId();
                }
            }
        }
        return null;
    }

    public ExternalPropertyResponse createExternalProperty(ExternalPropertyPartnerRequestBody externalPropertyPartnerDto) {
        Optional<ExternalPropertyPartner> isExternalExist = externalPropertyRepository.findByUserId(externalPropertyPartnerDto.getUserId());
        if (!isExternalExist.isPresent()) {

            ExternalPropertyPartner externalPropertyPartner = new ExternalPropertyPartner();
            externalPropertyPartner.setId(UUID.randomUUID().toString());
            externalPropertyPartner.setExternalPropertyComplexName(externalPropertyPartnerDto.getExternalPropertyComplexName());
            externalPropertyPartner.setUserId(externalPropertyPartnerDto.getUserId());
            externalPropertyPartner.setNeighborhood(externalPropertyPartnerDto.getNeighborhood());
            externalPropertyPartner.setCity(externalPropertyPartnerDto.getCity());
            externalPropertyPartner.setState(externalPropertyPartnerDto.getState());
            externalPropertyPartner.setCountry(externalPropertyPartnerDto.getCountry());
            externalPropertyPartner.setLongitude(externalPropertyPartnerDto.getLongitude());
            externalPropertyPartner.setLatitude(externalPropertyPartnerDto.getLatitude());
            if (externalPropertyPartnerDto.getCategory() >= 0 && externalPropertyPartnerDto.getCategory() <= 2) {
                PropertyType propertyType = PropertyType.values()[externalPropertyPartnerDto.getCategory()];
                externalPropertyPartner.setPropertyType(propertyType);
            } else {
                throw new RuntimeException("invalid category");
            }
            externalPropertyRepository.save(externalPropertyPartner);
            return new ModelMapper().map(externalPropertyPartner, ExternalPropertyResponse.class);
        }
        throw new RuntimeException("user has already added!");
    }

    @SneakyThrows
    public void sendMail(String userId, String shownUserId) {
        Optional<ChillowUser> firstRoommate = userRepository.findByIdAndIsDeletedFalse(userId);
        Optional<ChillowUser> secondRoommate = userRepository.findByIdAndIsDeletedFalse(shownUserId);
        if (firstRoommate.isPresent() && secondRoommate.isPresent()) {
            Optional<Swipe> findRightSwipedUser = swipeRepository.findByUserIdAndShownUserIdAndIsSwipedLeftFalseAndIsDeletedFalse(userId, shownUserId);
            if (findRightSwipedUser.isPresent()) {
                List<UserPreferredPropertyPartner> userPropertyPreferrenceList =
                        userPrefferedPropertyPartnerRepository.findByUserIdContainingAndIsPreferenceDeletedFalse(userId);

                List<UserPreferredPropertyPartner> shownUserPropertyPreferrenceList =
                        userPrefferedPropertyPartnerRepository.findByUserIdContainingAndIsPreferenceDeletedFalse(shownUserId);
                if (!userPropertyPreferrenceList.isEmpty() && !shownUserPropertyPreferrenceList.isEmpty()) {
                    for (UserPreferredPropertyPartner eachUserPreferrence : userPropertyPreferrenceList) {

                        for (UserPreferredPropertyPartner eachShownUserPreferrence : shownUserPropertyPreferrenceList) {

                            if (eachUserPreferrence.getPropertyPartner().equals(eachShownUserPreferrence.getPropertyPartner())) {
                                String id = eachShownUserPreferrence.getPropertyPartner();
                                Optional<PropertyPartnerContactDetail> propertyPartnerContactDetail =
                                        propertyPartnerContactDetailRepository.findByPropertyPartner(id);
                                if (propertyPartnerContactDetail.isPresent()) {
                                    mailFunction(propertyPartnerContactDetail.get().getEmail(), firstRoommate.get().getNumber(),
                                            firstRoommate.get().getName(), secondRoommate.get().getName(),
                                            secondRoommate.get().getNumber());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void mailFunction(String propertyPartnerEmail, String firstRoommateEmail, String firstRoommateName, String secondRoommateName, String secondRoommateEmail) throws MessagingException, IOException {

        javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper emailDetails = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        System.out.println(mailUsername);
        emailDetails.setFrom(mailUsername);
        emailDetails.setTo(propertyPartnerEmail);

//        ClassPathResource resource = new ClassPathResource("Twitter.png");
//
//        // Attach the image to the email
//        emailDetails.addInline("image", resource);

        emailDetails.setSubject("Onboarding");

        String html = "         <!doctype html>\n" +
                "         <html lang=\"en\">\n" +
                "         <head>\n" +
                "           <!-- Required meta tags -->\n" +
                "           <meta charset=\"utf-8\">\n" +
                "           <meta name=\"viewport\" content=\"width=device-width, initial-scale=1,\">\n" +
                "\n" +
                "           <!-- Bootstrap CSS -->\n" +
                "            <!--   <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\"> \n" +
                "             <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">  -->\n" +
                "             <link rel=\"stylesheet\" href=\"style.css\">\n" +
                "             <title>Chillow</title>\n" +
                "             <style>\n" +
                "       \n" +
                "               body{background-color: white;}\n" +
                "               .container{\n" +
                "                padding: 100px;\n" +
                "                \n" +
                "   }\n" +
                "   .img-fluid{\n" +
                "     top:13px;\n" +
                "     width:100%;\n" +
                "     height:550px;\n" +
                "\n" +
                "   }\n" +
                "   .logoDiv {\n" +
                "          position: absolute;\n" +
                "    top: 30px;\n" +
                "   }\n" +
                "   .logo{\n" +
                "    position: relative;\n" +
                "    left: 65px;\n" +
                "    height: 373px;\n" +
                "    width: 354.6px;\n" +
                "    z-index: -1;\n" +
                "   }\n" +
                "   .logo2{\n" +
                "     position:absolute;\n" +
                "     top:43;\n" +
                "     height:68px;\n" +
                "     width:63px;\n" +
                "     left: 0;\n" +
                "\n" +
                "   }\n" +
                "   .vector{\n" +
                "      position: absolute;\n" +
                "      width: 407px;\n" +
                "      height: 411px;\n" +
                "      right: 21px;\n" +
                "      top: 115px;\n" +
                "   }\n" +
                "\n" +
                ".container-fluid{padding: 100px; background-color: #fff; border-radius: 0px 0px 20px 30px; }\n" +
                "   .content{\n" +
                "      text-align: right;\n" +
                "      font-family: 'Poppins';\n" +
                "      font-style: normal;\n" +
                "      font-weight: 400;\n" +
                "      font-size: 20px;\n" +
                "      line-height: 30px;\n" +
                "   }\n" +
                "   .regards{\n" +
                "     text-align: right;\n" +
                "               }\n" +
                "               .question{ \n" +
                "                  text-align: center;\n" +
                "                  font-family: 'Poppins';\n" +
                "                  font-style: normal;\n" +
                "                  font-weight: 600;\n" +
                "                  font-size: 26px;\n" +
                "                  line-height: 39px;\n" +
                "               }\n" +
                "               .support{\n" +
                "                  text-align: center;\n" +
                "                  color: #828282;\n" +
                "               }\n" +
                "               .social-icons{\n" +
                "                  text-align: center;\n" +
                "                  padding: 5px;\n" +
                "                  \n" +
                "               }\n" +
                "               .heading-text{\n" +
                "                 position: absolute;\n" +
                "                 height: 66px;\n" +
                "                 left: 3.12%;\n" +
                "                 right: 54.84%;\n" +
                "                 top: calc(30% - 66px/2 + 6.5px);\n" +
                "                 font-family: 'Poppins';\n" +
                "                 font-style: normal;\n" +
                "                 font-weight: 600;\n" +
                "                 font-size: 22px;\n" +
                "                 line-height: 33px;\n" +
                "                 color: #F3F5F7;\n" +
                "\n" +
                "              }\n" +
                "              .main-image{\n" +
                "               display: inherit;\n" +
                "            }\n" +
                "            .main-image2{\n" +
                "               display: none;\n" +
                "            }\n" +
                "\n" +
                "            @media screen and (max-width: 600px){\n" +
                "               .container{padding: 0px}\n" +
                "               .heading-text>h3{text-align: center;position: relative;\n" +
                "   left: 66.12%;\n" +
                "   top: -139px;\n" +
                "/*   top: calc(100% - 66px/2 + 6.5px);*/\n" +
                "   }\n" +
                "   .main-image {display: none;}\n" +
                "   .main-image2 {\n" +
                "      display: inherit;\n" +
                "    position: absolute;\n" +
                "    left: 25%;\n" +
                "    bottom: 38px;\n" +
                "   }\n" +
                "   .logo{display: none;}\n" +
                "\n" +
                "   }\n" +
                "   .center {\n" +
                "      margin-left: auto;\n" +
                "      margin-right: auto;\n" +
                "    }\n" +
                "    .centerrr {\n" +
                " display: block;\n" +
                " margin-left: auto;\n" +
                " margin-right: auto;\n" +
                " padding-bottom: 20px;\n" +
                "/* width: 50%;*/\n" +
                "}\n" +
                "\n" +
                "\n" +
                "   @media screen and (max-width: 500px){\n" +
                "      .main-image2{\n" +
                "left: 20%;\n" +
                "bottom: 312px;      \n" +
                "      }\n" +
                "\n" +
                "   }\n" +
                "\n" +
                "   @media screen and (max-width: 400px){\n" +
                "\n" +
                "      .main-image2 {\n" +
                "         left: 7%;\n" +
                "         bottom: 274px;\n" +
                "         transform: scale(0.7);\n" +
                "      }\n" +
                "\n" +
                "   }\n" +
                "table, th, td {\n" +
                "  border: 1px solid black;\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "   </style>\n" +
                "   </head>\n" +
                "   <body>\n" +
                "      <div class=\"container\">\n" +
                "<h2 class=\"question\">Lease Holder Matches</h2>\n" +
                "        <table style=\"width:50%\" class=\"center\" align=\"center\">\n" +
                "  <tr>\n" +
                "    <th>Lease Holder</th>\n" +
                "    <th>Potential Roommmate</th> \n" +
                "    \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>" + firstRoommateName + ", " + firstRoommateEmail + "</td>\n" +
                "    <td>" + secondRoommateName + ", " + secondRoommateEmail + "</td>\n" +
                "      \n" +
                "    \n" +
                "  </tr>\n" +
                "</table>\n" +
                "\n" +
                "      <div class=\"container-fluid\">\n" +
                "         <div class=\"row\">           \n" +
                "         <div >\n" +
                "             <img src=\"https://firebasestorage.googleapis.com/v0/b/chillow-302106.appspot.com/o/chillow-logo.png?alt=media&token=fd15b24c-cc39-4f86-aef8-dc4f88d5b758\" width=\"300px\" height=\"100px\"  class=\"centerrr\">\n" +
                "            <div class=\"social-icons\">\n" +
                "               <img src=\"https://firebasestorage.googleapis.com/v0/b/chillow-302106.appspot.com/o/twitter.png?alt=media&token=795e8249-1bba-4cba-9437-85aad38cc4eb\" width=\"40px\" height=\"40px\">\n" +
                "               <img src=\"https://firebasestorage.googleapis.com/v0/b/chillow-302106.appspot.com/o/facebook.png?alt=media&token=926d71a5-972b-4790-afd5-fd2d0e9a353f\" width=\"40px\" height=\"40px\">\n" +
                "               <img src=\"https://firebasestorage.googleapis.com/v0/b/chillow-302106.appspot.com/o/link.jpg?alt=media&token=95f5435d-1249-4503-b664-faf44dc1b42b\" width=\"50px\" height=\"50px\">\n" +
                "             \n" +
                "            </div> \n" +
                "          \n" +
                "           <p class=\"support\">Copyright © 2023 Chillow, All right reserved.<br>\n" +
                "            You are receiving this email because you opted in via our website<br>\n" +
                "\n" +
                "       </p>\n" +
                "          <h2 class=\"question\">Our mailing address is:</h2>\n" +
                "            \n" +
                "             <div class=\"support\">\n" +
                "                <p>Chillow </p>\n" +
                "               </div>\n" +
                "          </div>\n" +
                "          <p class=\"support\">3527 S Federal Way Ste 103 PMB 117 <br>\n" +
                "            Boise, ID 83705-5228<br>\n" +
                "\n" +
                "       </p>\n" +
                "        <div class=\"support\">\n" +
                "                <p>Add us to your address book </p>\n" +
                "               </div>\n" +
                "               <p class=\"support\">Want to change how you receive these emails? <br>\n" +
                "           You can update your preferences or unsubscribe from this list.<br>\n" +
                "\n" +
                "       </p>\n" +
                "          </div>\n" +
                "\n" +
                "       </div>\n" +
                "      </div>\n" +
                "   </div>\n" +
                "   </body>\n" +
                "   </html>\n" +
                "\n";
        emailDetails.setText(html, true);
        try {
            javaMailSender.send(mimeMessage);
            log.info("Mail Sent Successfully...");
        } catch (RuntimeException e) {
            log.error("Failed to dispatch email");
        }
    }
}
