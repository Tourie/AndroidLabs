package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameActivity extends AppCompatActivity {

    LinearLayout myField;
    LinearLayout enemyField;
    DatabaseReference myRef;
    String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        roomId = intent.getStringExtra("RoomId");

        myRef = FirebaseDatabase.getInstance().getReference("Rooms").child(roomId);

        myField = findViewById(R.id.my_field);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                100, 100);
        params.setMargins(2, 2, 2, 2);
        for (int i=0; i < 10; ++i) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            myField.addView(linearLayout);
            for (int j = 0; j < 10; j++) {
                Button btn = new Button(getApplicationContext());
                btn.setId(i * 10 + j);
                btn.setLayoutParams(params);
                linearLayout.addView(btn);
            }
        }

        enemyField = findViewById(R.id.enemy_field);
    }

    private void disableButton(int id) {
        if(id >=0 ){
            Button btn = findViewById(id);
            btn.setEnabled(false);
        }
    }

    private void enableButton(int id){
        if(id >=0 ){
            Button btn = findViewById(id);
            btn.setEnabled(true);
            btn.setText("");
        }
    }
}