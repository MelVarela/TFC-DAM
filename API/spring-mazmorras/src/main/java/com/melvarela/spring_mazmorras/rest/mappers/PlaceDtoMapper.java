package com.melvarela.spring_mazmorras.rest.mappers;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.entities.PlaceEntity;
import com.melvarela.spring_mazmorras.rest.dtos.PlaceDto;

public class PlaceDtoMapper {

    public static PlaceDto placeEntityToDto(PlaceEntity place){
        return new PlaceDto(
            place.getId(),
            place.getName(),
            place.getPicture(),
            place.getCampaign().getId()
        );
    }

    public static PlaceEntity placeDtoToEntity(PlaceDto place, CampaignEntity campaign){
        return new PlaceEntity(
            place.getId(),
            place.getName(),
            place.getPicture(),
            campaign
        );
    }

}
