package com.melvarela.spring_mazmorras.rest.mappers;

import org.springframework.beans.factory.annotation.Autowired;

import com.melvarela.spring_mazmorras.entities.PlaceEntity;
import com.melvarela.spring_mazmorras.rest.dtos.PlaceDto;
import com.melvarela.spring_mazmorras.services.CampaignService;

public class PlaceDtoMapper {
    
    @Autowired
    static CampaignService service;

    public static PlaceDto placeEntityToDto(PlaceEntity place){
        return new PlaceDto(
            place.getId(),
            place.getName(),
            place.getPicture(),
            place.getCampaign().getId()
        );
    }

    public static PlaceEntity placeDtoToEntity(PlaceDto place){
        return new PlaceEntity(
            place.getId(),
            place.getName(),
            place.getPicture(),
            service.findById(place.getCampaign())
        );
    }

}
