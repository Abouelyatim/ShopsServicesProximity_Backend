package com.example.springmvcrest.product.service;

import com.example.springmvcrest.product.api.dto.CategoryDto;
import com.example.springmvcrest.product.api.mapper.CategoryMapper;
import com.example.springmvcrest.product.domain.Category;
import com.example.springmvcrest.product.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategory(){
        return categoryRepository.findAll()
                .stream()
                .filter(category -> category.getParentCategory()==null)
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public Category findCategoryByName(String name){
        return categoryRepository.findByName(name)
                .orElse(null);
    }
}