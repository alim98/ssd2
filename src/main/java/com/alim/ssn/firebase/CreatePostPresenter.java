/*
package com.alim.ssn.firebase;

import android.util.Log;
import android.widget.Toast;

import com.alim.ssn.webService.ApiClient;
import com.alim.ssn.webService.ApiInterface;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostPresenter implements CreatePostPresenterContainer {
    private CreatePost mView;
    
    public CreatePostPresenter(CreatePost view){
        this.mView=view;
      //  mAuth=FirebaseAuth.getInstance();
    }
    @Override
    public void saveInServer( String content, String field, String grade) {
        ApiInterface apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ResponseModel> call=apiInterface.storePost(getStudentId(), content, field, grade);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()){
                    mView.successfulMessage();
                }else {
                    mView.showErrorMessage();
                    Log.e("CREATE_POST", "onResponse: "+response.errorBody().toString() );
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                //Toast.makeText(CreatePost.this, "Error!"+t.toString(), Toast.LENGTH_LONG).show();
                mView.showErrorMessage();
                Log.e("CREATE_POST", "onFailure: "+t.toString() );
            }
        });
    }

    public String getStudentId(){
        // TODO: 10/20/19;

        return   "hello";
    }

}
*/
