package com.alim.ssn.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alim.ssn.R;
import com.alim.ssn.main.MainActivity;
import com.alim.ssn.studentProperties.Stid;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.et_phone_number_register)
    EditText phoneNumber;
    @BindView(R.id.et_name_register)
    EditText name;
    @BindView(R.id.et_user_name_register)
    EditText username;
    @BindView(R.id.et_password_register)
    EditText password;
    @BindView(R.id.tv_register_login)
    TextView login;
    //error texts
    @BindView(R.id.tv_register_empty_phone_error)
    TextView emptyPhoneError;
    @BindView(R.id.tv_register_empty_name_error)
    TextView emptyNameError;
    @BindView(R.id.tv_register_empty_user_name_error)
    TextView emptyUserNameError;
    @BindView(R.id.tv_register_empty_pass_error)
    TextView emptyPassError;
    @BindView(R.id.tv_register_short_pass_error)
    TextView shortPassError;
    @BindView(R.id.tv_register_free_user_name_text)
    TextView freeUsername;
    private Integer phonev;
    private String namev;
    private String usernamev;
    private String passwordv;
    private boolean doubleBackToExitPressedOnce=false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce)
            super.onBackPressed();

        doubleBackToExitPressedOnce=true;
        Toast.makeText(RegisterActivity.this, "برای خارج شدن از برنامه دوباره کلیک کنید", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setstatusBarColor();
        ButterKnife.bind(this);


    }

    @OnClick(R.id.btn_register)
    void registerPressed(){
    clearErrors();
        if (checkValidate()) {
            getValues();
            setContentView(R.layout.layout_phone_verification);
            Button register=findViewById(R.id.btn_verify);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    register();
                }
            });

        }
    }
//todo check is username free
private void setstatusBarColor() {
    if (Build.VERSION.SDK_INT >= 21) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }
}

    private void clearErrors() {
        shortPassError.setVisibility(View.INVISIBLE);
        emptyPassError.setVisibility(View.INVISIBLE);
        emptyUserNameError.setVisibility(View.INVISIBLE);
        emptyNameError.setVisibility(View.INVISIBLE);
        emptyPhoneError.setVisibility(View.INVISIBLE);

    }

    @OnClick(R.id.tv_register_login)
    void goToLoginPage(){
        this.finish();
        startActivity(new Intent(this, LoginActivity.class));

    }
    private void getValues() {
         phonev=Integer.valueOf(phoneNumber.getText().toString());
         namev=name.getText().toString();
         usernamev=username.getText().toString();
         passwordv=password.getText().toString();
    }

    private void register() {

        AuthController authController=new AuthController();

        authController.register(phonev, namev, usernamev, passwordv, new AuthController.OnRegisterComplete() {
            @Override
            public void onComplete(String token) {
                ((Stid)getApplication()).setStId(phonev);
                ((Stid)getApplication()).setToken(token);
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                RegisterActivity.this.finish();
            }

            @Override
            public void onFailure() {
                Toast.makeText(RegisterActivity.this, "Error in registering", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkValidate() {
        if (phoneNumber.getText().toString().isEmpty()){
            emptyPhoneError.setVisibility(View.VISIBLE);
        }
        if (name.getText().toString().isEmpty()){
            emptyNameError.setVisibility(View.VISIBLE);
        }
        if (username.getText().toString().isEmpty())
        {
            emptyUserNameError.setVisibility(View.VISIBLE);
        }
        if (password.getText().toString().isEmpty()) {
            emptyPassError.setVisibility(View.VISIBLE);
        }else if (password.getText().length()<6)
        {
            shortPassError.setVisibility(View.VISIBLE);
        }
        return !phoneNumber.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !username.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && password.getText().length() >= 6;

    }
}
