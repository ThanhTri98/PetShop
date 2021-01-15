package com.example.petmarket2020.Controllers;

import android.app.Activity;

import com.example.petmarket2020.DAL.UsersDAL;
import com.example.petmarket2020.Models.UsersModel;

public class MainMoreController {
    private final UsersDAL usersDAL;

    public MainMoreController(Activity activity) {
        usersDAL = new UsersDAL(activity);
    }

    public boolean checkLogin() {
        return usersDAL.userIsExists();
    }

    public void logout() {
        usersDAL.clearSession();
    }

    public UsersModel getUserDetail() {
        return usersDAL.getUserSession();
    }

}
