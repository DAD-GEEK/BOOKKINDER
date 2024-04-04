package com.grootgeek.apibookkinder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grootgeek.apibookkinder.entities.UserAPIEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAPiDto extends UserAPIEntity {

}
