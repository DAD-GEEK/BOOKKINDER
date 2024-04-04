package com.grootgeek.apibookkinder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponseDto {
    private final String typeToken;
    private final String jwtToken;

    public TokenResponseDto(String typeToken, String jwtToken) {
        this.typeToken = typeToken;
        this.jwtToken = jwtToken;

    }
}
