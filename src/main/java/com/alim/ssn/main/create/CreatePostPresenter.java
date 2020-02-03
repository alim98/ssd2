package com.alim.ssn.main.create;

import android.content.Context;

import java.io.File;
import java.math.BigInteger;

public interface CreatePostPresenter {
    void savePostInServer(String token , Context context,String desc, int studentId,  int size  , File file, String tags, CreatePostPresenterImpl.OnSavePostComplete onSavePostComplete);
}
