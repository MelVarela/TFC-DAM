package com.melvarela.spring_mazmorras.rest.mappers;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.entities.ObjectEntity;
import com.melvarela.spring_mazmorras.rest.dtos.ObjectDto;

public class ObjectDtoMapper {

    public static ObjectDto objectEntityToDto(ObjectEntity obxecto){
        return new ObjectDto(
            obxecto.getId(),
            obxecto.getName(),
            obxecto.getPrice(),
            obxecto.getPicture(),
            obxecto.getCampaign().getId()
        );
    }

    public static ObjectEntity objectDtoToEntity(ObjectDto obxecto, CampaignEntity campaign){
        return new ObjectEntity(
            obxecto.getId(),
            obxecto.getName(),
            obxecto.getCost(),
            obxecto.getPicture(),
            campaign
        );
    }

}
