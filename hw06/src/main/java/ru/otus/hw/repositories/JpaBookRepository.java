package ru.otus.hw.repositories;


import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    private static final String FETCH_GRAPH = "jakarta.persistence.fetchgraph";

    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id,
                Collections.singletonMap(FETCH_GRAPH, getAuthorGenresEntityGraph())));
    }

    @Override
    public List<Book> findAll() {
        var query = entityManager.createQuery("select b from Book b", Book.class);
        query.setHint(FETCH_GRAPH, getAuthorEntityGraph());
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        return entityManager.merge(book);
    }

    @Override
    public void delete(Book book) {
        entityManager.remove(book);
    }

    private EntityGraph<?> getAuthorEntityGraph() {
        return entityManager.getEntityGraph("book-author-entity-graph");
    }

    private EntityGraph<?> getAuthorGenresEntityGraph() {
        return entityManager.getEntityGraph("book-author-genres-entity-graph");
    }

    @Override
    public void deleteById(long id) {
        entityManager.createQuery("delete from Book b where b.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

}
