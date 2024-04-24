package com.backend.demo.repository.property;

import com.backend.demo.dto.property.ListedPropertyWithPagination;
import com.backend.demo.entity.property.ListedProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ListedPropertyRepository extends JpaRepository<ListedProperty, String> {
    List<ListedProperty> findAllByOwnerId(String userId);

    List<ListedProperty> findAllByIsVerifiedFalseAndIsAddedByAdminFalseAndDateAddedGreaterThanEqual(LocalDate minus60days);

    List<ListedProperty> findAllByIsVerifiedFalseAndIsAddedByAdminFalse();

    List<ListedProperty> findByOwnerId(String id);

    /* SIR QUERY */

//    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT\n" +
//            "                 (\n" +
//            "                   POW\n" +
//            "                   (\n" +
//            "                     69.1 *\n" +
//            "                     (\n" +
//            "                       lpl.latitude - ?3" +
//            "                     ), 2\n" +
//            "                   ) + POW(\n" +
//            "                     69.1 *\n" +
//            "                     (\n" +
//            "                       ?2 - lpl.longitude\n" +
//            "                     ) * COS(\n" +
//            "                       latitude / 57.3\n" +
//            "                            ), 2\n" +
//            "                   )\n" +
//            "                 ) as distance\n" +
//            "    from listing_property LP\n" +
//            "    inner join listed_property_location lpl\n" +
//            "        on LP.id = lpl.listed_property_id\n" +
//            "         where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//            "           and LP.is_added_by_admin = true\n" +
//            "           and LP.is_listing_deleted = false" +
//            "           and LP.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "           and LP.building_type IN ?4\n" +
//            "           and LP.is_coliving = ?7 OR LP.is_coliving = ?8\n" +
//            "           and LP.is_student_housing = ?9 OR LP.is_student_housing = ?10) UNION (\n" +
//            "           select *, SQRT\n" +
//            "                 (\n" +
//            "                   POW\n" +
//            "                   (\n" +
//            "                     69.1 *\n" +
//            "                     (\n" +
//            "                       lpl.latitude - ?3\n" +
//            "                     ), 2\n" +
//            "                   ) + POW(\n" +
//            "                     69.1 *\n" +
//            "                     (\n" +
//            "                       ?2 - lpl.longitude\n" +
//            "                     ) * COS(\n" +
//            "                       latitude / 57.3\n" +
//            "                            ), 2\n" +
//            "                   )\n" +
//            "                 ) as distance\n" +
//            "    from listing_property LP\n" +
//            "    inner join listed_property_location lpl\n" +
//            "        on LP.id = lpl.listed_property_id\n" +
//            "         where LP.is_added_by_admin = false\n" +
//            "           and LP.is_listing_deleted = false" +
//            "           and LP.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "           and LP.building_type IN ?4\n" +
//            "           and LP.is_coliving = ?7 OR LP.is_coliving = ?8\n" +
//            "           and LP.is_student_housing = ?9 OR LP.is_student_housing = ?10)) as properties WHERE properties.distance <= ?11 ORDER BY is_added_by_admin desc, distance ASC",
//            countQuery = "SELECT count(*) from ((select *, SQRT\n" +
//                    "                 (\n" +
//                    "                   POW\n" +
//                    "                   (\n" +
//                    "                     69.1 *\n" +
//                    "                     (\n" +
//                    "                       lpl.latitude - ?3" +
//                    "                     ), 2\n" +
//                    "                   ) + POW(\n" +
//                    "                     69.1 *\n" +
//                    "                     (\n" +
//                    "                       ?2 - lpl.longitude\n" +
//                    "                     ) * COS(\n" +
//                    "                       latitude / 57.3\n" +
//                    "                            ), 2\n" +
//                    "                   )\n" +
//                    "                 ) as distance\n" +
//                    "    from listing_property LP\n" +
//                    "    inner join listed_property_location lpl\n" +
//                    "        on LP.id = lpl.listed_property_id\n" +
//                    "         where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//                    "           and LP.is_added_by_admin = true\n" +
//                    "           and LP.is_listing_deleted = false" +
//                    "           and LP.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "           and LP.building_type IN ?4\n" +
//                    "           and LP.is_coliving = ?7 OR LP.is_coliving = ?8\n" +
//                    "           and LP.is_student_housing = ?9 OR LP.is_student_housing = ?10) UNION (\n" +
//                    "           select *, SQRT\n" +
//                    "                 (\n" +
//                    "                   POW\n" +
//                    "                   (\n" +
//                    "                     69.1 *\n" +
//                    "                     (\n" +
//                    "                       lpl.latitude - ?3\n" +
//                    "                     ), 2\n" +
//                    "                   ) + POW(\n" +
//                    "                     69.1 *\n" +
//                    "                     (\n" +
//                    "                       ?2 - lpl.longitude\n" +
//                    "                     ) * COS(\n" +
//                    "                       latitude / 57.3\n" +
//                    "                            ), 2\n" +
//                    "                   )\n" +
//                    "                 ) as distance\n" +
//                    "    from listing_property LP\n" +
//                    "    inner join listed_property_location lpl\n" +
//                    "        on LP.id = lpl.listed_property_id\n" +
//                    "         where LP.is_added_by_admin = false\n" +
//                    "           and LP.is_listing_deleted = false" +
//                    "           and LP.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "           and LP.building_type IN ?4\n" +
//                    "           and LP.is_coliving = ?7 OR LP.is_coliving = ?8\n" +
//                    "           and LP.is_student_housing = ?9 OR LP.is_student_housing = ?10)) as properties WHERE properties.distance <= ?11 ORDER BY is_added_by_admin desc, distance ASC"
//            , nativeQuery = true)
//    List<ListedPropertyWithPagination> findAllByParamsWithPagination(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Boolean isColivingTrue, Boolean isColivingFalse, Boolean isStudentHousingTrue, Boolean isStudentHousingFalse, Integer distance, Pageable pageable);

    /* SIR QUERY */




    /* MY QUERY WITHOUT IS_UPDATE_REQUIRED */


    /*  COLIVING FALSE, STUDENT HOUSING FALSE  */
    @Query(value = "\n" +
            "  select\n" +
            "  distance,\n" +
            "  properties.listed_property_id\n" +
            "from\n" +
            "  (\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        true as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = false\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id not in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = false\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.is_added_by_admin = false\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.owner_id != ?1\n" +
            "        and LP.is_coliving = false\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "  ) as properties\n" +
            "WHERE\n" +
            "  properties.distance <= ?7\n" +
            "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "  and properties.building_type IN ?4\n" +
            "ORDER by\n" +
            "  prefs desc,\n" +
            "  is_added_by_admin desc,\n" +
            "  distance asc",
    countQuery = "\n" +
            "  select\n" +
            "  count(*)\n" +
            "from\n" +
            "  (\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        true as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = false\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id not in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = false\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.is_added_by_admin = false\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.owner_id != ?1\n" +
            "        and LP.is_coliving = false\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "  ) as properties\n" +
            "WHERE\n" +
            "  properties.distance <= ?7\n" +
            "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "  and properties.building_type IN ?4\n" +
            "ORDER by\n" +
            "  prefs desc,\n" +
            "  is_added_by_admin desc,\n" +
            "  distance asc",
    nativeQuery = true)
    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingFalseAndStudentHousingFalse(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);

    /*  COLIVING FALSE, STUDENT HOUSING FALSE  */


    /*  COLIVING FALSE, STUDENT HOUSING TRUE  */

    @Query(value = "  select\n" +
            "  distance,\n" +
            "  properties.listed_property_id\n" +
            "from\n" +
            "  (\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        true as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = false\n" +
            "        and LP.is_student_housing = true\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id not in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = false\n" +
            "        and LP.is_student_housing = true\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.is_added_by_admin = false\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.owner_id != ?1\n" +
            "        and LP.is_coliving = false\n" +
            "        and LP.is_student_housing = true\n" +
            "    )\n" +
            "  ) as properties\n" +
            "WHERE\n" +
            "  properties.distance <= ?7\n" +
            "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "  and properties.building_type IN ?4\n" +
            "ORDER by\n" +
            "  prefs desc,\n" +
            "  is_added_by_admin desc,\n" +
            "  distance asc",
            countQuery = "  select\n" +
                    "  count(*)\n" +
                    "from\n" +
                    "  (\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        true as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_coliving = false\n" +
                    "        and LP.is_student_housing = true\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id not in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_coliving = false\n" +
                    "        and LP.is_student_housing = true\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.is_added_by_admin = false\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.owner_id != ?1\n" +
                    "        and LP.is_coliving = false\n" +
                    "        and LP.is_student_housing = true\n" +
                    "    )\n" +
                    "  ) as properties\n" +
                    "WHERE\n" +
                    "  properties.distance <= ?7\n" +
                    "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
                    "  and properties.building_type IN ?4\n" +
                    "ORDER by\n" +
                    "  prefs desc,\n" +
                    "  is_added_by_admin desc,\n" +
                    "  distance asc",
            nativeQuery = true)
    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingFalseAndStudentHousingTrue(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);


    /*  COLIVING FALSE, STUDENT HOUSING TRUE  */


    /*  COLIVING TRUE, STUDENT HOUSING FALSE  */

    @Query(value = "select\n" +
            "  distance,\n" +
            "  properties.listed_property_id\n" +
            "from\n" +
            "  (\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        true as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = true\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id not in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = true\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.is_added_by_admin = false\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.owner_id != ?1\n" +
            "        and LP.is_coliving = true\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "  ) as properties\n" +
            "WHERE\n" +
            "  properties.distance <= ?7\n" +
            "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "  and properties.building_type IN ?4\n" +
            "ORDER by\n" +
            "  prefs desc,\n" +
            "  is_added_by_admin desc,\n" +
            "  distance asc",
            countQuery = "select\n" +
                    "  count(*)\n" +
                    "from\n" +
                    "  (\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        true as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_coliving = true\n" +
                    "        and LP.is_student_housing = false\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id not in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_coliving = true\n" +
                    "        and LP.is_student_housing = false\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.is_added_by_admin = false\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.owner_id != ?1\n" +
                    "        and LP.is_coliving = true\n" +
                    "        and LP.is_student_housing = false\n" +
                    "    )\n" +
                    "  ) as properties\n" +
                    "WHERE\n" +
                    "  properties.distance <= ?7\n" +
                    "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
                    "  and properties.building_type IN ?4\n" +
                    "ORDER by\n" +
                    "  prefs desc,\n" +
                    "  is_added_by_admin desc,\n" +
                    "  distance asc",
            nativeQuery = true)
    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingTrueAndStudentHousingFalse(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);


    /*  COLIVING TRUE, STUDENT HOUSING FALSE  */


    /*  COLIVING TRUE, STUDENT HOUSING TRUE  */

    @Query(value = "  select\n" +
            "  distance,\n" +
            "  properties.listed_property_id\n" +
            "from\n" +
            "  (\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        true as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = true\n" +
            "        and LP.is_student_housing = true\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id not in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = true\n" +
            "        and LP.is_student_housing = true\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.is_added_by_admin = false\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.owner_id != ?1\n" +
            "        and LP.is_coliving = true\n" +
            "        and LP.is_student_housing = true\n" +
            "    )\n" +
            "  ) as properties\n" +
            "WHERE\n" +
            "  properties.distance <= ?7\n" +
            "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "  and properties.building_type IN ?4\n" +
            "ORDER by\n" +
            "  prefs desc,\n" +
            "  is_added_by_admin desc,\n" +
            "  distance asc",
            countQuery = "  select\n" +
                    "  count(*)\n" +
                    "from\n" +
                    "  (\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        true as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_coliving = true\n" +
                    "        and LP.is_student_housing = true\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id not in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_coliving = true\n" +
                    "        and LP.is_student_housing = true\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.is_added_by_admin = false\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.owner_id != ?1\n" +
                    "        and LP.is_coliving = true\n" +
                    "        and LP.is_student_housing = true\n" +
                    "    )\n" +
                    "  ) as properties\n" +
                    "WHERE\n" +
                    "  properties.distance <= ?7\n" +
                    "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
                    "  and properties.building_type IN ?4\n" +
                    "ORDER by\n" +
                    "  prefs desc,\n" +
                    "  is_added_by_admin desc,\n" +
                    "  distance asc",
            nativeQuery = true)
    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingTrueAndStudentHousingTrue(String userId,
                                                                                                               Double longitude,
                                                                                                               Double latitude,
                                                                                                               List<Integer> buildingType,
                                                                                                               Integer minRent,
                                                                                                               Integer maxRent,
                                                                                                               Integer distance,
                                                                                                               Pageable pageable);

    /*  COLIVING TRUE, STUDENT HOUSING TRUE  */


    /*  COLIVING IGNORED, STUDENT HOUSING IGNORED  */

