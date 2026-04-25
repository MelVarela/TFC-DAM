package com.melvarela.spring_mazmorras.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melvarela.spring_mazmorras.entities.ObjectEntity;
import com.melvarela.spring_mazmorras.repositories.ObjectRepository;

@Service
public class ObjectService {
    
    @Autowired
    ObjectRepository repository;

    @Transactional
    public ObjectEntity createObject(ObjectEntity entity){
        return repository.save(entity);
    }

    @Transactional
    public ObjectEntity updateObject(ObjectEntity entity){
        return repository.save(entity);
    }

    @Transactional
    public ObjectEntity deleteObject(ObjectEntity entity){
        repository.delete(entity);
        return entity;
    }

    @Transactional(readOnly = true)
    public List<ObjectEntity> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ObjectEntity findById(String id){
        Optional<ObjectEntity> got = repository.findById(id);
        if(got.isPresent()){
            return got.get();
        }else{
            return new ObjectEntity(null, null, 0, null, null);
        }
    }

    public List<ObjectEntity> findAllByCampaign(String campaignId) {
        return repository.findByCampaign(campaignId);
    }

}
