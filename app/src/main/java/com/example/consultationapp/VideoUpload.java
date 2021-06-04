package com.example.consultationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VideoUpload extends AppCompatActivity {
    private static final String TAG = "Video selected";
    private ImageView chooseVideoImageView;
    private Uri fileUri = null;
    private ProgressBar uploadingProgress;
    private TextView progressPercentageTextView;
    private EditText videoTitleEditText;
    private Button UploadButton;
    static final String COLLECTION_NAME = "videos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);


    }
}