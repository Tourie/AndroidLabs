package com.example.seabattle.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Room implements Serializable {
    public String id;
    public List<List<Integer>> field1 = new ArrayList<List<Integer>>();
    public List<List<Integer>> field2 = new ArrayList<List<Integer>>();
    public Room(){

    }
}
