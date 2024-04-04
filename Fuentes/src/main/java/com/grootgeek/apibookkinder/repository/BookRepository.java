package com.grootgeek.apibookkinder.repository;

import com.grootgeek.apibookkinder.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    List<BookEntity> findByIsbn(String isbn);
    Optional<BookEntity>  findByIsbnAndSellerUser(String isbn, String sellerUser);
    void deleteByIsbnAndSellerUser(String isbn, String sellerUser);
}

