package com.example.demo.RASBET_LN;

import java.util.HashMap;
import java.util.Map;

public class Promotion {

    /**
     * friendCode
     * bonus -> registando-se com esta promoçao ativa recebe x bonus
     * odd multiplier
     */

    private String type;    //Type of the promotion
    private boolean state;  //Active or not
    private float amount;  //Value it has
    private float minVal;

    public Promotion(String type, float amount, float minval){
        this.type = type;
        this.state = false;
        this.amount = amount;
        this.minVal = minval;
    }

    public Promotion(String type, float amount, float minval, boolean state){
        this.type = type;
        this.state = state;
        this.amount = amount;
        this.minVal = minval;
    }

    public boolean getState(){
        return this.state;
    }

    public void setState(boolean state){
        this.state = state;
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

    public float getMinVal() {
        return minVal;
    }

    public void setMinVal(float minVal) {
        this.minVal = minVal;
    }

    public Map<String,String> returnMap(){
        Map<String,String> res = new HashMap<>();

        res.put(this.type , this.toString());
        return res;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        if(type.equals("friendBonus")){
            sb.append("Bonus do código de amigo. \r\nValor recompensado é "+amount+ " euros.\r\n Esta promoção está ");
            if(this.state)
                sb.append("ativa.");
            else
                sb.append("desativa.");
        }

        if(type.equals("bonus")){
            sb.append("Bonus atribuido no registo. \r\nValor recompensado é "+amount+ " euros. \r\nEsta promoção está ");
            if(this.state)
                sb.append("ativa.");
            else
                sb.append("desativa.");
        }

        if(type.equals("multi")){
            sb.append("Odds a cima de "+minVal+" serão multiplicados por "+amount+ ".\r\n Esta promoção está ");
            if(this.state)
                sb.append("ativa.");
            else
                sb.append("desativa.");
        }
        return sb.toString();
    }

}
