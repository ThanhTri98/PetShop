package com.example.petmarket2020.HelperClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.petmarket2020.Views.ImagePickerActivity;

public class ShowImagePicker {
    private final Activity activity;
    private Context context;
    private final int REQUEST_IMAGE;

    public ShowImagePicker(Activity activity, int REQUEST_IMAGE) {
        this.REQUEST_IMAGE = REQUEST_IMAGE;
        this.activity = activity;
    }

    public void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(activity, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onCameraSelected() {
                launchCamera();
            }

            @Override
            public void onGallerySelected() {
                launchGallery();
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.REQUEST_CODE_TYPE, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // Gán tỉ lệ khóa là 1x1
        intent.putExtra(ImagePickerActivity.EXTRA_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.EXTRA_ASPECT_RATIO_X, 1);
        intent.putExtra(ImagePickerActivity.EXTRA_ASPECT_RATIO_Y, 1);
        activity.startActivityForResult(intent, REQUEST_IMAGE);

    }

    private void launchGallery() {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.REQUEST_CODE_TYPE, ImagePickerActivity.REQUEST_IMAGE_GALLERY);

        // Gán kích thước tối đa cho ảnh
        intent.putExtra(ImagePickerActivity.EXTRA_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.EXTRA_BITMAP_MAX_WIDTH, 480);
        intent.putExtra(ImagePickerActivity.EXTRA_BITMAP_MAX_HEIGHT, 640);

        activity.startActivityForResult(intent, REQUEST_IMAGE);
    }
}
