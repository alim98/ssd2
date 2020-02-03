package com.alim.ssn.main.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ssn.R;
import com.alim.ssn.main.home.PostAdapter;
import com.alim.ssn.model.Post;
import com.alim.ssn.model.Search;
import com.alim.ssn.model.Student;
import com.alim.ssn.model.Tag;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagSearchResult extends Fragment {
    private String mTag;
    @BindView(R.id.rv_tag_search_result)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_tag_text)
    TextView tagText;
    public TagSearchResult(){

    }
    public TagSearchResult(String tag)
    {
        mTag=tag;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tag_search_result, container, false);
        ButterKnife.bind(this, view);
        tagText.setText(mTag);
        search();
        return view;
    }

    private void search() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ApiInterface apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
        Call<List<Post>> call=apiInterface.searchPostsByTag(mTag);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                PostAdapter adapter=new PostAdapter(response.body(), getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }


}
