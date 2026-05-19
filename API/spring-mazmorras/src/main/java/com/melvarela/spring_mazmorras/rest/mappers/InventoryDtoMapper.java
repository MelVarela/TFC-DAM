package com.melvarela.spring_mazmorras.rest.mappers;

import com.melvarela.spring_mazmorras.entities.Ids.InventoryId;
import com.melvarela.spring_mazmorras.entities.InventoryEntity;
import com.melvarela.spring_mazmorras.rest.dtos.InventoryDto;

public class InventoryDtoMapper {
    
    public static InventoryEntity dtoToEntity(InventoryDto dto){
        return new InventoryEntity(
            new InventoryId(
                dto.getPersonaje(),
                dto.getObjeto()
            )
        );
    }

    public static InventoryDto entityToDto(InventoryEntity entity){
        return new InventoryDto(
            entity.getId().getPersonaje(),
            entity.getId().getObjeto()
        );
    }

}
