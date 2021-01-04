package com.example.seabattle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seabattle.models.Room;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreateGameActivity extends AppCompatActivity {

    LinearLayout field;
    TextView roomId;
    Button copyId;
    TextView instruction;

    protected int stage = 0;
    protected ArrayList<Integer> clickedButtonsId = new ArrayList<Integer>();
    protected int[][] fieldArray = new int[10][10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        String uniqueID = UUID.randomUUID().toString();
        roomId = findViewById(R.id.room_id);
        roomId.setText(uniqueID);

        copyId = findViewById(R.id.copy_id_btn);
        copyId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = "Успешно скопировано!";
                ClipboardManager clipboard = (ClipboardManager) getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(label, roomId.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });

        field = findViewById(R.id.field);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                100, 100);
        params.setMargins(2, 2, 2, 2);
        for (int i=0; i < 10; ++i){
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            field.addView(linearLayout);
            for (int j=0; j < 10; j++){
                Button btn = new Button(getApplicationContext());
                btn.setId(i*10+j);
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

        instruction = findViewById(R.id.instruction_ship_num);
        instruction.setText("4 клетки");
    }

    public void checkShip(View view) {
        if (stage == 0){
            //4-cell ship
            if(clickedButtonsId.size() != 4){
                Toast.makeText(getApplicationContext(), "Неправильно поставлен 4-ёх палубник!", Toast.LENGTH_SHORT).show();
                for (int index:clickedButtonsId) {
                    enableButton(index);
                }
                clickedButtonsId.clear();
                return;
            } else {
                int difference = clickedButtonsId.get(1)-clickedButtonsId.get(0);
                for (int i=2; i<4; i++){
                    int current_diff = clickedButtonsId.get(i)-clickedButtonsId.get(i-1);
                    if (difference != current_diff) {
                        Toast.makeText(getApplicationContext(), "Неправильно поставлен 4-ёх палубник!", Toast.LENGTH_SHORT).show();
                        for (int index:clickedButtonsId) {
                            enableButton(index);
                        }
                        clickedButtonsId.clear();
                        return;
                    }
                }

                for (int i=0; i<4; i++){
                    int id = clickedButtonsId.get(i);
                    fieldArray[id/10][id%10] = 1;
                    disableButtonsAround(id);
                }
                stage++;
                instruction.setText("3 клетки");
            }
        } else if (stage == 1 || stage == 2){
            //3-cell ship
            if(clickedButtonsId.size() != 3){
                Toast.makeText(getApplicationContext(), "Неправильно поставлен 3-ёх палубник!", Toast.LENGTH_SHORT).show();
                for (int index:clickedButtonsId) {
                    enableButton(index);
                }
                clickedButtonsId.clear();
                return;
            } else {
                int difference = clickedButtonsId.get(1)-clickedButtonsId.get(0);
                for (int i=2; i<3; i++){
                    int current_diff = clickedButtonsId.get(i)-clickedButtonsId.get(i-1);
                    if (difference != current_diff) {
                        Toast.makeText(getApplicationContext(), "Неправильно поставлен 3-ёх палубник!", Toast.LENGTH_SHORT).show();
                        for (int index:clickedButtonsId) {
                            enableButton(index);
                        }
                        clickedButtonsId.clear();
                        return;
                    }
                }

                for (int i=0; i<3; i++){
                    int id = clickedButtonsId.get(i);
                    fieldArray[id/10][id%10] = 1;
                    disableButtonsAround(id);
                }
                stage++;
                if(stage ==3){
                    instruction.setText("2 клетки");
                }
            }

        } else if(stage == 3 || stage == 4 || stage == 5) {
            if(clickedButtonsId.size() != 2){
                Toast.makeText(getApplicationContext(), "Неправильно поставлен 2-ух палубник!", Toast.LENGTH_SHORT).show();
                for (int index:clickedButtonsId) {
                    enableButton(index);
                }
                clickedButtonsId.clear();
                return;
            } else {
                int difference = clickedButtonsId.get(1)-clickedButtonsId.get(0);
                if (difference != 1 && difference != 10) {
                    Toast.makeText(getApplicationContext(), "Неправильно поставлен 2-ух палубник!", Toast.LENGTH_SHORT).show();
                    for (int index:clickedButtonsId) {
                        enableButton(index);
                    }
                    clickedButtonsId.clear();
                    return;
                }

                for (int i=0; i<2; i++){
                    int id = clickedButtonsId.get(i);
                    fieldArray[id/10][id%10] = 1;
                    disableButtonsAround(id);
                }
                stage++;
                if(stage == 6){
                    instruction.setText("1 клетка");
                }
            }

        } else if (stage == 6 || stage == 7 || stage == 8 || stage == 9){
            //1-cell ship
            if(clickedButtonsId.size() != 1){
                Toast.makeText(getApplicationContext(), "Неправильно поставлен 1-о палубник!", Toast.LENGTH_SHORT).show();
                for (int index:clickedButtonsId) {
                    enableButton(index);
                }
                clickedButtonsId.clear();
                return;
            } else {

                int id = clickedButtonsId.get(0);
                fieldArray[id/10][id%10] = 1;
                disableButtonsAround(id);

                stage++;
                if (stage > 9) {
                    for (int i=0; i<100; ++i){
                        disableButton(i);
                    }
                    instruction.setText("Все корабли расставлены!");
                    Button check = findViewById(R.id.check_btn);
                    check.setText("Начать игру!");
                }
            }

        } else {
            AfterFieldCreation();
        }
        clickedButtonsId.clear();
    }

    protected void AfterFieldCreation() {
        Room field = new Room();
        field.id = roomId.getText().toString();
        for (int i=0; i<10; i++){
            Integer[] integers = new Integer[10];
            for (int index = 0; index<10; ++index){
                integers[index] = fieldArray[i][index];
            }
            List<Integer> list = Arrays.asList(integers);
            field.field1.add(list);
        }
        // Save and create field in db

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("Rooms");
        myRef.child(roomId.getText().toString()).setValue(field);

        Query query = myRef;
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Room room = snapshot.getValue(Room.class);
//                if (room.id == roomId.getText().toString()){
//                    if (room.field2.size()>0){
//
//                    }
//                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Intent localIntent = new Intent(getApplicationContext(), GameActivity.class);
                localIntent.putExtra("RoomId", roomId.getText().toString());
                startActivity(localIntent);
                finish();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void disableButtonsAround(int id) {
        //above line
        for (int i=-11; i<=-9; i++){
            if((id+i)>=0 && id/10-1 == (id+i)/10){
                disableButton(id+i);
            }
        }
        //on line left
        if((id-1)/10 == id/10){
            disableButton(id-1);
        }
        //on line right
        if((id+1)/10 == id/10){
            disableButton(id+1);
        }
        //under line
        for (int i=9; i<=11; i++){
            if((id+i)<100 && id/10+1 == (id+i)/10){
                disableButton(id+i);
            }
        }
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