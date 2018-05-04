package com.example.user.springbootwebserviceexample_java;

public class Book {
    int id;
    String name;
    String author;

    public Book(int id, String name, String author) {
        super();
        this.id = id;
        this.name = name;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return String.format("Book [id=%s, name=%s, author=%s]", id, name, author);
    }
}
