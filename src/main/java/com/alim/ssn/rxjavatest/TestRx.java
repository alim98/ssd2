package com.alim.ssn.rxjavatest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alim.ssn.R;

public class TestRx extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rx);

        MyTestObservable myTestObservable=new MyTestObservable();
        myTestObservable.testObserver();
    }
}
