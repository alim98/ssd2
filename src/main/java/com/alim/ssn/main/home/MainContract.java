package com.alim.ssn.main.home;

import android.content.Context;

import com.alim.ssn.model.Post;

import java.util.List;

public interface MainContract {

    interface Presenter{
        void  onDestroy();
        void onRefreshButtonClicked();
        void requestGetData(Context context);
    }

    interface MainView{
        void showProgress();
        void hideProgress();
        void setDataToRecyclerView(List<Post> posts);

        void onResponseFailure(Throwable throwable);
    }

    interface GetNoticeIntractor{
        interface OnFinishedListener{
            void onFinish(List<Post> posts);

            void onFailure(Throwable t);
        }

        void GetNoticedArrayList(OnFinishedListener onFinishedListener, Context context);
    }
}
