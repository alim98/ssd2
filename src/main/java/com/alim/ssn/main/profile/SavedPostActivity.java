package com.alim.ssn.main.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import com.alim.ssn.R;
import com.alim.ssn.main.home.PostAdapter;
import com.alim.ssn.model.Post;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.alim.ssn.studentProperties.Stid;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedPostActivity extends AppCompatActivity {
    @BindView(R.id.rv_saved_posts)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_post);
        ButterKnife.bind(this);
        int stId = ((Stid) getApplication()).getStId();
        String token ="Bearer "+ ((Stid) getApplication()).getToken();
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<List<Post>> call = apiInterface.getSavedPosts(token, stId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    PostAdapter adapter=new PostAdapter( response.body(), SavedPostActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SavedPostActivity.this,RecyclerView.VERTICAL,false));
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }
}
