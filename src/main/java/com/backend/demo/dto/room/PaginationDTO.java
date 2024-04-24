package com.backend.demo.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO <T>{
    private List<T> data;
    private Integer currentPage;
    private Integer totalPages;
    private Integer totalItemsOnPage;
    private Integer pageSize;
    private Long totalItems;
}
