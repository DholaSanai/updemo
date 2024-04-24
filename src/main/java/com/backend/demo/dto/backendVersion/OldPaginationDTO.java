package com.backend.demo.dto.backendVersion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class OldPaginationDTO<T> {
    private List<T> data;
    private Integer pageNumber;
    private Integer totalPages;
    private Long totalElements;

}