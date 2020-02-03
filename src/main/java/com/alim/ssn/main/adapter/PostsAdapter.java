package com.alim.ssn.main.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alim.ssn.R;
import com.alim.ssn.controller.LikeController;
import com.alim.ssn.controller.LikeExistsModel;
import com.alim.ssn.model.Post;
import com.alim.ssn.model.Student;

import com.alim.ssn.webService.ApiClient;
import com.alim.ssn.webService.ApiInterface;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsVH> {

    private Context context;
    private List<Post> posts;
    private List<Student> students;
    private LikeController controller;
//    private FirebaseDatabase mDatabase;

//    private FirebaseAuth mAuth;
    private long count;
    private ApiInterface apiInterface;
     boolean isExists;
    public PostsAdapter(Context context, List<Post> posts, List<Student> students) {
        this.context = context;
        this.posts = posts;
        this.students = students;
    }

    @Override
    public PostsVH onCreateViewHolder(ViewGroup parent, int viewType) {
//        mDatabase=FirebaseDatabase.getInstance();

//        mAuth=FirebaseAuth.getInstance();

        View view= LayoutInflater.from(context).inflate(R.layout.layout_post_item, parent, false);

        return new PostsVH(view);
    }

    @Override
    public void onBindViewHolder(final PostsVH holder, int position) {
        final Post post=posts.get(position);

                //holder.authorName.setText(findStudentName(Integer.valueOf(post.getStudentId())));
                holder.content.setText(posts.get(position).getContent());
                holder.likesCount.setText(String.valueOf(post.getLikesCount()));
                Picasso.get().load("https://www.automatetheplanet.com/wp-content/uploads/2015/06/Test-URL-Redirects-WebDriver.jpg").into(holder.image);
                Picasso.get().load("https://dornsife.usc.edu/tools/mytools/PersonnelInfoSystem/DOC/Student/PHIL/photo_1078846.jpg").into(holder.profilePic);
//                likeExists(post.getId(), mAuth.getCurrentUser().getUid(), holder.likeIcon);
                holder.likeIcon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        controller=new LikeController(context, post, holder.likeIcon, holder.likesCount);
                        if (holder.likeIcon.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.test_like).getConstantState())){
                            controller.initLike(false);
                        }else {
                            controller.initLike(true);
                        }
                        controller.likeAction();
                    }
                });




        }


    public void likeExists(final int postId, final String studentId, final ImageView likeHolder) {
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<LikeExistsModel> call = apiInterface.likeExists(postId, studentId);

        call.enqueue(new Callback<LikeExistsModel>() {
            @Override
            public void onResponse(Call<LikeExistsModel> call, Response<LikeExistsModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getLikeExists() == 1) {
                        likeHolder.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.test_liked));
                        Log.i(TAG, "onResponse: like exists"+postId+" "+studentId);
                    } else {
                        likeHolder.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.test_like));
                        Log.i(TAG, "onResponse: like not exists"+postId+" "+studentId);

                    }
                }
            }

            @Override
            public void onFailure(Call<LikeExistsModel> call, Throwable t) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }
   /* public long findpPostLikeCount(String postId){
        DatabaseReference reference=mDatabase.getReference("post");

        reference.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Post post=snapshot.getValue(Post.class);
                    count=post.getLikesCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return count;
    }*/
  /*  public String findStudentName(int id){
        for (int i = 0; i < students.size(); i++) {
            if (id==students.get(i).getId()){
                if (students.get(i).getLast_name()!=null){
                    return students.get(i).getName()+students.get(i).getLast_name();
                }else {
                    return students.get(i).getName();
                }

            }
        }return "";
    }
    private String findStudentPic(int id){
        for (int i = 0; i < students.size(); i++) {
            if (id==students.get(i).getId()){

                return students.get(i).getPhotoUrl();
            }
        } return null;
    }*/
    public class PostsVH extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_post_author_profile_pic)
        ImageView profilePic;
        @BindView(R.id.tv_post_author_name)
        TextView authorName;
        @BindView(R.id.tv_post_content)
        TextView content;
        @BindView(R.id.iv_post_image)
        ImageView image;
        @BindView(R.id.tv_post_likes_count)
        TextView likesCount;
        @BindView(R.id.iv_post_like_icon)
        ImageView likeIcon;
        public PostsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
