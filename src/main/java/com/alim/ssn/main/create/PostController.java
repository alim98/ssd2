package com.alim.ssn.main.create;

import android.util.Log;
import android.widget.Toast;

import com.alim.ssn.main.home.PostAdapter;
import com.alim.ssn.model.Post;
import com.alim.ssn.model.StringResponseModel;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alim.ssn.main.home.GetNoticeIntractorImpl.TAG;

public class PostController {
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);





    public void getPost(String postId, OnGetPost onGetPost) {
        Call<Post> postCall = apiInterface.getPost(postId);
        postCall.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    onGetPost.onComplete(response.body());

                } else {
                    Log.e(TAG, "onResponse: on Get Post, PostController" + response.code());
                }

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                //todo if this is failure so delete like from table
            }
        });
    }

    interface OnGetPost {
        void onComplete(Post post);
    }

    public interface OnInCreaseOrDecreaseLike {
        void onComplete(int currentLikesCount);
    }
}
