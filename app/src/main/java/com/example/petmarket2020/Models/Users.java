package com.example.petmarket2020.Models;

import com.example.petmarket2020.HelperClass.Utils;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.util.List;

public class Users implements Serializable {
    private String uid;
    private String pwd;
    private String fullName;
    private String email;
    private String gender;
    private String dateOfBirth;
    private double coins;
    private double latitude;
    private double longitude;
    private String address;
    private List<String> favorites;
    private String phoneNumber;
    private String avatar;
    private boolean isEmailVerify;
    private String joinDate;

    public Users() {
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public boolean isEmailVerify() {
        return isEmailVerify;
    }

    public void setEmailVerify(boolean emailVerify) {
        isEmailVerify = emailVerify;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Register 1
    public Users(String uid, String pwd, String fullName, String gender, String dateOfBirth, String phoneNumber) {
        this.uid = uid;
        this.pwd = BCrypt.hashpw(pwd, BCrypt.gensalt(12));
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.isEmailVerify = false;
        this.joinDate = Utils.getCurrentDate();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
        this.pwd = pwd;
    }
}
