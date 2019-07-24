package com.example.mathete20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class solution extends AppCompatActivity {
    private TextView answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        answer = findViewById(R.id.answer);
        getSupportActionBar().hide();

        double equal= this.getIntent().getDoubleExtra("equal",0);
        //sets the solution to a text view
        answer.getText();
        answer.setText("" + equal);
    }
    //takes user back to the math activity to solve a new problem
    public void restart(View view){
        Intent intent = new Intent(this, math.class);
        startActivity(intent);
    }
}
