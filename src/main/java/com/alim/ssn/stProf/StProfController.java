package com.alim.ssn.stProf;

import com.alim.ssn.main.profile.ProfileController;
import com.alim.ssn.model.Post;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StProfController {
    private final ApiInterface apiInterface;

    StProfController(){
        apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
    }

    void getStudentPosts(int stId, String token, OnGetPostsComplete onGetPostsComplete){
        Call<List<Post>> call=apiInterface.getStudentPosts(token, stId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
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

    interface OnGetPostsComplete {
        void onSuccess(List<Post> posts);
        void onError();
    }
}
