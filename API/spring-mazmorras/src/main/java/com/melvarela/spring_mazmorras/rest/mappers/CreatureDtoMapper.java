package com.melvarela.spring_mazmorras.rest.mappers;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.entities.CreatureEntity;
import com.melvarela.spring_mazmorras.rest.dtos.CreatureDto;

public class CreatureDtoMapper {

    public static CreatureDto creatureEntityToDto(CreatureEntity creature){
        return new CreatureDto(
            creature.getId(),
            creature.getName(),
            creature.getRace(),
            creature.getPicture(),
            creature.getCampaign().getId()
        );
    }

    public static CreatureEntity creatureDtoToEntity(CreatureDto creature, CampaignEntity campaign){
        return new CreatureEntity(
            creature.getId(),
            creature.getName(),
            creature.getSpecies(),
            creature.getPicture(),
            campaign
        );
    }

}
