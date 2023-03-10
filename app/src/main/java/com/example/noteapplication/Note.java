package com.example.noteapplication;

class Note {
    String id;
    String title;
    String content;

    private Note() {
    }

    Note(String id, String content, String title) {
        this.id = id;
        this.content = content;
        this.title = title;

    }


    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }
}
