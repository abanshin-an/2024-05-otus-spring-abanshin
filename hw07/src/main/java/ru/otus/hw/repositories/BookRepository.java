package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository  extends JpaRepository<Book, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "book-entity-graph")
    Optional<Book> findById(long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "book-entity-graph")
    List<Book> findAll();

}
