package com.grootgeek.apibookkinder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponseDto {
    private final String typetoken;
    private final String jwtToken;


    public TokenResponseDto(String typetoken,String jwtToken ){
        this.typetoken = typetoken;
        this.jwtToken = jwtToken;

    }

    @Override
    public String toString() {
        return "TokenResponseDto{" +
                "typetoken='" + typetoken + '\'' +
                ", jwtToken='" + jwtToken + '\'' +
                '}';
    }
}
