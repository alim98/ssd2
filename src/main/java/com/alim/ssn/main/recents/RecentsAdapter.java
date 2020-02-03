package com.alim.ssn.main.recents;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ssn.R;
import com.alim.ssn.model.Recent;
import com.alim.ssn.model.Student;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.sql.Timestamp;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.RecentsVH> {
private Context context;
private List<Recent> recents;
private ApiInterface apiInterface;
ImageLoader imageLoader;
private Timestamp currentTime;
private List<Recent> sortedRecents;
    public RecentsAdapter(Context context, List<Recent> recents) {
        this.context = context;
        this.recents = recents;
        apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);

    }

    @NonNull
    @Override
    public RecentsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_recent_item, parent, false);
        imageLoader= ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
         currentTime = new Timestamp(System.currentTimeMillis());

        return new RecentsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentsVH holder, int position) {
        Recent recent=recents.get(position);
        Call<Student> call=apiInterface.getStPub(recent.getStId());
String result = DateUtils.getRelativeTimeSpanString(recent.getTsCreatedAt().getTime(), currentTime.getTime(), 0).toString();
holder.timeLeft.setText(result);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful())
                {
                    holder.stName.setText(response.body().getName()+" "+response.body().getLast_name());
                    imageLoader.displayImage(response.body().getPhotoUrl(), holder.stPic);
                }else {
                    Toast.makeText(context, "Errorr", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Toast.makeText(context, "Errorrrr", Toast.LENGTH_SHORT).show();
            }
        });
        switch (recent.getAction())
        {
            case "like":
                holder.actionTv.setText(R.string.liked_your_post);
                holder.actionIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_red_fill));
                break;
            case "comment":
                holder.actionTv.setText(R.string.commented_your_post);
                holder.actionIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_comment_black_24dp));
                break;
            case "follow":
                holder.actionTv.setText(R.string.following_you);
                holder.actionIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_people_outline_black_24dp));
                break;
        }
    }



    @Override
    public int getItemCount() {
        return recents.size();
    }

    class RecentsVH extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_recent_item_st_profile_pic)
        ImageView stPic;
        @BindView(R.id.tv_recent_item_st_profile_name)
        TextView stName;
        @BindView(R.id.tv_recent_item_action)
        TextView actionTv;
        @BindView(R.id.iv_recent_item_action)
        ImageView actionIv;
        @BindView(R.id.tv_recent_item_time_left)
        TextView timeLeft;
        public RecentsVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
