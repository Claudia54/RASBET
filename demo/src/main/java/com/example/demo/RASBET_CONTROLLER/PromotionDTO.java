package com.example.demo.RASBET_CONTROLLER;

import java.util.Map;

public class PromotionDTO {

    private String type;    //Type of the promotion
    private String description;

    public PromotionDTO(Map.Entry<String, String> l){
        this.type = l.getKey();
        this.description = l.getValue();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return "PromotionDTO{" +
                "type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
