package com.izhar.fetawa.ui.home;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.fetawa.Answer;
import com.izhar.fetawa.AnswerAdapter;
import com.izhar.fetawa.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    View root;
    RecyclerView recyclerView;
    List<Answer> answerList;
    List<String> ids;
    AnswerAdapter answerAdapter;
    ProgressBar progressBar;
    TextView empty;
    String date = "";
    String today = "";
    SharedPreferences sharedPreferences;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        ids = new ArrayList<>();
        progressBar = root.findViewById(R.id.pbar);
        sharedPreferences = root.getContext().getSharedPreferences("date", Context.MODE_PRIVATE);
        empty = root.findViewById(R.id.empty);
        setHasOptionsMenu(true);
        recyclerView = root.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        answerList =  new ArrayList<>();
        if (sharedPreferences.getString("day","").length() == 0){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("day", simpleDateFormat.format(new Date()));
            editor.commit();
        }
        setData(sharedPreferences.getString("day", simpleDateFormat.format(new Date())));
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.date){
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.date_picker);
            final DatePicker datePicker = dialog.findViewById(R.id.date_picker);
            Button ok = dialog.findViewById(R.id.ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String day = datePicker.getDayOfMonth() + "";
                    String month = datePicker.getMonth()+1 + "";
                    if (day.length() == 1 )
                        day = "0" + day;
                    if (month.length() == 1 )
                        month = "0" + month;
                    today = day + "-" + month + "-" + datePicker.getYear();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("day", today);
                    editor.commit();
                    setData(today);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        return true;
    }

    void setData(String date){
        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("answered").child(date);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                answerList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //Answer request = snapshot.getValue(Answer.class);
                    String question, answer, id;
                    id = snapshot.getKey();
                    question = snapshot.child("question").getValue().toString();
                    answer = snapshot.child("answer").getValue().toString();
                    //answerList.add(0,request);
                    answerList.add(new Answer(id, question, answer));
                    ids.add(snapshot.getKey());
                }
                if (answerList.size() == 0 )
                    empty.setVisibility(View.VISIBLE);
                else
                    empty.setVisibility(View.GONE);
                answerAdapter = new AnswerAdapter(answerList, root.getContext(), ids);
                recyclerView.setAdapter(answerAdapter);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}