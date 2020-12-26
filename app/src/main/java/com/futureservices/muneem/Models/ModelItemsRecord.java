package com.futureservices.muneem.Models;

public class ModelItemsRecord {
    public int TransactionID;
    public String date;
    public int ExpenseType;
    public String ItemName;
    public Double ItemPrice;


    public ModelItemsRecord(int transactionID, String date, Integer expenseType, String itemName, Double itemPrice) {
        TransactionID = transactionID;
        this.date = date;
        ExpenseType = expenseType;
        ItemName = itemName;
        ItemPrice = itemPrice;
    }

    public int getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(int transactionID) {
        TransactionID = transactionID;
    }

    public ModelItemsRecord() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getExpenseType() {
        return ExpenseType;
    }

    public void setExpenseType(Integer expenseType) {
        ExpenseType = expenseType;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public Double getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        ItemPrice = itemPrice;
    }
}
