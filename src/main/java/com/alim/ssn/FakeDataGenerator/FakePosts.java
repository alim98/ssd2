package com.alim.ssn.FakeDataGenerator;

import com.alim.ssn.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class FakePosts {

    public Observable<Post> getPosts() {
        Observable<Post> observable;
        observable = Observable.create(new ObservableOnSubscribe<Post>() {
            @Override
            public void subscribe(ObservableEmitter<Post> e) throws Exception {
                for (int i = 0; i < 10; i++) {
                    Post post = new Post();
                    post.setContent("جزوه درس سیستم عامل استاد حسنی");
                    Random random = new Random();
                    int random1 = random.nextInt((1000));
                    int random2 = random.nextInt(5000);
                    post.setLikesCount(random1);
                    post.setSize(random2);
                    e.onNext(post);
                }
            }
        });
        return observable;
    }
}
