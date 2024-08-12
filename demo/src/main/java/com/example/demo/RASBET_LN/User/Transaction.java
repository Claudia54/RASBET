package com.example.demo.RASBET_LN.User;

import java.util.HashMap;
import java.util.Map;

public class Transaction{

    public static String deposit = "Deposit";

    //Money that was withdraw by the user
    public static String withdraw = "Withdraw";

    //Money that was earn from a bet
    public static String won = "Bet Won";

    //Money that was betted
    public static String bet = "Money Betted";

    //Change coin:
    public static String coin = "Change Currency";

    public static String bonus = "Money from Bonus";

    public static String euro = "EUR";

    private String type;
    private float amount;

    public Transaction(String type, float amount){
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    private String stringType(){
        if(this.type.equals(deposit)){
            return "Dinheiro depositado na carteira.";
        }
        else if(this.type.equals(withdraw)){
            return "Dinheiro levantado da carteira.";
        }
        else if(this.type.equals(won)){
            return "Dinheiro ganho numa aposta.";
        }
        else if(this.type.equals(bet)){
            return "Dinheiro usado para pagar uma aposta.";
        }
        else if(this.type.equals(coin)){
            return "Mudança de moeda.";
        }
        else if(this.type.equals(bonus)){
            return "Saldo bonus ganho por uma promoção";
        }
        return "";
    }

    public Map<String,String> returnMap(){
        Map<String,String> res = new HashMap<>();
        res.put(stringType(), String.valueOf(this.amount));
        return res;
    }

    public String toString(){
        String output = "";
        if(this.type.equals(deposit)||this.type.equals(won)){
            output+= "| "+this.type+" | +"+this.amount+" |";
        }
        else if(this.type.equals(coin)){
            output += "| "+this.type+" | -------- |";
        }
        else{
            output+= "| "+this.type+" | -"+this.amount+" |";
        }
        return output;
    }
}
