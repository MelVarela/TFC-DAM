package com.melvarela.spring_mazmorras.rest.mappers;

import org.springframework.beans.factory.annotation.Autowired;

import com.melvarela.spring_mazmorras.entities.ObjectEntity;
import com.melvarela.spring_mazmorras.rest.dtos.ObjectDto;
import com.melvarela.spring_mazmorras.services.CampaignService;

public class ObjectDtoMapper {
 
    @Autowired
    static CampaignService service;

    public static ObjectDto objectEntityToDto(ObjectEntity obxecto){
        return new ObjectDto(
            obxecto.getId(),
            obxecto.getName(),
            obxecto.getPrice(),
            obxecto.getPicture(),
            obxecto.getCampaign().getId()
        );
    }

    public static ObjectEntity objectDtoToEntity(ObjectDto obxecto){
        return new ObjectEntity(
            obxecto.getId(),
            obxecto.getName(),
            obxecto.getCost(),
            obxecto.getPicture(),
            service.findById(obxecto.getCampaign())
        );
    }

}
