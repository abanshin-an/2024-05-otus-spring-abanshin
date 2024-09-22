package ru.otus.hw.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "comments")
@NamedEntityGraph(name = "comment-book-author-genres-entity-graph",
        attributeNodes = {@NamedAttributeNode(value = "book", subgraph = "comment1")},
        subgraphs = {@NamedSubgraph(name = "comment1",
                attributeNodes = {
                        @NamedAttributeNode("author"),
                        @NamedAttributeNode("genres")})})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;

    @Column(name = "content")
    private String content;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    @BatchSize(size = 10)
    private Book book;

}
