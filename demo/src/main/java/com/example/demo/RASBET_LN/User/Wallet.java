package com.example.demo.RASBET_LN.User;

import java.util.HashMap;
import java.util.Map;

public class Wallet {

    private float balance;

    private String currency;

    private float bonusBalance;


    public float getBalance() {
        return this.balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getBonusBalance() {
        return this.bonusBalance;
    }

    public void setBonusBalance(float bonusBalance) {
        this.bonusBalance = bonusBalance;
    }

    public Map<String,String> returnMap(){
        Map<String,String> res = new HashMap<>();

        res.put("balance", String.valueOf(this.balance));
        res.put("currency", this.currency);
        res.put("bonusBalance", String.valueOf(this.bonusBalance));

        return res;
    }

}
