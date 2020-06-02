package com.alim.ssn.main.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alim.ssn.R;
import com.alim.ssn.model.Post;
import com.alim.ssn.model.Search;
import com.alim.ssn.model.Student;
import com.alim.ssn.model.Tag;
import com.alim.ssn.stProf.StProfActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentStudentSearch extends Fragment {
    @BindView(R.id.lv_student_search)
    ListView listView;
    @BindView(R.id.tv_no_student_exists)
    TextView noStudentFind;
    private List<Student> exStudents = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_student, container, false);
        ButterKnife.bind(this, view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student selectedStudent = exStudents.get(position);
                Intent intent = new Intent(getContext(), StProfActivity.class);
                intent.putExtra(StProfActivity.FIRST_AND_LAST_NAME, selectedStudent.getName() );
                intent.putExtra(StProfActivity.PHONE_NUMBER, selectedStudent.getPhone_number());
                startActivity(intent);
            }
        });
        return view;
    }

    public void onTextChanged(String text) {
        SearchController searchController = new SearchController();
        searchController.search(text, new SearchController.OnSearchComplete() {
            @Override
            public void onTagComplete(List<Tag> tags) {

            }

            @Override
            public void onPostComplete(List<Post> posts) {

            }

            @Override
            public void onPeopleComplete(List<Student> students) {
                ArrayAdapter<String> adapter;

                List<String> names = new ArrayList<>();
                exStudents.clear();
                if (students != null) {

                    for (int i = 0; i < students.size(); i++) {
                        names.add(students.get(i).getName());
                        exStudents.add(students.get(i));
                    }
                    noStudentFind.setVisibility(View.VISIBLE);
                }else {
                    noStudentFind.setVisibility(View.INVISIBLE);
                }
                adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_list_item, names);
                listView.setAdapter(adapter);
            }

            @Override
            public void onSearchComplete(Search search) {

            }
        });
    }

}
