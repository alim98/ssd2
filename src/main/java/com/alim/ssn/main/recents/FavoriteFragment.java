package com.alim.ssn.main.recents;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alim.ssn.R;
import com.alim.ssn.main.home.GetNoticeIntractorImpl;
import com.alim.ssn.main.home.HomeFragment;
import com.alim.ssn.main.home.PresenterImpl;
import com.alim.ssn.model.Recent;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.alim.ssn.studentProperties.Stid;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        swipeRefreshLayout = view.findViewById(R.id.srl_recent_ft);
        swipeRefreshLayout.setOnRefreshListener(new SwipeListener());
        getRecent();
        return view;
    }

    private void getRecent() {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        int stId = ((Stid) getContext().getApplicationContext()).getStId();
        String token = ((Stid) getContext().getApplicationContext()).getToken();
        String bearerToken = "Bearer " + token;
        Call<List<Recent>> call = apiInterface.getRecents(stId, bearerToken);
        call.enqueue(new Callback<List<Recent>>() {
            @Override
            public void onResponse(Call<List<Recent>> call, Response<List<Recent>> response) {
                if (response.isSuccessful()) {
                    swipeRefreshLayout.setRefreshing(false);
                    setupRecyclerView(response.body());
                }else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Recent>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setupRecyclerView(List<Recent> recent) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_recents);
        for (int i = 0; i < recent.size(); i++) {
            try {
                Timestamp createdAt = convertStringToTimeStamp(recent.get(i).getCreatedAt());
                recent.get(i).setTsCreatedAt(createdAt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(recent, new Comparator<Recent>() {
            @Override
            public int compare(Recent o1, Recent o2) {
                return o2.getTsCreatedAt().compareTo(o1.getTsCreatedAt());
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        RecentsAdapter adapter = new RecentsAdapter(getContext(), recent);
        recyclerView.setAdapter(adapter);
    }

    private Timestamp convertStringToTimeStamp(String createdAt) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date parseDate = dateFormat.parse(createdAt);
        Timestamp timestamp = new Timestamp(parseDate.getTime());
        return timestamp;
    }

    class SwipeListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            getRecent();
        }
    }
}
