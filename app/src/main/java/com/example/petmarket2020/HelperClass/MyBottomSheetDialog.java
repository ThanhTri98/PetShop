package com.example.petmarket2020.HelperClass;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petmarket2020.Controllers.FlashSellController;
import com.example.petmarket2020.Models.PostPackageModel;
import com.example.petmarket2020.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MyBottomSheetDialog extends BottomSheetDialogFragment {
    private static final double SALE_5 = 0.1, SALE_10 = 0.3;
    private PostPackageModel postPackageModel;
    private TextView tvTitle2, tvPrice, tvPrice2;
    private LinearLayout llPayment;
    private RadioGroup rg;
    private FlashSellController flashSellController;
    private String postId;

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setFlashSellController(FlashSellController flashSellController) {
        this.flashSellController = flashSellController;
    }

    public static MyBottomSheetDialog newInstance(PostPackageModel postPackageModel) {
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("pkgModel", postPackageModel);
        bottomSheetDialog.setArguments(bundle);
        return bottomSheetDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            postPackageModel = (PostPackageModel) bundle.getSerializable("pkgModel");

        }
    }

    private BottomSheetDialog bottomSheetDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.z_bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(view);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        getWidget(view);
        setData();
        return bottomSheetDialog;
    }

    private void getWidget(View view) {
        tvTitle2 = view.findViewById(R.id.tvTitle2);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvPrice2 = view.findViewById(R.id.tvPrice2);
        rg = view.findViewById(R.id.rg);
        llPayment = view.findViewById(R.id.llPayment);
    }

    private void setData() {
        if (postPackageModel != null) {
            AtomicInteger day = new AtomicInteger();
            AtomicReference<Double> priceTotal = new AtomicReference<>((double) 0);
            long price = postPackageModel.getPrice();
            String prices = Utils.formatCurrencyVN(price);
            tvPrice.setText(prices);
            tvPrice2.setText(prices);
            rg.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rd1) {
                    day.set(1);
                    tvTitle2.setText("Đẩy tin 1 ngày");
                    tvPrice.setText(Utils.formatCurrencyVN(price));
                    tvPrice2.setText(Utils.formatCurrencyVN(price));
                } else if (checkedId == R.id.rd2) {
                    day.set(3);
                    priceTotal.set(price * 3 - (price * 3 * SALE_5));
                    tvPrice.setText(Utils.formatCurrencyVN(priceTotal.get()));
                    tvPrice2.setText(Utils.formatCurrencyVN(priceTotal.get()));
                    tvTitle2.setText("Đẩy tin 3 ngày");
                } else {
                    day.set(7);
                    priceTotal.set(price * 7 - (price * 7 * SALE_10));
                    tvPrice.setText(Utils.formatCurrencyVN(priceTotal.get()));
                    tvPrice2.setText(Utils.formatCurrencyVN(priceTotal.get()));
                    tvTitle2.setText("Đẩy tin 7 ngày");
                }
            });
            llPayment.setOnClickListener(v -> {
                if (flashSellController != null) {
                    if (flashSellController.checkMoney(priceTotal.get())) {
                        flashSellController.paymentProcess(priceTotal.get(), postId, postPackageModel, day.get());
                        bottomSheetDialog.dismiss();
                    } else
                        flashSellController.showSBMargin(llPayment);
                }

            });
        }
    }
}
