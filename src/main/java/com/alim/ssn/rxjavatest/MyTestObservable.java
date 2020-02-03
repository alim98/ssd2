package com.alim.ssn.rxjavatest;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static androidx.constraintlayout.widget.Constraints.TAG;


class MyTestObservable {
    Observable<Integer> observable=Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> e) throws Exception {
            e.onNext(1);
            e.onNext(2);
            e.onNext(3);
            e.onNext(4);

            e.onComplete();
        }
    });

    void testObserver(){

        Observer<Integer> observer=new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "onNext: "+integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: "+e.toString());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        };
        observable.subscribe(observer);
    }
}
