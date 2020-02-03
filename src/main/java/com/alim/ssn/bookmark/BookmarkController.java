package com.alim.ssn.bookmark;

import android.util.Log;

import com.alim.ssn.model.Post;
import com.alim.ssn.model.SuccessModel;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.alim.ssn.studentProperties.Stid;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BookmarkController {
    private ApiInterface apiInterface;
    public BookmarkController(){
        apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
    }
    public void save(String token, int stId, String postId, OnSaveComplete onSaveComplete){

        Call<SuccessModel> call=apiInterface.savePost(token, stId, postId);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                if (response.isSuccessful()) {
                    onSaveComplete.onComplete();
                }else {
                    onSaveComplete.onFailure();
                    Log.e(TAG, "onResponse: BookmarkController"+response.code() );
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                onSaveComplete.onFailure();
                Log.e(TAG, "onFailure: BookmarkController:"+t.toString() );
            }
        });
    }

    public void deleteSavedPost(String token,int stId,  String postId, OnDeleteComplete onDeleteComplete){
        Call<SuccessModel> call=apiInterface.deleteSavedPost(token,stId,  postId);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                if (response.isSuccessful()) {
                    onDeleteComplete.onComplete();
                }
                else {
                    Log.e(TAG, "onResponse: BookmarkController"+response.code() );
                    onDeleteComplete.onFailure();
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                Log.e(TAG, "onFailure: BookmarkController:"+t.toString() );
                onDeleteComplete.onFailure();
            }
        });
    }

    public void getPosts(String token, int stId, OnGetPostsComplete onGetPostsComplete){
        Call<List<Post>> call=apiInterface.getSavedPosts(token, stId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    onGetPostsComplete.onComplete(response.body());
                }else {
                    onGetPostsComplete.onFailure();
                    Log.e(TAG, "onResponse: BookmarkController"+response.code() );
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e(TAG, "onFailure: BookmarkController:"+t.toString() );
                onGetPostsComplete.onFailure();
            }
        });

    }
    public void check(String token , int stId, String postId, OnCheckController onCheckController){
        Call<SuccessModel> call=apiInterface.checkSaved(token, stId, postId);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                if (response.isSuccessful()) {
                    onCheckController.onComplete(true);
                }else {
                    onCheckController.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                onCheckController.onFailure();
                Log.e(TAG, "onFailure: BookmarkController:"+t.toString() );
            }
        });
    }

    public  interface OnSaveComplete{
        void onComplete();
        void onFailure();
    }

    public interface OnDeleteComplete{
        void onComplete();
        void onFailure();
    }
    public interface OnGetPostsComplete{
        void onComplete(List<Post> posts);
        void onFailure();
    }
   public interface OnCheckController{
        void onComplete(boolean isSaved);
        void onFailure();
    }
}
