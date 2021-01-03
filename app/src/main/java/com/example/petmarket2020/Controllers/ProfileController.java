package com.example.petmarket2020.Controllers;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.petmarket2020.DAL.UsersDAL;
import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Interfaces.IUsers;
import com.example.petmarket2020.Models.UsersModel;
import com.example.petmarket2020.Views.ProfileActivity;

import java.util.Random;

public class ProfileController {
    private final UsersDAL usersDAL;
    private final Activity activity;

    public ProfileController(Activity activity) {
        this.activity = activity;
        usersDAL = new UsersDAL(activity);
    }

    public void updateUserInfo(UsersModel usersModel, byte[] avatar, RelativeLayout rlBar) {
        rlBar.setVisibility(View.VISIBLE);
        String newName = "";
        if (avatar != null) {
            String photoName = usersModel.getUid() + new Random().nextInt(44) + ".jpg";
            newName = NodeRootDB.STORAGE_PROFILE + "/" + photoName;
        }
        IUsers iUsers = isSu -> {
            if (isSu) {
                ProfileActivity.isLoadAvatar = 0;
                Toast.makeText(activity, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                Utils.hiddenKeyboard(activity);
            } else {
                Toast.makeText(activity, "Đã xảy ra lỗi, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
            }
            rlBar.setVisibility(View.INVISIBLE);
        };
        usersDAL.updateUserInfo(usersModel, avatar, newName, iUsers);
    }

    public void updateVerifyInfo(int type) {
        usersDAL.updateVerifyInfo(type);
    }

    public void verifyPhone(String phoneNumber) {
        usersDAL.verifyPhone(phoneNumber);
    }
    public UsersModel getUserDetail(){
        return usersDAL.getUserDetail();
    }
}
