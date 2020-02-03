package com.alim.ssn.main.profile.following;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ssn.R;
import com.alim.ssn.model.Student;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingVH> {
    private List<Student> students;
    private Context context;

    public FollowingAdapter(List<Student> students, Context context) {
        this.students = students;
        this.context = context;
    }

    @NonNull
    @Override
    public FollowingAdapter.FollowingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_st_item, parent, false);
        return new FollowingVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.FollowingVH holder, int position) {
        holder.name.setText(students.get(position).getName());
        if (students.get(position).getPhotoUrl()!=null){
            Picasso.get().load(students.get(position).getPhotoUrl()).into(holder.prfPic);
        }else {
            holder.prfPic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.avatar));
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class FollowingVH extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_st_layout_item_prf_pic)
        ImageView prfPic;
        @BindView(R.id.tv_st_layout_item_name)
        TextView name;

        public FollowingVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
