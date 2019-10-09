package com.example.rothman.smta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ResultActivity extends Activity {
    TextView tv, tv2, tv3;
    Button btnRestart;
    public static int num=1;int gg;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tv = (TextView)findViewById(R.id.tvres);
        tv2 = (TextView)findViewById(R.id.tvres2);
        tv3 = (TextView)findViewById(R.id.tvres3);
        btnRestart = (Button) findViewById(R.id.btnRestart);

        StringBuffer sb = new StringBuffer();
        sb.append("Correct answers: " + QuestionsActivity.correct + "\n");
        StringBuffer sb2 = new StringBuffer();
        sb2.append("Wrong Answers: " + QuestionsActivity.wrong + "\n");
        StringBuffer sb3 = new StringBuffer();
        sb3.append("Final Score: " + QuestionsActivity.correct + "\n");
        gg=QuestionsActivity.correct;
        tv.setText(sb);
        tv2.setText(sb2);
        tv3.setText(sb3);
SyncData orderData = new SyncData();
        orderData.execute("");
        QuestionsActivity.correct=0;
        QuestionsActivity.wrong=0;


        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });
    }
    //private static final String DB_URL = "jdbc:mysql://DATABASE_IP/DATABASE_NAME";
    private static final String DB_URL = "jdbc:mysql://172.20.40.129/onlinecourse"; //"jdbc:mysql://DATABASE_IP/DATABASE_NAME";
    private static final String USER = "test";
    private static final String PASS = "UJ9xonNmqHRQD7EI";
    private boolean success = false; // boolean
    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(ResultActivity.this, "Synchronising",
                    "ListView Loading! Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); //Connection Object

                if (conn == null) {
                    success = false;
                } else {
                    // Change below query according to your own database.
                    String query = "INSERT INTO marks(test_name,grade) VALUES ('Test"+(num++)+"'"+",'"+gg+"')";
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(query);
//                    if (rs == null) // if resultset not null, I add items to itemArraylist using class created
//                    {
//                        msg = "No Data found!";
//                        success = false;
//                    }
//                    else
//                    {
//                        msg = "Done";
//                        success = true;
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my ListView
        {
            progress.dismiss();
            Toast.makeText(ResultActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {

                } catch (Exception ex) {

                }

            }
        }
    }

}
