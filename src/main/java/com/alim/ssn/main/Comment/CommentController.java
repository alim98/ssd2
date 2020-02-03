package com.alim.ssn.main.Comment;

import android.util.Log;

import com.alim.ssn.model.SuccessModel;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alim.ssn.main.home.GetNoticeIntractorImpl.TAG;

public class CommentController {
    ApiInterface apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
    public  void send(String token,int stId, String postId,String comment,  OnComment onComment)
    {
       Call<SuccessModel> call= apiInterface.comment(token, stId, postId, comment);
       call.enqueue(new Callback<SuccessModel>() {
           @Override
           public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
               if (response.isSuccessful()) {
                   onComment.onComplete();
               }else {
                   onComment.onError();
               }
           }

           @Override
           public void onFailure(Call<SuccessModel> call, Throwable t) {
               Log.e(TAG, "onFailure: OnComment: " +t.toString());
           }
       });
    }

    public void getComments(String postId,OnGetComments onGetComments)
    {
        Call<List<Comment>> call=apiInterface.getComments(postId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()){
                    onGetComments.onComplete(response.body());
                }else {
                    onGetComments.onError();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                onGetComments.onError();
            }
        });
    }

    public interface OnGetComments{
        void onComplete(List<Comment> comments);
        void onError();
    }
    public interface OnComment{
        void onComplete();
        void onError();
    }
}
