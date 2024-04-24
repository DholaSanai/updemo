package com.backend.demo.repository.room;

import com.backend.demo.dto.room.ElementCount;
import com.backend.demo.dto.room.RadiusLocationId;
import com.backend.demo.dto.room.UsersInRadiusOfRoom;
import com.backend.demo.entity.room.ChillowRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChillowRoomRepository extends JpaRepository<ChillowRoom, String> {

    List<ChillowRoom> findByUserIdAndIsDeletedFalse(String userId);

    Page<ChillowRoom> findAllByIsDeletedFalse(Pageable pageable);

//    @Query(value = "SELECT d.* FROM (SELECT u.id as id, u.name as name, cr.id as locationId, " +
//            "       (SELECT SQRT(POW(69.1 * (crl.latitude - :latitude), 2) + POW(69.1 * (:longitude - crl.longitude) * COS(crl.latitude / 57.3), 2))  ) as distance " +
//            "FROM chillow_user u LEFT JOIN listed_property cr on u.id = cr.user_id " +
//            "JOIN chillow_room_location crl on cr.id = crl.chillow_room_id ORDER BY distance) as d WHERE d.distance < :radius ;",
//            nativeQuery = true)
//    List<UsersInRadiusOfRoom> findUsersWithinRadius(@Param("longitude") Double longitude,
//                                                    @Param("latitude") Double latitude,
//                                                    @Param("radius") Integer radius);

    @Query(value = "SELECT d.id FROM (SELECT u.id as id," +
            "                  (SELECT SQRT(POW(69.1 * (ul.latitude - :latitude), 2) + POW(69.1 * (:longitude - ul.longitude) * COS(ul.latitude / 57.3), 2))  ) as distance" +
            "            FROM chillow_user u LEFT JOIN user_location ul on u.id = ul.user_id WHERE u.id != :userId and u.want_to = 1 ORDER BY distance) as d WHERE d.distance < 100;",
            nativeQuery = true)
    List<UsersInRadiusOfRoom> findUsersWithinRadius(@Param("longitude") Double longitude,
                                                    @Param("latitude") Double latitude,
                                                    @Param("userId") String userId);


    Page<ChillowRoom> findByChillowRoomLocation_LongitudeAndChillowRoomLocation_Latitude(Float longitude, Float latitude, Pageable pageable);

    List<ChillowRoom> findByIdIn(List<String> ids);

    @Query(value = "SELECT COUNT(propertyResult.*) as count FROM ( SELECT cr.id as Id, " +
            "(SELECT SQRT(POW(69.1 * (crl.latitude - :latitude), 2) + POW(69.1 * ( :longitude - crl.longitude) * COS(crl.latitude / 57.3), 2))  ) as distance " +
            "FROM chillow_room cr JOIN chillow_room_location crl on cr.id = crl.chillow_room_id WHERE NOT cr.user_id = :userId AND cr.deleted = false) as propertyResult where propertyResult.distance < :radius ;", nativeQuery = true)
    ElementCount countByLongitudeAndLatitudeAndRadius(@Param("longitude") Float longitude,
                                                      @Param("latitude") Float latitude,
                                                      @Param("userId") String userId,
                                                      @Param("radius") Double radius);

    @Query(value = "SELECT propertyResult.id as Id, propertyResult.distance as distance FROM (SELECT cr.id,\n" +
            "            (SELECT SQRT(POW(69.1 * (crl.latitude - :latitude ), 2) + POW(69.1 * ( :longitude - crl.longitude) * COS(crl.latitude / 57.3), 2))  ) as distance\n" +
            "            FROM chillow_room cr JOIN chillow_room_location crl on cr.id = crl.chillow_room_id WHERE NOT cr.user_id = :userId AND cr.deleted = false\n" +
            "            ORDER BY distance\n" +
            "            OFFSET :page LIMIT :size) as propertyResult where propertyResult.distance < :radius ;", nativeQuery = true)
    List<RadiusLocationId> findByLongitudeAndLatitudeAndRadius(@Param("longitude") Float longitude,
                                                               @Param("latitude") Float latitude,
                                                               @Param("radius") Double radius,
                                                               @Param("page") Long page,
                                                               @Param("userId") String userId,
                                                               @Param("size") Integer size);

    int countByChillowRoomLocation_AddressContainingAndChillowRoomLocation_CityContainingAndChillowRoomLocation_CountyContainingAndChillowRoomLocation_StateContaining(String address,String city,String county,String state);

//    int countByChillowRoomLocation_AddressContaining(String address);

//    Page<ChillowRoom> findByChillowRoomLocation_AddressContainingOrChillowRoomLocation_CityContainingOrChillowRoomLocation_NeighborhoodContainingOrChillowRoomLocation_StateContainingOrChillowRoomLocation_CountyContaining(String address, String city,String neighborhood,String state,String county);


}
