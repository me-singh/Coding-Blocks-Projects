package com.example.android.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button= (Button) findViewById(R.id.button);
        final MyAsyncTask myAsyncTask=new MyAsyncTask();//object made outside of OnClickListner makes it unique and increases security and error in an app

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                myAsyncTask.getStatus();//pending,running,finished->>can be checked using if else statements
//                myAsyncTask.cancel(true);//if true->then cancel running  & pending and  if false->then cancel pending only
                myAsyncTask.execute(100000000L,200000L,60000000L);
            }
        });
    }

    //for creating progress dialog and make it work in asyncronous ways(i.e. one thread at a time)
    class MyAsyncTask extends AsyncTask<Long,Long,String>{

        ProgressDialog progressDialog;

        //main/UI thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);//creating new dialog
            progressDialog.setMessage("counting...");//initial message
            progressDialog.setTitle("Please Wait");//title of dialog
//            progressDialog.setCancelable(false);//if false->dialog will not disapper if user touches outside of it
            progressDialog.show();//to display
        }

        //main/UI thread
        //while download or progress in action
        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage("Counting Done till: "+ values[0]);
        }

        //New Background thread
        //execute in external thread(Background thread)->all changes while showing dialog on the screen(imp to override)
        @Override
        protected String doInBackground(Long... integers) {//Ist parameter of AsyncTask<>{} as its parameter
            Long input=integers[0];
            for (long i=0;i<input;i++)
            {
                if(i%1000000==0)
                {
                    publishProgress(i/1000000);//parameter in publishProgress passed to onProgressUpdate for parallel display of changes
                }
            }
            return "Counting Done";//return is passed to onPostExecute to display result
        }

        //main/UI thread
        //String to show as a toast
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            progressDialog.setMessage(s);//to display message on dialog after execution/process reqd
            Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            //for holding progressdialog stay on screen for specific time
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressDialog.hide();//to hode progressdialog after completion

        }

    }
}
