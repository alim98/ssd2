package com.alim.ssn.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Integer phonev;
    private String namev;
    private String usernamev;
    private String passwordv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.btn_register)
    void registerPressed(){
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
        return phoneNumber != null && name != null && username != null && password != null && password.getText().length() >= 6;

    }
}
