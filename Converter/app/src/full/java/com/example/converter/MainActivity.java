package com.example.converter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.content.ClipData;
import android.content.ClipboardManager;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String[] categories_list = {"Вес", "Объём", "Длина"};
    final String[] weight_list = {"кг", "г", "мг"};
    final String[] length_list = {"дм", "м", "см"};
    final String[] volume_list = {"дм3", "м3", "см3"};
    final String KEY_INPUT_UNIT = "input_unit";
    final String KEY_OUTPUT_UNIT = "output_unit";
    final String KEY_INPUT_VAL = "inputVal";
    final String KEY_OUTPUT_VAL = "outputVal";
    final String KEY_CATEGORY = "category";

    InfoViewModel viewModel = new InfoViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("OnCreate");
        //Create spinner
        Spinner categories = (Spinner)findViewById(R.id.category);
        Spinner input_unit = (Spinner)findViewById(R.id.input_unit);
        Spinner output_unit = (Spinner)findViewById(R.id.output_unit);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categories_list);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categories.setAdapter(adapter);

        onRestoreState();

        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println("Selected " + parent.getItemAtPosition(position));
                String selectedItem = (String)parent.getItemAtPosition(position);
                Spinner category = (Spinner) findViewById(R.id.category);
                Spinner input_unit = (Spinner)findViewById(R.id.input_unit);
                Spinner output_unit = (Spinner)findViewById(R.id.output_unit);
                SetSpinnerMenu(category,input_unit,-1);
                SetSpinnerMenu(category,output_unit,-1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("NoSelected");
            }
        });


    }

    public void onNumberClick(View view){
        Button button = (Button)view;
        EditText editText = (EditText)findViewById(R.id.input_edit_text);
        String number = button.getText().toString();
        if(editText.getText().length() == 0 && number.equals(".")) editText.append("0");
        editText.append(number);
    }

    public void onDeleteClick(View view) {
        EditText editText = (EditText)findViewById(R.id.input_edit_text);
        if(editText.length() != 0) {
            editText.setText(editText.getText().delete(editText.getText().length() - 1, editText.getText().length()));
        }
    }

    public void onConvertClick(View view) {
        Spinner spinner_category = (Spinner)findViewById(R.id.category);
        Spinner input_unit = (Spinner)findViewById(R.id.input_unit);
        Spinner output_unit = (Spinner)findViewById(R.id.output_unit);
        String input_type = input_unit.getSelectedItem().toString();
        String output_type = output_unit.getSelectedItem().toString();
        String category = spinner_category.getSelectedItem().toString();

        EditText input_edit_text = (EditText)findViewById(R.id.input_edit_text);
        EditText out_edit_text = (EditText)findViewById(R.id.output_edit_text);
        String input_val = input_edit_text.getText().toString();
        if (input_val.length() == 0) {
            Toast toast=Toast.makeText(getApplicationContext(),"Поле пустое",Toast.LENGTH_SHORT);
            toast.setMargin(50,50);
            toast.show();
        }
        else {
            try {
                if (category.equals("Вес")) {
                    weightConvert(input_val, input_type, output_type, out_edit_text);
                } else if (category.equals("Объём")) {
                    volumeConvert(input_val, input_type, output_type, out_edit_text);
                } else if (category.equals("Длина")) {
                    lengthConvert(input_val, input_type, output_type, out_edit_text);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Неизвестный тип конвертации", Toast.LENGTH_SHORT);
                    toast.setMargin(50, 50);
                    toast.show();
                }
            } catch (RuntimeException exception){
                out_edit_text.setText("Wrong input!");
            }
        }
    }

    public void weightConvert(String input_val, String input_type, String output_type, EditText out_edit_text) {
        double inputVal = Double.parseDouble(input_val);
        if(input_type == "кг") inputVal*= Math.pow(10,6);
        else if(input_type == "г") inputVal *= Math.pow(10,3);
        if(output_type == "г") inputVal /= Math.pow(10,3);
        else if(output_type == "кг") inputVal /= Math.pow(10,6);
        out_edit_text.setText(String.valueOf(inputVal));
    }
    public void volumeConvert(String input_val, String input_type, String output_type, EditText out_edit_text) {
        double inputVal = Double.parseDouble(input_val);
        if(input_type == "дм3") inputVal*= Math.pow(10,3);
        else if(input_type == "м3") inputVal *= Math.pow(10,6);
        if(output_type == "дм3") inputVal /= Math.pow(10,3);
        else if(output_type == "м3") inputVal /= Math.pow(10,6);
        out_edit_text.setText(String.valueOf(inputVal));
    }
    public void lengthConvert(String input_val, String input_type, String output_type, EditText out_edit_text) {
        double inputVal = Double.parseDouble(input_val);
        if(input_type == "дм") inputVal*= 10;
        else if(input_type == "м") inputVal *= 100;
        if(output_type == "дм") inputVal /= 10;
        else if(output_type == "м") inputVal /= 100;
        out_edit_text.setText(String.valueOf(inputVal));
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void SaveState(){
        Spinner category = (Spinner)findViewById(R.id.category);
        Spinner input_unit = (Spinner)findViewById(R.id.input_unit);
        Spinner output_unit = (Spinner)findViewById(R.id.output_unit);
        EditText inputEdit = (EditText)findViewById(R.id.input_edit_text);
        EditText outputEdit = (EditText)findViewById(R.id.output_edit_text);

        viewModel.SaveState(category.getSelectedItemPosition(),input_unit.getSelectedItemPosition(),
                output_unit.getSelectedItemPosition(),Double.parseDouble(inputEdit.getText().toString()),
                Double.parseDouble(outputEdit.getText().toString()));
    }

    public void onRestoreState(){
        Bundle result = viewModel.GetState();
        Spinner category = (Spinner)findViewById(R.id.category);
        Spinner input_unit = (Spinner)findViewById(R.id.input_unit);
        Spinner output_unit = (Spinner)findViewById(R.id.output_unit);
        EditText inputEdit = (EditText)findViewById(R.id.input_edit_text);
        EditText outputEdit = (EditText)findViewById(R.id.output_edit_text);

        category.setSelection(result.getInt(KEY_CATEGORY), true);

        SetSpinnerMenu(category, input_unit, result.getInt(KEY_INPUT_UNIT));
        SetSpinnerMenu(category,output_unit,result.getInt(KEY_OUTPUT_UNIT));
        inputEdit.setText(String.valueOf(result.getDouble(KEY_INPUT_VAL)));
        outputEdit.setText(String.valueOf(result.getDouble(KEY_OUTPUT_VAL)));
    }

    public void SetSpinnerMenu(Spinner category, Spinner spinner,int index) {
        ArrayAdapter<String>adapter;
        if(category.getSelectedItem().toString().equals("Объём")) {
            adapter = new ArrayAdapter<String>(category.getContext(), R.layout.support_simple_spinner_dropdown_item, volume_list);
        } else if(category.getSelectedItem().toString().equals("Вес")) {
            adapter = new ArrayAdapter<String>(category.getContext(), R.layout.support_simple_spinner_dropdown_item, weight_list);
        } else {
            adapter = new ArrayAdapter<String>(category.getContext(), R.layout.support_simple_spinner_dropdown_item, length_list);
        }
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (index!=-1) {
            spinner.setSelection(index, true);
        }
        spinner.setSelection(index,true);
    }

    public void CopyTextInput(View view) {
        EditText editText = (EditText)findViewById(R.id.input_edit_text);
        CopyText(editText.getText().toString());
    }

    public void CopyTextOutput(View view) {
        EditText editText = (EditText)findViewById(R.id.output_edit_text);
        CopyText(editText.getText().toString());
    }

    public void CopyText(String text){
        String label = "Успешно скопировано!";
        ClipboardManager clipboard = (ClipboardManager) getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }
}
