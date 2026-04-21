package com.melvarela.spring_mazmorras.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.melvarela.spring_mazmorras.entities.NoteEntity;

@Repository
public interface NotesRepository extends JpaRepository<NoteEntity, String> {

    List<NoteEntity> findByOwner(String owner);
    
}
