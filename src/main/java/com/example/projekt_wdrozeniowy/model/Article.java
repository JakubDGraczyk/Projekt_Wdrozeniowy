package com.example.projekt_wdrozeniowy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table
@Setter
@Getter
@ToString
public class Article implements Comparable<Article> {
    @Id
    @PrimaryKey
    private UUID id;
    @Indexed
    private LocalDateTime date;
    private String title;
    private String content;
    private String author;
    private boolean exported;

    Article() {
        this.id = UUID.randomUUID();
        this.date = LocalDateTime.now();
        this.date = this.date.withNano(0);
        this.exported = false;
    }

    public Article(String title, String content, String author) {
        this();
        this.title = title;
        this.content = content;
        this.author = author;
    }

    @Override
    public int compareTo(Article o) {
        return this.date.compareTo(o.getDate());
    }
}
