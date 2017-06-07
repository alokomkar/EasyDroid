package com.sortedqueue.learnandroid.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by Alok on 02/06/17.
 */

public class CodeFileReaderTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String code;
    private String fileId;

    public interface OnDataReadListener {
        void onDataReadComplete( String code );
    }

    //To display progress Dialog
    private ProgressDialog progressDialog;

    //Interface to communicate back the response to UI
    private OnDataReadListener onDataReadListener;

    public CodeFileReaderTask(Context context, String fileId, OnDataReadListener onDataReadListener) {
        this.context = context;
        this.fileId = fileId;
        this.onDataReadListener = onDataReadListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        readFileAsList(context);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if( progressDialog != null ) {
            progressDialog.dismiss();
        }
        if( onDataReadListener != null ) {
            onDataReadListener.onDataReadComplete(code);
        }
    }

    private void readFileAsList(Context context) {
        String line;
        try {

            InputStream inputStream = context.getResources().openRawResource(
                    context.getResources().getIdentifier(fileId,
                            "raw", context.getPackageName()));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            code = "";
            while ((line = bufferedReader.readLine()) != null) {
                code += line + "\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
