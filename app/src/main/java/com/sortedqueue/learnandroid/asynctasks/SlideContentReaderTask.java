package com.sortedqueue.learnandroid.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sortedqueue.learnandroid.constants.LearnDroidConstants;
import com.sortedqueue.learnandroid.topic.SlideContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Alok on 07/06/17.
 */

public class SlideContentReaderTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String code;
    private String fileId;
    private ArrayList<SlideContent> contentArrayList;

    public interface OnDataReadListener {
        void onDataReadComplete( ArrayList<SlideContent> contentArrayList );
    }

    //To display progress Dialog
    private ProgressDialog progressDialog;

    //Interface to communicate back the response to UI
    private OnDataReadListener onDataReadListener;

    public SlideContentReaderTask(Context context, String fileId, OnDataReadListener onDataReadListener) {
        this.context = context;
        this.fileId = fileId;
        this.onDataReadListener = onDataReadListener;
        this.contentArrayList = new ArrayList<>();
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
            onDataReadListener.onDataReadComplete(contentArrayList);
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
                if( line.startsWith("<image>") ) {
                    contentArrayList.add(new SlideContent(code, LearnDroidConstants.CONTENT_TYPE_TEXT));
                    code = "";
                    line = line.replace("<image>", "");
                    line = line.replace("</image>", "");
                    contentArrayList.add(new SlideContent(line, LearnDroidConstants.CONTENT_TYPE_IMAGE));

                }
                else if( line.startsWith("<code>") ) {
                    contentArrayList.add(new SlideContent(code, LearnDroidConstants.CONTENT_TYPE_TEXT));
                    code = "";
                    line = line.replace("<code>", "");
                    line = line.replace("</code>", "");
                    contentArrayList.add(new SlideContent(line, LearnDroidConstants.CONTENT_TYPE_CODE));
                }
                else {
                    code += line + "\n";
                }

            }
            if( contentArrayList.size() == 0 ) {
                if( code.trim().length() > 0 ) {
                    contentArrayList.add( new SlideContent(code, LearnDroidConstants.CONTENT_TYPE_TEXT) );
                }
            }
            else if( code.trim().length() > 0 ) {
                contentArrayList.add( new SlideContent(code, LearnDroidConstants.CONTENT_TYPE_TEXT) );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
