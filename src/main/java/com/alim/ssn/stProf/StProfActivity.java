package com.alim.ssn.stProf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alim.ssn.R;
import com.alim.ssn.model.Post;
import com.alim.ssn.model.Student;
import com.alim.ssn.model.SuccessModel;
import com.alim.ssn.model.University;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.alim.ssn.studentProperties.Stid;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StProfActivity extends AppCompatActivity {

    private static final String TAG = "StProfActivity";
    @BindView(R.id.tv_st_prf_activity_name)
    TextView flName;
    @BindView(R.id.btn_st_prf_follow)
    Button followBtn;
    @BindView(R.id.tv_st_prf_activity_uni)
    TextView uni;
    @BindView(R.id.iv_st_prf_activity_img)
    CircleImageView profileImage;
    @BindView(R.id.tv_st_followers_count_profile)
    TextView followers;
    @BindView(R.id.tv_st_followings_count_profile)
    TextView followings;
    @BindView(R.id.tv_st_posts_count_profile)
    TextView postsCount;
    @BindView(R.id.rv_st_prf_posts)
    RecyclerView recyclerView;
    public static final String FIRST_AND_LAST_NAME = "flName";
    public static final String PHONE_NUMBER = "phone_number";
    private int id;
    ApiInterface apiInterface;
    int alreadyFollowed = -1;
    private int stid;
    private String token;
    private String bearerToken;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st_prof);
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        stid = ((Stid) getApplication()).getStId();
        token = ((Stid) getApplication()).getToken();
        bearerToken = "Bearer " + token;

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String extra = intent.getStringExtra(FIRST_AND_LAST_NAME);
        id = intent.getIntExtra(PHONE_NUMBER, 0);
        firstTimeisFollowed(id);

        getStudent(id);

    }

    @OnClick(R.id.btn_st_prf_follow)
    void onFollowClicked() {
        if (alreadyFollowed == -1) {
            isFollowed(id, new OnCheckFollowComplete() {
                @Override
                public void onComplete(boolean isFollowed) {
                    if (isFollowed) {
                        unFollow(id);

                        followBtn.setText(R.string.unfollow);
                    } else {
                        follow(id);

                        followBtn.setText(R.string.follow);
                    }
                }
            });
        } else if (alreadyFollowed == 1) {
            unFollow(id);
        } else if (alreadyFollowed == 0) {
            follow(id);
        }
    }

    private void unFollow(int id) {
        Call<SuccessModel> call = apiInterface.unFollow(token, stid, id);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(StProfActivity.this, "UnFollowed", Toast.LENGTH_SHORT).show();
                        followBtn.setText(R.string.follow);
                        alreadyFollowed = 0;
                    } else {
                        Toast.makeText(StProfActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StProfActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                Log.e(TAG, "onFailure: StProfActivity,Follow: " + t.toString());
            }
        });
    }

    private void follow(int id) {
        Call<SuccessModel> call = apiInterface.follow(token , stid, id);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(StProfActivity.this, "Followed", Toast.LENGTH_SHORT).show();
                        followBtn.setText(R.string.unfollow);
                        alreadyFollowed = 1;
                    } else {
                        Toast.makeText(StProfActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StProfActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                Log.e(TAG, "onFailure: StProfActivity,Follow: " + t.toString());
            }
        });

    }

    private void getStudent(int stid) {
        Call<Student> call = apiInterface.getStPub(stid);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    student = response.body();
                    setupViews();
                    setupRecyclerView();
                } else {
                    Toast.makeText(StProfActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Toast.makeText(StProfActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        StProfController controller=new StProfController();
        controller.getStudentPosts(id, token, new StProfController.OnGetPostsComplete() {
            @Override
            public void onSuccess(List<Post> posts) {
                for (int i = 0; i < posts.size();i++) {
                    Timestamp createdAt;
                    try {
                        createdAt = convertCreatedAtToTimeStamp(posts.get(i).getCreated_at());
                        posts.get(i).setTsCreatedAt(createdAt);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                Collections.sort(posts, new Comparator<Post>() {
                    @Override
                    public int compare(Post o1, Post o2) {
                        return o2.getTsCreatedAt().compareTo(o1.getTsCreatedAt());
                    }
                });
                StProfAdapter adapter=new StProfAdapter(posts, StProfActivity.this,student);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError() {

            }
        });
    }

    private Timestamp convertCreatedAtToTimeStamp(String created_at) throws ParseException {

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date parseDate=dateFormat.parse(created_at);
        Timestamp timestamp=new Timestamp(parseDate.getTime());
        return timestamp;
    }

    private void setupViews() {
        flName.setText(student.getName() );

        if (student.getPhotoUrl() != null) {

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));

            imageLoader.displayImage(student.getPhotoUrl(), profileImage);
        }
        getUni(student.getUniversity(), new OnGetUniComplete() {
            @Override
            public void onSuccess(University university) {
                uni.setText(university.getName());
            }

            @Override
            public void onFailure() {

            }
        });
        followers.setText(String.valueOf(student.getFollowersCount()));
        followings.setText(String.valueOf(student.getFollowingsCount()));
        postsCount.setText(String.valueOf(student.getPostsCount()));
    }

    void getUni(int uniCode, OnGetUniComplete onGetUniComplete) {
        Call<University> call = apiInterface.getUniversity(uniCode);
        call.enqueue(new Callback<University>() {
            @Override
            public void onResponse(Call<University> call, Response<University> response) {
                if (response.isSuccessful()) {
                    onGetUniComplete.onSuccess(response.body());
                } else {
                    onGetUniComplete.onFailure();
                }
            }

            @Override
            public void onFailure(Call<University> call, Throwable t) {
                onGetUniComplete.onFailure();
            }
        });
    }

    private void isFollowed(int id, OnCheckFollowComplete onCheckFollowComplete) {
        Call<SuccessModel> call = apiInterface.isFollowed(stid, id);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isSuccess()) {
                        onCheckFollowComplete.onComplete(true);
                    } else {
                        onCheckFollowComplete.onComplete(false);
                    }
                } else {
                    Log.e(TAG, "onResponse: CheckFollowStProfActivity, Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                Log.e(TAG, "onFailure: CheckFollowStProfActivity " + t.toString());
            }
        });
    }

    private void firstTimeisFollowed(int id) {
        Call<SuccessModel> call = apiInterface.isFollowed(stid, id);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isSuccess()) {
                        followBtn.setText(R.string.unfollow);
                    } else {
                        followBtn.setText(R.string.follow);
                    }
                } else {
                    Log.e(TAG, "onResponse: CheckFollowStProfActivity, Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                Log.e(TAG, "onFailure: CheckFollowStProfActivity " + t.toString());
            }
        });
    }

    interface OnCheckFollowComplete {
        void onComplete(boolean isFollowed);
    }

    interface OnGetUniComplete {
        void onSuccess(University university);

        void onFailure();
    }
}
