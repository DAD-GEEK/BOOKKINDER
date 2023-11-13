package com.grootgeek.apibookkinder.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BOOKS_BOOKKINDER", schema = "ADMIN")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private String quality;
    @Column
    private String price;
    @Column
    private String quantity;
    @Column
    private String description;
    @Column
    private String observations;
    @Column
    private String onSale;
    @Column
    private String urlImage;

    public BookEntity() {super();
    }

    public BookEntity(String ID, String isbn, String name, String author, String category, String sellerUser, String quality, String price, String quantity, String description, String observations, String onSale, String urlImage) {
        this.ID = ID;
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.category = category;
        this.sellerUser = sellerUser;
        this.quality = quality;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.observations = observations;
        this.onSale = onSale;
        this.urlImage = urlImage;
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
                ", quality=" + quality +
                ", price=" + price +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", observations='" + observations + '\'' +
                ", onSale=" + onSale +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}
