package com.example.rothman.smta;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegesterStActivity extends Activity {
    EditText name, password;
    String Name, Password;
    Context ctx=this;
    String NAME=null, PASSWORD=null, EMAIL=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester_st);
        name = (EditText) findViewById(R.id.textid);
        password = (EditText) findViewById(R.id.textpassword);
    }
    public void main_login(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();


        Toast.makeText(getApplicationContext(),"Hello Javatpoint"+Name+Password,Toast.LENGTH_SHORT).show();

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", Name);
            postData.put("password", Password);

            Toast.makeText(getApplicationContext(),postData.toString(),Toast.LENGTH_SHORT).show();

            new SendDeviceDetails().execute("http://172.20.40.129/SMTA/login.php", postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SendDeviceDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
                Log.e("RRRR",data);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }
        public boolean isJSONValid(String test) {
            try {
                new JSONObject(test);
            } catch (JSONException ex) {

                try {
                    new JSONArray(test);
                } catch (JSONException ex1) {
                    return false;
                }
            }
            return true;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject root = null;
            String err=null;
            String st = null;
            try {
                Log.i("tagconvertstr", "["+result+"]");
                 root = new JSONObject(result);
               // Log.e("TAT", isJSONValid(result)+result+"HHHH"+root.getString("id")); //this is expecting a response code to be sent from your server upon receiving the POST data

                //JSONObject user_data = root.getJSONObject("user_data");
                NAME =root.getString("id");
                st=root.getString("status");
               Log.e("saaaaaaaaaaj", st); // this is expecting a response code to be sent from your server upon receiving the POST data

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }
            if(NAME != null){
            if(!NAME.toString().equals(null)) {
                Intent i = new Intent(ctx, MainActivity.class);
                i.putExtra("id", NAME);

                startActivity(i);}
            }else{
                Toast.makeText(getApplicationContext(),"Invalid USERName or Password",Toast.LENGTH_SHORT).show();
                name.setText("");
                password.setText("");
            }
        }

    }
    public static void restartActivity(Activity act){
        Intent intent=new Intent();
        intent.setClass(act, act.getClass());
        ((Activity)act).startActivity(intent);
        ((Activity)act).finish();
    }
}
