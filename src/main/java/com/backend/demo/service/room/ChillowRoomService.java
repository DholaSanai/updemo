package com.backend.demo.service.room;

import com.backend.demo.dto.room.ChillowRoomChatDto;
import com.backend.demo.dto.room.ChillowRoomDto;
import com.backend.demo.dto.room.PaginationDTO;

import java.util.List;

public interface ChillowRoomService {
    ChillowRoomDto getById(String id);

    List<ChillowRoomDto> getByUser(String userId);

    PaginationDTO<ChillowRoomDto> getAll(String userId, Integer page, Integer size);

    PaginationDTO<ChillowRoomDto> getAll(String userId, Integer page, Integer size, Float longitude, Float latitude, Integer radius);


    boolean validateLocation(String buildingType, String address, String city,String county,String state);
    ChillowRoomDto saveRoom(ChillowRoomDto room);

    boolean deleteRoom(String id);

    boolean deleteRoomsOfUser(String userId);

    List<ChillowRoomChatDto> saveRoomChat(String ownerId, String userChat, String roomId, String requestingUserId);
}
