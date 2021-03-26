package com.izhar.fetawa.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.izhar.fetawa.Question;
import com.izhar.fetawa.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final EditText editText = root.findViewById(R.id.question);
        final Button button = root.findViewById(R.id.btn_send);
        final ProgressBar progressBar = root.findViewById(R.id.pbar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().length() > 15){
                    button.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    editText.setEnabled(false);
                    sendQuestion(editText, button, progressBar);
                }
            }
        });
        return root;
    }

    private void sendQuestion(final EditText editText, final Button button, final ProgressBar progressBar) {
        String question = editText.getText().toString();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("username", "").length() == 0){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", "user" + System.currentTimeMillis());
            editor.commit();
        }
        String usernam = sharedPreferences.getString("username", "'unknown");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("questions");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(new Date());
        Question question1 = new Question(question, usernam, "unchecked");
        databaseReference.child(date).child(System.currentTimeMillis()+"").setValue(question1)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "your question has been sent successfully", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                        editText.setEnabled(true);
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }
}