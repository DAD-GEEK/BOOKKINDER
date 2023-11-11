package com.grootgeek.apibookkinder.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "USERS_BOOKKINDER", schema = "ADMIN")
@Data
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

    public UserAppEntity(String email, String password, String name, String lastName, String phone, String role, String rating) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "UserAppEntity{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
