package com.example.demo.RASBET_CONTROLLER;

import java.util.LinkedList;
import java.util.List;

public class BetDTO {

    private float money;
    private float possible_earnings;

    /*
     * Description of guesses:
     * Each bet can have more than one guess
     * Each guess is composed by:
     *      idGame, result guessed, odd of that result guessed, name Part
     */
    private List<List<String>> guesses;

    // lista de game on bet

    public BetDTO(List<List<String>> dataNotStructure){
        this.guesses = new LinkedList<>();
        if(!dataNotStructure.isEmpty()) {

            List<String> data = dataNotStructure.get(0);

            this.money = Float.valueOf(data.get(0));
            this.possible_earnings = Float.valueOf(data.get(1));

            //this.guesses = new LinkedList<>();

            int i = 0;
            for (List<String> l1 : dataNotStructure) {
                if (i != 0) {
                    this.guesses.add(l1);
                }
                i++;
            }
        }
    }


    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getPossible_earnings() {
        return possible_earnings;
    }

    public void setPossible_earnings(float possible_earnings) {
        this.possible_earnings = possible_earnings;
    }

    public List<List<String>> getGuesses() {
        return guesses;
    }

    public void setGuesses(List<List<String>> guesses) {
        this.guesses = guesses;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Money: ").append(this.money).append(" | ");
        sb.append("Possible_Earnings: ").append(this.possible_earnings).append("\n");
        for(List<String> l: this.guesses){
            sb.append("\t * Game: "+l.get(0)+" | Guess: "+l.get(1)+" | Odd: "+l.get(2)+"\n");
        }
        return sb.toString();
    }

}
