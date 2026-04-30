package com.melvarela.spring_mazmorras.rest.dtos;

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
public class CreatureDto {
    String id;
    String name;
    String species;
    String picture;
    String campaign;
}
