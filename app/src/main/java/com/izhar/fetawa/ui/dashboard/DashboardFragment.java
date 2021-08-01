package com.izhar.fetawa.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.fetawa.Answer;
import com.izhar.fetawa.Fav;
import com.izhar.fetawa.FavAdapter;
import com.izhar.fetawa.R;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    List<Answer> answerList = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    Fav myFav;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        myFav = new Fav(getContext());
        progressBar = root.findViewById(R.id.pbar);
        recyclerView = root.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        answerList = myFav.getAllFav();
        ids = myFav.getIds();
        FavAdapter adapter = new FavAdapter(answerList, getContext());
        recyclerView.setAdapter(adapter);
        return root;
    }
}