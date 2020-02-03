package com.alim.ssn.main.search;

import com.alim.ssn.model.Post;
import com.alim.ssn.model.Search;
import com.alim.ssn.model.Student;
import com.alim.ssn.model.Tag;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchController
{
    private ApiInterface apiInterface;

    SearchController(){
        apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
    }

  /*  void searchByTag(){

    }

    void searchByPeople(){

    }
    void searchByPost(){

    }*/
    void search(String keyword, OnSearchComplete onSearchComplete){
        Call<Search> call=apiInterface.search(keyword);
        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if ((response.body() != null ? response.body().getStudents() : null) != null) {
                    onSearchComplete.onPeopleComplete(response.body().getStudents());
                }else {
                    onSearchComplete.onPeopleComplete(null);
                }
                if ((response.body() != null ? response.body().getPosts() : null) != null) {
                    onSearchComplete.onPostComplete(response.body().getPosts());
                }else {
                    onSearchComplete.onPostComplete(null);
                }
                if ((response.body() != null ? response.body().getTags() : null) != null) {
                    onSearchComplete.onTagComplete(response.body().getTags());
                }else {
                    onSearchComplete.onTagComplete(null);
                }
                if (response.body() != null) {
                    onSearchComplete.onSearchComplete(response.body());

                }else {
                    onSearchComplete.onSearchComplete(null);
                }

            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

            }
        });
    }

    interface  OnSearchComplete{
         void onTagComplete(List<Tag> tags);
         void onPostComplete(List<Post> posts);
         void onPeopleComplete(List<Student> students);
         void onSearchComplete(Search search);
    }
}
