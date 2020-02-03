package com.alim.ssn.main.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ssn.R;
import com.alim.ssn.model.Post;
import com.alim.ssn.model.Search;
import com.alim.ssn.model.Student;
import com.alim.ssn.model.Tag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTagSearch extends Fragment {
@BindView(R.id.lv_tag_search)
ListView listView;
@BindView(R.id.tv_no_tag_exists)
    TextView noTagExists;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_search_tag, container, false);
        ButterKnife.bind(this, view);

    return view;
    }

    public void onTextChanges(String text)
    {
        SearchController controller=new SearchController();
        controller.search(text, new SearchController.OnSearchComplete() {
            @Override
            public void onTagComplete(List<Tag> tags) {
                List<String> tagNames=new ArrayList<>();
                ArrayAdapter<String> arrayAdapter;
                if (tags != null) {
                    for (Tag tag:
                            tags) {
                        tagNames.add(tag.getTitle());
                    }
                arrayAdapter=new ArrayAdapter<String>(getContext(), R.layout.simple_list_item, tagNames);
                    noTagExists.setVisibility(View.INVISIBLE);
                    listView.setAdapter(arrayAdapter);
                }else {
                    noTagExists.setVisibility(View.VISIBLE);
                    listView.setAdapter(null);

                }


            }

            @Override
            public void onPostComplete(List<Post> posts) {

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
