package com.alim.ssn.main.create;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alim.ssn.newWebService.ApiClient;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONObject;

import java.io.File;

public class CreatePostPresenterImpl implements CreatePostPresenter{
    private static final String TAG="createPost";
    @Override
    public void savePostInServer(String token, Context context, String desc, int studentId, int size, File postImage, String tags , OnSavePostComplete onSavePostComplete) {
        String BASE_URL= ApiClient.BASE_URL;
        AndroidNetworking.upload(BASE_URL+"post")
                .addHeaders("Authorization", token)
                .addMultipartFile("profile_url", postImage)
                .addMultipartParameter("desc", desc)
                .addMultipartParameter("student_id", String.valueOf(studentId))
           /*     .addMultipartParameter("grade", grade)
                .addMultipartParameter("major", major)*/
                .addMultipartParameter("size", String.valueOf(size))
                .addMultipartParameter("tags", tags)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                }).getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Post Created successfully", Toast.LENGTH_SHORT).show();
                    onSavePostComplete.onSuccess();
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(context, "Error in create post!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onError: "+anError.getErrorDetail() );
            }
        });
    }




   public interface OnSavePostComplete{
        void onSuccess();
    }
}
