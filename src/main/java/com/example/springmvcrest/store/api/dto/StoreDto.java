package com.example.springmvcrest.store.api.dto;

import com.example.springmvcrest.product.api.dto.CategoryDto;
import lombok.Data;

import java.util.List;

@Data
public class StoreDto {
    private Long id;
    private String name;
    private String description;
    private StoreAddressDto storeAddress;
    private String imageStore;
    private List<CategoryDto> defaultCategories;
}
