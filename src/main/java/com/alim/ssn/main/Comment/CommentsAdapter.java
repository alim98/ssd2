package com.alim.ssn.main.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ssn.R;
import com.alim.ssn.model.Student;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsAdapter  extends RecyclerView.Adapter<CommentsAdapter.CommentsVH> {

    private List<Comment>comments;
    private Context context;
    private ApiInterface apiInterface;
    public CommentsAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_comments_item,parent,false);
        apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
        return new CommentsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsVH holder, int position) {
        Comment comment=comments.get(position);

        apiInterface.getStudent(comment.getStId()).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    Picasso.get().load(response.body().getPhotoUrl()).into(holder.profile);
                    holder.stName.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {

            }
        });
        holder.content.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

     class CommentsVH extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_comments_prof_icon)
        CircleImageView profile;

        @BindView(R.id.tv_comments_st_name)
        TextView stName;

        @BindView(R.id.tv_comments_item_content)
        TextView content;
        public CommentsVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
