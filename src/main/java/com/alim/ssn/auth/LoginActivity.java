package com.alim.ssn.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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

import static com.alim.ssn.auth.AuthController.API_TOKEN_KEY;
import static com.alim.ssn.auth.AuthController.API_TOKEN_SH;
import static com.alim.ssn.auth.AuthController.PHONE_NUMBER_KEY;
import static com.alim.ssn.auth.AuthController.USER_API_TOKEN;

public class LoginActivity extends AppCompatActivity {
    AuthController controller;
    @BindView(R.id.et_user_name_login)
    EditText userName;
    @BindView(R.id.et_password_login)
    EditText password;
    private String apiToken;
    private int phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new AuthController();
        SharedPreferences sh = getSharedPreferences(API_TOKEN_SH, MODE_PRIVATE);
         apiToken = sh.getString(API_TOKEN_KEY, null);
         phoneNumber = sh.getInt(PHONE_NUMBER_KEY, 0);
        if (apiToken != null && phoneNumber!=0) {
            controller.checkToken("Bearer "+apiToken , phoneNumber, new AuthController.OnCheckTokenComplete() {
                @Override
                public void onComplete(boolean isValid) {

                    startApp(isValid);
                }
            });
        } else {
            startApp(false);
        }

    }

    private void startApp(boolean loggedIn) {
        if (loggedIn) {

            ((Stid)getApplication()).setStId(phoneNumber);
            ((Stid)getApplication()).setToken(apiToken);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USER_API_TOKEN, apiToken);
            startActivity(intent);
            this.finish();
        } else {
            setContentView(R.layout.activity_login);
            setstatusBarColor();
            ButterKnife.bind(this);
            Button login=findViewById(R.id.btn_login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginOnClick();
                }
            });

            TextView register=findViewById(R.id.tv_login_register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });
        }
    }

    private void setstatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    void loginOnClick(){
        int phoneNumberv=Integer.valueOf(userName.getText().toString());
        String passwordv=password.getText().toString();
        //todo check fiedls not null
        controller.login(phoneNumberv, passwordv, new AuthController.OnLoginComplete() {
            @Override
            public void onComplete(String token) {

                SharedPreferences sharedPreferences=getSharedPreferences(API_TOKEN_SH, MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt(PHONE_NUMBER_KEY, phoneNumberv);
                editor.putString(API_TOKEN_KEY, token);
                ((Stid)getApplication()).setStId(phoneNumberv);
                ((Stid)getApplication()).setToken(token);
                editor.apply();

                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure() {
                Toast.makeText(LoginActivity.this, "Error in login", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
