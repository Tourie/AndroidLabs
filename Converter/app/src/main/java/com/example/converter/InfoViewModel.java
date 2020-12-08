package com.example.converter;

import android.os.Bundle;

public class InfoViewModel {
    final String KEY_CATEGORY = "category";
    final String KEY_INPUT_UNIT = "input_unit";
    final String KEY_OUTPUT_UNIT = "output_unit";
    final String KEY_INPUT_VAL = "inputVal";
    final String KEY_OUTPUT_VAL = "outputVal";

    private int indexCategory = 0;
    private int indexInputElement = 0;
    private int indexOutputElement = 0;
    private double inputVal = 0;
    private double outputVal = 0;

    public void SaveState(int indexCategory, int indexInputElement, int indexOutputElement, double inputVal, double outputVal){
        this.indexCategory = indexCategory;
        this.indexInputElement = indexInputElement;
        this.indexOutputElement = indexOutputElement;
        this.inputVal = inputVal;
        this.outputVal = outputVal;
    }

    public Bundle GetState(){
        Bundle result = new Bundle();
        result.putInt(KEY_CATEGORY, indexCategory);
        result.putInt(KEY_INPUT_UNIT, indexInputElement);
        result.putInt(KEY_OUTPUT_UNIT, indexOutputElement);
        result.putDouble(KEY_INPUT_VAL, inputVal);
        result.putDouble(KEY_OUTPUT_VAL, outputVal);
        return result;
    }
}
