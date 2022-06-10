package com.aseds.reviewapp.Model;

public class Comment {

    private String commentid;
    private String comment;
    private String publisher;
    private String date;

    public Comment() {
    }

    public Comment(String id, String comment, String publisher, String date) {
        this.commentid = id;
        this.comment = comment;
        this.publisher = publisher;
        this.date = date;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
