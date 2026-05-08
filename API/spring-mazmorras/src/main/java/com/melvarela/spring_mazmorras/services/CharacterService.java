package com.melvarela.spring_mazmorras.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.entities.CharacterEntity;
import com.melvarela.spring_mazmorras.repositories.CharacterRepository;

@Service
public class CharacterService {
    
    @Autowired
    CharacterRepository repository;

    @Transactional
    public CharacterEntity createCharacter(CharacterEntity character){
        character.setId(LocalDateTime.now().toString() + "char");
        if(character.getPicture().equals("")) character.setPicture("https://deltarune.com/assets/images/ie-info.png");
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

    @Transactional
    public CharacterEntity deleteCharacter(String id){
        CharacterEntity character = repository.findById(id).get();
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

    public List<CharacterEntity> findAllByCampaign(CampaignEntity campaign) {
        return repository.findByCampaign(campaign);
    }

}
