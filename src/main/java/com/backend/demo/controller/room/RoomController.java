package com.backend.demo.controller.room;

import com.backend.demo.dto.room.*;
import com.backend.demo.service.room.ChillowRoomService;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chillow-rooms/api/v1/")
@Slf4j
public class RoomController {

    @Autowired
    ChillowRoomService chillowRoomService;

    @Value("${token.secret}")
    private String tokenSecret;

    @GetMapping("/{id}")
    public ChillowRoomDto getRoom(@PathVariable String id) {
        return chillowRoomService.getById(id);
    }

    public String getUserIdFromToken(String token){
        String temp = token.replace("Bearer ", "");
        if (tokenSecret == null || tokenSecret.isEmpty())
            return null;
        try {
            return Jwts.parser().
                    setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8)).
                    parseClaimsJws(temp).
                    getBody().getSubject();
        }
        catch(Exception e){
            log.error("Error occured while decoding the string",e);
        }
        return null;
    }
    @GetMapping("/rooms")
    public PaginationDTO<ChillowRoomDto> getRoomsByPagination(@RequestHeader("Authorization") String authToken,
                                                              @RequestParam(defaultValue = "0", required = false) Integer page,
                                                              @RequestParam(defaultValue = "10", required = false) Integer size,
                                                              @RequestParam(defaultValue = "0", required = false) Float longitude,
                                                              @RequestParam(defaultValue = "0", required = false) Float latitude,
                                                              @RequestParam(defaultValue = "100", required = false) Integer radius) {
        if (page < 0) {
            throw new ArithmeticException("Page can not be less than 0");
        }

        if (size < 0) {
            throw new ArithmeticException("Size of content can not be less than 0");
        }
        String userId = getUserIdFromToken(authToken);
        if (longitude == 0 && latitude == 0) {
            return chillowRoomService.getAll(userId,page, size);
        }

        return chillowRoomService.getAll(userId,page, size, longitude, latitude, radius);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ChillowRoomDto>> getRoomByUserId(@PathVariable String id) {
        return ResponseEntity.of(Optional.ofNullable(chillowRoomService.getByUser(id)));
    }

    @PostMapping("/save-room")
    public ChillowRoomDto saveRoom(@Valid @RequestBody ChillowRoomDto room) {
        return chillowRoomService.saveRoom(room);
    }

    @PostMapping("/check/address")
    public Boolean checkIfAddressAlreadyTaken(@Valid @RequestBody AddressValidation body){
        return chillowRoomService.validateLocation(body.getBuildingType(), body.getAddress(),
                body.getCity(), body.getCounty(), body.getState());
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteRoom(@PathVariable String id) {
        return chillowRoomService.deleteRoom(id);
    }

    @DeleteMapping("/user/room-listings")
    public boolean deleteUserRooms(@RequestParam String userId) {
        return chillowRoomService.deleteRoomsOfUser(userId);
    }

    @PostMapping("/room/chat")
    public List<ChillowRoomChatDto> saveChat(@RequestHeader("Authorization") String authToken, @RequestBody SaveRoomChat body){
        String userId = getUserIdFromToken(authToken);
        return chillowRoomService.saveRoomChat(userId,body.getUserChat(), body.getRoomId(),body.getRequestingUserId());
    }

}
