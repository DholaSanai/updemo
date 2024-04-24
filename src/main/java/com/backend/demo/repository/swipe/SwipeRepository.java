package com.backend.demo.repository.swipe;

import com.backend.demo.entity.swipe.Swipe;
import com.backend.demo.model.ChillowUserByDistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SwipeRepository extends JpaRepository<Swipe, UUID> {
    Optional<Swipe> findByUserIdAndShownUserIdAndIsSwipedLeftFalseAndIsDeletedFalse(String userId, String shownUserId);

    List<Swipe> findByUserId(String userId);

    List<Swipe> findByUserIdAndIsDeleted(String userId, Boolean isDeleted);

    boolean existsByUserIdAndShownUserIdAndIsDeletedFalse(String userId, String shownUserId);

@Query(value = "SELECT * FROM (SELECT  u.id,u.name,u.email,u.birth_date,u.number,u.last_login, l.longitude, l.latitude, \n" +
        "            (SELECT SQRT(POW(69.1 * (l.latitude - :latitude), 2) + POW(69.1 * (:longitude - l.longitude) * COS(latitude / 57.3), 2)) \n" +
        "             ) as distance \n" +
        "             FROM chillow_user u left outer join user_location l on u.id = l.user_id \n" +
        "             WHERE NOT u.id = :userId \n" +
        "             AND u.is_deleted = false \n" +
        "             AND u.is_admin = false \n" +
        "             AND u.id NOT IN (SELECT shown_user_id FROM SWIPE WHERE user_id = :userId) \n" +
        "             ORDER BY distance) AS dt WHERE dt.distance <= :distance", nativeQuery = true)
List<ChillowUserByDistance> findUsersWithLocation(@Param("userId") String userId, @Param("longitude") Double longitude,
                                                  @Param("latitude") Double latitude, @Param("distance") Integer distance);

    List<Swipe> findAllByUserIdAndIsSwipedLeftTrue(String userId);

    List<Swipe> findByUserIdAndIsDeletedAndIsSwipedLeftFalse(String userId, boolean b);

    Optional<Swipe> findByUserIdAndShownUserId(String requestedById, String requestedToId);
}
