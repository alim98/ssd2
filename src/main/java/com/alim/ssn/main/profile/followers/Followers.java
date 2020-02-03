package com.alim.ssn.main.profile.followers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.alim.ssn.R;
import com.alim.ssn.main.profile.FollowModel;
import com.alim.ssn.model.Student;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Followers extends AppCompatActivity {
    @BindView(R.id.rv_followers_list)
    RecyclerView recyclerView;
    private static final String TAG = "followers";
    private int stId = 148136410;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        ButterKnife.bind(this);
        getFollowers();



    }

    private void setupRecyclerView(List<Student> students) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        FollowersAdapter adapter=new FollowersAdapter(students, this);
        recyclerView.setAdapter(adapter);
    }

    private List<Student> getStudents(List<Integer> followers, OnFollowersComplete onFollowersComplete) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < followers.size(); i++) {
            Call<Student> call = apiInterface.getStudent(followers.get(i));
            int finalI = i;
            call.enqueue(new Callback<Student>() {
                @Override
                public void onResponse(Call<Student> call, Response<Student> response) {
                    if (response.isSuccessful()) {
                        students.add(response.body());
                        if(finalI ==followers.size()-1){
                            onFollowersComplete.onComplete(students);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Student> call, Throwable t) {
                    Log.e(TAG, "onFailure: getFollowers" + t.toString());
                }
            });
        }
        //    Log.i(TAG, "getStudents: "+students.get(1));
        return students;
    }

    private List<Integer> getFollowers() {
        List<Integer> followersId = new ArrayList<>();
        Call<List<FollowModel>> call = apiInterface.getFollowers(stId);
        call.enqueue(new Callback<List<FollowModel>>() {
            @Override
            public void onResponse(Call<List<FollowModel>> call, Response<List<FollowModel>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    for (int i = 0; i < response.body().size(); i++) {
                        followersId.add(response.body().get(i).getFollowerId());
                    }
                    List<Student> followers = getStudents(followersId, new OnFollowersComplete() {
                        @Override
                        public void onComplete(List<Student> students) {
                            setupRecyclerView(students);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<FollowModel>> call, Throwable t) {

            }
        });
        return followersId;
    }
}
interface OnFollowersComplete{
    void onComplete(List<Student> students);
}