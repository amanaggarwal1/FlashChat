package com.amanaggarwal1.flashchat;

public class InstantMessage {
    private String authorEmail;
    private String authorName;
    private String message;


    public InstantMessage( String email, String author, String message){
        authorName = author;
        this.message = message;
        authorEmail = email;
    }

    public InstantMessage(){
        authorEmail = "Some Bug";
        authorName = "Black Hat";
        message = "This message was not intended";
    }

    public String getAuthorEmail(){
        return authorEmail;
    }

    public String getAuthorName(){
        return authorName;
    }
    public String getMessage(){
        return message;
    }

}
