package com.melvarela.spring_mazmorras.rest.mappers;

import org.springframework.beans.factory.annotation.Autowired;

import com.melvarela.spring_mazmorras.entities.CreatureEntity;
import com.melvarela.spring_mazmorras.rest.dtos.CreatureDto;
import com.melvarela.spring_mazmorras.services.CampaignService;

public class CreatureDtoMapper {
    
    @Autowired
    static CampaignService service;

    public static CreatureDto creatureEntityToDto(CreatureEntity creature){
        return new CreatureDto(
            creature.getId(),
            creature.getName(),
            creature.getRace(),
            creature.getPicture(),
            creature.getCampaign().getId()
        );
    }

    public static CreatureEntity creatureDtoToEntity(CreatureDto creature){
        return new CreatureEntity(
            creature.getId(),
            creature.getName(),
            creature.getSpecies(),
            creature.getPicture(),
            service.findById(creature.getCampaign())
        );
    }

}
