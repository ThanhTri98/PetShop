package com.example.petmarket2020.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.petmarket2020.Controllers.ProfileController;
import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.Models.UsersModel;
import com.example.petmarket2020.R;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String PHONE_PATTERN = "^0[3-9][0-9]{8}$";
    private static final String PWD_SPECIAL = "***";
    private RelativeLayout rlIvProfile, rlBar, rlPwd;
    private static final int REQUEST_IMAGE = 1;
    private static final int REQUEST_MAP = 2;
    private static final int REQUEST_VERIFY = 3;
    private EditText etEmail, etFullName, etPwd, etPhoneNumber;
    private TextView tvGender, tvDob, tvAddress, tvJoinDate;
    private ImageView ivBack, ivAvatar, ivEditAddress, ivEditName, ivEditEmail, ivEditPwd, ivEditGender, ivEditDob, ivEditPhone;
    private Button btnVerifyEmail, btnVerifyPhone, btnUpdate;
    private View vMailStatusEmail, vMailStatusPhone;
    private UsersModel usersModel;
    private ProfileController profileController;

    public static int isLoadAvatar = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWidget();
        setListener();
        usersModel = (UsersModel) getIntent().getSerializableExtra(NodeRootDB.USERS);
        profileController = new ProfileController(this);
        updateUI();
    }

    @SuppressLint("ResourceAsColor")
    private void updateUI() {
        if (usersModel.getAvatar() == null) {
            ivAvatar.setImageResource(R.drawable.ic_login_user);
        } else {
            Glide.with(ProfileActivity.this)
                    .load(FirebaseStorage.getInstance()
                            .getReference().child(usersModel.getAvatar())).into(ivAvatar);
        }
        etFullName.setText(usersModel.getFullName());
        if (usersModel.getPhoneNumber() != null) {
            etPhoneNumber.setText(usersModel.getPhoneNumber());
            if (usersModel.isPhoneVerified()) {
                ivEditPhone.setVisibility(View.INVISIBLE);
                btnVerifyPhone.setVisibility(View.INVISIBLE);
                vMailStatusPhone.setBackgroundResource(R.drawable.ic_verified_blue);
            }
        } else {
            btnVerifyPhone.setVisibility(View.INVISIBLE);
            vMailStatusPhone.setVisibility(View.INVISIBLE);
        }
        if (usersModel.getEmail() != null) {
            etEmail.setText(usersModel.getEmail());
            if (usersModel.isEmailVerified()) {
                ivEditEmail.setVisibility(View.INVISIBLE);
                btnVerifyEmail.setVisibility(View.INVISIBLE);
                vMailStatusEmail.setBackgroundResource(R.drawable.ic_verified_blue);
            }
        } else {
            btnVerifyEmail.setVisibility(View.INVISIBLE);
            vMailStatusEmail.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(usersModel.getAddress())) {
            tvAddress.setText(usersModel.getAddress());
        }
        if (usersModel.getPwd().equals(PWD_SPECIAL)) {
            rlPwd.setVisibility(View.GONE);
        } else {
            etPwd.setText(usersModel.getPwd());
        }
        tvGender.setText(usersModel.getGender());
        tvDob.setText(usersModel.getDateOfBirth());
        tvJoinDate.setText(usersModel.getJoinDate());
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        rlIvProfile.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        ivEditAddress.setOnClickListener(this);
        ivEditName.setOnClickListener(this);
        ivEditEmail.setOnClickListener(this);
        ivEditPhone.setOnClickListener(this);

        btnVerifyPhone.setOnClickListener(this);
    }

    private void getWidget() {
        vMailStatusEmail = findViewById(R.id.vMailStatusEmail);
        vMailStatusPhone = findViewById(R.id.vMailStatusPhone);

        rlIvProfile = findViewById(R.id.rlIvProfile);
        rlBar = findViewById(R.id.rlBar);
        rlPwd = findViewById(R.id.rlPwd);

        btnVerifyEmail = findViewById(R.id.btnVerifyEmail);
        btnVerifyPhone = findViewById(R.id.btnVerifyPhone);
        btnUpdate = findViewById(R.id.btnUpdate);

        etEmail = findViewById(R.id.etEmail);
        etFullName = findViewById(R.id.etFullName);
        tvAddress = findViewById(R.id.tvAddress);
        etPwd = findViewById(R.id.etPwd);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);


        tvGender = findViewById(R.id.tvGender);
        tvDob = findViewById(R.id.tvDob);
        tvJoinDate = findViewById(R.id.tvJoinDate);

        ivBack = findViewById(R.id.ivBack);
        ivEditAddress = findViewById(R.id.ivEditAddress);
        ivEditName = findViewById(R.id.ivEditName);
        ivEditEmail = findViewById(R.id.ivEditEmail);
        ivEditPwd = findViewById(R.id.ivEditPwd);
        ivEditGender = findViewById(R.id.ivEditGender);
        ivEditDob = findViewById(R.id.ivEditDob);
        ivAvatar = findViewById(R.id.ivAvatar);
        ivEditPhone = findViewById(R.id.ivEditPhone);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.rlIvProfile:
                showImagePickerOptions();
                break;
            case R.id.btnUpdate:
                updateInfo();
                break;
            case R.id.ivEditAddress:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(new Intent(ProfileActivity.this, MapActivity.class), REQUEST_MAP);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                break;
            case R.id.ivEditName:
                etFullName.setEnabled(true);
                etFullName.requestFocus();
                break;
            case R.id.ivEditEmail:
                etEmail.setEnabled(true);
                etEmail.requestFocus();
                break;
            case R.id.ivEditPhone:
                etPhoneNumber.setEnabled(true);
                etPhoneNumber.requestFocus();
                break;
            case R.id.btnVerifyPhone:
                String phoneNumber = etPhoneNumber.getText().toString();
                Intent intent = new Intent(this, VerifyCodeActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivityForResult(intent, REQUEST_VERIFY);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MAP) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(ProfileActivity.this, MapActivity.class), REQUEST_MAP);
            }
        }
    }

    private void updateInfo() {
        String fullName = etFullName.getText().toString();
        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(this, "Vui lòng nhập họ tên!", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        Log.d("KAYY", "size: " + hashMap.size());
        if (!fullName.equals(usersModel.getFullName())) {
            hashMap.put(SessionManager.KEY_FULLNAME, fullName);
        }
        if (!usersModel.isPhoneVerified()) {
            String phoneNumber = etPhoneNumber.getText().toString();
            if (!TextUtils.isEmpty(phoneNumber)) {
                if (!Pattern.matches(PHONE_PATTERN, phoneNumber)) {
                    Toast.makeText(this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!phoneNumber.equals(usersModel.getPhoneNumber()))
                        hashMap.put(SessionManager.KEY_PHONE, phoneNumber);
                }
            }

        }
        if (!usersModel.isEmailVerified()) {
            String email = etEmail.getText().toString();
            if (!TextUtils.isEmpty(email)) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!email.equals(usersModel.getEmail()))
                        hashMap.put(SessionManager.KEY_EMAIL, email);
                }
            }
        }
        String address = tvAddress.getText().toString();
        if (!TextUtils.isEmpty(address) && !address.equals(usersModel.getAddress())) {
            hashMap.put(SessionManager.KEY_ADDRESS, address);
            hashMap.put(SessionManager.KEY_LATITUDE, usersModel.getLatitude());
            hashMap.put(SessionManager.KEY_LONGITUDE, usersModel.getLongitude());
        }
