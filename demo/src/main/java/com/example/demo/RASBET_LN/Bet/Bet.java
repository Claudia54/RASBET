package com.example.demo.RASBET_LN.Bet;

import java.util.*;

public class Bet {
    private int idBet;
    private float money;
    private float possible_earnings;
    private String email;
    private String state;
    private List<List<String>> guesses; //Cada uma

    private static String NOT_SET = "NOT_SET";
    private static String SET = "SET";
    private static String CONCLUDED = "CONCLUDED";


    private static int checked = 1;
    private static int not_checked = 0;

    //Constructor used for data that is loaded from the database:
    public Bet(int idBet, float money, float possible_earnings, String email, String state){
        this.idBet = idBet;
        this.money = money;
        this.possible_earnings = possible_earnings;
        this.email = email;
        this.state= state;
        this.guesses = new LinkedList<>();
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public void addGuesses(String idGame, String guess, String odd){
        List g = new LinkedList<String>();
        g.add(idGame);
        g.add(guess);
        g.add(odd);
        this.guesses.add(g);
    }

    public List<List<String>> getGuesses() {
        return guesses;
    }

    public float getPossible_earnings(){
        return this.possible_earnings;
    }

    public void setPossible_earnings(float possible_earnings){
        this.possible_earnings = possible_earnings;
    }

    public float getMoney(){
        return this.money;
    }

    public int getIdBet(){
        return this.idBet;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Email: ").append(this.email).append(" | ");
        sb.append("Bet: ").append(this.idBet).append(" | ");
        sb.append("Money: ").append(this.money).append(" | ");
        sb.append("Possible_Earnings: ").append(this.possible_earnings).append(" | ");
        sb.append("State: ").append(this.state).append("\n");
        for(List<String> l: this.guesses){
            sb.append("\t * Game: "+l.get(0)+" | Guess: "+l.get(1)+" | Odd: "+l.get(2));
        }
        return sb.toString();
    }

    public static String noset(){
        String val =  Bet.NOT_SET;
        return val;
    }

    public static String set(){
        String val =  Bet.SET;
        return val;
    }

    public static String concluded(){
        String val = Bet.CONCLUDED;
        return val;
    }

    public static int getChecked(){
        return checked;
    }
    public static int getNot_checked(){
        return not_checked;
    }

    /**
     * First list will be the general data.
     * The rest are the game, guess, odd;
     * @return List<List<String>>
     */
    public List<List<String>> notStructured(){
        List<List<String>> newData = new LinkedList<>();
        //Build a list for the lose data:

        List <String> l = new LinkedList<>();

        l.add(Float.toString(this.money));
        l.add(Float.toString(this.possible_earnings));
        newData.add(l);

        newData.addAll(guesses);
        return newData;
    }
}
