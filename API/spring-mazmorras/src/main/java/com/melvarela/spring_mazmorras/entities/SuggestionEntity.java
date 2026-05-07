package com.melvarela.spring_mazmorras.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "sugerencias")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SuggestionEntity {
    
    @Id
    @Field("_id")
    private Integer id;

    @Field
    private String text;

    @Field
    private char type; //S -> Sugerencia | E -> Error

}
