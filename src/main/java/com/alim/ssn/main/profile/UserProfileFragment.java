package com.alim.ssn.main.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alim.ssn.R;
import com.alim.ssn.auth.AuthController;
import com.alim.ssn.auth.LoginActivity;
import com.alim.ssn.main.home.GetNoticeIntractorImpl;
import com.alim.ssn.main.home.HomeFragment;
import com.alim.ssn.main.home.PresenterImpl;
import com.alim.ssn.model.Post;
import com.alim.ssn.model.Student;
import com.alim.ssn.model.University;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.alim.ssn.studentProperties.Stid;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment  {
   @BindView(R.id.tv_followers_count_profile)
    TextView followers;
    @BindView(R.id.tv_followings_count_profile)
    TextView followings;
    @BindView(R.id.tv_posts_count_profile)
    TextView postsCount;
    @BindView(R.id.rv_prf_posts)
    RecyclerView recyclerView;
    @BindView(R.id.dl_profile)
    DrawerLayout drawerLayout;
    @BindView(R.id.nv_profile)
    NavigationView navigationView;
    @BindView(R.id.iv_menu_profile)
    ImageView menu;
    private ProfileController profileController;
    private Student mStudent;
    private int stId;
    private View view;
    private String mToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=  inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
         stId=((Stid) Objects.requireNonNull(getContext()).getApplicationContext()).getStId();
        mToken = "Bearer " +((Stid) Objects.requireNonNull(getActivity()).getApplication()).getToken();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_logout:
                        logout();
                        break;
                    case R.id.navigation_saved_posts:
                        openSavedPostActivity();
                        break;
                }
                return true;
            }
        });
        profileController=new ProfileController();
         getStudent();
        setupDrawer();
        return  view;
    }

    private void setupDrawer() {
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }

    private void getStudent() {
        profileController.getStudentPub(stId, new ProfileController.OnGetStudentPubComplete() {
            @Override
            public void onSuccess(Student student) {
                mStudent=student;
                setupRecyclerView();
                setupProfile();
            }

            @Override
            public void onError() {
                Toast.makeText(getContext(), "ERRROOOOOR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupProfile() {
        TextView name=view.findViewById(R.id.tv_prof_st_name);
        ImageView profile=view.findViewById(R.id.iv_prf_img);
        TextView uni=view.findViewById(R.id.tv_prf_uni);
        if (mStudent.getLast_name() != null) {
            name.setText(mStudent.getName()+" "+mStudent.getLast_name());
        }else {
            name.setText(mStudent.getName());
        }
        Picasso.get().load(mStudent.getPhotoUrl()).into(profile);
        profileController.getUni(mStudent.getUniversity(), new ProfileController.OnGetUniComplete() {
            @Override
            public void onSuccess(University university) {
                uni.setText(university.getName());
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "eroor", Toast.LENGTH_SHORT).show();
            }
        });

        followers.setText(String.valueOf(mStudent.getFollowersCount()));
        followings.setText(String.valueOf(mStudent.getFollowingsCount()));
        postsCount.setText(String.valueOf(mStudent.getPostsCount()));
    }

    private void setupRecyclerView() {


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        String token=((Stid) Objects.requireNonNull(getContext()).getApplicationContext()).getToken();
        String bearerToken="Bearer "+ token;
        profileController.getStudentPosts(stId, bearerToken, new ProfileController.OnGetPostsComplete() {
            @Override
            public void onSuccess(List<Post> posts) {
                for (int i = 0; i < posts.size();i++) {
                    try {
                      Timestamp createdAt=  convertCreatedAtToTimeStamp(posts.get(i).getCreated_at());
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
                ProfileAdapter adapter=new ProfileAdapter(posts,getContext(), mStudent );
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



    private void openSavedPostActivity() {
        Intent intent=new Intent(getActivity(), SavedPostActivity.class);
        startActivity(intent);
    }

    private void logout() {
        AuthController authController = new AuthController();
        int stId = ((Stid) Objects.requireNonNull(getActivity()).getApplication()).getStId();
        authController.logout(stId, mToken, new AuthController.OnlogoutComplete() {
            @Override
            public void onComplete() {
                startActivity(new Intent(getContext(), LoginActivity.class));
                Objects.requireNonNull(getActivity()).finish();

            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Error in signout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnPostsGet{
        void onGet(List<Post> posts);
    }
   /* @OnClick(R.id.tv_following_profile)
    public void followingsShow(){
        Intent intent=new Intent(getActivity(), Followings.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_follower_profile)
    public void followersShow(){
        Intent intent=new Intent(getActivity(), Followers.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_about_us)
    void aboutUs(){
        startActivity(new Intent(getActivity() , AboutUs.class));
    }*/

}