//        if (fullName.equals(usersModel.getFullName())
//                && isLoadAvatar == 0
//                && address.equals(usersModel.getAddress())) {
//            Toast.makeText(ProfileActivity.this, "Không có gì để cập nhật", Toast.LENGTH_LONG).show();
//            return;
//        }
//        usersModel.setFullName(fullName);
//        usersModel.setEmail(email);
//        usersModel.setAddress(address);
        // avatar
        if (isLoadAvatar != 0) {
            Bitmap bitmap = ((BitmapDrawable) ivAvatar.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            hashMap.put(SessionManager.KEY_AVATAR, baos.toByteArray());
        }
        if (hashMap.size() == 0) {
            Toast.makeText(this, "Không có gì để cập nhật", Toast.LENGTH_SHORT).show();
            return;
        }
        profileController.updateUserInfo(usersModel, hashMap, rlBar);

    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
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
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.REQUEST_CODE_TYPE, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // Gán tỉ lệ khóa là 1x1
        intent.putExtra(ImagePickerActivity.EXTRA_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.EXTRA_ASPECT_RATIO_X, 1);
        intent.putExtra(ImagePickerActivity.EXTRA_ASPECT_RATIO_Y, 1);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGallery() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.REQUEST_CODE_TYPE, ImagePickerActivity.REQUEST_IMAGE_GALLERY);

        // Gán kích thước tối đa cho ảnh
        intent.putExtra(ImagePickerActivity.EXTRA_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.EXTRA_BITMAP_MAX_WIDTH, 480);
        intent.putExtra(ImagePickerActivity.EXTRA_BITMAP_MAX_HEIGHT, 640);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void loadImageProfile(String url) {
        isLoadAvatar = 1;
        Glide.with(this).load(url)
                .into(ivAvatar);
        ivAvatar.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getParcelableExtra("path");
                loadImageProfile(uri.toString());
            }
        } else if (requestCode == REQUEST_MAP) {
            if (resultCode == RESULT_OK && data != null) {
                double latitude = data.getDoubleExtra(MapActivity.KEY_LATITUDE, 0);
                double longitude = data.getDoubleExtra(MapActivity.KEY_LONGITUDE, 0);
                String address = data.getStringExtra(MapActivity.KEY_ADDRESS);
                usersModel.setLatitude(latitude);
                usersModel.setLongitude(longitude);
                int index = address.lastIndexOf(",");
                tvAddress.setText(address.substring(0, index));
            }
        } else if (requestCode == REQUEST_VERIFY) {
            if (resultCode == RESULT_OK && data != null) {
                usersModel = (UsersModel) data.getSerializableExtra(NodeRootDB.USERS);
                updateUI();
            }
        }
    }

}