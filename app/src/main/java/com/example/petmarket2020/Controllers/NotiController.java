package com.example.petmarket2020.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Adapters.RV_NotiAdapter;
import com.example.petmarket2020.DAL.NotiDAL;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.Models.NotificationsModel;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.Models.UsersModel;
import com.example.petmarket2020.Views.PostDetailActivity;

import java.util.List;

public class NotiController {
    private Activity activity;
    private NotiDAL notiDAL;
    private SessionManager sessionManager;

    public NotiController(Activity activity) {
        this.activity = activity;
        this.notiDAL = new NotiDAL();
        sessionManager = new SessionManager(activity);
    }

    public void getNoti(RecyclerView recyclerView) {
        UsersModel usersModel = sessionManager.getUserSession();
        if (usersModel != null) {
            String uId = usersModel.getUid();
            RV_NotiAdapter rv_notiAdapter = new RV_NotiAdapter((postId, notiId) -> {
                notiDAL.getInfoAdd(postId, new IControlData() {
                    @Override
                    public void responseData(Object data) {
                        Object[] rs = (Object[]) data;
                        String peType = (String) rs[0];
                        long price = (long) rs[1];
                        Intent intent = new Intent(activity, PostDetailActivity.class);
                        intent.putExtra("postId", postId);
                        intent.putExtra("peType", peType);
                        intent.putExtra("price", price);
                        activity.startActivity(intent);
                        Log.e("NOTTTI", notiId + "");
                        if (notiId == 0) return;
                        notiDAL.updateIsCheck(uId, notiId);
                    }
                });
            });
            notiDAL.getNoti(uId, new IControlData() {
                @Override
                public void responseData(Object data) {
                    List<NotificationsModel> notificationsModelList = (List<NotificationsModel>) data;
                    rv_notiAdapter.setData(notificationsModelList);
                    recyclerView.setAdapter(rv_notiAdapter);
                }
            });
        }
    }

}
