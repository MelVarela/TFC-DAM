package com.melvarela.spring_mazmorras.rest.mappers;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.entities.CharacterEntity;
import com.melvarela.spring_mazmorras.rest.dtos.CharacterDto;

public class CharacterDtoMapper {

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

    public static CharacterEntity characterDtoToEntity(CharacterDto character, CampaignEntity campaign){
        return new CharacterEntity(
            character.getId(),
            character.getName(),
            character.getClase(),
            character.getSubClase(),
            character.getPg(),
            character.getMaxPg(),
            character.getPicture(),
            campaign
        );
    }
}
