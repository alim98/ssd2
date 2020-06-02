package com.alim.ssn.main.profile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alim.ssn.R;
import com.alim.ssn.auth.AuthController;
import com.alim.ssn.auth.LoginActivity;
import com.alim.ssn.model.Post;
import com.alim.ssn.model.Student;
import com.alim.ssn.model.University;
import com.alim.ssn.studentProperties.Stid;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends Fragment  {
    private static final int EDIT_PRF_REQUEST_CODE = 1005;
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
    @BindView(R.id.fab_profile_edit)
    FloatingActionButton prfEdit;
    @BindView(R.id.tv_profile_collapsed_st_name)
    TextView collapsedStName;
    @BindView(R.id.srl_profile)
    SwipeRefreshLayout refreshLayout;
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
        ImageView menu=view.findViewById(R.id.iv_menu_profile);
        menu.setOnClickListener(view1 -> drawerLayout.openDrawer(GravityCompat.END)
);
        setstatusBarColor();
        setupToolbar();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_logout:
                        logout();
                        break;
                    case R.id.navigation_security_setting:
                        startActivity(new Intent(getActivity(), SecuritySetting.class));
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
        prfEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), EditprofileActivity.class);
                startActivityForResult(intent,EDIT_PRF_REQUEST_CODE);
            }
        });
        RelativeLayout relativeLayout=view.findViewById(R.id.rl_profile_toolbar);

        AppBarLayout appBarLayout=view.findViewById(R.id.app_bar33);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            boolean isShow=false;
            int scrollRange=-1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset ==0) {
                    isShow = true;
                    relativeLayout.setVisibility(View.VISIBLE);
                    refreshLayout.setEnabled(false);
                    refreshLayout.setRefreshing(false);

                } else if (isShow) {
                    isShow = false;
                    relativeLayout.setVisibility(View.GONE);


                }
                if (verticalOffset == 0) {
                    refreshLayout.setEnabled(true);
                }
//todo fitsisystemwindow bad work in start
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStudent();
            }
        });

        return  view;
    }

    private void setupToolbar() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==EDIT_PRF_REQUEST_CODE&&resultCode==RESULT_OK){
            getStudent();
        }
    }
    @OnClick(R.id.iv_menu_profile)
     void setupDrawer() {
drawerLayout.openDrawer(GravityCompat.END);
    }

    private void getStudent() {
        profileController.getStudentPub(stId, new ProfileController.OnGetStudentPubComplete() {
            @Override
            public void onSuccess(Student student) {
                mStudent=student;
                setupRecyclerView();
                setupProfile();
                refreshLayout.setRefreshing(false);

            }

            @Override
            public void onError() {
                Toast.makeText(getContext(), "ERRROOOOOR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupProfile() {
        TextView name=view.findViewById(R.id.tv_prof_st_name);
        collapsedStName.setText(mStudent.getName());
        ImageView profile=view.findViewById(R.id.iv_prf_img);
        TextView uni=view.findViewById(R.id.tv_prf_uni);
        TextView field=view.findViewById(R.id.tv_prf_field);

            name.setText(mStudent.getName());
        if (mStudent.getPhotoUrl() != null) {

            Picasso.get().load(mStudent.getPhotoUrl()).into(profile);
        }
        profileController.getUni(mStudent.getUniversity(), new ProfileController.OnGetUniComplete() {
            @Override
            public void onSuccess(University university) {
                if (university.getName()!=null){
                    uni.setVisibility(View.VISIBLE);
                    uni.setText(university.getName());
                }else {
                    uni.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "eroor", Toast.LENGTH_SHORT).show();
            }
        });

        followers.setText(String.valueOf(mStudent.getFollowersCount()));
        followings.setText(String.valueOf(mStudent.getFollowingsCount()));
        postsCount.setText(String.valueOf(mStudent.getPostsCount()));
        if (!mStudent.getField().isEmpty()){
            field.setVisibility(View.VISIBLE);
            field.setText(mStudent.getField());
        }else {
            field.setVisibility(View.GONE);
        }

    }
    private void setstatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
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
