package com.example.petmarket2020.Models;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

public class Users implements Serializable {
    private String fullName;
    private String email;
    private String Uid;
    private String pwd;
    private String gender;
    private String dateOfBirth;
    private String phoneNumber;

    public Users() {
    }

    public Users(String fullName, String uid, String pwd, String gender, String dateOfBirth, String phoneNumber) {
        this.fullName = fullName;
        Uid = uid;
        this.pwd = BCrypt.hashpw(pwd, BCrypt.gensalt(12));
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = BCrypt.hashpw(pwd, BCrypt.gensalt(12));
    }
}
