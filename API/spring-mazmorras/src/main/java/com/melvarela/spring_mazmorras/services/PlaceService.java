package com.melvarela.spring_mazmorras.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.entities.PlaceEntity;
import com.melvarela.spring_mazmorras.repositories.PlaceRepository;

@Service
public class PlaceService {
    
    @Autowired
    PlaceRepository repository;

    @Transactional
    public PlaceEntity createPlace(PlaceEntity place){
        place.setId(LocalDateTime.now().toString() + "plac");
        return repository.save(place);
    }

    @Transactional
    public PlaceEntity updatePlace(PlaceEntity place){
        return repository.save(place);
    }

    @Transactional
    public PlaceEntity deletePlace(PlaceEntity place){
        repository.delete(place);
        return place;
    }

    @Transactional
    public PlaceEntity deletePlace(String id){
        PlaceEntity place = repository.findById(id).get();
        repository.delete(place);
        return place;
    }

    @Transactional(readOnly = true)
    public List<PlaceEntity> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public PlaceEntity fndById(String id){
        Optional<PlaceEntity> got = repository.findById(id);
        if(got.isPresent()){
            return got.get();
        }else{
            return new PlaceEntity(null, null, null, null);
        }
    }

    public List<PlaceEntity> findAllByCampaign(CampaignEntity campaign) {
        return repository.findByCampaign(campaign);
    }

}
