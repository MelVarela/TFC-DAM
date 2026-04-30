package com.melvarela.spring_mazmorras.rest.mappers;

import com.melvarela.spring_mazmorras.entities.NoteEntity;
import com.melvarela.spring_mazmorras.rest.dtos.NoteDto;

public class NoteDtoMapper {
    
    public static NoteDto noteEntityToDto(NoteEntity note){
        return new NoteDto(
            note.getId(),
            note.getName(),
            note.getContent(),
            note.isDm(),
            note.isEditing(),
            note.getOwner()
        );
    }

    public static NoteEntity noteDtoToEntity(NoteDto note){
        return new NoteEntity(
            note.getId(),
            note.getName(),
            note.getContent(),
            note.getDm(),
            note.getEditing(),
            note.getOwner()
        );
    }

}
