package com.grootgeek.apibookkinder.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

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
    private String id;
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
    
    @Value("${book.google.items}")
    private String items;

    @Value("${book.google.volumeInfo}")
    private String volumeInfo;
    public BookEntity(JSONObject jsResponse) {
        this.name = jsResponse.getJSONArray(items).getJSONObject(0).getJSONObject(volumeInfo).getString("title");
        this.author = jsResponse.getJSONArray(items).getJSONObject(0).getJSONObject(volumeInfo).getJSONArray("authors").getString(0);
        this.category = jsResponse.getJSONArray(items).getJSONObject(0).getJSONObject(volumeInfo).getJSONArray("categories").getString(0);
        this.description = jsResponse.getJSONArray(items).getJSONObject(0).getJSONObject(volumeInfo).getString("description");
        this.urlImage = jsResponse.getJSONArray(items).getJSONObject(0).getJSONObject(volumeInfo).getJSONObject("imageLinks").getString("thumbnail");
    }
    public BookEntity() {

    }

    public BookEntity(BookEntity bookEntity) {
        this.id = bookEntity.getId();
        this.isbn = bookEntity.getIsbn();
        this.name = bookEntity.getName();
        this.author = bookEntity.getAuthor();
        this.category = bookEntity.getCategory();
        this.sellerUser = bookEntity.getSellerUser();
        this.quality = bookEntity.getQuality();
        this.price = bookEntity.getPrice();
        this.quantity = bookEntity.getQuantity();
        this.description = bookEntity.getDescription();
        this.observations = bookEntity.getObservations();
        this.onSale = bookEntity.getOnSale();
        this.urlImage = bookEntity.getUrlImage();

    }
}
