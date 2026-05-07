package com.melvarela.spring_mazmorras.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melvarela.spring_mazmorras.entities.SuggestionEntity;
import com.melvarela.spring_mazmorras.repositories.SuggestionRepository;

@Service
public class SuggestionService {
    
    @Autowired
    SuggestionRepository repository;

    @Transactional
    public SuggestionEntity createSuggestion(SuggestionEntity suggestion){
        suggestion.setId((int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        return repository.save(suggestion);
    }

    @Transactional
    public SuggestionEntity updateSuggestion(SuggestionEntity suggestion){
        return repository.save(suggestion);
    }

    @Transactional
    public SuggestionEntity deleteSuggestion(SuggestionEntity suggestion){
        repository.delete(suggestion);
        return suggestion;
    }

    @Transactional
    public SuggestionEntity deleteSuggestion(int id){
        SuggestionEntity toDelete = findById(id);
        repository.deleteById(id);
        return toDelete;
    }

    @Transactional(readOnly = true)
    public List<SuggestionEntity> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public SuggestionEntity findById(int id){
        Optional<SuggestionEntity> got = repository.findById(id);
        if(got.isPresent()){
            return got.get();
        }else{
            return new SuggestionEntity();
        }
    }

}
