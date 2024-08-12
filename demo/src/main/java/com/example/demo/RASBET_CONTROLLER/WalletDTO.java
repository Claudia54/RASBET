package com.example.demo.RASBET_CONTROLLER;

import java.util.Map;

public class WalletDTO {

    private float balance;

    private String currency;

    private float bonusBalance;

    public WalletDTO(Map<String, String> l){
        this.balance = Float.valueOf(l.get("balance"));
        this.currency = l.get("currency");
        this.bonusBalance = Float.valueOf(l.get("bonusBalance"));
    }

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

    @Override
    public String toString() {
        return "WalletDTO{" +
                "balance=" + balance +
                ", currency='" + currency + '\'' +
                ", bonusBalance=" + bonusBalance +
                '}';
    }
}
