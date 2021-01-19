package com.example.petmarket2020.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Adapters.RV_PostPackageAdapter;
import com.example.petmarket2020.DAL.PostPackageDAL;
import com.example.petmarket2020.HelperClass.MyBottomSheetDialog;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.Models.PostPackageModel;
import com.example.petmarket2020.Views.CoinsActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FlashSellController {
    private final Activity activity;
    private final PostPackageDAL postPackageDAL;
    private final FragmentManager fragmentManager;
    private FlashSellController flashSellController;

    public FlashSellController(Activity activity, FragmentManager fragmentManager) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        postPackageDAL = new PostPackageDAL(activity);
        flashSellController = this;
    }

    public void getPostPackage(RecyclerView recyclerView, String postId) {
        postPackageDAL.getPostPackage(new IControlData() {
            @Override
            public void responseData(Object data) {
                List<PostPackageModel> postPackageModels = (List<PostPackageModel>) data;
                RV_PostPackageAdapter rv_postPackageAdapter = new RV_PostPackageAdapter(postPackageModels, postPackageModel -> {
                    MyBottomSheetDialog myBottomSheetDialog = MyBottomSheetDialog.newInstance(postPackageModel);
                    myBottomSheetDialog.setFlashSellController(flashSellController);
                    myBottomSheetDialog.setPostId(postId);
                    myBottomSheetDialog.show(fragmentManager, myBottomSheetDialog.getTag());
                });
                recyclerView.setAdapter(rv_postPackageAdapter);
            }
        });
    }

    public boolean checkMoney(double price) {
        return postPackageDAL.checkMoney(price);
    }

    public void paymentProcess(double totalPrice, String postId, PostPackageModel postPackageModel,int day) {
        postPackageDAL.paymentProcess(totalPrice, postId, postPackageModel,day, new IControlData() {
            @Override
            public void isSuccessful(boolean isSu) {
                if (isSu) {
                    Toast.makeText(activity, "Mua gói tin thành công", Toast.LENGTH_LONG).show();
                    activity.finish();
                }
            }
        });
    }

    public void showSBMargin(View v) {
        Snackbar sb = Snackbar.make(v, "Số dư của bạn không đủ để thực hiện.", Snackbar.LENGTH_SHORT)
                .setAction("NẠP TIỀN", v1 -> activity.startActivity(new Intent(activity, CoinsActivity.class)));
        sb.setActionTextColor(Color.CYAN);
//        sb.setAnchorView(activity.findViewById(R.id.vTmp));
        sb.show();
    }

}
