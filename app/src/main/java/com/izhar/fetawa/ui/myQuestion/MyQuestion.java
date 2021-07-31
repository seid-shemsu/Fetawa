package com.izhar.fetawa.ui.myQuestion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.fetawa.Answer;
import com.izhar.fetawa.MineAdapter;
import com.izhar.fetawa.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyQuestion extends Fragment {
    View root;
    RecyclerView recyclerView;
    List<Answer> answerList;
    List<String> ids;
    MineAdapter answerAdapter;
    ProgressBar progressBar;
    TextView empty;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.my_question, container, false);
        progressBar = root.findViewById(R.id.pbar);
        empty = root.findViewById(R.id.empty);
        recyclerView = root.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        answerList =  new ArrayList<>();
        ids = new ArrayList<>();
        setData();
        return root;
    }
    void setData(){
        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "unknown");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                answerList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //Answer ans = snapshot.getValue(Answer.class);
                    String question, answer, id;
                    id = snapshot.getKey();
                    question = snapshot.child("question").getValue().toString();
                    answer = snapshot.child("answer").getValue().toString();
                    //answerList.add(0, ans);
                    answerList.add(0, new Answer(id, question, answer));
                    ids.add(snapshot.getKey());
                }
                if (answerList.size() == 0 )
                    empty.setVisibility(View.VISIBLE);
                else
                    empty.setVisibility(View.GONE);
                answerAdapter = new MineAdapter(answerList, root.getContext());
                recyclerView.setAdapter(answerAdapter);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
