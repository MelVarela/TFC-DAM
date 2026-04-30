package com.melvarela.spring_mazmorras.rest.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CharacterDto {
    String id;
    String name;
    String clase;
    String subClase;
    int pg;
    int maxPg;
    String picture;
    String campaign;
    List<String> objects;
}
