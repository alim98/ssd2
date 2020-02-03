package com.alim.ssn.main.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPostSearch extends Fragment  {
private SearchController controller=new SearchController();
//    @BindView(R.id.rv_search_post)
    RecyclerView recyclerView;
    TextView noPostFind;
    private OnTextChangedListener mListener;



    public FragmentPostSearch(){

    }
    public static FragmentPostSearch newInstance(){
        return  new FragmentPostSearch();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.rv_search_post);
        noPostFind=view.findViewById(R.id.tv_no_post_exists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }


    public void onTextChanged(String text)
    {
        controller.search(text, new SearchController.OnSearchComplete() {
            @Override
            public void onTagComplete(List<Tag> tags) {

            }

            @Override
            public void onPostComplete(List<Post> posts) {



                PostAdapter adapter = null;
                if (posts != null) {
                 adapter=new PostAdapter(posts, getContext());
                 noPostFind.setVisibility(View.GONE);
                }else {
                    noPostFind.setVisibility(View.VISIBLE);
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onPeopleComplete(List<Student> students) {

            }

            @Override
            public void onSearchComplete(Search search) {

            }
        });
    }
}
