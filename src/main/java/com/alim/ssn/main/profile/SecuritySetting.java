package com.alim.ssn.main.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alim.ssn.R;
import com.alim.ssn.model.StringResponseModel;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.alim.ssn.studentProperties.Stid;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class SecuritySetting extends AppCompatActivity {
    @BindView(R.id.et_security_setting_old_pass)
    EditText oldPass;
    @BindView(R.id.et_security_setting_new_pass)
    EditText newPass;
    @BindView(R.id.et_security_setting_confirm_pass)
    EditText confirmPass;

    String oldPassv;//old password value
    String newPassv;
    String confirmPassv;

    //error
    @BindView(R.id.tv_security_setting_old_pass_empty_error)
    TextView oldPassEmpty;
    @BindView(R.id.tv_security_setting_confirm_do_not_match_error)
    TextView doNotMatchError;
    @BindView(R.id.tv_security_setting_short_pass)
    TextView shortPassError;
    @BindView(R.id.tv_security_setting_incorrect_old_pass)
    TextView incorrectPass;
    private int stId;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_setting);
        ButterKnife.bind(this);
        setstatusBarColor();
        stId=((Stid) getApplicationContext()).getStId();
        mToken = "Bearer " +((Stid) getApplicationContext()).getToken();
        setupViews();
    }

    @OnClick(R.id.fab_security_setting_done)
    void changePassword(){
        getValues();

        clearErrors();
        if (checkValidate())
        {
            ApiInterface apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
            Call<StringResponseModel> call=apiInterface.changePassword(mToken, stId,oldPassv,newPassv);
            call.enqueue(new Callback<StringResponseModel>() {
                @Override
                public void onResponse(Call<StringResponseModel> call, Response<StringResponseModel> response) {
                    if (response.isSuccessful())
                    {
                        Toast.makeText(SecuritySetting.this, "رمز با موفقیت تغییر یافت", Toast.LENGTH_SHORT).show();
                        SecuritySetting.this.finish();
                    }else if (response.code()==422)
                    {
                        Toast.makeText(SecuritySetting.this, "رمز قبلی اشتباه است", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<StringResponseModel> call, Throwable t) {
                    Toast.makeText(SecuritySetting.this, "مشکل در ارتباط با اینترنت", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.iv_security_setting_back_button)
    void onBackClicked(){
        SecuritySetting.this.finish();
    }
    private void clearErrors() {
        oldPassEmpty.setVisibility(View.INVISIBLE);
        shortPassError.setVisibility(View.INVISIBLE);
        doNotMatchError.setVisibility(View.INVISIBLE);
        incorrectPass.setVisibility(View.INVISIBLE);
    }
    private void setstatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
    private boolean checkValidate() {


        if (oldPassv==null){

            oldPassEmpty.setVisibility(View.VISIBLE);
            return false;
        }else if (newPassv.length()<6)
        {
            shortPassError.setVisibility(View.VISIBLE);
            return false;
        }
        else if(!newPassv.equals(confirmPassv))
        {
            doNotMatchError.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private void setupViews() {

    }

    private void getValues() {
        oldPassv=oldPass.getText().toString();
        newPassv=newPass.getText().toString();
        confirmPassv=confirmPass.getText().toString();
    }
}
