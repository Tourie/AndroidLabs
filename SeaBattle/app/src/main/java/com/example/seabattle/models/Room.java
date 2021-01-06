package com.example.seabattle.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Room implements Serializable {
    public String id;
    public int[][] field1;
    public int[][] field2;
    public String hostUser;
    public String user;
    public StateGame stateGame;
    public Room(){
    }
}
