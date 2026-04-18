package com.melvarela.spring_mazmorras.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.repositories.CampaignRepository;

@Service
public class CampaignService {
    
    @Autowired
    CampaignRepository repository;

    @Transactional
    public CampaignEntity createCampaign(CampaignEntity campaign){
        return repository.save(campaign);
    }

    @Transactional
    public CampaignEntity updateCampaign(CampaignEntity campaign){
        return repository.save(campaign);
    }

    @Transactional
    public CampaignEntity deleteCampaign(CampaignEntity campaign){
        repository.delete(campaign);
        return campaign;
    }

    @Transactional(readOnly = true)
    public List<CampaignEntity> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public CampaignEntity findById(String id){
        Optional<CampaignEntity> got = repository.findById(id);
        if(got.isPresent()){
            return got.get();
        }else{
            return new CampaignEntity(
                null,
                null,
                null
            );
        }
    }

}
