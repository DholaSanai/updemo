package com.backend.demo.entity.user;//package com.backend.chillow.entity.user;
//
//import com.backend.chillow.enums.property.PropertyType;
//import lombok.*;
//
//import javax.persistence.*;
//import java.time.Instant;
//
//@Builder
//@Setter
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "listing_properties")
//public class ListingProperties {
//    @Id
//    @Column(name = "id", nullable = false)
//    private String id;
//    @ManyToOne
//    @JoinColumn(name = "chillow_housing_id")
//    private ChillowHousing chillowHousing;
//
//    @Column(name = "owner_id")
//    private String owner;
//
//    @Column(name = "name")
//    private String name;
//
//    @Column(name = "property_type")
//    private PropertyType propertyType;
//
//    @Column(name = "is_deleted")
//    private boolean isDeleted;
////    @Column(name = "location")
////    private String location;
//
//    @Column(name = "created_at")
//    private Instant createdAt;
//
//    @Column(name = "updated_at")
//    private Instant updatedAt;
//
//}
