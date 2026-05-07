package com.melvarela.spring_mazmorras.rest.mappers;

import com.melvarela.spring_mazmorras.entities.SuggestionEntity;
import com.melvarela.spring_mazmorras.rest.dtos.SuggestionDto;

public class SuggestionDtoMapper {
    
    public static SuggestionDto entityToDto(SuggestionEntity suggestion){
        return new SuggestionDto(
            suggestion.getId(),
            suggestion.getText(),
            suggestion.getType()
        );
    }

    public static SuggestionEntity dtoToEntity(SuggestionDto suggestion){
        return new SuggestionEntity(
            suggestion.getId(),
            suggestion.getText(),
            suggestion.getType()
        );
    }

}
