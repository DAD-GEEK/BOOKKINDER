package com.grootgeek.apibookkinder.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "USERS_BOOKKINDER", schema = "ADMIN")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAppEntity {

    @Id
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String lastName;
    @Column
    private String phone;
    @Column
    private String role;
    @Column
    private String rating;

    public UserAppEntity() {
        super();
    }

    public UserAppEntity(UserAppEntity userAppEntity) {
        this.email = userAppEntity.email;
        this.password = userAppEntity.password;
        this.name = userAppEntity.name;
        this.lastName = userAppEntity.lastName;
        this.phone = userAppEntity.phone;
        this.role = userAppEntity.role;
        this.rating = userAppEntity.rating;
    }
}
