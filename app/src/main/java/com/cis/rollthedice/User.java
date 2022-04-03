package com.cis.rollthedice;

public class User {
    private String fullname;
    private String email;
    private String password;
    private String score;

    public User(String fullname, String email, String password, String score) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.score = score;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
