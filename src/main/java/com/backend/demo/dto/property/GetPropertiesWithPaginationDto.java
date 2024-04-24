package com.backend.demo.dto.property;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPropertiesWithPaginationDto {
    List<GetListedPropertiesResponseBody> properties;
    private int page;
    private int size;
    private int totalPages;
    private int status;
}
