package org.vitalii.fedyk.librarygenerated.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.vitalii.fedyk.librarygenerated.model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b " +
           "FROM Book b " +
           "JOIN FETCH b.author " +
           "WHERE b.id IN :ids")
    List<Book> findAllByIds(List<Long> ids);
    boolean existsByAuthorId(Long authorId);
}