package com.teksen.newsapp;


import android.os.Parcel;
import android.os.Parcelable;

public class NewsDTO implements Parcelable {
    private Long id;
    private String sourceId;
    private String sourceName;
    private String author;
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String publishedAt;
    private String content;

    public NewsDTO() {
    }

    public NewsDTO(Long id, String sourceId, String sourceName, String author, String title, String description, String url, String imageUrl, String publishedAt, String content) {
        this.id = id;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    protected NewsDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        sourceId = in.readString();
        sourceName = in.readString();
        author = in.readString();
        title = in.readString();
        description = in.readString();
        url = in.readString();
        imageUrl = in.readString();
        publishedAt = in.readString();
        content = in.readString();
    }

    public static final Creator<NewsDTO> CREATOR = new Creator<NewsDTO>() {
        @Override
        public NewsDTO createFromParcel(Parcel in) {
            return new NewsDTO(in);
        }

        @Override
        public NewsDTO[] newArray(int size) {
            return new NewsDTO[size];
        }
    };

    public Long getId() {
        return id;
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

    public String getTitle() {
        return title;
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

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(sourceId);
        dest.writeString(sourceName);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(imageUrl);
        dest.writeString(publishedAt);
        dest.writeString(content);
    }
}

