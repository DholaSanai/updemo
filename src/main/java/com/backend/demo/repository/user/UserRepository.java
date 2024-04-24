package com.backend.demo.repository.user;

import com.backend.demo.entity.user.ChillowUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ChillowUser, String> {

    Optional<ChillowUser> findByEmail(String email);

    boolean existsByNumber(String number);
    Optional<ChillowUser> findByEmailAndIsDeletedFalse(String email);

    List<ChillowUser> findByIdIn(List<String> ids);

    List<ChillowUser> findByIdInOrderByLastLoginDesc(List<String> ids);

    @Query(value = "SELECT * From (SELECT u.id, u.name, u.email, u.birth_date, u.number,l.longitude,l.latitude,(SQRT(POW(69.1 * (l.latitude - :latitude), 2) + POW(69.1 * (:longitude - l.longitude) * COS(l.latitude / 57.3), 2)) \n" +
            "         ) as distance FROM chillow_user u LEFT OUTER JOIN user_location l on l.user_id = u.id " +
            "         WHERE NOT u.id = :userId" +
            "             and u.id NOT IN (:ids)\n" +
            "           ORDER BY distance) as dt where dt.distance < 100", nativeQuery = true)
    List<Tuple> findByIdNotInWithDistanceLessThanHundred(@Param("userId") String userId,
                                                         @Param("ids") List<String> ids,
                                                         @Param("longitude") Double longitude,
                                                         @Param("latitude") Double latitude);


    boolean existsByEmail(String email);

    Optional<ChillowUser> findById(String id);
    List<ChillowUser> findAllByIdIn(List<String> id);

    List<ChillowUser> findAllByNumberIn(List<String> numbers);

    List<ChillowUser> findAllByEmailIn(List<String> emailAddresses);

    List<ChillowUser> findByNumber(String number);

    Optional<ChillowUser> findOneByNumber(String number);

    Optional<ChillowUser> findByIdAndIsDeletedFalse(String id);
}
