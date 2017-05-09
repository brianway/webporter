package com.brianway.webporter.data.elasticsearch;

public class Document {
    protected String id;
    protected String content;

    public Document(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("id: %s, data: %s", id, content);
    }
}
