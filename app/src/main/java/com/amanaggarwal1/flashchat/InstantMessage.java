package com.amanaggarwal1.flashchat;

public class InstantMessage {
    private String author;
    private String message;

    public InstantMessage(String author, String message){
        this.author = author;
        this.message = message;
    }

    public InstantMessage(){
        author = "Black Hat";
        message = "Some Error";
    }
    public String getAuthor(){
        return author;
    }
    public String getMessage(){
        return message;
    }
}
