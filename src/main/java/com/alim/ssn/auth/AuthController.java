package com.alim.ssn.auth;

import android.util.Log;

import com.alim.ssn.model.StringResponseModel;
import com.alim.ssn.model.SuccessModel;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AuthController {
    private ApiInterface apiInterface;
    static final String API_TOKEN_SH = "apitokensh";
    static final String API_TOKEN_KEY = "apitokenkey";
    static final String PHONE_NUMBER_KEY = "phonenumberkey";
    public static final String USER_API_TOKEN = "userapitoken";

    public AuthController() {
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    }

    void checkToken(String token, int phoneNumber, OnCheckTokenComplete onCheckTokenComplete) {
        Call<SuccessModel> call = apiInterface.checkToken(token, phoneNumber);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isSuccess()) {
                        onCheckTokenComplete.onComplete(true);
                    } else {
                        onCheckTokenComplete.onComplete(false);
                    }
                } else {
                    Log.e(TAG, "onResponse: AuthController" + response.code());
                    onCheckTokenComplete.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                Log.e(TAG, "onFailure: AuthController" + t.toString());
            }
        });
    }

    void login(int stId, String postId, OnLoginComplete onLoginComplete) {
        Call<StringResponseModel> call = apiInterface.login(stId, postId);
        call.enqueue(new Callback<StringResponseModel>() {
            @Override
            public void onResponse(Call<StringResponseModel> call, Response<StringResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    onLoginComplete.onComplete(response.body().getResponse());
                } else {
                    Log.e(TAG, "onResponse: AuthController" + response.code());

                }
            }

            @Override
            public void onFailure(Call<StringResponseModel> call, Throwable t) {
                Log.e(TAG, "onFailure: AuthController" + t.toString());
                onLoginComplete.onFailure();
            }
        });
    }

    public void logout(int stId, String apiToken, OnlogoutComplete onlogoutComplete) {
        Call<StringResponseModel> call=apiInterface.logout(apiToken, stId);
        call.enqueue(new Callback<StringResponseModel>() {
            @Override
            public void onResponse(Call<StringResponseModel> call, Response<StringResponseModel> response) {
                if (response.isSuccessful()) {
                    onlogoutComplete.onComplete();
                }else {
                    onlogoutComplete.onFailure();
                    Log.e(TAG, "onResponse: AuthController" + response.code());

                }
            }

            @Override
            public void onFailure(Call<StringResponseModel> call, Throwable t) {
                Log.e(TAG, "onFailure: AuthController" + t.toString());
            onlogoutComplete.onFailure();
            }
        });
    }

    void register(int stId, String name, String username, String password, OnRegisterComplete onRegisterComplete){
        Call<StringResponseModel> call=apiInterface.register(stId, name, username, password);
        call.enqueue(new Callback<StringResponseModel>() {
            @Override
            public void onResponse(Call<StringResponseModel> call, Response<StringResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    onRegisterComplete.onComplete(response.body().getResponse());
                }else {
                    onRegisterComplete.onFailure();
                    Log.e(TAG, "onResponse: AuthController" + response.code());
                }
            }

            @Override
            public void onFailure(Call<StringResponseModel> call, Throwable t) {
                Log.e(TAG, "onFailure: AuthController" + t.toString());
                onRegisterComplete.onFailure();
            }
        });
    }

    interface OnCheckTokenComplete {
        void onComplete(boolean isValid);
    }

    interface OnLoginComplete {
        void onComplete(String token);

        void onFailure();
    }

    public interface OnlogoutComplete {
        void onComplete();

        void onFailure();
    }

    public interface OnRegisterComplete{
        void onComplete(String token);
        void onFailure();
    }
}
