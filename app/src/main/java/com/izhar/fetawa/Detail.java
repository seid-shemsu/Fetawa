package com.izhar.fetawa;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class Detail extends AppCompatActivity {
    TextView question_textview, answer_textview;
    ImageView icon;
    SeekBar seekBar;
    CardView text_card, voice_card;
    String question, answer, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Answer");
        setContentView(R.layout.activity_detail);

        question = getIntent().getExtras().getString("question");
        answer = getIntent().getExtras().getString("answer");
        id = getIntent().getExtras().getString("id");
        icon = findViewById(R.id.file_icon);
        seekBar = findViewById(R.id.seekbar);
        text_card = findViewById(R.id.text_card);
        voice_card = findViewById(R.id.voice_card);
        question_textview = findViewById(R.id.question);
        question_textview.setText(question);
        if (answer.startsWith("https://")) {
            text_card.setVisibility(View.GONE);
            String root = Environment.getExternalStorageDirectory().toString();
            File dir = new File(root + "/fetawa/audios");
            final File file = new File(dir, id + ".mp3");
            if (file.isFile() && file.length() > 0) {
                icon.setImageDrawable(getResources().getDrawable(R.drawable.play));
            }
            icon.setOnClickListener(v -> {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.play));
                    } else {
                        mediaPlayer.start();
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    }
                } else {
                    if (file.isFile() && file.length() > 0) {
                        play();
                    } else
                        download();
                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }

        else {
            voice_card.setVisibility(View.GONE);
            answer_textview = findViewById(R.id.answer);
            answer_textview.setText(answer);
        }
    }

    private void download() {
        //final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), course_code + part_number + ".mp3");
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File dir = new File(root + "/fetawa/audios");
            dir.mkdirs();
            final File file = new File(dir, id + ".mp3");

            if (file.isFile() && file.length() > 0) {
                play();
            } else {
                final Dialog dialog = new Dialog((this));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.downloading);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("answered").child(id);
                try {
                    file.createNewFile();
                    storageReference.getFile(file)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    dialog.dismiss();
                                    play();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(Detail.this, "ERROR 99\n" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (Exception e) {
                    Toast.makeText(Detail.this, "ERROR 100\n" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(Detail.this, "ERROR 101\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    MediaPlayer mediaPlayer;
    private void play() {
        mediaPlayer = new MediaPlayer();
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File dir = new File(root + "/fetawa/audios");
            final File file = new File(dir, id + ".mp3");
            mediaPlayer.setDataSource(file.toString());
            mediaPlayer.prepare();
            mediaPlayer.start();
            icon.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            seekBar.setMax(mediaPlayer.getDuration());
            int startTime = mediaPlayer.getCurrentPosition();
            handler.postDelayed(updateSeekbar, 100);
            seekBar.setProgress(startTime);
        } catch (final Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    Handler handler = new Handler();
    private Runnable updateSeekbar = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(this, 1000);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer != null)
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
