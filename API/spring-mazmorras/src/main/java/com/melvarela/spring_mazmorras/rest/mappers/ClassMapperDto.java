package com.melvarela.spring_mazmorras.rest.mappers;

import java.util.ArrayList;
import java.util.List;

import com.melvarela.spring_mazmorras.rest.dtos.ClassDto;

public class ClassMapperDto {
    
    public static ClassDto fromSingleJson(String json){
        String index;
        String name;
        List<String> subClases = new ArrayList<>();

        index = json.substring(json.indexOf("\"index\"") + 9, json.indexOf(",\"name\":") - 1);
        name = json.substring(json.indexOf("\"name\"") + 8, json.indexOf(",\"hit_die\":") - 1);

        String sc = json.substring(json.indexOf("\"subclasses\":") + 13);
        sc = sc.substring(0, sc.indexOf("],") - 1);
        String[] scE = sc.split("}");
        for (String s : scE) {
            if(s.contains("\"name\":") && s.contains(",\"url\":")){
                subClases.add(s.substring(s.indexOf("\"name\":") + 8, s.indexOf(",\"url\":") - 1));
            }
        }

        return new ClassDto(
            index, name, subClases
        );
    }

    public static List<ClassDto> fromAllJson(String json){
        String[] strs = json.split("}");
        List<ClassDto> clases = new ArrayList<>();

        for (String strs2 : strs) {
            if(strs2.contains("name") && strs2.contains(",\"url\":")){
                clases.add(
                    new ClassDto(
                        strs2.substring(strs2.indexOf("\"index\"") + 9, strs2.indexOf(",\"name\":") - 1),
                        strs2.substring(strs2.indexOf("\"name\"") + 8, strs2.indexOf(",\"url\":") - 1),
                        List.of()
                    )
                );
            }
        }

        return clases;
    }

}
