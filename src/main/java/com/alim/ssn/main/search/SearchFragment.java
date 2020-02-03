package com.alim.ssn.main.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alim.ssn.R;
import com.alim.ssn.stProf.StProfActivity;
import com.alim.ssn.model.Student;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alim.ssn.main.home.GetNoticeIntractorImpl.TAG;

public class SearchFragment extends Fragment {
    @BindView(R.id.sv_main)
    EditText searchView;

    @BindView(R.id.tl_search)
    TabLayout tabLayout;
    @BindView(R.id.vp_search)
    ViewPager viewPager;

    private String mTag = null;

    public SearchFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        viewPager.setAdapter(new SectionPagerAdapter(getActivity().getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    String postTag = "android:switcher:" + R.id.vp_search + ":" + 2;
                    String studentTag = "android:switcher:" + R.id.vp_search + ":" + 0;
                    String tagsTag = "android:switcher:" + R.id.vp_search + ":" + 1;

                    FragmentPostSearch fragmentPostSearch = (FragmentPostSearch) getActivity().getSupportFragmentManager().findFragmentByTag(postTag);
                    FragmentStudentSearch fragmentStudentSearch  = (FragmentStudentSearch) getActivity().getSupportFragmentManager().findFragmentByTag(studentTag);
                    FragmentTagSearch fragmentTagSearch= (FragmentTagSearch) getActivity().getSupportFragmentManager().findFragmentByTag(tagsTag);
                    if (fragmentPostSearch != null) {
                        fragmentPostSearch.onTextChanged(s.toString());
                    }
                    if (fragmentStudentSearch != null) {
                        fragmentStudentSearch.onTextChanged(s.toString());
                    }
                    if (fragmentTagSearch!=null)
                    {
                        fragmentTagSearch.onTextChanges(s.toString());
                    }
            }
        }});


        return view;
    }

    private void search(String newText, OnSearchComplete onSearchComplete) {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<List<Student>> listCall = apiInterface.searchStudents(newText);
        listCall.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    onSearchComplete.OnComplete(response.body());
                } else {
                    Log.e(TAG, "onResponse: searchFragment" + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.e(TAG, "onFailure: searchFragment" + t.toString());
            }
        });
    }

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
    }

    interface OnSearchComplete {
        void OnComplete(List<Student> students);
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {
        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    default:
                    return new FragmentStudentSearch();
                case 1:
                    return new FragmentTagSearch();
                case 2:
                    return new FragmentPostSearch();
            }

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                default:
                    return getString(R.string.people);
                case 1:
                    return getString(R.string.tags);
                case 2:
                    return getString(R.string.posts);

            }
        }
    }}

