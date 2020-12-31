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
import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.Models.Users;
import com.example.petmarket2020.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rlImgProfile, rlBar;
    private static final int REQUEST_IMAGE = 1;
    private static final int REQUEST_MAP = 2;
    private EditText etEmail, etFullName, etAddress;
    private TextView tvPhoneNumber, tvGender, tvDob;
    private ImageView imgBack, imgAvatar, ivEditAddress, ivEditName, ivEditEmail, ivEditPwd, ivEditGender, ivEditDob;
    private Button btnVerify, btnUpdate;
    private View vMailStatus;
    private SessionManager sessionManager;
    private String uId;
    private Users users;

    //FireBase
    private DatabaseReference mDBRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWidget();
        setListener();
        users = (Users) getIntent().getSerializableExtra(NodeRootDB.USERS);
        uId = users.getUid();
        sessionManager = new SessionManager(this);
        mDBRef = FirebaseDatabase.getInstance().getReference(NodeRootDB.USERS);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        updateUI(users);
    }

    private void updateUI(Users users) {
        etFullName.setText(users.getFullName());
        Log.d("EMAIL", users.getEmail());
        if (!TextUtils.isEmpty(users.getEmail())) {
            etEmail.setText(users.getEmail());
        } else {
            etEmail.setText(getString(R.string.PrfHaveNot));
            btnVerify.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(users.getAddress())) {
            etAddress.setText(users.getAddress());
        } else {
            etAddress.setText(getString(R.string.PrfHaveNot));
        }
        tvPhoneNumber.setText(users.getPhoneNumber());
        tvGender.setText(users.getGender());
        tvDob.setText(users.getDateOfBirth());
        if (users.getAvatar() == null) {
            imgAvatar.setImageResource(R.drawable.ic_login_user);
        } else {
            Glide.with(ProfileActivity.this).load(mStorageRef.child(users.getAvatar())).into(imgAvatar);
        }
    }

    private void setListener() {
        imgBack.setOnClickListener(this);
        rlImgProfile.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        ivEditAddress.setOnClickListener(this);
        ivEditName.setOnClickListener(this);
        ivEditEmail.setOnClickListener(this);
    }

    private void getWidget() {
        rlImgProfile = findViewById(R.id.rlImgProfile);
        rlBar = findViewById(R.id.rlBar);

        btnVerify = findViewById(R.id.btnVerify);
        btnUpdate = findViewById(R.id.btnUpdate);

        etEmail = findViewById(R.id.etEmail);
        etFullName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvGender = findViewById(R.id.tvGender);
        tvDob = findViewById(R.id.tvDob);

        imgBack = findViewById(R.id.imgBack);

        ivEditAddress = findViewById(R.id.ivEditAddress);
        ivEditName = findViewById(R.id.ivEditName);
        ivEditEmail = findViewById(R.id.ivEditEmail);
        ivEditPwd = findViewById(R.id.ivEditPwd);
        ivEditGender = findViewById(R.id.ivEditGender);
        ivEditDob = findViewById(R.id.ivEditDob);
        imgAvatar = findViewById(R.id.imgAvatar);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.rlImgProfile:
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
                if (users.getEmail().equals(""))
                    etEmail.setText("");
                etEmail.requestFocus();
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
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(this, "Vui lòng nhập họ tên!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = etAddress.getText().toString();
        if (fullName.equals(users.getFullName())
                && email.equals(users.getEmail())
                && address.equals(users.getAddress())) {
            Toast.makeText(ProfileActivity.this, "Không có gì để cập nhật", Toast.LENGTH_LONG).show();
            return;
        }
        users.setFullName(fullName);
        users.setEmail(email);
        users.setAddress(address);
        // avatar
        String node = NodeRootDB.STORAGE_PROFILE;
        String photoName = uId + new Random().nextInt(44) + ".jpg";
        Bitmap bitmap = ((BitmapDrawable) imgAvatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        mStorageRef.child(users.getAvatar()).delete().addOnCompleteListener(task -> {
            mStorageRef.child(node + "/" + photoName).putBytes(baos.toByteArray());
        });
        users.setAvatar(node + "/" + photoName);
        rlBar.setVisibility(View.VISIBLE);
        mDBRef.child(uId).setValue(users).addOnSuccessListener(aVoid -> {
            sessionManager.setInfo(fullName, email, address, node + "/" + photoName);
            rlBar.setVisibility(View.INVISIBLE);
            Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
        });

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

    public void launchCamera() {
        Intent intent = new Intent(ProfileActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.REQUEST_CODE_TYPE, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // Gán tỉ lệ khóa là 1x1
        intent.putExtra(ImagePickerActivity.EXTRA_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.EXTRA_ASPECT_RATIO_X, 1);
        intent.putExtra(ImagePickerActivity.EXTRA_ASPECT_RATIO_Y, 1);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public void launchGallery() {
        Intent intent = new Intent(ProfileActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.REQUEST_CODE_TYPE, ImagePickerActivity.REQUEST_IMAGE_GALLERY);

        // Gán kích thước tối đa cho ảnh
        intent.putExtra(ImagePickerActivity.EXTRA_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.EXTRA_BITMAP_MAX_WIDTH, 480);
        intent.putExtra(ImagePickerActivity.EXTRA_BITMAP_MAX_HEIGHT, 640);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public void loadImageProfile(String url) {
        Glide.with(this).load(url)
                .into(imgAvatar);
        imgAvatar.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                loadImageProfile(uri.toString());
            }
        } else if (requestCode == REQUEST_MAP) {
            if (resultCode == RESULT_OK) {
                double latitude = data.getDoubleExtra(MapActivity.KEY_LATITUDE, 0);
                double longitude = data.getDoubleExtra(MapActivity.KEY_LONGITUDE, 0);
                String address = data.getStringExtra(MapActivity.KEY_ADDRESS);
                users.setLatitude(latitude);
                users.setLongitude(longitude);
                int index = address.lastIndexOf(",");
                etAddress.setText(address.substring(0, index));
            }
        }
    }

}