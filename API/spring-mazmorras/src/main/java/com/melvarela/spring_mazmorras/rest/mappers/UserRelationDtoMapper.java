package com.melvarela.spring_mazmorras.rest.mappers;

import org.springframework.beans.factory.annotation.Autowired;

import com.melvarela.spring_mazmorras.entities.Ids.UserRelationId;
import com.melvarela.spring_mazmorras.entities.UserRelationEntity;
import com.melvarela.spring_mazmorras.rest.dtos.UserRelationDto;
import com.melvarela.spring_mazmorras.services.CampaignService;
import com.melvarela.spring_mazmorras.services.UserService;

public class UserRelationDtoMapper {

    @Autowired
    private static CampaignService campaignService;
    @Autowired
    private static UserService userService;
    
    public static UserRelationDto userRelationEntityToDto(UserRelationEntity entity){
        return new UserRelationDto(
            entity.getId().getUsuario(),
            entity.getId().getCampanna(),
            entity.getHorario(),
            entity.getRol(),
            entity.isAceptada()
        );
    }

    public static UserRelationEntity userRelationDtoToEntity(UserRelationDto dto){
        return new UserRelationEntity(
            new UserRelationId(dto.getUser(), dto.getCampaign()),
            dto.getSchedule(),
            dto.getRol(),
            dto.isAccepted(),
            userService.findById(dto.getUser()),
            campaignService.findById(dto.getCampaign())
        );
    }

}
