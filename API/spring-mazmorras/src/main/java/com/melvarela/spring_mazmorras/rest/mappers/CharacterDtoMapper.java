package com.melvarela.spring_mazmorras.rest.mappers;

import org.springframework.beans.factory.annotation.Autowired;

import com.melvarela.spring_mazmorras.entities.CharacterEntity;
import com.melvarela.spring_mazmorras.rest.dtos.CharacterDto;
import com.melvarela.spring_mazmorras.services.CampaignService;

public class CharacterDtoMapper {

    @Autowired
    static CampaignService service;

    public static CharacterDto characterEntityToDto(CharacterEntity character){
        return new CharacterDto(
            character.getId(),
            character.getName(),
            character.getClase(),
            character.getSubclass(),
            character.getPg(),
            character.getMaxPg(),
            character.getPicture(),
            character.getCampaign().getId(),
            null
        );
    }

    public static CharacterEntity characterDtoToEntity(CharacterDto character){
        return new CharacterEntity(
            character.getId(),
            character.getName(),
            character.getClase(),
            character.getSubClase(),
            character.getPg(),
            character.getMaxPg(),
            character.getPicture(),
            service.findById(character.getCampaign())
        );
    }
}
