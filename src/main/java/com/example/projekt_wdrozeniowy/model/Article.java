package com.example.projekt_wdrozeniowy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Table
@Setter
@Getter
@ToString
public class Article {
    @Id
    @PrimaryKey
    private UUID id;
    @Indexed
    @Column("creation_date")
    private LocalDateTime date;
    private String title;
    private String contents;
    private String author;
    private boolean exported;

    public Article() {
        this.id = UUID.randomUUID();
        this.date = LocalDateTime.now();
        this.date = this.date.withNano(0);
        this.exported = false;
    }

    public Article(String title, String contents, String author) {
        this();
        this.title = title;
        this.contents = contents;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
