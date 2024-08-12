package com.example.demo.RASBET_CONTROLLER;

import java.util.Map;

public class TransactionDTO {
    private String type;
    private float amount;

    public TransactionDTO(Map.Entry<String, String> l){
        this.type = l.getKey();
        this.amount = Float.valueOf(l.getValue());
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

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }
}
