package com.alim.ssn.main.create;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alim.ssn.R;
import com.alim.ssn.studentProperties.Stid;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreatePost extends AppCompatActivity  {


    private static final int MULTIPLE_PERMISSIONS = 5132;
    private static final int ACTION_PICK_REQUEST_CODE = 1231;
    private static final int PICKFILE_REQUEST_CODE = 1232;
    private static final int FILE_TYPE_IMAGE=0;
    private static final int FILE_TYPE_FILE=1;
    private static final int FILE_TYPE_NO_FILE=-1;
    @BindView(R.id.et_create_post_title)
    EditText titleEt;
    @BindView(R.id.et_create_post_content)
    EditText descEt;
//    @BindView(R.id.et_create_post_field)
//    EditText majorEt;
//    @BindView(R.id.et_create_post_grade)
//    EditText gradeEt;





    private void displayListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("انتخاب کنید");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layout_dialog_list);
        adapter.add("عکس");
        adapter.add("فایل");
        builder.setCancelable(true);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        uploadImage();
                        break;
                    case 1:
                        uploadFile();
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void uploadFile() {
        if (checkPermissions()) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            startActivityForResult(intent, PICKFILE_REQUEST_CODE);
        }

    }

    private void uploadImage() {
        if (checkPermissions()) {
            Intent pick = new Intent(Intent.ACTION_PICK);
            pick.setType("image/*");
            pick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(pick, ACTION_PICK_REQUEST_CODE);
        }

    }

    private boolean checkPermissions() {
        int result;
     /*   List<String> listPermissionsNeeded = new ArrayList<>();
       *//* for (String p : premissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
                return false;
            }*//*
        }*/
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSIONS
                && grantResults.length > 0) {
            String permissionDenied = "";
            for (String per : permissions) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionDenied += "\n" + per;
                }
            }
            if (permissionDenied.length() > 0) {
                Toast.makeText(CreatePost.this, "permission " + permissionDenied + " denied!", Toast.LENGTH_SHORT).show();
            }

        }
    }





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);

    }

}