package com.alim.ssn.main.home;

import android.widget.Toast;

import com.alim.ssn.main.profile.ProfileController;
import com.alim.ssn.model.Student;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentController {
    ApiInterface apiInterface;

    StudentController(){
        apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
    }

    void getStudentPub(int stid, OnGetStudentPubComplete onGetStudentPubComplete)
    {
        Call<Student> call=apiInterface.getStPub(stid);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    onGetStudentPubComplete.onSuccess(response.body());
                }
                else {
                    onGetStudentPubComplete.onError();
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                onGetStudentPubComplete.onError();
            }
        });
    }
    interface OnGetStudentPubComplete{
        void onSuccess(Student student);
        void onError();
    }
}
