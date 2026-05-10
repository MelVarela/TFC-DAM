package com.melvarela.spring_mazmorras.repositories;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.melvarela.spring_mazmorras.rest.dtos.ClassDto;
import com.melvarela.spring_mazmorras.rest.mappers.ClassMapperDto;

@Repository
public class DndApiRepository {
    
    final String API_URL = "https://www.dnd5eapi.co/api/2014/";

    public List<ClassDto> getAllClases(){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "classes"))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return ClassMapperDto.fromAllJson(response.body());
        }catch(Exception e){
            System.out.println("ERR: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public ClassDto getClase(String clase){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "classes/" + clase))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return ClassMapperDto.fromSingleJson(response.body());
        }catch(Exception e){
            System.out.println("ERR: " + e.getMessage());
            e.printStackTrace();
            return new ClassDto();
        }
    }

    public List<String> getSubclasesForClass(String clase){
       try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "classes/" + clase))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ClassDto claseDto = ClassMapperDto.fromSingleJson(response.body());
            return claseDto.getSubClases();
       }catch(Exception e){
            System.out.println("ERR: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
}
