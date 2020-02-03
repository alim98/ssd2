package com.alim.ssn.controller;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alim.ssn.R;
import com.alim.ssn.firebase.ResponseModel;
import com.alim.ssn.model.Like;
import com.alim.ssn.model.Post;
import com.alim.ssn.webService.ApiClient;
import com.alim.ssn.webService.ApiInterface;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class LikeController {

    private Context context;
    private Post post;
    private ImageView likesImageView;
    private boolean isLiked;
    private TextView likeCounterTextView;

    long prevLikeCount;
    private ApiInterface apiInterface;
    public LikeController(Context context, Post post, ImageView likesImageView, TextView likeCounterTextView) {
        this.context = context;
        this.post = post;
        this.likesImageView = likesImageView;
        this.likeCounterTextView = likeCounterTextView;

        apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);

    }

    public void addLike() {
        post.setLikesCount(post.getLikesCount()+1);
        likeCounterTextView.setText(String.valueOf(post.getLikesCount()));

        isLiked = true;
        likesImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.test_liked));


        /*Like like = new Like(mAuth.getCurrentUser().getUid(), post.getId());
        like.setId(mLikeRef.push().getKey());
        mLikeRef.child(like.getId()).setValue(like);
*/
        Call<ResponseModel>  call= apiInterface.updatePostLikesCount(post.getId(), post.getLikesCount());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e(TAG, "onFailure: error in add like"+t.toString() );
            }
        });

//        addLikeToLikesTable(mAuth.getCurrentUser().getUid(), post.getId());

    }


/*    public long findpPostLikeCount(String postId){
        DatabaseReference reference=mDatabase.getReference("post");

        reference.child(postId).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        Post post=snapshot1.getValue(Post.class);
                        prevLikeCount=post.getLikesCount();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return prevLikeCount;
    }*/

    public void removeLike() {
        post.setLikesCount(post.getLikesCount()-1);
        likeCounterTextView.setText(String.valueOf(post.getLikesCount()));
        isLiked = false;
        likesImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.test_like));

        apiInterface=ApiClient.getRetrofit().create(ApiInterface.class);

        Call<ResponseModel> call=apiInterface.updatePostLikesCount(post.getId(), post.getLikesCount());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e(TAG, "onFailure: error in remove like from database"+t.toString() );
            }
        });
    }

    public void initLike(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public void likeAction( ) {
        if (!isLiked) {
            addLike();
        } else {
            removeLike();
        }
    }
    public void addLikeToLikesTable(String studentId, int postId){
        apiInterface=ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ResponseModel> call=apiInterface.addLikeToLikesTable(studentId, postId);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "onResponse: "+response.errorBody().toString()+" "+response.code() );
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
            }
        });
    }
}

