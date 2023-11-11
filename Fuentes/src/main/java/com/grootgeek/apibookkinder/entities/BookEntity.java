package com.grootgeek.apibookkinder.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BOOKS_BOOKKINDER", schema = "ADMIN")
@Data
public class BookEntity {

    @Id
    @Column
    private String ID;
    @Column
    private String isbn;
    @Column
    private String name;
    @Column
    private String author;
    @Column
    private String category;
    @Column
    private String sellerUser;
    @Column
    private float Quality;
    @Column
    private int price;
    @Column
    private int quantity;
    @Column
    private String description;
    @Column
    private String observations;
    @Column
    private Boolean OnSale;

    public BookEntity() {
        super();
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "ID='" + ID + '\'' +
                ", isbn='" + isbn + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", sellerUser='" + sellerUser + '\'' +
                ", Quality=" + Quality +
                ", price=" + price +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", observations='" + observations + '\'' +
                ", OnSale=" + OnSale +
                '}';
    }
}
