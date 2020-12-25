package com.futureservices.muneem;

public class ModelMilkRecord {
    public String Date;
    public Double MilkQuantity;
    public Double MilkRate;

    public ModelMilkRecord(String date, Double milkQuantity, Double milkRate) {
        Date = date;
        MilkQuantity = milkQuantity;
        MilkRate = milkRate;
    }

    public ModelMilkRecord() {

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Double getMilkQuantity() {
        return MilkQuantity;
    }

    public void setMilkQuantity(Double milkQuantity) {
        MilkQuantity = milkQuantity;
    }

    public Double getMilkRate() {
        return MilkRate;
    }

    public void setMilkRate(Double milkRate) {
        MilkRate = milkRate;
    }
}
