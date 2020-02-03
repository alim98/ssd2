package com.alim.ssn.main.Comment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ssn.R;

import java.util.List;

public class CommentsFragment extends AppCompatActivity {


    public static final String POST_ID="postId";



    private void setupRecyclerView(List<Comment> comments) {
        RecyclerView recyclerView=findViewById(R.id.rv_post_comments);
        CommentsAdapter adapter = null;
        if (comments != null) {
             adapter=new CommentsAdapter(comments, this);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_comments);
        String postId=getIntent().getStringExtra(POST_ID);
        CommentController commentController=new CommentController();
        commentController.getComments(postId, new CommentController.OnGetComments() {
            @Override
            public void onComplete(List<Comment> comments) {
               setupRecyclerView(comments);
            }

            @Override
            public void onError() {
                Toast.makeText(CommentsFragment.this, "Errrorrrrrr", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
