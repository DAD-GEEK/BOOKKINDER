package com.grootgeek.apibookkinder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grootgeek.apibookkinder.entities.BookEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDto extends BookEntity {
}
