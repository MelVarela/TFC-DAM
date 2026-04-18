package com.melvarela.spring_mazmorras.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melvarela.spring_mazmorras.entities.CharacterEntity;
import com.melvarela.spring_mazmorras.repositories.CharacterRepository;

@Service
public class CharacterService {
    
    @Autowired
    CharacterRepository repository;

    @Transactional
    public CharacterEntity createCharacter(CharacterEntity character){
        return repository.save(character);
    }

    @Transactional
    public CharacterEntity updateCharacter(CharacterEntity character){
        return repository.save(character);
    }

    @Transactional
    public CharacterEntity deleteCharacter(CharacterEntity character){
        repository.delete(character);
        return character;
    }

    @Transactional(readOnly = true)
    public List<CharacterEntity> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public CharacterEntity findById(String id){
        Optional<CharacterEntity> got = repository.findById(id);
        if(got.isPresent()){
            return got.get();
        }else{
            return new CharacterEntity(null, null, null, null, 0, 0, null, null);
        }
    }

}
