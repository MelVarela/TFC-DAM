package com.melvarela.spring_mazmorras.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melvarela.spring_mazmorras.entities.CreatureEntity;
import com.melvarela.spring_mazmorras.repositories.CreatureRepository;

@Service
public class CreatureService {
    
    @Autowired
    CreatureRepository repository;

    @Transactional
    public CreatureEntity createCreature(CreatureEntity creature){
        creature.setId(LocalDateTime.now().toString() + "crea");
        return repository.save(creature);
    }

    @Transactional
    public CreatureEntity updateCreature(CreatureEntity creature){
        return repository.save(creature);
    }

    @Transactional
    public CreatureEntity deleteCreature(CreatureEntity creature){
        repository.delete(creature);
        return creature;
    }

    @Transactional
    public CreatureEntity deleteCreature(String id){
        CreatureEntity creature = repository.findById(id).get();
        repository.delete(creature);
        return creature;
    }

    @Transactional(readOnly = true)
    public List<CreatureEntity> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public CreatureEntity findById(String id){
        Optional<CreatureEntity> got = repository.findById(id);
        if(got.isPresent()){
            return got.get();
        }else{
            return new CreatureEntity(null, null, null, null, null);
        }
    }

    public List<CreatureEntity> findAllByCampaign(String campaignId) {
        return repository.findByCampaign(campaignId);
    }

}
