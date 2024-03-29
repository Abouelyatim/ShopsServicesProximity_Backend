package com.example.springmvcrest.store.service;

import com.example.springmvcrest.product.api.dto.CategoryDto;
import com.example.springmvcrest.product.api.mapper.CategoryMapper;
import com.example.springmvcrest.product.domain.Category;
import com.example.springmvcrest.product.service.CategoryService;
import com.example.springmvcrest.storage.FileStorage;
import com.example.springmvcrest.storage.FileStorageException;
import com.example.springmvcrest.store.api.dto.*;
import com.example.springmvcrest.store.api.mapper.StoreAddressMapper;
import com.example.springmvcrest.store.api.mapper.StoreInformationMapper;
import com.example.springmvcrest.store.api.mapper.StoreMapper;
import com.example.springmvcrest.store.domain.Store;
import com.example.springmvcrest.store.repository.StoreRepository;
import com.example.springmvcrest.store.service.exception.MultipleStoreException;
import com.example.springmvcrest.store.service.exception.StoreNotFoundException;
import com.example.springmvcrest.utils.Response;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final StoreInformationMapper storeInformationMapper;
    private final CategoryService categoryService;
    private final StoreAddressMapper storeAddressMapper;
    private final FileStorage fileStorage;
    private CategoryMapper categoryMapper;

    @Named("getStoreName")
    public String getStoreName(Store store) { return  store.getName(); }

    @Named("getStoreAddress")
    public String getStoreAddress(Store store) { return  store.getStoreAddress().getFullAddress(); }

    @Named("getStoreAddressLat")
    public Double getStoreAddressLat(Store store) { return  store.getStoreAddress().getLatitude(); }
    @Named("getStoreAddressLon")
    public Double getStoreAddressLon(Store store) { return  store.getStoreAddress().getLongitude(); }

    @Named("getStoreAddressUpdated")
    public StoreAddressDto getStoreAddressUpdated(Store store) {
        return  storeAddressMapper.ToDto(
                store.getStoreAddress()
        );
    }

    @Named("getStoreFollowers")
    public Integer getStoreFollowers(Store store) {
        return  store.getFollowers().size();
    }

    @Named("getStoreId")
    public Long getStoreId(Store store) { return  store.getId(); }

    @Named("findStoreByProviderId")
    public Store findStoreByProviderId(Long id){
        return storeRepository.findByProviderId(id)
                .orElseThrow(StoreNotFoundException::new);
    }

    @Named("findStoreById")
    public Store findStoreById(Long id){
        return storeRepository.findById(id)
                .orElseThrow(StoreNotFoundException::new);
    }

    public Store saveStore(Store store){
        return storeRepository.save(store);
    }

    private Boolean hasStore(Long id){
        return storeRepository.findByProviderId(id)
                .isPresent();
    }

    public Response<String> setStoreCategory(Long providerId,List<String> categories){
        if(categories!=null && !categories.isEmpty()){
            Store store=findStoreByProviderId(providerId);
            Set<Category> collect = categories.stream()
                    .map(categoryService::findCategoryByName)
                    .collect(Collectors.toSet());
            store.setDefaultCategories(collect);
            storeRepository.save(store);
        }
        return new Response<>("created.");
    }

    public List<CategoryDto> getStoreCategories(Long providerId){
        Store store = storeRepository.findByProviderId(providerId)
                .orElse(null);

        return store != null ? store.getDefaultCategories()
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList()) : new ArrayList<CategoryDto>();
    }

    public StoreCreationDto create(StoreCreationDto storeCreationDto, MultipartFile storeImage) {
        if(storeImage!=null){
            saveStoreImage(storeImage);
        }

        return Optional.of(storeCreationDto)
                .map(storeMapper::ToModel)
                .filter(store -> !hasStore(store.getProvider().getId()))
                .map(this::setStoreAddress)
                .map(storeRepository::save)
                .map(storeMapper::ToDto)
                .orElseThrow(MultipleStoreException::new);
    }

    private void saveStoreImage(MultipartFile image) {
        try {
            if (image != null) {
                fileStorage.upload(image.getOriginalFilename(),
                        "smartCity-files", image.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("error.file.upload");
        }
    }

    private Store setStoreAddress(Store store){
        store.getStoreAddress().setStore(store);
        return store;
    }

    public Response<String> setStoreInformation(StoreInformationCreationDto storeInformationDto){
        Store store = findStoreByProviderId(storeInformationDto.getProviderId());
       // store.setAddress(storeInformationDto.getAddress());
        store.setTelephoneNumber(storeInformationDto.getTelephoneNumber());
        store.setDefaultTelephoneNumber(storeInformationDto.getDefaultTelephoneNumber());

        Set<Category> collect = storeInformationDto.getDefaultCategories().stream()
                .map(categoryService::findCategoryByName)
                .collect(Collectors.toSet());
        store.setDefaultCategories(collect);
        storeRepository.save(store);
        return new Response<>("created.");
    }

    public StoreInformationDto getStoreInformation(Long providerId){
        return Optional.of(findStoreByProviderId(providerId))
                .map(storeInformationMapper::ToDto)
                .orElseThrow(StoreNotFoundException::new);
    }

    public StoreInformationDto getStoreInformationByStoreId (Long storeId){
        return Optional.of(findStoreById(storeId))
                .map(storeInformationMapper::ToDto)
                .orElseThrow(StoreNotFoundException::new);
    }

    public List<StoreDto> findStoreByDistance(double latitude, double longitude, double distance, String category) {
        return storeRepository.findStoreAround(latitude, longitude, distance)
                .stream()
                .filter(store -> checkCategory(store,category))
                .map(storeMapper::toDto)
                .collect(Collectors.toList());
    }

    private Boolean checkCategory(Store store,String category){
        if (category.equals("")){
            return true;
        }
        Category savedCategory = categoryService.findCategoryByName(category);
        return store.getDefaultCategories().contains(savedCategory);
    }
}