//    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT(POW\n" +
//            "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//            "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//            "                inner join listed_property_location lpl\n" +
//            "                    on LP.id = lpl.listed_property_id\n" +
//            "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//            "                       and LP.is_added_by_admin = true\n" +
//            "                       and LP.is_listing_deleted = false\n" +
//            "                        UNION\n" +
//            "                (\n" +
//            "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//            "                           as distance from listing_property\n" +
//            "                           LP inner join listed_property_location lpl\n" +
//            "                               on LP.id = lpl.listed_property_id\n" +
//            "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1\n" +
//            "                                           )\n" +
//            ")) as properties WHERE properties.distance <= ?7 and\n" +
//            "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "                  and properties.is_listing_deleted = false\n" +
//            "                and properties.building_type IN ?4\n" +
////            "                and properties.is_update_required = false\n" +
//            "                    ORDER BY is_added_by_admin desc, distance ASC",
//            countQuery = "SELECT count(*) from ((select *, SQRT(POW\n" +
//                    "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//                    "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//                    "                inner join listed_property_location lpl\n" +
//                    "                    on LP.id = lpl.listed_property_id\n" +
//                    "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//                    "                       and LP.is_added_by_admin = true\n" +
//                    "                       and LP.is_listing_deleted = false\n" +
//                    "                        UNION\n" +
//                    "                (\n" +
//                    "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//                    "                           as distance from listing_property\n" +
//                    "                           LP inner join listed_property_location lpl\n" +
//                    "                               on LP.id = lpl.listed_property_id\n" +
//                    "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1\n" +
//                    "                                           )\n" +
//                    ")) as properties WHERE properties.distance <= ?7 and\n" +
//                    "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "                  and properties.is_listing_deleted = false\n" +
//                    "                and properties.building_type IN ?4\n" +
////                    "                and properties.is_update_required = false\n" +
//                    "                    ORDER BY is_added_by_admin desc, distance ASC",
//            nativeQuery = true)
//    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingIgnoredAndStudentHousingIgnored(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);

    @Query(value = "select distance, properties.listed_property_id from ((select\n" +
            "\t*, SQRT(POW(69.1 * (lpl.latitude - ?3), 2) + POW(\n" +
            "    69.1 * (?2 - lpl.longitude) *COS( latitude / 57.3), 2)) as distance, true as prefs\n" +
            "from listing_property LP inner join listed_property_location lpl\n" +
            "on LP.id = lpl.listed_property_id\n" +
            "where LP.property_partner_id in (\n" +
            "\tselect property_partner from user_preferred_property_partner where user_id = ?1\n" +
            "\t)\n" +
            "\tand LP.is_added_by_admin = true\n" +
            "\tand LP.is_listing_deleted = false)\n" +
            "\n" +
            "union\n" +
            "\n" +
            "(select\n" +
            "\t*, SQRT(POW(69.1 * (lpl.latitude - ?3), 2) + POW(\n" +
            "    69.1 * (?2 - lpl.longitude) *COS( latitude / 57.3), 2)) as distance, false as prefs\n" +
            "from listing_property LP inner join listed_property_location lpl\n" +
            "on LP.id = lpl.listed_property_id\n" +
            "where LP.property_partner_id not in (\n" +
            "\tselect property_partner from user_preferred_property_partner where user_id = ?1\n" +
            "\t)\n" +
            "\tand LP.is_added_by_admin = true\n" +
            "\tand LP.is_listing_deleted = false)\n" +
            "\n" +
            "union\n" +
            "\n" +
            "(select\n" +
            "\t*, SQRT(POW(69.1 * (lpl.latitude - ?3), 2) + POW(\n" +
            "    69.1 * (?2 - lpl.longitude) *COS( latitude / 57.3), 2)) as distance, false as prefs\n" +
            "from listing_property LP inner join listed_property_location lpl\n" +
            "on LP.id = lpl.listed_property_id\n" +
            "where LP.is_added_by_admin = false\n" +
            "\tand LP.is_listing_deleted = false and LP.owner_id != ?1)) as properties\n" +
            "\tWHERE properties.distance <= ?7 and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "and properties.building_type IN ?4 ORDER by prefs desc, is_added_by_admin desc, distance asc",
    countQuery = "select count(*) from ((select\n" +
            "\t*, SQRT(POW(69.1 * (lpl.latitude - ?3), 2) + POW(\n" +
            "    69.1 * (?2 - lpl.longitude) *COS( latitude / 57.3), 2)) as distance, true as prefs\n" +
            "from listing_property LP inner join listed_property_location lpl\n" +
            "on LP.id = lpl.listed_property_id\n" +
            "where LP.property_partner_id in (\n" +
            "\tselect property_partner from user_preferred_property_partner where user_id = ?1\n" +
            "\t)\n" +
            "\tand LP.is_added_by_admin = true\n" +
            "\tand LP.is_listing_deleted = false)\n" +
            "\n" +
            "union\n" +
            "\n" +
            "(select\n" +
            "\t*, SQRT(POW(69.1 * (lpl.latitude - ?3), 2) + POW(\n" +
            "    69.1 * (?2 - lpl.longitude) *COS( latitude / 57.3), 2)) as distance, false as prefs\n" +
            "from listing_property LP inner join listed_property_location lpl\n" +
            "on LP.id = lpl.listed_property_id\n" +
            "where LP.property_partner_id not in (\n" +
            "\tselect property_partner from user_preferred_property_partner where user_id = ?1\n" +
            "\t)\n" +
            "\tand LP.is_added_by_admin = true\n" +
            "\tand LP.is_listing_deleted = false)\n" +
            "\n" +
            "union\n" +
            "\n" +
            "(select\n" +
            "\t*, SQRT(POW(69.1 * (lpl.latitude - ?3), 2) + POW(\n" +
            "    69.1 * (?2 - lpl.longitude) *COS( latitude / 57.3), 2)) as distance, false as prefs\n" +
            "from listing_property LP inner join listed_property_location lpl\n" +
            "on LP.id = lpl.listed_property_id\n" +
            "where LP.is_added_by_admin = false\n" +
            "\tand LP.is_listing_deleted = false and LP.owner_id != ?1)) as properties\n" +
            "\tWHERE properties.distance <= ?7 and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "and properties.building_type IN ?4 ORDER by prefs desc, is_added_by_admin desc, distance asc",
    nativeQuery = true)
    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingIgnoredAndStudentHousingIgnored(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);

    /*  COLIVING IGNORED, STUDENT HOUSING IGNORED  */

    /*  COLIVING IGNORED, STUDENT HOUSING TRUE  */

    @Query(value = "  select\n" +
            "  distance,\n" +
            "  properties.listed_property_id\n" +
            "from\n" +
            "  (\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        true as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_student_housing = true\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id not in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_student_housing = true\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.is_added_by_admin = false\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.owner_id != ?1\n" +
            "        and LP.is_student_housing = true\n" +
            "    )\n" +
            "  ) as properties\n" +
            "WHERE\n" +
            "  properties.distance <= ?7\n" +
            "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "  and properties.building_type IN ?4\n" +
            "ORDER by\n" +
            "  prefs desc,\n" +
            "  is_added_by_admin desc,\n" +
            "  distance asc",
            countQuery = "  select\n" +
                    "  count(*)\n" +
                    "from\n" +
                    "  (\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        true as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_student_housing = true\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id not in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_student_housing = true\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.is_added_by_admin = false\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.owner_id != ?1\n" +
                    "        and LP.is_student_housing = true\n" +
                    "    )\n" +
                    "  ) as properties\n" +
                    "WHERE\n" +
                    "  properties.distance <= ?7\n" +
                    "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
                    "  and properties.building_type IN ?4\n" +
                    "ORDER by\n" +
                    "  prefs desc,\n" +
                    "  is_added_by_admin desc,\n" +
                    "  distance asc",
            nativeQuery = true)
    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingIgnoredAndStudentHousingTrue(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);

    /*  COLIVING IGNORED, STUDENT HOUSING TRUE  */

    /*  COLIVING IGNORED, STUDENT HOUSING FALSE  */

    @Query(value = "select\n" +
            "  distance,\n" +
            "  properties.listed_property_id\n" +
            "from\n" +
            "  (\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        true as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id not in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.is_added_by_admin = false\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.owner_id != ?1\n" +
            "        and LP.is_student_housing = false\n" +
            "    )\n" +
            "  ) as properties\n" +
            "WHERE\n" +
            "  properties.distance <= ?7\n" +
            "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "  and properties.building_type IN ?4\n" +
            "ORDER by\n" +
            "  prefs desc,\n" +
            "  is_added_by_admin desc,\n" +
            "  distance asc",
            countQuery = "select\n" +
                    "  distance,\n" +
                    "  properties.listed_property_id\n" +
                    "from\n" +
                    "  (\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        true as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_student_housing = false\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id not in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_student_housing = false\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.is_added_by_admin = false\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.owner_id != ?1\n" +
                    "        and LP.is_student_housing = false\n" +
                    "    )\n" +
                    "  ) as properties\n" +
                    "WHERE\n" +
                    "  properties.distance <= ?7\n" +
                    "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
                    "  and properties.building_type IN ?4\n" +
                    "ORDER by\n" +
                    "  prefs desc,\n" +
                    "  is_added_by_admin desc,\n" +
                    "  distance asc",
            nativeQuery = true)
    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingIgnoredAndStudentHousingFalse(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);

    /*  COLIVING IGNORED, STUDENT HOUSING FALSE  */

    /*  COLIVING TRUE, STUDENT HOUSING IGNORED  */

    @Query(value = "  select\n" +
            "  distance,\n" +
            "  properties.listed_property_id\n" +
            "from\n" +
            "  (\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        true as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = true\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id not in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = true\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.is_added_by_admin = false\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.owner_id != ?1\n" +
            "        and LP.is_coliving = true\n" +
            "    )\n" +
            "  ) as properties\n" +
            "WHERE\n" +
            "  properties.distance <= ?7\n" +
            "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "  and properties.building_type IN ?4\n" +
            "ORDER by\n" +
            "  prefs desc,\n" +
            "  is_added_by_admin desc,\n" +
            "  distance asc",
            countQuery = "  select\n" +
                    "  count(*)\n" +
                    "from\n" +
                    "  (\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        true as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_coliving = true\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id not in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_coliving = true\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.is_added_by_admin = false\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.owner_id != ?1\n" +
                    "        and LP.is_coliving = true\n" +
                    "    )\n" +
                    "  ) as properties\n" +
                    "WHERE\n" +
                    "  properties.distance <= ?7\n" +
                    "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
                    "  and properties.building_type IN ?4\n" +
                    "ORDER by\n" +
                    "  prefs desc,\n" +
                    "  is_added_by_admin desc,\n" +
                    "  distance asc",
            nativeQuery = true)
    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingTrueAndStudentHousingIgnored(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);


    /*  COLIVING TRUE, STUDENT HOUSING IGNORED  */

    /*  COLIVING FALSE, STUDENT HOUSING IGNORED  */

    @Query(value = "select\n" +
            "  distance,\n" +
            "  properties.listed_property_id\n" +
            "from\n" +
            "  (\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        true as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = false\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.property_partner_id not in (\n" +
            "          select\n" +
            "            property_partner\n" +
            "          from\n" +
            "            user_preferred_property_partner\n" +
            "          where\n" +
            "            user_id = ?1\n" +
            "        )\n" +
            "        and LP.is_added_by_admin = true\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.is_coliving = false\n" +
            "    )\n" +
            "    union\n" +
            "    (\n" +
            "      select\n" +
            "        *,\n" +
            "        SQRT(\n" +
            "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
            "        ) as distance,\n" +
            "        false as prefs\n" +
            "      from\n" +
            "        listing_property LP\n" +
            "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
            "      where\n" +
            "        LP.is_added_by_admin = false\n" +
            "        and LP.is_listing_deleted = false\n" +
            "        and LP.owner_id != ?1\n" +
            "        and LP.is_coliving = false\n" +
            "    )\n" +
            "  ) as properties\n" +
            "WHERE\n" +
            "  properties.distance <= ?7\n" +
            "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "  and properties.building_type IN ?4\n" +
            "ORDER by\n" +
            "  prefs desc,\n" +
            "  is_added_by_admin desc,\n" +
            "  distance asc",
            countQuery = "select\n" +
                    "  count(*)\n" +
                    "from\n" +
                    "  (\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        true as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_coliving = false\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.property_partner_id not in (\n" +
                    "          select\n" +
                    "            property_partner\n" +
                    "          from\n" +
                    "            user_preferred_property_partner\n" +
                    "          where\n" +
                    "            user_id = ?1\n" +
                    "        )\n" +
                    "        and LP.is_added_by_admin = true\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.is_coliving = false\n" +
                    "    )\n" +
                    "    union\n" +
                    "    (\n" +
                    "      select\n" +
                    "        *,\n" +
                    "        SQRT(\n" +
                    "          POW(69.1 * (lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(latitude / 57.3), 2)\n" +
                    "        ) as distance,\n" +
                    "        false as prefs\n" +
                    "      from\n" +
                    "        listing_property LP\n" +
                    "        inner join listed_property_location lpl on LP.id = lpl.listed_property_id\n" +
                    "      where\n" +
                    "        LP.is_added_by_admin = false\n" +
                    "        and LP.is_listing_deleted = false\n" +
                    "        and LP.owner_id != ?1\n" +
                    "        and LP.is_coliving = false\n" +
                    "    )\n" +
                    "  ) as properties\n" +
                    "WHERE\n" +
                    "  properties.distance <= ?7\n" +
                    "  and properties.monthly_rent BETWEEN ?5 AND ?6\n" +
                    "  and properties.building_type IN ?4\n" +
                    "ORDER by\n" +
                    "  prefs desc,\n" +
                    "  is_added_by_admin desc,\n" +
                    "  distance asc",
            nativeQuery = true)
    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingFalseAndStudentHousingIgnored(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);


    /*  COLIVING FALSE, STUDENT HOUSING IGNORED  */



    /* MY QUERY WITHOUT IS_UPDATE_REQUIRED */



    /* MY QUERY WITH IS_UPDATE_REQUIRED = FALSE*/

