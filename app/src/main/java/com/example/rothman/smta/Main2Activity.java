package com.example.rothman.smta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static android.support.constraint.Constraints.TAG;

public class Main2Activity extends Activity {
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    public static String data="";
String sref;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.list );


        JSONObject postData = new JSONObject();
        //Toast.makeText(getApplicationContext(),postData.toString(),Toast.LENGTH_SHORT).show();

        new SendDeviceDetails().execute("http://172.20.40.129/SMTA/getcourse.php",postData.toString());


        // Create and populate a List of planet names.
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
        listAdapter.add( "Ceres" );
        listAdapter.add( "Pluto" );
        listAdapter.add( "Haumea" );
        listAdapter.add( "Makemake" );
        Toast.makeText(getApplicationContext(),"MM"+data,Toast.LENGTH_SHORT).show();
        //listAdapter.add( "Eris" );
        //Log.e("MMMMM",data);

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );
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
                   // Log.e("GGGG",current+"");
                }

                Log.e("RRRR",data+"bbbb"+params);
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
                //NAME =root.getString("id");
                st=root.getString("result");
                Log.e("ref", st); // this is expecting a response code to be sent from your server upon receiving the POST data

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }
//            if(NAME != null){
//                if(!NAME.toString().equals(null)) {
//                    Intent i = new Intent(ctx, Main2Activity.class);
//                    i.putExtra("id", NAME);
//
//                    startActivity(i);}
//            }else{
//                Toast.makeText(getApplicationContext(),"Invalid USERName or Password",Toast.LENGTH_SHORT).show();
//                name.setText("");
//                password.setText("");
//            }

        }

    }
}
