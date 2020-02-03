package com.alim.ssn.main;


import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.alim.ssn.R;
import com.alim.ssn.main.adapter.PostsAdapter;
import com.alim.ssn.main.adapter.PostsPresenter;
import com.alim.ssn.main.home.HomeFragment;
import com.alim.ssn.main.profile.UserProfileFragment;
import com.alim.ssn.main.recents.FavoriteFragment;
import com.alim.ssn.main.search.SearchFragment;
import com.alim.ssn.model.Post;
import com.alim.ssn.test.FakeStudentGenerator;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.List;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PostsViewContainer {
//    public static final String ACTION_USER_TOKEN = "action_user_token";
    PostsAdapter adapter;

    @BindView(R.id.navigation_view)
    BottomNavigationView bnv;
    /*
    public static final String TOKEN_PREF_NAME = "token_pref_name_200";
    public static final String TOKEN_PREF_KEY = "token_pref_key_201";*/
    PostsPresenter mPresenter;
    final Fragment home = new HomeFragment(this);
    final Fragment profile = new UserProfileFragment();
    final Fragment searchFragment = new SearchFragment();
    final Fragment recentsFragment = new FavoriteFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = home;


    private String token;
    private boolean flagForCheckToken;
    public static Context contextOfApplication;//for use getcontextresolver in fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm.beginTransaction().add(R.id.main_activity_container, recentsFragment,"4").hide(recentsFragment).commit();
        fm.beginTransaction().add(R.id.main_activity_container, searchFragment, "2").hide(searchFragment).commit();
        fm.beginTransaction().add(R.id.main_activity_container, profile, "5").hide(profile).commit();
        fm.beginTransaction().add(R.id.main_activity_container, home, "1").commit();



        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();//for use getcontextresolver in fragment

        ButterKnife.bind(this);
        setupViews();
        mPresenter = new PostsPresenter(this);

        mPresenter.getPostsFromServer();



        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.navigation_home:

                        fm.beginTransaction().hide(active).show(home).commit();
                        active=home;
                        return true;

                    case R.id.navigation_search:
                        fm.beginTransaction().hide(active).show(searchFragment).commit();
                        active=searchFragment;
                        return true;
                    /*case R.id.navigation_create:
                        fm.beginTransaction().hide(active).show(createPostFragment).commit();
                        active=createPostFragment;
                        return true;*/
                    case R.id.navigation_recents:
                        fm.beginTransaction().hide(active).show(recentsFragment).commit();
                        active=recentsFragment;
                        return true;
                    case R.id.navigation_profile:
                        fm.beginTransaction().hide(active).show(profile).commit();
                        active=profile;
                        return true;
                }
                return false;
            }
        });
        BadgeDrawable badgeDrawable=bnv.getOrCreateBadge(R.id.navigation_search);
        badgeDrawable.setNumber(999);
        bnv.setSelectedItemId(R.id.navigation_home);

    }

    private void openFragment(Fragment fragment, String backStackName) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.main_activity_container, fragment);
        transaction.addToBackStack(backStackName);
        transaction.commit();
    }


    private void setupViews() {

        //   Bundle getStudentName=getIntent().getExtras();
        //   assert getStudentName != null;
        //   String studentNameValue=getStudentName.getString(STUDENT_NAME);
        //   studentInfo.setText(studentNameValue);

    }


    private void checkRegistered() {
       /* SharedPreferences pref = getSharedPreferences(TOKEN_PREF_NAME, MODE_PRIVATE);
        String checkToken = pref.getString(TOKEN_PREF_KEY, "null");
        if (!checkToken.equals("null")) {
            checkLogin(checkToken);

        } else {
            Intent intent = new Intent(this, RegisterOrLoginActivity.class);
            startActivity(intent);
            this.finish();
        }*/
    }

   /* private boolean checkLogin(String checkToken) {
        flagForCheckToken = false;

        Call<String> call = apiInterface.checkToken("Bearer " + checkToken);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    flagForCheckToken = true;

                } else {
                    flagForCheckToken = false;
                    checkRegistered();

                    Intent intent = new Intent(MainActivity.this, RegisterOrLoginActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                flagForCheckToken = false;
            }
        });
        return flagForCheckToken;
    }*/


  /*  private List<Post> getPostFireBase(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("post");
        reference.keepSynced(true);
        final List<Post> posts=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              *//*  Iterator<DataSnapshot> dataSnapshots=dataSnapshot.getChildren().iterator();

                while (dataSnapshots.hasNext()){
                    DataSnapshot dataSnapshotChild=dataSnapshots.next();
                    Post post=dataSnapshotChild.getValue(Post.class);
                    posts.add(post);
                }*//*
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Post post=postSnapshot.getValue(Post.class);
                    posts.add(post);
                }
                //setupRecyclerView(posts, FakeStudentGenerator.getStudents());
                if (!alreadyExecuted){

                    alreadyExecuted=true;
                }

                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return posts;
    }*/

   /* private List<Student> getStudents() {
        ApiInterface apiInterface=ApiClient.getRetrofit().create(ApiInterface.class);
        Call<List<Student>> call=apiInterface.getStudents();
        final List<Student> students = null;
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful()){
                    students.addAll(response.body());
                }
                Log.e("retrofitError", "onResponse: "+response.errorBody().toString() );
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.e("students", "onFailure: "+t.toString() );
            }
        });
        return students;
    }*/


    @Override
    public void onGetDataSuccess(List<Post> posts) {
        adapter = new PostsAdapter(this, posts, FakeStudentGenerator.getStudents());
        //Todo move to home fragment  setupRecyclerView( );
    }

    @Override
    public void onGetDataFailure() {
        Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
    }
   /* private List<Post> getStudentPosts(int id) {
        ApiInterface apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
        Call<List<Post>> call;
        call = apiInterface.getPosts(id);
         final List<Post> mPosts =new ArrayList<>() ;
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()){
                    Log.i("retrofitResponse", "onResponse: "+response.body().get(0).getContent());
                    Toast.makeText(MainActivity.this, "Ok", Toast.LENGTH_LONG).show();
                   mPosts.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("Retrofit", "onFailure: "+t.toString() );

            }
        });
        return mPosts;
    }*/
}
