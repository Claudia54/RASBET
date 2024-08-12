package com.example.demo.RASBET_LN.Sport;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
    final public String ACT = "active";
    final public String SUS = "suspended";
    final public String CLO = "closed";
    final private String OUT_X = "X";
    private String idGame;
    private int idSport;
    private String state;
    private LocalDateTime date;
    private String outcome; //Should be the index of the Participant that won;
    private float X;
    private Map<String, Float> participantsOdds = new LinkedHashMap<>();

    public Game(){}

    public Game(String idGame, int idSport, String state, String outcome, LocalDateTime date){
        this.idGame = idGame;
        this.idSport = idSport;
        this.state = state;
        this.outcome = outcome;
        this.date = date;
    }

    public Game(String idGame, int id_Sport, String outcome, LocalDateTime date){
        this.idGame = idGame;
        this.idSport = id_Sport;
        this.state = ACT;
        this.outcome = outcome;
        this.date = date;
    }

    public void addParticipant(String team, float odd){
        this.participantsOdds.put(team, odd);
    }

    public void addDraw(float odd){
        this.X = odd;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getIdGame(){
        return this.idGame;
    }

    public int getIdSport(){
        return this.idSport;
    }

    public String getState(){
        return this.state;
    }

    public float getX(){
        return this.X;
    }

    /**
     * Should be the index of the user that won;
     * Or Draw
     * @return
     */
    public String getOutcome(){
        return this.outcome;
    }

    public LocalDateTime getDate(){return this.date;}

    /**
     * Function that returns the odd of a participant given an index(idx). This index must start in 0.
     * @param idx
     * @return float
     */
    public float retOddIdx(int idx){
        float odd = 1;
        int i = 0;
        for(Map.Entry<String,Float> p : this.participantsOdds.entrySet()){
            if(i == idx) {
                odd = p.getValue();
                break;
            }
            i++;
        }
        return odd;
    }

    public Map<String, Float> getParticipantsOdds() {
        return participantsOdds;
    }

    public static String closed(){
        Game g = new Game();
        String s = g.CLO;
        g = null;
        return s;
    }

    public static String open(){
        Game g = new Game();
        String s = g.ACT;
        g = null;
        return s;
    }

    public static String suspended(){
        Game g = new Game();
        String s = g.SUS;
        g = null;
        return s;
    }

    public static String drawOutcome(){
        Game g = new Game();
        String s = g.OUT_X;
        g = null;
        return s;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        String date = Game.parseDate(this.getDate());
        sb.append(this.getIdGame()).append( " | ");
        sb.append(this.getIdSport()).append(" | ");
        sb.append(this.getState()).append(" | ");
        sb.append(this.getOutcome()).append(" | ");
        sb.append(date).append(" | ");
        sb.append(this.getX()).append(" | ");
        for(Map.Entry<String, Float> e: this.participantsOdds.entrySet()){
            sb.append(e.getKey()).append(" | ");
            sb.append(e.getValue()).append(" | ");
        }
        return sb.toString();
    }

    /**
     * Function that should be call if the result insert is _x_:
     * @param result
     * @return
     */
    public static String parseResult2WithDraw(String result){
        String[] tokens = result.split("x");
        int home = 0, away = 0;
        try {
            home = Integer.parseInt(tokens[0]);
            away = Integer.parseInt(tokens[1]);
        }
        catch (NumberFormatException n){
            return result;
        }

        String outcome = "";
        if(home == away){
            outcome = Game.drawOutcome(); //Draw
        }
        else if (home > away){
            outcome = Integer.toString(1); //V1
        }
        else if(home < away){
            outcome = Integer.toString(2); //V2
        }
        return outcome;
    }

    /**
     * Function that should be used if the name of the participant is passed
     * @param result == Name
     * @return String == idx
     */
    public String parseResultName(String result){
        String outcome = "";
        if(this.participantsOdds.keySet().contains(result)){
            int i = 0;
            for (String s: this.participantsOdds.keySet()){
                i++;
                if(s.equals(result)){
                    outcome = Integer.toString(i);
                    break;
                }
            }
        }
        return outcome;
    }

    public String parseResult(String result){
        String outcome;

        String regex = "(?=.*[0-9])x(?=.*[0-9])"+"(?=\\S+$).{3}"; // Can't have whitespaces
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);
        boolean matchFound = matcher.find();
        if(matchFound)
            outcome = parseResult2WithDraw(result);
        else
            outcome = parseResultName(result);

        return outcome;
    }

    public static String parseDate(LocalDateTime date){
        DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateDB = formatterDB.format(date);
        return dateDB;
    }


    public boolean isClosed(){
        if(this.state.equals(Game.closed()))
            return true;
        else return false;
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
        Map<String,String> m1 = new LinkedHashMap<>();
        for (Map.Entry<String,Float> e : this.participantsOdds.entrySet()){
            m1.put(e.getKey(), String.valueOf(e.getValue()));
        }
        l.add(m1);
        return l;
    }

}
