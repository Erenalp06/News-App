package com.teksen.newsapp.dto;

public class FavoriteDTO {

    private Long id;
    private String email;
    private String title;
    private String content;
    private String sourceId;
    private String sourceName;
    private String author;
    private String description;
    private String url;
    private String imageUrl;
    private String publishedAt;

    public FavoriteDTO() {
    }

    public FavoriteDTO(Long id, String email, String title, String content, String sourceId, String sourceName, String author, String description, String url, String imageUrl, String publishedAt) {
        this.id = id;
        this.email = email;
        this.title = title;
        this.content = content;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.author = author;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.publishedAt = publishedAt;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    @Override
    public String toString() {
        return "FavoriteDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                '}';
    }
}
