package com.example.seabattle.models;

import java.util.ArrayList;
import java.util.List;

public class RoomDb {
    public String id;
    public String field1;
    public String field2;
    public String hostUser;
    public String user;
    public StateGame stateGame = StateGame.UserTurn;
    public RoomDb(){
    }
}

