package com.melvarela.spring_mazmorras.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melvarela.spring_mazmorras.entities.NoteEntity;
import com.melvarela.spring_mazmorras.repositories.NotesRepository;

@Service
public class NoteService {
    
    @Autowired
    NotesRepository repository;

    @Transactional
    public NoteEntity createNote(NoteEntity note){
        note.setId(LocalDateTime.now().toString() + "note");
        return repository.save(note);
    }

    @Transactional
    public NoteEntity updateNote(NoteEntity note){
        return repository.save(note);
    }

    @Transactional
    public NoteEntity deleteNote(NoteEntity note){
        repository.delete(note);
        return note;
    }

    @Transactional(readOnly = true)
    public List<NoteEntity> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public NoteEntity findById(String id){
        Optional<NoteEntity> got = repository.findById(id);
        if(got.isPresent()){
            return got.get();
        }else{
            return new NoteEntity(null, null, null, false, false, null);
        }
    }

    public List<NoteEntity> findAllByOwner(String owner) {
        return repository.findByOwner(owner);
    }

}
