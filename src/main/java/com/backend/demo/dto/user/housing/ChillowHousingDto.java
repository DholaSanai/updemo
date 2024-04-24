package com.backend.demo.dto.user.housing;

import com.backend.demo.enums.user.WantTo;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ChillowHousingDto {
    private String moveInDate;
    private Integer leaseTerm;
    private Integer budgetStart;
    private Integer budgetEnd;
    private WantTo wantTo;
    private boolean lookingForRoommate;
    private List<PartnerPropertiesDto> partnerPropertiesGeneralSpacesList;
    private List<PartnerPropertiesDto> partnerPropertiesCoLivingSpacesList;
    private List<PartnerPropertiesDto> partnerPropertiesOffCampusSpacesList;

}
