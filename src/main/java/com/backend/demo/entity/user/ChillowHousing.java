package com.backend.demo.entity.user;//package com.backend.chillow.entity.user;
//
//import com.backend.chillow.enums.user.WantTo;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.time.Instant;
//import java.util.List;
//
//@Entity
//@AllArgsConstructor
//@RequiredArgsConstructor
//@Setter
//@Getter
//@Table(name = "chillow_housing")
//public class ChillowHousing {
//    @Id
//    @Column(name = "id", nullable = false)
//    private String id;
//
//    @Column(name = "move_in_date")
//    private String moveInDate;
//
//    @Column(name = "lease_term")
//    private Integer leaseTerm;
//
//    @Column(name = "budget_start")
//    private Integer budgetStart;
//
//    @Column(name = "budget_end")
//    private Integer budgetEnd;
//
//    @Column(name = "want_to")
//    private WantTo wantTo;
//
//    @Column(name = "looking_for_roommate")
//    private boolean lookingForRoommate;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chillowHousing", cascade = CascadeType.ALL)
//    private List<PartnerProperties> partnerPropertiesGeneralSpacesList;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chillowHousing", cascade = CascadeType.ALL)
//    @Column(name = "partner_properties_co_living_spaces")
//    private List<PartnerProperties> partnerPropertiesCoLivingSpacesList;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chillowHousing", cascade = CascadeType.ALL)
//    @Column(name = "partner_properties_off_campus_spaces")
//    private List<PartnerProperties> partnerPropertiesOffCampusSpacesList;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chillowHousing", cascade = CascadeType.ALL)
//    @Column(name = "listing_properties_general_spaces")
//    private List<ListingProperties> listingPropertiesGeneralSpacesList;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chillowHousing", cascade = CascadeType.ALL)
//    @Column(name = "listing_properties_co_living_spaces")
//    private List<ListingProperties> listingPropertiesCoLivingSpacesList;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chillowHousing", cascade = CascadeType.ALL)
//    @Column(name = "listing_properties_off_campus_spaces")
//    private List<ListingProperties> listingPropertiesOffCampusSpacesList;
//
//    @Column(name = "user_id")
//    private String user;
//
//    @Column(name = "created_at")
//    private Instant createdAt;
//
//    @Column(name = "updated_at")
//    private Instant updatedAt;
//}
