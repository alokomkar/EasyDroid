package com.sortedqueue.learnandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sortedqueue.learnandroid.asynctasks.FileReaderTask;

import io.github.kbiakov.codeview.CodeView;

public class MainActivity extends AppCompatActivity implements FileReaderTask.OnDataReadListener {

    private CodeView codeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        codeView = (CodeView) findViewById(R.id.codeView);
        new FileReaderTask(MainActivity.this, R.raw.main_activity, this).execute();

    }

    @Override
    public void onDataReadComplete(String code) {
        codeView.setCode(code, "java");
    }
}
