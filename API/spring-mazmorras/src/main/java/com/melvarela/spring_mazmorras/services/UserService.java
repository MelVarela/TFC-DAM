package com.melvarela.spring_mazmorras.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melvarela.spring_mazmorras.entities.UserEntity;
import com.melvarela.spring_mazmorras.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    UserRepository repository;

    @Transactional
    public UserEntity createUser(UserEntity user){
        return repository.save(user);
    }

    @Transactional
    public UserEntity updateUser(UserEntity user){
        return repository.save(user);
    }

    @Transactional
    public UserEntity deleteUser(UserEntity user){
        repository.delete(user);
        return user;
    }

    @Transactional
    public UserEntity deleteUser(String email){
        UserEntity user = repository.findById(email).get();
        repository.delete(user);
        return user;
    }

    @Transactional(readOnly = true)
    public List<UserEntity> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public UserEntity findById(String id){
        Optional<UserEntity> got = repository.findById(id);
        if(got.isPresent()){
            return got.get();
        }else{
            return new UserEntity(null, null, null, null);
        }
    }

}
