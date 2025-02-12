package com.example.assignment_pedro_silva.dto;

public class BookDTO {
    private String uuid;
    private String title;
    private String author;

    public BookDTO(String uuid, String title, String author) {
        this.uuid = uuid;
        this.title = title;
        this.author = author;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
