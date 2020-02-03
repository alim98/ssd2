package com.alim.ssn.firebase;

import com.alim.ssn.model.Post;

public interface CreatePostPresenterContainer {
    void saveInServer( String content, String field, String grade);
}
