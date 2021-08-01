package com.izhar.fetawa;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            ask();
        });
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_about, R.id.navigation_mine).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        if (Build.VERSION.SDK_INT >= 23)
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[] {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 101);
            }
    }

    private void ask() {
        Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.fragment_notifications);
        dialog.show();
        final EditText editText = dialog.findViewById(R.id.question);
        final Button button = dialog.findViewById(R.id.btn_send);
        final ProgressBar progressBar = dialog.findViewById(R.id.pbar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().length() > 15){
                    button.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    editText.setEnabled(false);
                    sendQuestion(dialog, editText, button, progressBar);
                }
            }
        });
    }

    private void sendQuestion(Dialog dialog, final EditText editText, final Button button, final ProgressBar progressBar) {
        String question = editText.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("username", "").length() == 0){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", "user" + System.currentTimeMillis());
            editor.apply();
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
                        Toast.makeText(MainActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "your question has been sent successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPreferences = getSharedPreferences("date", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("day", "");
        editor.commit();
        super.onDestroy();
    }
}
