package com.futureservices.muneem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModelCashRecord {

    public String Date;
    public int Mode;
    public Integer Direction;
    public Double Amount;
    public Double AvailableCash;
    public String Subject;

    public ModelCashRecord() {
    }

    public ModelCashRecord(String date, int mode, Integer direction, Double amount, Double availableCash, String subject) {
        Date = date;
        Mode = mode;
        Direction = direction;
        Amount = amount;
        AvailableCash = availableCash;
        Subject = subject;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new Date();
        Date = formatter.format(date);
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getMode() {
        return Mode;
    }

    public void setMode(int mode) {
        Mode = mode;
    }

    public Integer getDirection() {
        return Direction;
    }

    public void setDirection(Integer direction) {
        Direction = direction;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public Double getAvailableCash() {
        return AvailableCash;
    }

    public void setAvailableCash(Double availableCash) {
        AvailableCash = availableCash;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }
}
