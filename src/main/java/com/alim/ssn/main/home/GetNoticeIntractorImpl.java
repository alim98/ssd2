package com.alim.ssn.main.home;

import android.content.Context;
import android.util.Log;


import com.alim.ssn.model.Post;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.alim.ssn.studentProperties.Stid;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class GetNoticeIntractorImpl implements MainContract.GetNoticeIntractor {
    public static final String TAG="webserviceError";
    @Override
    public void GetNoticedArrayList(OnFinishedListener onFinishedListener, Context context) {

//createFakePostsWithRxJava(onFinishedListener);
    GetRealPostsFromServer(onFinishedListener, context);

    }


    private void GetRealPostsFromServer(OnFinishedListener onFinishedListener, Context context) {
        ApiInterface apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
        int stId=((Stid)context.getApplicationContext()).getStId();
        String token=((Stid)context.getApplicationContext()).getToken();
        token="Bearer "+token;
        Call<List<Post>> call=apiInterface.getPostsOfFollowings(token, stId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        onFinishedListener.onFinish(response.body());
                    }else {
                        onFinishedListener.onFailure(new Throwable("posts is null"));
                        Log.i(TAG, "onResponse: posts is null");
                    }

                }else {
                    onFinishedListener.onFailure(new Throwable("bad response " +response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString() );
                onFinishedListener.onFailure(new Throwable(t));
            }
        });
    }


}
