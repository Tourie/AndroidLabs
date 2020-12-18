package com.example.converter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class ConvertFragmentInfo extends Fragment {

    final String[] categories_list = {"Вес", "Объём", "Длина"};
    final String[] weight_list = {"кг", "г", "мг"};
    final String[] length_list = {"дм", "м", "см"};
    final String[] volume_list = {"дм3", "м3", "см3"};

    final public String KEY_INPUT_UNIT = "input_unit";
    final public String KEY_OUTPUT_UNIT = "output_unit";
    final public String KEY_INPUT_VAL = "inputVal";
    final public String KEY_OUTPUT_VAL = "outputVal";
    final public String KEY_CATEGORY = "category";

    private int indexCategory = 0;
    private int indexInputElement = 0;
    private int indexOutputElement = 0;
    private double inputVal = 0;
    private double outputVal = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState !=null) {
            indexCategory = savedInstanceState.getInt(KEY_CATEGORY);
            indexInputElement = savedInstanceState.getInt(KEY_INPUT_UNIT);
            indexOutputElement = savedInstanceState.getInt(KEY_OUTPUT_UNIT);
            inputVal = savedInstanceState.getDouble(KEY_INPUT_VAL);
            outputVal = savedInstanceState.getDouble(KEY_OUTPUT_VAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_convert_info, container, false);
         //Create spinner
        Spinner categories = (Spinner)view.findViewById(R.id.category);
        Spinner input_unit = (Spinner)view.findViewById(R.id.input_unit);
        Spinner output_unit = (Spinner)view.findViewById(R.id.output_unit);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), R.layout.support_simple_spinner_dropdown_item, categories_list);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
        onRestoreState(view);

        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println("Selected " + parent.getItemAtPosition(position));
                String selectedItem = (String)parent.getItemAtPosition(position);
                Spinner category = (Spinner) getView().findViewById(R.id.category);
                Spinner input_unit = (Spinner)getView().findViewById(R.id.input_unit);
                Spinner output_unit = (Spinner)getView().findViewById(R.id.output_unit);
                SetSpinnerMenu(category,input_unit,-1);
                SetSpinnerMenu(category,output_unit,-1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("NoSelected");
            }
        });
        return view;
    }


    public void onRestoreState(View view){
        Spinner category = (Spinner)view.findViewById(R.id.category);
        Spinner input_unit = (Spinner)view.findViewById(R.id.input_unit);
        Spinner output_unit = (Spinner)view.findViewById(R.id.output_unit);
        EditText inputEdit = (EditText)view.findViewById(R.id.input_edit_text);
        EditText outputEdit = (EditText)view.findViewById(R.id.output_edit_text);

        category.setSelection(indexCategory, true);
        SetSpinnerMenu(category, input_unit, indexInputElement);
        SetSpinnerMenu(category,output_unit,indexOutputElement);
        inputEdit.setText(String.valueOf(inputVal));
        outputEdit.setText(String.valueOf(outputVal));
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

    @Override
    public void onSaveInstanceState(Bundle bundle){

        Spinner category = (Spinner)getView().findViewById(R.id.category);
        Spinner input_unit = (Spinner)getView().findViewById(R.id.input_unit);
        Spinner output_unit = (Spinner)getView().findViewById(R.id.output_unit);
        EditText inputEdit = (EditText)getView().findViewById(R.id.input_edit_text);
        EditText outputEdit = (EditText)getView().findViewById(R.id.output_edit_text);

        bundle.putInt(KEY_CATEGORY, category.getSelectedItemPosition());
        bundle.putInt(KEY_INPUT_UNIT, input_unit.getSelectedItemPosition());
        bundle.putInt(KEY_OUTPUT_UNIT, output_unit.getSelectedItemPosition());
        bundle.putDouble(KEY_INPUT_VAL, Double.parseDouble(inputEdit.getText().toString()));
        bundle.putDouble(KEY_OUTPUT_VAL, Double.parseDouble(outputEdit.getText().toString()));
        super.onSaveInstanceState(bundle);
    }
}