package com.alim.ssn.main.home;

import android.content.Context;

import com.alim.ssn.model.Post;

import java.util.List;

public class PresenterImpl implements MainContract.Presenter, MainContract.GetNoticeIntractor.OnFinishedListener {
    private MainContract.MainView mMainView;
    private MainContract.GetNoticeIntractor getNoticeIntractor;
    public PresenterImpl(MainContract.MainView mainView, MainContract.GetNoticeIntractor getNoticeIntractor){
       this.mMainView=mainView;
        this.getNoticeIntractor = getNoticeIntractor;
    }
    @Override
    public void onDestroy() {
        mMainView=null;
    }

    @Override
    public void onRefreshButtonClicked() {
        mMainView.showProgress();
    }

    @Override
    public void requestGetData(Context context) {
        getNoticeIntractor.GetNoticedArrayList(this, context);
    }

    @Override
    public void onFinish(List<Post> posts) {
        mMainView.setDataToRecyclerView(posts);
        mMainView.hideProgress();
    }



    @Override
    public void onFailure(Throwable t) {
        mMainView.onResponseFailure(t);
        mMainView.hideProgress();
    }
}
