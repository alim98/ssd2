package com.alim.ssn.main.adapter;

import android.util.Log;

import com.alim.ssn.main.MainActivity;
import com.alim.ssn.main.Posts;
import com.alim.ssn.main.PostsPresenterContainer;
import com.alim.ssn.model.Post;
import com.alim.ssn.webService.ApiClient;
import com.alim.ssn.webService.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsPresenter implements PostsPresenterContainer {
   private MainActivity mView;

    public PostsPresenter(MainActivity mView) {
        this.mView = mView;
    }

    @Override
    public void getPostsFromServer() {
        ApiInterface apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
        Call<Posts> call=apiInterface.getPosts("bXfY1FAhtReWuHpXrEnygbUt7Ll1");
        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if (response.isSuccessful()){
                    onSuccess(response.body().getPosts());
                }else {
                    onGetPostFailure();
                    Log.e("Retrofit", "onResponse: "+response.errorBody().toString() );
                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                onGetPostFailure();
                Log.e("Retrofit", "onFailure: "+t.toString() );
            }
        });

    }

    @Override
    public void onSuccess(List<Post> posts) {
        mView.onGetDataSuccess(posts);
    }

    @Override
    public void onGetPostFailure() {
        mView.onGetDataFailure();
    }




}
