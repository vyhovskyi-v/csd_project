package com.github.vyhovskyi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private String name;
    private String groupName;
    private String description;
    private Integer manufacturerId;
    private Integer minQuantity;
    private Integer maxQuantity;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;



}
