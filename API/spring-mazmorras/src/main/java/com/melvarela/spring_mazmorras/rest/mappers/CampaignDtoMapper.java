package com.melvarela.spring_mazmorras.rest.mappers;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.rest.dtos.CampaignDto;

public class CampaignDtoMapper {
    public static CampaignDto campaignEntityToDto(CampaignEntity campaign){
        return new CampaignDto(
            campaign.getId(),
            campaign.getName(),
            campaign.getProfilePicture()
        );
    }

    public static CampaignEntity campaignDtoToEntity(CampaignDto campaign){
        return new CampaignEntity(
            campaign.getId(),
            campaign.getName(),
            campaign.getPicture()
        );
    }
}
