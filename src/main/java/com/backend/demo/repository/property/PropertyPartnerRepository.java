package com.backend.demo.repository.property;

import com.backend.demo.dto.property.ListedPropertyWithPagination;
import com.backend.demo.dto.property.PropertyPartnersByDistance;
import com.backend.demo.entity.property.PropertyPartner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyPartnerRepository extends JpaRepository<PropertyPartner, String> {
    List<PropertyPartner> findByComplexNameIgnoreCaseContaining(String complexName, Pageable pageable);

    @Query(value = "SELECT * FROM \n" +
            "(SELECT u.id, u.complex_name, u.description, u.is_partner_property_deleted,\n" +
            "               u.max_price, u.min_price, u.parking_fee, u.pets, u.smoke, \n" +
            "               (SELECT SQRT(POW(69.1 * (l.latitude - :latitude), 2) + POW(69.1 * (:longitude - l.longitude) * COS(latitude / 57.3), 2)) ) as distance\n" +
            "               FROM property_partner u left outer join property_partner_location l on U.id = l.property_partner_id\n" +
            "               WHERE u.is_partner_property_deleted = false ) AS dt WHERE dt.distance < 100;", nativeQuery = true)
    List<PropertyPartnersByDistance> findPropertyPartnersByDistance(@Param("longitude") Double longitude, @Param("latitude") Double latitude);

    @Query(value = "select\n" +
            "  d.listed_property_id,\n" +
            "  d.distance\n" +
            "from\n" +
            "  (\n" +
            "    select\n" +
            "      *,\n" +
            "      sqrt(\n" +
            "        pow(69.1 * (lpl.latitude - ?2), 2) + pow(\n" +
            "          69.1 * (lpl.longitude - ?1) * cos(lpl.latitude / 57.3),\n" +
            "          2\n" +
            "        )\n" +
            "      ) as distance\n" +
            "    from\n" +
            "      listing_property lp\n" +
            "      INNER JOIN listed_property_location lpl on lp.id = lpl.listed_property_id\n" +
            "    where\n" +
            "      lp.property_partner_id = ?3 and lp.owner_id = '72eae5e1-b844-4a5e-8f49-1ab6deb55011'\n" +
            "      and lp.is_added_by_admin = true\n" +
            "    ORDER BY\n" +
            "      distance\n" +
            "  ) as d\n" +
            "where\n" +
            "  d.distance < 25",
    nativeQuery = true)
    List<ListedPropertyWithPagination> findListingsByRadius(Double longitude, Double latitude, String propertyPartnerId);

    List<PropertyPartner> findAllByIdIn(List<String> propertyPartnerIdList);
}
