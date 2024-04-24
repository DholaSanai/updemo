package com.backend.demo.repository.user;

import com.backend.demo.entity.user.ChillowUser;
import com.backend.demo.entity.user.ChillowUserImage;
import com.backend.demo.model.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChillowUserImagesRepository extends JpaRepository<ChillowUserImage, String> {
    void deleteByIdIn(List<String> ids);

    void deleteByUser(ChillowUser user);

    @Query(value = "select u.user_id as id,u.file as file from chillow_user_image u " +
            "where u.user_id in :ids and u.sequence = 0 and u.deleted = false"
            , nativeQuery = true)
    List<ImageFile> findImagesInUserIds(List<String> ids);

    ChillowUserImage findByUserAndIsDeletedFalse(String ownerId);
}
