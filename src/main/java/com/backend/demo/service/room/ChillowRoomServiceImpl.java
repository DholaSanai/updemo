package com.backend.demo.service.room;

import com.backend.demo.components.QueueSender;
import com.backend.demo.dto.room.*;
import com.backend.demo.dto.user.chillowUser.ChillowUserProfile;
import com.backend.demo.entity.room.*;
import com.backend.demo.exceptions.AlreadyExistingChatException;
import com.backend.demo.repository.room.ChillowRoomRepository;
import com.backend.demo.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChillowRoomServiceImpl implements ChillowRoomService {
    @Autowired
    ChillowRoomRepository chillowRoomRepository;
    @Autowired
    private QueueSender queueSender;
    @Autowired
    private UserService userServiceClient;

    @Override
    public ChillowRoomDto getById(String id) {
        Optional<ChillowRoom> room = chillowRoomRepository.findById(id);
        if (room.isPresent()) {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(room.get(), ChillowRoomDto.class);
        }
        return null;
    }

    @Override
    public List<ChillowRoomDto> getByUser(String userId) {
        List<ChillowRoom> rooms = chillowRoomRepository.findByUserIdAndIsDeletedFalse(userId);
        if (!CollectionUtils.isEmpty(rooms)) {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return rooms.stream().map(room -> {
                room.getChillowRoomImages().removeIf(ChillowRoomImage::getIsDeleted);
                return modelMapper.map(room, ChillowRoomDto.class);
            }).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public PaginationDTO<ChillowRoomDto> getAll(String userId, Integer page, Integer size) {
        log.info("Default listings API called");
        Pageable pageable = PageRequest.of(page, size);
        Page<ChillowRoom> roomPage = chillowRoomRepository.findAllByIsDeletedFalse(pageable);
        if (roomPage.hasContent()) {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            List<String> userIds = roomPage.getContent().stream().map(ChillowRoom::getUserId).collect(Collectors.toList());
            Map<String, String> profileImagesOfOwners = null;
            try {
                profileImagesOfOwners = userServiceClient.getProfileImagesByIds(userIds);
            } catch (Exception exception) {
                log.error("Error occured while getting profile images of users", exception);
            }
            Map<String, String> finalProfileImagesOfOwners = profileImagesOfOwners;
            List<ChillowRoomDto> content = roomPage.getContent().stream().filter(x -> !x.getUserId().equals(userId))
                    .map(room -> {
                                ChillowRoomDto mappedRoom = modelMapper.map(room, ChillowRoomDto.class);
                                if (!CollectionUtils.isEmpty(finalProfileImagesOfOwners)) {
                                    mappedRoom.setUserProfileImage(finalProfileImagesOfOwners.get(mappedRoom.getUserId()));
                                }
                                return mappedRoom;
                            }
                    ).collect(Collectors.toList());
            return new PaginationDTO<>(content, page, roomPage.getTotalPages(),
                    roomPage.getNumberOfElements(), size, roomPage.getTotalElements());
        }
        log.info("No listings found");
        return new PaginationDTO<>(Collections.EMPTY_LIST, page, 0, 0, size, 0L);
    }

    @Override
    public boolean validateLocation(String buildingType, String address, String city, String county, String state) {
        if (buildingType.equals("Apartment")) {
            return true;
        }
        int existingRooms = chillowRoomRepository.countByChillowRoomLocation_AddressContainingAndChillowRoomLocation_CityContainingAndChillowRoomLocation_CountyContainingAndChillowRoomLocation_StateContaining(address,city,county,state);
//        int existingRooms = chillowRoomRepository.countByChillowRoomLocation_AddressContaining(address);

        return existingRooms <= 0;
    }

    @Override
    public PaginationDTO<ChillowRoomDto> getAll(String userId, Integer page, Integer size, Float longitude,
                                                Float latitude, Integer radius) {
        Pageable pageable = PageRequest.of(page, size);

        List<RadiusLocationId> roomList = chillowRoomRepository.
                findByLongitudeAndLatitudeAndRadius(longitude, latitude, radius.doubleValue(),
                        pageable.getOffset(), userId,pageable.getPageSize());
        ElementCount totalElements = chillowRoomRepository.countByLongitudeAndLatitudeAndRadius(longitude, latitude,userId,radius.doubleValue());
        if (!CollectionUtils.isEmpty(roomList)) {
            List<ChillowRoom> roomsOnPage = chillowRoomRepository.findByIdIn(roomList.stream().
                    filter(x -> x.getDistance() < radius).map(RadiusLocationId::getId).collect(Collectors.toList()));
            List<String> userIds = roomsOnPage.stream().map(ChillowRoom::getUserId).collect(Collectors.toList());
            List<ChillowUserProfile> profileImagesOfOwners = null;
            try {
                profileImagesOfOwners = userServiceClient.getUserProfileByIds(userIds);
            } catch (Exception exception) {
                log.error("Error occured while getting profile images of users", exception);
            }
            List<ChillowUserProfile> finalProfileImagesOfOwners = profileImagesOfOwners;
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            List<ChillowRoomDto> content = roomsOnPage.stream().filter(x -> !x.getUserId().equals(userId))
                    .map(room -> {
                        ChillowRoomDto mappedRoom = modelMapper.map(room, ChillowRoomDto.class);
                        if (!CollectionUtils.isEmpty(finalProfileImagesOfOwners)) {
                            Optional<ChillowUserProfile> foundProfile = finalProfileImagesOfOwners.stream().
                                    filter(x -> x.getId().equals(room.getUserId())).findFirst();
                            if (foundProfile.isPresent()) {
                                mappedRoom.setUserProfileImage(foundProfile.get().getProfileImage());
                                mappedRoom.setUserName(foundProfile.get().getName());
                            }
                        }
                        return mappedRoom;
                    }).collect(Collectors.toList());
            Integer totalPages = Math.toIntExact(totalElements.getCount() / (size));
            return new PaginationDTO<>(content, page, totalPages,
                    roomsOnPage.size(), size, totalElements.getCount());
        }
        log.info("No listings found");
        return new PaginationDTO<>(Collections.EMPTY_LIST, page, 0, 0, size, 0L);
    }

    public void dispatchNotificationToNearbyUsers(ChillowRoomDto data) {
        try {
            log.info("Notifying users on new location added");
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            String json = mapper.writeValueAsString(data);
            queueSender.sendToRoomQueue(json);
        } catch (JsonProcessingException exception) {
            log.error("Error occured while mapping to json", exception);
        }
    }


    @Override
    public ChillowRoomDto saveRoom(ChillowRoomDto room) {
        ChillowRoom roomEntity;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        if (StringUtils.isEmpty(room.getId())) {
            roomEntity = modelMapper.map(room, ChillowRoom.class);
            roomEntity.setIsDeleted(false);
            roomEntity.setUpdatedAt(LocalDateTime.now());
            if (StringUtils.isEmpty(roomEntity.getId())) {
                roomEntity.setId(UUID.randomUUID().toString());
            }
            ChillowRoomLocation location = roomEntity.getChillowRoomLocation();
            if (StringUtils.isEmpty(location.getId())) {
                location.setId(UUID.randomUUID().toString());
            }
            location.setChillowRoom(roomEntity);
            ChillowRoomAmenities amenities = roomEntity.getChillowRoomAmenities();
            if (StringUtils.isEmpty(amenities.getId())) {
                amenities.setId(UUID.randomUUID().toString());
            }
            amenities.setChillowRoom(roomEntity);
            List<ChillowRoomImage> images = roomEntity.getChillowRoomImages();
            if (!CollectionUtils.isEmpty(images)) {
                images.forEach(i -> {
                    if (StringUtils.isEmpty(i.getId())) {
                        i.setId(UUID.randomUUID().toString());
                    }
                    i.setIsDeleted(false);
                    i.setChillowRoom(roomEntity);
                });
            }
            ChillowRoomDto mappedEntity = modelMapper.map(chillowRoomRepository.save(roomEntity), ChillowRoomDto.class);
            dispatchNotificationToNearbyUsers(mappedEntity);
            return mappedEntity;
        } else {
            Optional<ChillowRoom> existingRoom = chillowRoomRepository.findById(room.getId());
            if (existingRoom.isPresent()) {
                roomEntity = existingRoom.get();
                roomEntity.setBathrooms(room.getBathrooms());
                roomEntity.setBedrooms(room.getBedrooms());
                roomEntity.setWhenAvailable(room.getWhenAvailable());
                roomEntity.setFemaleRoommates(room.getFemaleRoommates());
                roomEntity.setMaleRoommates(room.getMaleRoommates());
                roomEntity.setBuildingType(room.getBuildingType());
                roomEntity.setLeaseTerm(room.getLeaseTerm());
                roomEntity.setParkingCost(room.getParkingCost());
                roomEntity.setMonthlyRent(room.getMonthlyRent());
                roomEntity.setPetPreference(room.isPetPreference());
                roomEntity.setPropertySize(room.getPropertySize());
                roomEntity.setHomePreference(room.getHomePreference());
                roomEntity.setRoomDescription(room.getRoomDescription());
                roomEntity.setUtilitiesIncluded(room.getUtilitiesIncluded());
                roomEntity.setIsDeleted(false);
                roomEntity.setCreatedAt(LocalDateTime.now());

                ChillowRoomAmenities amenities = modelMapper.map(room.getChillowRoomAmenities(),
                        ChillowRoomAmenities.class);
                amenities.setChillowRoom(roomEntity);
                amenities.setId(roomEntity.getChillowRoomAmenities().getId());
                roomEntity.setChillowRoomAmenities(amenities);

                ChillowRoomLocation location = modelMapper.map(room.getChillowRoomLocation(),
                        ChillowRoomLocation.class);
                location.setChillowRoom(roomEntity);
                location.setId(roomEntity.getChillowRoomLocation().getId());
                roomEntity.setChillowRoomLocation(location);

                roomEntity.getChillowRoomImages().forEach(x -> x.setIsDeleted(true));
                List<ChillowRoomImage> newImages = room.getChillowRoomImages().stream().map(img -> {
                    ChillowRoomImage i = modelMapper.map(img, ChillowRoomImage.class);
                    i.setIsDeleted(false);
                    i.setId(UUID.randomUUID().toString());
                    i.setChillowRoom(roomEntity);
                    return i;
                }).collect(Collectors.toList());

                roomEntity.getChillowRoomImages().addAll(newImages);
                ChillowRoomDto mappedEntity = modelMapper.map(chillowRoomRepository.save(roomEntity), ChillowRoomDto.class);
                dispatchNotificationToNearbyUsers(mappedEntity);
                return mappedEntity;
            }

        }
        throw new NullPointerException("Room data is null");
    }

    @Override
    public boolean deleteRoom(String id) {
        Optional<ChillowRoom> room = chillowRoomRepository.findById(id);
        if (room.isPresent()) {
            room.get().setIsDeleted(true);
            chillowRoomRepository.save(room.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRoomsOfUser(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        List<ChillowRoom> allRooms = chillowRoomRepository.findByUserIdAndIsDeletedFalse(userId);
        allRooms.forEach(eachRoom -> {
            eachRoom.setIsDeleted(true);
        });
        chillowRoomRepository.saveAll(allRooms);
        return true;
    }

    @Override
    public List<ChillowRoomChatDto> saveRoomChat(String userId, String userChat, String roomId, String requestingUserId) {
        Optional<ChillowRoom> roomOfUser = chillowRoomRepository.findById(roomId);
        if (roomOfUser.isPresent()) {
            log.info("user list found");
            //check if chat already exists

            List<ChillowRoomChat> chats = roomOfUser.get().getChillowRoomChats();
            if (CollectionUtils.isEmpty(chats)) {
                chats = new ArrayList<>();
            } else {
                for (ChillowRoomChat chat : chats) {
                    if (chat.getChat().contains(userChat)) {
                        throw new AlreadyExistingChatException("Chat with user already exists Already exists");
                    }
                }
            }


            ChillowRoomChat newChat = new ChillowRoomChat(UUID.randomUUID().toString(), userChat, Strings.EMPTY, roomOfUser.get());
            String profileImage = Strings.EMPTY;
            try {
                profileImage = userServiceClient.getUserProfileImage(requestingUserId);
            } catch (Exception exception) {
                log.error("Service not reachable", exception);
            }

            if (!StringUtils.isEmpty(profileImage)) {
                newChat.setProfileImage(profileImage);
            }
            chats.add(newChat);
            roomOfUser.get().setChillowRoomChats(chats);
            chillowRoomRepository.save(roomOfUser.get());
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            return chats.stream().map(x -> modelMapper.map(x, ChillowRoomChatDto.class)).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }
}
