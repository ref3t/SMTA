
package com.example.rothman.smta;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class MainActivity extends Activity {
    ImageView imageView;
    Button button;
    Button btnScan;
    Button btnLogin;
    Button sendnote,send,quiz;
    EditText editText,sendnotetext;
    String EditTextValue;
    Thread thread;
    public final static int QRcodeWidth = 350;
    Bitmap bitmap;
    String editText2;
    TextView tv_qr_readTxt;
String BigData="";
    String St;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);
        sendnotetext = (EditText) findViewById(R.id.evalText);
        button = (Button) findViewById(R.id.button);
        btnScan = (Button) findViewById(R.id.btnScan);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tv_qr_readTxt = (TextView) findViewById(R.id.tv_qr_readTxt);
        editText2 = getIntent().getStringExtra("id");
        editText.setText(editText2);
        editText.setVisibility(View.INVISIBLE);
        sendnote = (Button) findViewById(R.id.SendNote) ;
        quiz = (Button) findViewById(R.id.quiz);
        send = (Button) findViewById(R.id.send) ;
        send.setVisibility(View.INVISIBLE);
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity3.class));
            }});
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 St = sendnotetext.getText().toString();
                if(St.length()==0)
                    Toast.makeText(MainActivity.this, "Please Enter Your Note", Toast.LENGTH_LONG).show();
else{
    SyncData orderData = new SyncData();
                    orderData.execute("");
                    sendnotetext.setText("");
                    sendnotetext.setVisibility(View.INVISIBLE);
                    send.setVisibility(View.INVISIBLE);
                }

            }
        });
        sendnote.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              sendnotetext.setVisibility(View.VISIBLE);
              send.setVisibility(View.VISIBLE);

          }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!editText.getText().toString().isEmpty()) {
                    EditTextValue = editText.getText().toString();

                    try {
                        bitmap = TextToImageEncode(EditTextValue);

                        imageView.setImageBitmap(bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {
                    editText.requestFocus();
                    Toast.makeText(MainActivity.this, "Please Enter Your Scanned Test", Toast.LENGTH_LONG).show();
                }

            }
        });
//        btnlogin.setOnClickListener(new View.OnClickListener() {
//              @Override
//              public void onClick(View view) {
//                  startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//
//              }
//        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MessageActivity.class));
                // startActivity(new Intent(MainActivity.this,RegesterActiviry.class));

            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                Toast.makeText(MainActivity.this, "The text of QR code is ", Toast.LENGTH_LONG).show();


            }
        });

    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor) : getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 350, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");

                tv_qr_readTxt.setText(result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents() + "rrrrr", Toast.LENGTH_LONG).show();
                JSONObject postData = new JSONObject();
                try {
                    postData.put("id", editText2);

                    postData.put("date",result.getContents());
                    Toast.makeText(getApplicationContext(), postData.toString(), Toast.LENGTH_SHORT).show();

                    new SendDeviceDetails().execute("http://172.20.40.129/SMTA/attendance.php", postData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }


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
            progress = ProgressDialog.show(MainActivity.this, "Synchronising",
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
                    String query = "INSERT INTO Note(note,student_id) VALUES ('"+St+"'"+",'"+editText2.toString()+"')";
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
            Toast.makeText(MainActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {

                } catch (Exception ex) {

                }

            }
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
               // Toast.makeText(this, "Scanned: " + "rrrrr", Toast.LENGTH_LONG).show();

                //Main2Activity.data=data;
               // Log.e("RRRR",data);
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
            String test;
            try {
                Log.i("tagconvertstr", "["+result+"]");
                root = new JSONObject(result);
                // Log.e("TAT", isJSONValid(result)+result+"HHHH"+root.getString("id")); //this is expecting a response code to be sent from your server upon receiving the POST data

                //JSONObject user_data = root.getJSONObject("user_data");
                test =root.getString("id");
                st=root.getString("inputref2");
                Log.e("saja4", st); // this is expecting a response code to be sent from your server upon receiving the POST data

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }
//                if(test != null){
//                    if(!NAME.toString().equals(null)) {
//                        Intent i = new Intent(ctx, MainActivity.class);
//                        i.putExtra("id", NAME);
//
//                        startActivity(i);}
//                }else{
//                    Toast.makeText(getApplicationContext(),"Invalid USERName or Password",Toast.LENGTH_SHORT).show();
//                    name.setText("");
//                    password.setText("");
//                }
        }

    }

}
