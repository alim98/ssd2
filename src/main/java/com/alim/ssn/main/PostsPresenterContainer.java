package com.alim.ssn.main;

import com.alim.ssn.model.Post;

import java.util.List;

public interface PostsPresenterContainer {
    void getPostsFromServer();

    void onSuccess(List<Post> posts);
    void onGetPostFailure();

}
