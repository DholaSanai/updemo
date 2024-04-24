package com.backend.demo.dto.property.featured;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeaturedPropertyPartnerResponseBody {
    private List<FeaturedPartners> featuredPartners;
}
