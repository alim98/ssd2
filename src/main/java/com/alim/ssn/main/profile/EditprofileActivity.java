package com.alim.ssn.main.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alim.ssn.R;
import com.alim.ssn.model.Student;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.alim.ssn.studentProperties.Stid;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditprofileActivity extends AppCompatActivity {
    private static final int READ_EXTERNAL_PERMISSION_REQUEST_CODE = 1003;
    private static final int PICK_IMAGE_REQUEST_CODE = 1004;
    private static final int MULTIPLE_PERMISSIONS = 5132;

    @BindView(R.id.iv_edit_prf_back_button)
    ImageView backBtn;
    @BindView(R.id.iv_edit_prf_avatar_pic)
    ImageView avatar;
    @BindView(R.id.tv_edit_prf_change_avatar)
    TextView changeAvatar;
    @BindView(R.id.et_edit_prf_username)
    EditText userName;
    @BindView(R.id.et_edit_prf_email)
    EditText email;

    @BindView(R.id.et_edit_prf_name)
    EditText name;
    @BindView(R.id.et_edit_prf_field)
    EditText field;
    @BindView(R.id.rl_edit_prf_uni)
    RelativeLayout uni;
    @BindView(R.id.rl_edit_prf_age)
    RelativeLayout age;
    @BindView(R.id.rl_edit_prf_gender)
    RelativeLayout gender;
    @BindView(R.id.fav_edit_prf_done)
    FloatingActionButton done;
    @BindView(R.id.tv_edit_prf_birthday)
    TextView birthdayText;
    @BindView(R.id.tv_edit_prf_gender)
    TextView genderText;
    @BindView(R.id.tv_edit_prf_uni)
    TextView uniText;

    File avatarFile;
    private int genderv;
    private int birthdayTextv;
    private String unamev;
    private String emailv;
    private String passv;
    private String namev;
    private String fieldv;
    private String[] premissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private int stId;
    private String token;

    @OnClick(R.id.iv_edit_prf_back_button)
    void onBackButtonClicked(){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        ButterKnife.bind(this);
         stId = ((Stid) Objects.requireNonNull(this).getApplicationContext()).getStId();
         token = "Bearer " + ((Stid) Objects.requireNonNull(this).getApplicationContext()).getToken();
        setupEditTextFonts();
        setupDialogBoxes();
        setupDefaults();

        done.setOnClickListener(view ->
        {
            getValues();
            updateStudent();
        });
        changeAvatar.setOnClickListener(view ->
        {
            if (checkReadPermission()) {
                Intent pickFromGallery = new Intent();
                pickFromGallery.setType("image/*");
                pickFromGallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(pickFromGallery, PICK_IMAGE_REQUEST_CODE);
            }
        });
    }

    private void getValues() {
        unamev = userName.getText().toString();
        emailv = email.getText().toString();
        namev = name.getText().toString();
        fieldv=field.getText().toString();
        //int uniCodev=

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                byte[] bytes = null;
                Uri uri = data.getData();
                   /* Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50,stream);
                    bytes=stream.toByteArray();
                    avatarFile=new File("avatar");
                    FileOutputStream fos=new FileOutputStream(avatarFile);
                    fos.write(bytes);
                    fos.flush();
                    fos.close();*/
                   avatarFile=new File(getPath(uri));

            }

        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private boolean checkReadPermission() {
        ActivityCompat.requestPermissions(this, premissions, MULTIPLE_PERMISSIONS);

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : premissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
                return false;
            }
        }
        return true;

    }
    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){
        if (requestCode == MULTIPLE_PERMISSIONS
                && grantResults.length > 0) {
            String permissionDenied = "";
            for (String per : permissions) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionDenied += "\n" + per;
                }
            }
            if (permissionDenied.length() > 0) {
                Toast.makeText(this, "permission " + permissionDenied + " denied!", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void updateStudent() {
        if (!checkValidate())
        {
            Toast.makeText(this, "لطفا فیلد های ستاره دار را پر کنید", Toast.LENGTH_SHORT).show();
            return;
        }
        String baseUrl = ApiClient.BASE_URL;


        AndroidNetworking.upload(baseUrl + "student/" + stId + "/edit").
                addHeaders("Authorization", token).
                addMultipartFile("profile_url", avatarFile)
                .addMultipartParameter("username", unamev)
                .addMultipartParameter("email", emailv)
                .addMultipartParameter("name", namev)
                .addMultipartParameter("gender", String.valueOf(genderv))
                .addMultipartParameter("birthday", String.valueOf(birthdayTextv))
                .addMultipartParameter("field", fieldv)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                }).getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(EditprofileActivity.this, "success", Toast.LENGTH_SHORT).show();
                EditprofileActivity.this.setResult(RESULT_OK);
                EditprofileActivity.this.finish();
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(EditprofileActivity.this, "failure", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "onError: " + anError.getErrorBody());
                Log.i("TAG", "onError: " + anError.getMessage());
            }
        });

    }

    private boolean checkValidate() {
        boolean check=true;
        if (unamev.isEmpty())
        {
            check=false;
        }
        if (namev.isEmpty())
        {
            check=false;
        }
        return check;
    }

    private void setupDefaults() {
        ApiInterface apiInterface =ApiClient.getRetrofit().create(ApiInterface.class);
        Call<Student> call=apiInterface.getStPub(stId);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.body() != null) {
                    setupValues(response.body());
                }
            }



            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Toast.makeText(EditprofileActivity.this, "در اتصال به اینترنت خطایی رخ داده است", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setupValues(Student body) {
        userName.setText(body.getUsername());
        email.setText(body.getEmail());
        name.setText(body.getName());
        field.setText(body.getField());
        if (body.getBirthday()!=-1)
        {
            birthdayText.setTextColor(ContextCompat.getColor(EditprofileActivity.this, R.color.textDefaultColor));
            birthdayText.setText(String.valueOf(body.getBirthday()));
        }
        birthdayTextv=body.getBirthday();
        if (body.getGender()!=-1)
        {
            genderText.setTextColor(ContextCompat.getColor(EditprofileActivity.this, R.color.textDefaultColor));
        }
        if (genderv==1){
            genderText.setText("مرد");
        } else if (genderv == 0) {
            genderText.setText("زن");
        }
        genderv=body.getGender();
    }
    private void setupDialogBoxes() {

        gender.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditprofileActivity.this);
            builder.setTitle("سن").setItems(R.array.gender, (dialogInterface, i) -> {
                switch (i) {
                    case 0:
                        genderText.setText("مرد");
                        genderText.setTextColor(ContextCompat.getColor(EditprofileActivity.this, R.color.textDefaultColor));
                        genderv = 1;
                        break;
                    case 1:
                        genderText.setText("زن");
                        genderText.setTextColor(ContextCompat.getColor(EditprofileActivity.this, R.color.textDefaultColor));
                        genderv = 0;
                        break;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        age.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditprofileActivity.this);
            builder.setTitle("سن");
            builder.setItems(R.array.birthday, (dialogInterface, i) -> {

                birthdayText.setText(String.valueOf(1360 + i));
                birthdayText.setTextColor(ContextCompat.getColor(EditprofileActivity.this, R.color.textDefaultColor));
                birthdayTextv = 1360 + i;
            });


            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }

    private void setupEditTextFonts() {
        Typeface yekan = Typeface.createFromAsset(getAssets(), "fonts/yekan.ttf");

        userName.setTypeface(yekan);
        email.setTypeface(yekan);
        name.setTypeface(yekan);
    }
}
