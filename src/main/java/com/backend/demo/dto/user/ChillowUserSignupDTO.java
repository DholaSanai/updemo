package com.backend.demo.dto.user;

import com.backend.demo.dto.user.chillowUser.ChillowUserLocationDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserSignupDTO {
    private String id;
    private String name;
    private String email;
    private LocalDate birthDate;
    @JsonProperty("pronouns")
    private String pronouns;
    @JsonProperty("images")
    private List<ChillowUserSignupImagesDTO> images;
    private Integer wantTo;
    private boolean isRoomate;
    private ChillowUserLocationDto location;
    private List<String> generalLivingList;
    private List<String> coLivingList;
    private List<String> offCampusList;

}
