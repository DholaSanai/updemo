package com.backend.demo.dto.property;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyPartnerWithCategories {
    private List<PropertyPartnerResponseBody> generalLivingList;
    private List<PropertyPartnerResponseBody> coLivingList;
    private List<PropertyPartnerResponseBody> offCampusList;
}
