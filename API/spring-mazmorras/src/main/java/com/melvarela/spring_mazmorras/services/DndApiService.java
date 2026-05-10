package com.melvarela.spring_mazmorras.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melvarela.spring_mazmorras.repositories.DndApiRepository;
import com.melvarela.spring_mazmorras.rest.dtos.ClassDto;

@Service
public class DndApiService {
    
    @Autowired
    DndApiRepository repository;

    public List<ClassDto> getAllClases(){
        return repository.getAllClases();
    }

    public ClassDto getClase(String clase){
        return repository.getClase(clase);
    }

    public List<String> getSubclasesForClass(String clase){
        return repository.getSubclasesForClass(clase);
    }
}
