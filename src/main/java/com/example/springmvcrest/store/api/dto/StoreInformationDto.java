package com.example.springmvcrest.store.api.dto;

import com.example.springmvcrest.product.api.dto.CategoryDto;
import lombok.Data;

import java.util.List;

@Data
public class StoreInformationDto {
    private Long providerId;
    private String address;
    private String telephoneNumber;
    private String defaultTelephoneNumber;
    private List<CategoryDto> defaultCategoriesList;
}
