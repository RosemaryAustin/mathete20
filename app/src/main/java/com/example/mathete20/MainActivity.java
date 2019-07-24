package com.example.mathete20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = "here";
    private Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this hides the action bar (thats the thing at the top of the app view)
        getSupportActionBar().hide();



    }
    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // takes the user to the activity math
        Intent intent = new Intent(this, math.class);
        startActivity(intent);
    }
}
