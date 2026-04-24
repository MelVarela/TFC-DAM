package com.melvarela.spring_mazmorras.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melvarela.spring_mazmorras.entities.Ids.UserRelationId;
import com.melvarela.spring_mazmorras.entities.UserRelationEntity;
import com.melvarela.spring_mazmorras.repositories.UserRelationRepository;
import com.melvarela.spring_mazmorras.rest.dtos.UserRelationDto;

@Service
public class UserRelationService {
    
    @Autowired
    UserRelationRepository repository;

    @Transactional
    public UserRelationEntity createUser(UserRelationEntity userRelation){
        return repository.save(userRelation);
    }

    @Transactional
    public UserRelationEntity updateUser(UserRelationEntity userRelation){
        return repository.save(userRelation);
    }

    @Transactional
    public UserRelationEntity deleteUser(UserRelationEntity userRelation){
        repository.delete(userRelation);
        return userRelation;
    }

    @Transactional(readOnly = true)
    public List<UserRelationEntity> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public UserRelationEntity findById(UserRelationId id){
        Optional<UserRelationEntity> got = repository.findById(id);
        if(got.isPresent()){
            return got.get();
        }else{
            return new UserRelationEntity(new UserRelationId(null, null), null, '0', false, null, null);
        }
    }

    @Transactional(readOnly = true)
    public List<UserRelationEntity> findByCampaign(String campaignId){
        return repository.findByCampannaEntity(campaignId);
    }

}
