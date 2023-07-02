package ru.team38.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableObject {
    private Integer offset;
    private Sort sort;
    private Integer pageSize;
    private Boolean paged;
    private Boolean unpaged;
    private Integer pageNumber;
}