//    /*  COLIVING FALSE, STUDENT HOUSING FALSE  */
//    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT(POW\n" +
//            "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//            "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//            "                inner join listed_property_location lpl\n" +
//            "                    on LP.id = lpl.listed_property_id\n" +
//            "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//            "                       and LP.is_added_by_admin = true\n" +
//            "                       and LP.is_listing_deleted = false\n" +
//            "                       and LP.is_coliving = false\n" +
//            "                       and LP.is_student_housing = false\n" +
//            "                        UNION\n" +
//            "                (\n" +
//            "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//            "                           as distance from listing_property\n" +
//            "                           LP inner join listed_property_location lpl\n" +
//            "                               on LP.id = lpl.listed_property_id\n" +
//            "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1 and\n" +
//            "                                    LP.is_coliving = false\n" +
//            "                                         and\n" +
//            "                                      LP.is_student_housing = false\n" +
//            "                                           )\n" +
//            ")) as properties WHERE properties.distance <= ?7 and\n" +
//            "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "                  and properties.is_listing_deleted = false\n" +
//            "                and properties.building_type IN ?4\n" +
//            "                and properties.is_update_required = false\n" +
//            "                    ORDER BY is_added_by_admin desc, distance ASC",
//            countQuery = "SELECT count(*) from ((select *, SQRT(POW\n" +
//                    "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//                    "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//                    "                inner join listed_property_location lpl\n" +
//                    "                    on LP.id = lpl.listed_property_id\n" +
//                    "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//                    "                       and LP.is_added_by_admin = true\n" +
//                    "                       and LP.is_listing_deleted = false\n" +
//                    "                       and LP.is_coliving = false\n" +
//                    "                       and LP.is_student_housing = false\n" +
//                    "                        UNION\n" +
//                    "                (\n" +
//                    "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//                    "                           as distance from listing_property\n" +
//                    "                           LP inner join listed_property_location lpl\n" +
//                    "                               on LP.id = lpl.listed_property_id\n" +
//                    "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1and\n" +
//                    "                                    LP.is_coliving = false\n" +
//                    "                                         and\n" +
//                    "                                      LP.is_student_housing = false\n" +
//                    "                                           )\n" +
//                    ")) as properties WHERE properties.distance <= ?7 and\n" +
//                    "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "                  and properties.is_listing_deleted = false\n" +
//                    "                and properties.building_type IN ?4\n" +
//                    "                and properties.is_update_required = false\n" +
//                    "                    ORDER BY is_added_by_admin desc, distance ASC",
//            nativeQuery = true)
//    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingFalseAndStudentHousingFalse(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);
//
//    /*  COLIVING FALSE, STUDENT HOUSING FALSE  */
//
//
//    /*  COLIVING FALSE, STUDENT HOUSING TRUE  */
//
//    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT(POW\n" +
//            "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//            "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//            "                inner join listed_property_location lpl\n" +
//            "                    on LP.id = lpl.listed_property_id\n" +
//            "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//            "                       and LP.is_added_by_admin = true\n" +
//            "                       and LP.is_listing_deleted = false\n" +
//            "                       and LP.is_coliving = false\n" +
//            "                       and LP.is_student_housing = true\n" +
//            "                        UNION\n" +
//            "                (\n" +
//            "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//            "                           as distance from listing_property\n" +
//            "                           LP inner join listed_property_location lpl\n" +
//            "                               on LP.id = lpl.listed_property_id\n" +
//            "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1 and\n" +
//            "                                    LP.is_coliving = false\n" +
//            "                                         and\n" +
//            "                                      LP.is_student_housing = true\n" +
//            "                                           )\n" +
//            ")) as properties WHERE properties.distance <= ?7 and\n" +
//            "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "                  and properties.is_listing_deleted = false\n" +
//            "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//            "                    ORDER BY is_added_by_admin desc, distance ASC",
//            countQuery = "SELECT count(*) from ((select *, SQRT(POW\n" +
//                    "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//                    "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//                    "                inner join listed_property_location lpl\n" +
//                    "                    on LP.id = lpl.listed_property_id\n" +
//                    "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//                    "                       and LP.is_added_by_admin = true\n" +
//                    "                       and LP.is_listing_deleted = false\n" +
//                    "                       and LP.is_coliving = false\n" +
//                    "                       and LP.is_student_housing = true\n" +
//                    "                        UNION\n" +
//                    "                (\n" +
//                    "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//                    "                           as distance from listing_property\n" +
//                    "                           LP inner join listed_property_location lpl\n" +
//                    "                               on LP.id = lpl.listed_property_id\n" +
//                    "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1 and\n" +
//                    "                                    LP.is_coliving = false\n" +
//                    "                                         and\n" +
//                    "                                      LP.is_student_housing = true\n" +
//                    "                                           )\n" +
//                    ")) as properties WHERE properties.distance <= ?7 and\n" +
//                    "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "                  and properties.is_listing_deleted = false\n" +
//                    "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//                    "                    ORDER BY is_added_by_admin desc, distance ASC",
//            nativeQuery = true)
//    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingFalseAndStudentHousingTrue(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);
//
//
//    /*  COLIVING FALSE, STUDENT HOUSING TRUE  */
//
//
//    /*  COLIVING TRUE, STUDENT HOUSING FALSE  */
//
//    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT(POW\n" +
//            "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//            "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//            "                inner join listed_property_location lpl\n" +
//            "                    on LP.id = lpl.listed_property_id\n" +
//            "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//            "                       and LP.is_added_by_admin = true\n" +
//            "                       and LP.is_listing_deleted = false\n" +
//            "                       and LP.is_coliving = true\n" +
//            "                       and LP.is_student_housing = false\n" +
//            "                        UNION\n" +
//            "                (\n" +
//            "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//            "                           as distance from listing_property\n" +
//            "                           LP inner join listed_property_location lpl\n" +
//            "                               on LP.id = lpl.listed_property_id\n" +
//            "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1 and\n" +
//            "                                    LP.is_coliving = true\n" +
//            "                                         and\n" +
//            "                                      LP.is_student_housing = false\n" +
//            "                                           )\n" +
//            ")) as properties WHERE properties.distance <= ?7 and\n" +
//            "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "                  and properties.is_listing_deleted = false\n" +
//            "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//            "                    ORDER BY is_added_by_admin desc, distance ASC",
//            countQuery = "SELECT count(*) from ((select *, SQRT(POW\n" +
//                    "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//                    "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//                    "                inner join listed_property_location lpl\n" +
//                    "                    on LP.id = lpl.listed_property_id\n" +
//                    "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//                    "                       and LP.is_added_by_admin = true\n" +
//                    "                       and LP.is_listing_deleted = false\n" +
//                    "                       and LP.is_coliving = true\n" +
//                    "                       and LP.is_student_housing = false\n" +
//                    "                        UNION\n" +
//                    "                (\n" +
//                    "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//                    "                           as distance from listing_property\n" +
//                    "                           LP inner join listed_property_location lpl\n" +
//                    "                               on LP.id = lpl.listed_property_id\n" +
//                    "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1 and\n" +
//                    "                                    LP.is_coliving = true\n" +
//                    "                                         and\n" +
//                    "                                      LP.is_student_housing = false\n" +
//                    "                                           )\n" +
//                    ")) as properties WHERE properties.distance <= ?7 and\n" +
//                    "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "                  and properties.is_listing_deleted = false\n" +
//                    "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//                    "                    ORDER BY is_added_by_admin desc, distance ASC",
//            nativeQuery = true)
//    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingTrueAndStudentHousingFalse(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);
//
//
//    /*  COLIVING TRUE, STUDENT HOUSING FALSE  */
//
//
//    /*  COLIVING TRUE, STUDENT HOUSING TRUE  */
//
//    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT(POW\n" +
//            "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//            "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//            "                inner join listed_property_location lpl\n" +
//            "                    on LP.id = lpl.listed_property_id\n" +
//            "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//            "                       and LP.is_added_by_admin = true\n" +
//            "                       and LP.is_listing_deleted = false\n" +
//            "                       and LP.is_coliving = true\n" +
//            "                       and LP.is_student_housing = true\n" +
//            "                        UNION\n" +
//            "                (\n" +
//            "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//            "                           as distance from listing_property\n" +
//            "                           LP inner join listed_property_location lpl\n" +
//            "                               on LP.id = lpl.listed_property_id\n" +
//            "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1 and\n" +
//            "                                    LP.is_coliving = true\n" +
//            "                                         and\n" +
//            "                                      LP.is_student_housing = true\n" +
//            "                                           )\n" +
//            ")) as properties WHERE properties.distance <= ?7 and\n" +
//            "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "                  and properties.is_listing_deleted = false\n" +
//            "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//            "                    ORDER BY is_added_by_admin desc, distance ASC",
//            countQuery = "SELECT count(*) from ((select *, SQRT(POW\n" +
//                    "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//                    "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//                    "                inner join listed_property_location lpl\n" +
//                    "                    on LP.id = lpl.listed_property_id\n" +
//                    "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//                    "                       and LP.is_added_by_admin = true\n" +
//                    "                       and LP.is_listing_deleted = false\n" +
//                    "                       and LP.is_coliving = true\n" +
//                    "                       and LP.is_student_housing = true\n" +
//                    "                        UNION\n" +
//                    "                (\n" +
//                    "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//                    "                           as distance from listing_property\n" +
//                    "                           LP inner join listed_property_location lpl\n" +
//                    "                               on LP.id = lpl.listed_property_id\n" +
//                    "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1 and\n" +
//                    "                                    LP.is_coliving = true\n" +
//                    "                                         and\n" +
//                    "                                      LP.is_student_housing = true\n" +
//                    "                                           )\n" +
//                    ")) as properties WHERE properties.distance <= ?7 and\n" +
//                    "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "                  and properties.is_listing_deleted = false\n" +
//                    "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//                    "                    ORDER BY is_added_by_admin desc, distance ASC",
//            nativeQuery = true)
//    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingTrueAndStudentHousingTrue(String userId,
//                                                                                                         Double longitude,
//                                                                                                         Double latitude,
//                                                                                                         List<Integer> buildingType,
//                                                                                                         Integer minRent,
//                                                                                                         Integer maxRent,
//                                                                                                         Integer distance,
//                                                                                                         Pageable pageable);
//
//    /*  COLIVING TRUE, STUDENT HOUSING TRUE  */
//
//
//    /*  COLIVING IGNORED, STUDENT HOUSING IGNORED  */
//
//    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT(POW\n" +
//            "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//            "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//            "                inner join listed_property_location lpl\n" +
//            "                    on LP.id = lpl.listed_property_id\n" +
//            "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//            "                       and LP.is_added_by_admin = true\n" +
//            "                       and LP.is_listing_deleted = false\n" +
//            "                        UNION\n" +
//            "                (\n" +
//            "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//            "                           as distance from listing_property\n" +
//            "                           LP inner join listed_property_location lpl\n" +
//            "                               on LP.id = lpl.listed_property_id\n" +
//            "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1\n" +
//            "                                           )\n" +
//            ")) as properties WHERE properties.distance <= ?7 and\n" +
//            "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "                  and properties.is_listing_deleted = false\n" +
//            "                and properties.building_type IN ?4\n" +
//            "                and properties.is_update_required = false\n" +
//            "                    ORDER BY is_added_by_admin desc, distance ASC",
//            countQuery = "SELECT count(*) from ((select *, SQRT(POW\n" +
//                    "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//                    "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//                    "                inner join listed_property_location lpl\n" +
//                    "                    on LP.id = lpl.listed_property_id\n" +
//                    "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//                    "                       and LP.is_added_by_admin = true\n" +
//                    "                       and LP.is_listing_deleted = false\n" +
//                    "                        UNION\n" +
//                    "                (\n" +
//                    "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//                    "                           as distance from listing_property\n" +
//                    "                           LP inner join listed_property_location lpl\n" +
//                    "                               on LP.id = lpl.listed_property_id\n" +
//                    "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1\n" +
//                    "                                           )\n" +
//                    ")) as properties WHERE properties.distance <= ?7 and\n" +
//                    "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "                  and properties.is_listing_deleted = false\n" +
//                    "                and properties.building_type IN ?4\n" +
//                    "                and properties.is_update_required = false\n" +
//                    "                    ORDER BY is_added_by_admin desc, distance ASC",
//            nativeQuery = true)
//    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingIgnoredAndStudentHousingIgnored(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);
//
//    /*  COLIVING IGNORED, STUDENT HOUSING IGNORED  */
//
//    /*  COLIVING IGNORED, STUDENT HOUSING TRUE  */
//
//    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT(POW\n" +
//            "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//            "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//            "                inner join listed_property_location lpl\n" +
//            "                    on LP.id = lpl.listed_property_id\n" +
//            "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//            "                       and LP.is_added_by_admin = true\n" +
//            "                       and LP.is_listing_deleted = false\n" +
//            "                       and LP.is_student_housing = true\n" +
//            "                        UNION\n" +
//            "                (\n" +
//            "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//            "                           as distance from listing_property\n" +
//            "                           LP inner join listed_property_location lpl\n" +
//            "                               on LP.id = lpl.listed_property_id\n" +
//            "                                       where LP.is_added_by_admin = false and LP.owner_id != ?1\n" +
//            "\n" +
//            "                                         and\n" +
//            "                                      LP.is_student_housing = true\n" +
//            "                                           )\n" +
//            ")) as properties WHERE properties.distance <= ?7 and\n" +
//            "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "                  and properties.is_listing_deleted = false\n" +
//            "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//            "                    ORDER BY is_added_by_admin desc, distance ASC",
//            countQuery = "SELECT count(*) from ((select *, SQRT(POW\n" +
//                    "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//                    "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//                    "                inner join listed_property_location lpl\n" +
//                    "                    on LP.id = lpl.listed_property_id\n" +
//                    "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//                    "                       and LP.is_added_by_admin = true\n" +
//                    "                       and LP.is_listing_deleted = false\n" +
//                    "                       and LP.is_student_housing = true\n" +
//                    "                        UNION\n" +
//                    "                (\n" +
//                    "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//                    "                           as distance from listing_property\n" +
//                    "                           LP inner join listed_property_location lpl\n" +
//                    "                               on LP.id = lpl.listed_property_id\n" +
//                    "                               where LP.is_added_by_admin = false  and LP.owner_id != ?1\n" +
//                    "\n" +
//                    "                                         and\n" +
//                    "                                      LP.is_student_housing = true\n" +
//                    "                                           )\n" +
//                    ")) as properties WHERE properties.distance <= ?7 and\n" +
//                    "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "                  and properties.is_listing_deleted = false\n" +
//                    "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//                    "                    ORDER BY is_added_by_admin desc, distance ASC",
//            nativeQuery = true)
//    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingIgnoredAndStudentHousingTrue(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);
//
//    /*  COLIVING IGNORED, STUDENT HOUSING TRUE  */
//
//    /*  COLIVING IGNORED, STUDENT HOUSING FALSE  */
//
//    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT(POW\n" +
//            "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//            "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//            "                inner join listed_property_location lpl\n" +
//            "                    on LP.id = lpl.listed_property_id\n" +
//            "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//            "                       and LP.is_added_by_admin = true\n" +
//            "                       and LP.is_listing_deleted = false\n" +
//            "                       and LP.is_student_housing = false\n" +
//            "                        UNION\n" +
//            "                (\n" +
//            "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//            "                           as distance from listing_property\n" +
//            "                           LP inner join listed_property_location lpl\n" +
//            "                               on LP.id = lpl.listed_property_id\n" +
//            "                                       where LP.is_added_by_admin = false  and LP.owner_id != ?1\n" +
//            "\n" +
//            "                                         and\n" +
//            "                                      LP.is_student_housing = false\n" +
//            "                                           )\n" +
//            ")) as properties WHERE properties.distance <= ?7 and\n" +
//            "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "                  and properties.is_listing_deleted = false\n" +
//            "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//            "                    ORDER BY is_added_by_admin desc, distance ASC",
//            countQuery = "SELECT count(*) from ((select *, SQRT(POW\n" +
//                    "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//                    "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//                    "                inner join listed_property_location lpl\n" +
//                    "                    on LP.id = lpl.listed_property_id\n" +
//                    "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//                    "                       and LP.is_added_by_admin = true\n" +
//                    "                       and LP.is_listing_deleted = false\n" +
//                    "                       and LP.is_student_housing = false\n" +
//                    "                        UNION\n" +
//                    "                (\n" +
//                    "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//                    "                           as distance from listing_property\n" +
//                    "                           LP inner join listed_property_location lpl\n" +
//                    "                               on LP.id = lpl.listed_property_id\n" +
//                    "                                       where LP.is_added_by_admin = false  and LP.owner_id != ?1\n" +
//                    "\n" +
//                    "                                         and\n" +
//                    "                                      LP.is_student_housing = false\n" +
//                    "                                           )\n" +
//                    ")) as properties WHERE properties.distance <= ?7 and\n" +
//                    "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "                  and properties.is_listing_deleted = false\n" +
//                    "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//                    "                    ORDER BY is_added_by_admin desc, distance ASC",
//            nativeQuery = true)
//    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingIgnoredAndStudentHousingFalse(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);
//
//    /*  COLIVING IGNORED, STUDENT HOUSING FALSE  */
//
//    /*  COLIVING TRUE, STUDENT HOUSING IGNORED  */
//
//    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT(POW\n" +
//            "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//            "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//            "                inner join listed_property_location lpl\n" +
//            "                    on LP.id = lpl.listed_property_id\n" +
//            "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//            "                       and LP.is_added_by_admin = true\n" +
//            "                       and LP.is_listing_deleted = false\n" +
//            "                       and LP.is_coliving = true\n" +
//            "                        UNION\n" +
//            "                (\n" +
//            "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//            "                           as distance from listing_property\n" +
//            "                           LP inner join listed_property_location lpl\n" +
//            "                               on LP.id = lpl.listed_property_id\n" +
//            "                                       where LP.is_added_by_admin = false  and LP.owner_id != ?1\n" +
//            "\n" +
//            "                                         and\n" +
//            "                                      LP.is_coliving = true\n" +
//            "                                           )\n" +
//            ")) as properties WHERE properties.distance <= ?7 and\n" +
//            "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "                  and properties.is_listing_deleted = false\n" +
//            "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//            "                    ORDER BY is_added_by_admin desc, distance ASC",
//            countQuery = "SELECT count(*) from ((select *, SQRT(POW\n" +
//                    "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//                    "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//                    "                inner join listed_property_location lpl\n" +
//                    "                    on LP.id = lpl.listed_property_id\n" +
//                    "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//                    "                       and LP.is_added_by_admin = true\n" +
//                    "                       and LP.is_listing_deleted = false\n" +
//                    "                       and LP.is_coliving = true\n" +
//                    "                        UNION\n" +
//                    "                (\n" +
//                    "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//                    "                           as distance from listing_property\n" +
//                    "                           LP inner join listed_property_location lpl\n" +
//                    "                               on LP.id = lpl.listed_property_id\n" +
//                    "                                       where LP.is_added_by_admin = false  and LP.owner_id != ?1\n" +
//                    "\n" +
//                    "                                         and\n" +
//                    "                                      LP.is_coliving = true\n" +
//                    "                                           )\n" +
//                    ")) as properties WHERE properties.distance <= ?7 and\n" +
//                    "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "                  and properties.is_listing_deleted = false\n" +
//                    "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//                    "                    ORDER BY is_added_by_admin desc, distance ASC",
//            nativeQuery = true)
//    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingTrueAndStudentHousingIgnored(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);
//
//
//    /*  COLIVING TRUE, STUDENT HOUSING IGNORED  */
//
//    /*  COLIVING FALSE, STUDENT HOUSING IGNORED  */
//
//    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT(POW\n" +
//            "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//            "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//            "                inner join listed_property_location lpl\n" +
//            "                    on LP.id = lpl.listed_property_id\n" +
//            "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//            "                       and LP.is_added_by_admin = true\n" +
//            "                       and LP.is_listing_deleted = false\n" +
//            "                       and LP.is_coliving = false\n" +
//            "                        UNION\n" +
//            "                (\n" +
//            "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//            "                           as distance from listing_property\n" +
//            "                           LP inner join listed_property_location lpl\n" +
//            "                               on LP.id = lpl.listed_property_id\n" +
//            "                                       where LP.is_added_by_admin = false  and LP.owner_id != ?1\n" +
//            "\n" +
//            "                                         and\n" +
//            "                                      LP.is_coliving = false\n" +
//            "                                           )\n" +
//            ")) as properties WHERE properties.distance <= ?7 and\n" +
//            "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//            "                  and properties.is_listing_deleted = false\n" +
//            "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//            "                    ORDER BY is_added_by_admin desc, distance ASC",
//            countQuery = "SELECT count(*) from ((select *, SQRT(POW\n" +
//                    "                               (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
//                    "                                   latitude / 57.3), 2)) as distance from listing_property LP\n" +
//                    "                inner join listed_property_location lpl\n" +
//                    "                    on LP.id = lpl.listed_property_id\n" +
//                    "                     where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
//                    "                       and LP.is_added_by_admin = true\n" +
//                    "                       and LP.is_listing_deleted = false\n" +
//                    "                       and LP.is_coliving = false\n" +
//                    "                        UNION\n" +
//                    "                (\n" +
//                    "                     select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
//                    "                           as distance from listing_property\n" +
//                    "                           LP inner join listed_property_location lpl\n" +
//                    "                               on LP.id = lpl.listed_property_id\n" +
//                    "                                       where LP.is_added_by_admin = false  and LP.owner_id != ?1\n" +
//                    "\n" +
//                    "                                         and\n" +
//                    "                                      LP.is_coliving = false\n" +
//                    "                                           )\n" +
//                    ")) as properties WHERE properties.distance <= ?7 and\n" +
//                    "                      properties.monthly_rent BETWEEN ?5 AND ?6\n" +
//                    "                  and properties.is_listing_deleted = false\n" +
//                    "                and properties.building_type IN ?4 and properties.is_update_required = false\n" +
//                    "                    ORDER BY is_added_by_admin desc, distance ASC",
//            nativeQuery = true)
//    List<ListedPropertyWithPagination> findAllByParamsWithPaginationAndCoLivingFalseAndStudentHousingIgnored(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Integer distance, Pageable pageable);


    /* MY QUERY WITH IS_UPDATE_REQUIRED = FALSE */



    List<ListedProperty> findAllByOwnerIdAndIsListingDeletedFalse(String userId, Pageable pageable);

    @Query(value = "select lp from ListedProperty lp join lp.location lpl " +
            "where lp.ownerId = :ownerId and lp.isListingDeleted = false and " +
            "(lower(lp.complexName) like concat('%',lower(:complexName),'%') or lower(lpl.city) like concat('%',:city,'%'))")
    List<ListedProperty> findByOwnerOrByLocationOfListing(@Param("ownerId")String ownerId,
                                               @Param("complexName") String complexName,
                                               @Param("city")String city, Pageable pageable);
    List<ListedProperty> findAllByIdIn(List<String> listedPropertyIds);



    /* NEW ATTEMPT*/

    @Query(value = "SELECT properties.listed_property_id, distance from ((select *, SQRT(POW\n" +
            "                                          (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
            "                                              latitude / 57.3), 2)) as distance from listing_property LP\n" +
            "                           inner join listed_property_location lpl\n" +
            "                               on LP.id = lpl.listed_property_id\n" +
            "                                where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
            "                                  and LP.is_added_by_admin = true\n" +
            "                                  and LP.is_listing_deleted = false\n" +
            "                                  and (?7 is null or LP.is_coliving = ?7)\n" +
            "                                  and (?8 is null or LP.is_student_housing = ?8)\n" +
            "                                   UNION\n" +
            "                           (\n" +
            "                                select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
            "                                      as distance from listing_property\n" +
            "                                      LP inner join listed_property_location lpl\n" +
            "                                          on LP.id = lpl.listed_property_id\n" +
            "                                                  where LP.is_added_by_admin = false and\n" +
            "                                               (?7 is null or LP.is_coliving = ?7)\n" +
            "                                                    and\n" +
            "                                                 (?8 is null or LP.is_student_housing = ?8)\n" +
            "                                                      )\n" +
            "            )) as properties WHERE properties.distance <= ?9 and\n" +
            "                                 properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "                             and properties.is_listing_deleted = false\n" +
            "                           and properties.building_type IN ?4                  ORDER BY is_added_by_admin desc, distance ASC",
            countQuery = "SELECT COUNT(*) from ((select *, SQRT(POW\n" +
            "                                          (69.1 *(lpl.latitude - ?3), 2) + POW(69.1 * (?2 - lpl.longitude) * COS(\n" +
            "                                              latitude / 57.3), 2)) as distance from listing_property LP\n" +
            "                           inner join listed_property_location lpl\n" +
            "                               on LP.id = lpl.listed_property_id\n" +
            "                                where LP.property_partner_id in (select property_partner from user_preferred_property_partner where user_id = ?1)\n" +
            "                                  and LP.is_added_by_admin = true\n" +
            "                                  and LP.is_listing_deleted = false\n" +
            "                                  and (?7 is null or LP.is_coliving = ?7)\n" +
            "                                  and (?8 is null or LP.is_student_housing = ?8)\n" +
            "                                   UNION\n" +
            "                           (\n" +
            "                                select *, SQRT(POW(69.1 *(lpl.latitude - ?3), 2) + POW(69.1 *(?2 - lpl.longitude) * COS(latitude / 57.3), 2))\n" +
            "                                      as distance from listing_property\n" +
            "                                      LP inner join listed_property_location lpl\n" +
            "                                          on LP.id = lpl.listed_property_id\n" +
            "                                                  where LP.is_added_by_admin = false and\n" +
            "                                               (?7 is null or LP.is_coliving = ?7)\n" +
            "                                                    and\n" +
            "                                                 (?8 is null or LP.is_student_housing = ?8)\n" +
            "                                                      )\n" +
            "            )) as properties WHERE properties.distance <= ?9 and\n" +
            "                                 properties.monthly_rent BETWEEN ?5 AND ?6\n" +
            "                             and properties.is_listing_deleted = false\n" +
            "                           and properties.building_type IN ?4 ORDER BY is_added_by_admin desc, distance ASC",
    nativeQuery = true)
    Page<ListedPropertyWithPagination> findAllByParam(String userId, Double longitude, Double latitude, List<Integer> buildingType, Integer minRent, Integer maxRent, Boolean isColiving, Boolean isStudentHousing, Integer distance, Pageable pageable);

    List<ListedProperty> findAllByPropertyPartnerId(String propertyPartnerId);

    /* NEW ATTEMPT*/
}
