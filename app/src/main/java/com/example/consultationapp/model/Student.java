package com.example.consultationapp.model;

public class Student {

    String firstname,lastname,role,phoneNumber,email,course,url,uid;

    public Student(){
        //needed for firebase
    }

    public Student(String firstname, String lastname, String role, String phoneNumber, String email, String course, String url, String uid) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.course = course;
        this.url = url;
        this.uid = uid;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
