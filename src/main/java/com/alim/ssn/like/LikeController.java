package com.alim.ssn.like;

import android.util.Log;

import com.alim.ssn.main.create.PostController;
import com.alim.ssn.model.StringResponseModel;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alim.ssn.main.home.GetNoticeIntractorImpl.TAG;

public class LikeController {
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private PostController postController = new PostController();

    public void isLiked(int stId, String postId, OnGetIsLiked onGetIsLiked) {
        Call<LikeModel> call = apiInterface.isLiked(stId, postId);
        call.enqueue(new Callback<LikeModel>() {
            @Override
            public void onResponse(Call<LikeModel> call, Response<LikeModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isLiked()) {
                        onGetIsLiked.onGet(true);
                    }
                } else {
                    Log.e(TAG, "onResponse: isLiked" + response.code());
                    onGetIsLiked.onGet(false);
                }
            }

            @Override
            public void onFailure(Call<LikeModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });


    }

    public void Like(String token , int stId, String postId, OnLikeComplete onLikeComplete) {
        Call<LikeModel> call = apiInterface.like(token, stId, postId);
        call.enqueue(new Callback<LikeModel>() {
            @Override
            public void onResponse(Call<LikeModel> call, Response<LikeModel> response) {
                if (response.isSuccessful()) {

                    onLikeComplete.onComplete();

                } else {
                    Log.e(TAG, "onResponse: error in like post, Error code:" + response.code());
                    onLikeComplete.onError(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<LikeModel> call, Throwable t) {
                Log.e(TAG, "onFailure: error in like post " + t.toString());
            }
        });
    }

    public void unLike(String token , int stId, String postId, OnUnlikeComplete onUnlikeComplete) {

        Call<LikeModel> unLike = apiInterface.unlike(token, stId, postId);
        unLike.enqueue(new Callback<LikeModel>() {
            @Override
            public void onResponse(Call<LikeModel> call, Response<LikeModel> response) {
                if (response.isSuccessful()) {
                   onUnlikeComplete.onComplete();
                } else {
                    Log.e(TAG, "onResponse: on unLike" + response.code());
                    onUnlikeComplete.onError();
                }
            }

            @Override
            public void onFailure(Call<LikeModel> call, Throwable t) {
                onUnlikeComplete.onError();
                Log.e(TAG, "onFailure: onUnlike"+t.toString() );
            }


        });
    }

    private void getLike(int stId, String postId, OnLikeIdGet onLikeComplete) {
        Call<StringResponseModel> likeModelCall = apiInterface.getLikeId(stId, postId);
        likeModelCall.enqueue(new Callback<StringResponseModel>() {
            @Override
            public void onResponse(Call<StringResponseModel> call, Response<StringResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    onLikeComplete.onComplete(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<StringResponseModel> call, Throwable t) {
                Log.i(TAG, "onFailure: onGetLike" + t.toString());
            }
        });
    }


    public interface OnLikeIdGet {
        void onComplete(String likeId);
    }

    public interface OnUnlikeComplete {
        void onComplete();

        void onError();
    }

    public interface OnGetIsLiked {
        void onGet(boolean isLiked);
    }

    public interface OnLikeComplete {
        void onComplete();

        void onError(String e);
    }
}
