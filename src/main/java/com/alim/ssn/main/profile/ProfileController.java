package com.alim.ssn.main.profile;


import com.alim.ssn.model.Post;
import com.alim.ssn.model.Student;
import com.alim.ssn.model.University;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileController {
    private ApiInterface apiInterface;
    ProfileController(){
        apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
    }

    void getStudentPosts(int stId, String token, OnGetPostsComplete onGetPostsComplete){
        Call<List<Post>> call=apiInterface.getStudentPosts(token, stId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call,Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    onGetPostsComplete.onSuccess(response.body());
                }else {
                    onGetPostsComplete.onError();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                onGetPostsComplete.onError();
            }
        });
    }
    void getStudent(int stId, OnGetStudentComplete onGetStudentComplete){

        Call<Student> call=apiInterface.getStudent(stId);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    onGetStudentComplete.onSuccess(response.body());
                }
                else {
                    onGetStudentComplete.onError();
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                    onGetStudentComplete.onError();
            }
        });
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
    void getUni(int uniCode, OnGetUniComplete onGetUniComplete)
    {
        Call<University> call=apiInterface.getUniversity(uniCode);
        call.enqueue(new Callback<University>() {
            @Override
            public void onResponse(Call<University> call, Response<University> response) {
                if (response.isSuccessful()) {
                    onGetUniComplete.onSuccess(response.body());
                }else {
                    onGetUniComplete.onFailure();
                }
            }

            @Override
            public void onFailure(Call<University> call, Throwable t) {
                onGetUniComplete.onFailure();
            }
        });
    }

    interface OnGetStudentPubComplete{
        void onSuccess(Student student);
        void onError();
    }
    interface OnGetStudentComplete{
        void onSuccess(Student student);
        void onError();
    }
    interface OnGetPostsComplete {
        void onSuccess(List<Post> posts);
        void onError();
    }
    interface OnGetUniComplete{
        void onSuccess(University university);
        void onFailure();
    }
}
