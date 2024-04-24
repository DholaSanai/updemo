package com.backend.demo.repository.property;

import com.backend.demo.entity.property.ListedProperty;
import com.backend.demo.entity.property.ListedPropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListedPropertyImagesRepository extends JpaRepository<ListedPropertyImage, String> {

    List<ListedPropertyImage> findAllByListedProperty(ListedProperty presentListedProperty);
}
