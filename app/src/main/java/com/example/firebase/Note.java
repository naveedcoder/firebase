package com.example.firebase;

public class Note {
    String Title;
    String Description;
    String timeStamp;

    public Note() {
    }

    public Note(String title, String description, String timeStamp) {
        Title = title;
        Description = description;
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
