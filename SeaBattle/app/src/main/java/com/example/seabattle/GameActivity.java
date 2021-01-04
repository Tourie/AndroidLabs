package com.example.seabattle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.seabattle.models.Room;
import com.example.seabattle.models.RoomDb;
import com.example.seabattle.models.StateGame;
import com.example.seabattle.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    LinearLayout myField;
    LinearLayout enemyField;
    DatabaseReference myRef;
    String roomId;
    Room room_cur;

    // Cells states:
    //0 - start
    //1 - ship
    //2 - miss
    //3 - kill


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        roomId = intent.getStringExtra("RoomId");
        myRef = FirebaseDatabase.getInstance().getReference("Rooms").child(roomId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RoomDb room = snapshot.getValue(RoomDb.class);
                room_cur = new Room();
                room_cur.id = room.id;
                room_cur.hostUser = room.hostUser;
                room_cur.stateGame = room.stateGame;
                room_cur.user = room.user;
                Gson gson = new Gson();
                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(room.hostUser)){
                    room_cur.field1 = gson.fromJson(room.field1, int[][].class);
                    room_cur.field2 = gson.fromJson(room.field2, int[][].class);
                } else {
                    room_cur.field1 = gson.fromJson(room.field2, int[][].class);
                    room_cur.field2 = gson.fromJson(room.field1, int[][].class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RoomDb roomDb = snapshot.getValue(RoomDb.class);
                Gson gson = new Gson();
                room_cur.stateGame = roomDb.stateGame;
                if(roomDb.hostUser.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    room_cur.field1 = gson.fromJson(roomDb.field1, int[][].class);
                    room_cur.field2 = gson.fromJson(roomDb.field2, int[][].class);
                } else  {
                    room_cur.field2 = gson.fromJson(roomDb.field1, int[][].class);
                    room_cur.field1 = gson.fromJson(roomDb.field2, int[][].class);
                }

                if (room_cur.stateGame == StateGame.GameEnded) {
                    for (int i=100; i<200; ++i){
                        disableButton(i);
                    }
                } else {
                    updateAllButtons();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myField = findViewById(R.id.my_field);
        generateField(myField,0);
        for(int i=0; i<100; ++i){
            disableButton(i);
        }
        enemyField = findViewById(R.id.enemy_field);
        generateField(enemyField, 100);

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
        }
    }

    private void generateField(LinearLayout view, int second){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                75, 75);
        params.setMargins(2, 2, 2, 2);
        for (int i=0; i < 10; ++i) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            view.addView(linearLayout);
            for (int j = 0; j < 10; j++) {
                Button btn = new Button(getApplicationContext());
                btn.setId(i * 10 + j + second);
                btn.setLayoutParams(params);
                btn.setTextColor(Color.BLACK);
                linearLayout.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = btn.getId();
                        disableButton(btn.getId());
                        int state = room_cur.field2[(id-100)/10][(id-100)%10];
                        if (state == 1) {
                            btn.setText("X");
                            room_cur.field2[(id-100)/10][(id-100)%10] = 3;
                        } else{
                            room_cur.field2[(id-100)/10][(id-100)%10] = 2;
                            if(room_cur.hostUser.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                room_cur.stateGame = StateGame.UserTurn;
                            } else {
                                room_cur.stateGame = StateGame.HostTurn;
                            }
                        }
                        Gson gson = new Gson();
                        Map<String, Object> field = new HashMap<>();
                        if(room_cur.hostUser.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            field.put("/field2", gson.toJson(room_cur.field2));
                        } else {
                            field.put("/field1", gson.toJson(room_cur.field2));
                        }
                        room_cur.stateGame = isEnded() ? StateGame.GameEnded : room_cur.stateGame;
                        if (room_cur.stateGame == StateGame.GameEnded) {
                            DatabaseReference userProfile = FirebaseDatabase.getInstance().getReference("profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            userProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    Map<String, Object> stats = new HashMap<>();
                                    if (user.total != null) {
                                        stats.put("/total", user.total+1);
                                    }
                                    else {
                                        stats.put("/total", 0);
                                    }
                                    userProfile.updateChildren(stats);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        field.put("/stateGame", room_cur.stateGame);
                        myRef.updateChildren(field);
                    }
                });
            }
        }
    }

    private boolean isEnded() {
        int my_killed_ships = 0;
        int enemies_killed_ships = 0;
        for(int i=0; i<100; ++i){
            my_killed_ships += room_cur.field1[i/10][i%10] == 3 ? 1 : 0;
            enemies_killed_ships += room_cur.field2[i/10][i%10] == 3 ? 1 : 0;
        }
        if (my_killed_ships == 20){
            Toast.makeText(getApplicationContext(), "Вы проиграли", Toast.LENGTH_SHORT);
            return true;
        } else if(enemies_killed_ships == 20){
            Toast.makeText(getApplicationContext(), "Вы выиграли", Toast.LENGTH_SHORT);
            return true;
        } else {
            return false;
        }
    }

    private void updateAllButtons() {
        for (int i=0; i < 200; ++i) {
            Button btn = findViewById(i);
            int cur = i < 100 ? room_cur.field1[i/10][i%10] : room_cur.field2[(i-100)/10][(i-100)%10];
            if (i >= 100){
                if ((FirebaseAuth.getInstance().getCurrentUser().getUid().equals(room_cur.hostUser) &&
                    room_cur.stateGame == StateGame.HostTurn) || (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(room_cur.user) &&
                        room_cur.stateGame == StateGame.UserTurn)) {
                    enableButton(i);
                } else {
                    disableButton(i);
                }
            }
            switch (cur)
            {
                case 2:
                    btn.setText("*");
                    disableButton(i);
                    break;
                case 3:
                    btn.setText("X");
                    disableButton(i);
                    break;
            }
        }
    }
}