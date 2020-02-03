package com.alim.ssn.main;

import com.alim.ssn.model.Post;

import java.util.List;

public interface PostsViewContainer {
        void onGetDataSuccess(List<Post> posts);
        void onGetDataFailure();}