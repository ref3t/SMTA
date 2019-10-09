package com.example.rothman.smta;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
public class MainActivity3 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Button startbutton=(Button)findViewById(R.id.button);
        final EditText nametext=(EditText)findViewById(R.id.editName);
nametext.setVisibility(View.INVISIBLE);
                startbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=nametext.getText().toString();
                        Intent intent=new Intent(getApplicationContext(),QuestionsActivity.class);
                        intent.putExtra("myname",name);
                        startActivity(intent);
                    }
                });


    }
}
