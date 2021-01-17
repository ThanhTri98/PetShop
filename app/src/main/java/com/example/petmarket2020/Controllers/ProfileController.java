package com.example.petmarket2020.Controllers;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.petmarket2020.DAL.UsersDAL;
import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.Models.UsersModel;
import com.example.petmarket2020.Views.ProfileActivity;
import com.example.petmarket2020.Views.VerifyCodeActivity;

import java.util.HashMap;
import java.util.Random;

public class ProfileController {
    private final UsersDAL usersDAL;
    private final Activity activity;

    public ProfileController(Activity activity) {
        this.activity = activity;
        usersDAL = new UsersDAL(activity);
    }

    public void updateUserInfo(UsersModel usersModel, HashMap<String, Object> dataUpdate, RelativeLayout rlBar) {
        rlBar.setVisibility(View.VISIBLE);
        String newAvatar;
        if (dataUpdate.containsKey(UsersDAL.KEY_AVATAR)) {
            String photoName = usersModel.getUid() + new Random().nextInt(44) + ".jpg";
            newAvatar = NodeRootDB.STORAGE_PROFILE + "/" + photoName;
            dataUpdate.put("newAvatar", newAvatar);
        }
        usersDAL.updateUserInfo(usersModel, dataUpdate, new IControlData() {
            @Override
            public void isSuccessful(boolean isSu) {
                if (isSu) {
                    ProfileActivity.isLoadAvatar = 0;
                    Toast.makeText(activity, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Utils.hiddenKeyboard(activity);
                } else {
                    Toast.makeText(activity, "Đã xảy ra lỗi, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                }
                rlBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void updateVerifyInfo(int type, String uid) {
        usersDAL.updateVerifyInfo(type, uid);
    }

    public void verifyPhone(String phoneNumber) {
        phoneNumber = "+84" + phoneNumber.substring(1);
        usersDAL.verifyPhone(phoneNumber, new IControlData() {
            @Override
            public void responseData(Object data) {
                VerifyCodeActivity.codeResponse = (String) data;
            }
        });
    }

    public UsersModel getUserDetail() {
        return usersDAL.getUserSession();
    }
}
