package ru.otus.hw.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "author-entity-graph", attributeNodes = {@NamedAttributeNode("author")})

@NamedEntityGraph(
        name = "book-author-genre-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("author")
                ,@NamedAttributeNode("genres")
//                ,@NamedAttributeNode(value = "comments", subgraph = "comments-subgraph"),
//        }
//       ,subgraphs = {
//                @NamedSubgraph(
//                        name = "comments-subgraph",
//                        attributeNodes = {
//                                @NamedAttributeNode("book_id")
//                        }
//                )
        }
)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;

    @Column(name="title", nullable = false)
    private String title;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ToString.Exclude
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

//    @ToString.Exclude
//    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    @JoinColumn(name = "book_id")
//    private List<Comment> comments;

}
