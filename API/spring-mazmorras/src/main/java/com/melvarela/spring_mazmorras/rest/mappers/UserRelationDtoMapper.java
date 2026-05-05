package com.melvarela.spring_mazmorras.rest.mappers;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.entities.Ids.UserRelationId;
import com.melvarela.spring_mazmorras.entities.UserEntity;
import com.melvarela.spring_mazmorras.entities.UserRelationEntity;
import com.melvarela.spring_mazmorras.rest.dtos.UserRelationDto;

public class UserRelationDtoMapper {
    
    public static UserRelationDto userRelationEntityToDto(UserRelationEntity entity){
        return new UserRelationDto(
            entity.getId().getUsuario(),
            entity.getId().getCampanna(),
            entity.getHorario(),
            entity.getRol(),
            entity.isAceptada()
        );
    }

    public static UserRelationEntity userRelationDtoToEntity(UserRelationDto dto, CampaignEntity campaign, UserEntity user){
        return new UserRelationEntity(
            new UserRelationId(dto.getUser(), dto.getCampaign()),
            dto.getSchedule(),
            dto.getRole(),
            dto.isAccepted(),
            user,
            campaign
        );
    }

}
