package com.example.seabattle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seabattle.models.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinGameActivity extends CreateGameActivity {

    EditText roomId;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        roomId = findViewById(R.id.room_id_input);
        mRef = FirebaseDatabase.getInstance().getReference("Rooms");
        field = findViewById(R.id.field);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                100, 100);
        params.setMargins(2, 2, 2, 2);
        for (int i=0; i < 10; ++i) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            field.addView(linearLayout);
            for (int j = 0; j < 10; j++) {
                Button btn = new Button(getApplicationContext());
                btn.setId(i * 10 + j);
                btn.setLayoutParams(params);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn.setText("К");
                        btn.setTextColor(Color.BLACK);
                        btn.setEnabled(false);
                        clickedButtonsId.add(btn.getId());
                    }
                });
                linearLayout.addView(btn);
            }
        }
    }
    @Override
    public void AfterFieldCreation() {
        String roomIdString = roomId.getText().toString();
        Room room = new Room();
        room.id = roomId.getText().toString();
//        for (int i=0; i<10; i++){
//            Integer[] integers = new Integer[10];
//            for (int index = 0; index<10; ++index){
//                integers[index] = fieldArray[i][index];
//            }
//            List<Integer> list = Arrays.asList(integers);
//            room.field2.add(list);
//        }

        Gson gson = new Gson();
        Map<String, Object> update = new HashMap<>();
        update.put("/user", FirebaseAuth.getInstance().getCurrentUser().getUid());
        update.put("/field2", gson.toJson(fieldArray));
        mRef.child(roomId.getText().toString().replace("\"", "").trim()).updateChildren(update).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                //start game
                Intent localIntent = new Intent(getApplicationContext(), GameActivity.class);
                localIntent.putExtra("RoomId", roomId.getText().toString().replace("\"", "").trim());
                startActivity(localIntent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Что-то пошло не так.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}