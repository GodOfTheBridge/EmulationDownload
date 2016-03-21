package com.gotb.emulationdownload;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    Button btnOtherActions, btnStart, btnCancel;
    CheckBox cbShow;
    TextView tvHiddenProgress, tvFinal, tvCountFiles;
    ProgressBar progressBar;
    ProgressBarAsyncTask progressBarAsyncTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cbShow = (CheckBox) findViewById(R.id.cbShow);

        tvHiddenProgress = (TextView) findViewById(R.id.tvHiddenProgress);
        tvFinal = (TextView) findViewById(R.id.tvFinal);
        tvCountFiles = (TextView) findViewById(R.id.tvCountFiles);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOtherActions = (Button) findViewById(R.id.btnOtherActions);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart.setEnabled(false);
                progressBarAsyncTask = new ProgressBarAsyncTask();
                progressBarAsyncTask.execute("File_1", "File_2", "File_3");
            }
        });

        btnOtherActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Do some action while do in background", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarAsyncTask.cancel(false);
                btnStart.setEnabled(true);
            }
        });

        cbShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvHiddenProgress.setVisibility(View.VISIBLE);
                    tvCountFiles.setVisibility(View.VISIBLE);
                } else {
                    tvHiddenProgress.setVisibility(View.GONE);
                    tvCountFiles.setVisibility(View.GONE);
                }

            }
        });

    }

    class ProgressBarAsyncTask extends AsyncTask<String, Integer, String> {

        private int progressBarValue = 0, count = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Start progress", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            for(String str: params) {
                count++;
                while (progressBarValue < 100) {
                    progressBarValue++;
                    publishProgress(progressBarValue);
                    SystemClock.sleep(30);
                    if (isCancelled()){
                        count = 0;
                        return null;
                    }
                }
                progressBarValue = 0;
            }
            return "It is the End";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            tvHiddenProgress.setText(values[0] + "%");
            tvCountFiles.setText("Download file " + count);
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            btnStart.setEnabled(true);
            tvFinal.setText(str);
            count = 0;
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressBar.setProgress(0);
            tvHiddenProgress.setText("0%");
            tvCountFiles.setText("Download");
        }


    }
}