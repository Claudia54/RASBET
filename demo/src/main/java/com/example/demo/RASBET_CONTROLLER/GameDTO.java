package com.example.demo.RASBET_CONTROLLER;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameDTO {
    private String idGame;
    private int idSport;
    private String date;
    private String outcome;
    private float X;
    private boolean followed;
    private Map<String, Float> participantsOdds = new LinkedHashMap<>();

    public GameDTO(List<Map<String,String>> l){
        Map<String,String> aux = l.get(0);
        this.idGame = aux.get("idGame");
        this.idSport = Integer.parseInt(aux.get("idSport"));
        this.date = aux.get("date");
        this.outcome = aux.get("outcome");
        this.X = Float.valueOf(aux.get("X"));
        this.followed = false;
        for (Map.Entry<String,String> e : l.get(1).entrySet()){
            this.participantsOdds.put(e.getKey(),Float.valueOf(e.getValue()));
        }
    }

    public String getIdGame() {
        return this.idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public int getIdSport() {
        return this.idSport;
    }

    public void setIdSport(int idSport) {
        this.idSport = idSport;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOutcome() {
        return this.outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public float getX() {
        return this.X;
    }

    public void setX(float x) {
        X = x;
    }

    public Map<String, Float> getParticipantsOdds() {
        return this.participantsOdds;
    }

    public void setParticipantsOdds(Map<String, Float> participantsOdds) {
        this.participantsOdds = participantsOdds;
    }

    public boolean getFollowed(){
        return this.followed;
    }
    public void setFollowed(boolean val){
        this.followed =val;
    }


    public List<Map<String,String>> returnMap(){
        List<Map<String,String>> l = new ArrayList<>();
        Map<String,String> m = new LinkedHashMap<>();
        m.put("idGame", this.idGame);
        m.put("idSport", String.valueOf(this.idSport));
        m.put("date", this.date.toString());
        m.put("outcome", this.outcome);
        m.put("X", String.valueOf(this.X));
        l.add(m);
        m.clear();
        for (Map.Entry<String,Float> e : this.participantsOdds.entrySet()){
            m.put(e.getKey(), String.valueOf(e.getValue()));
        }
        l.add(m);
        return l;
    }

}
