package com.futureservices.muneem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalVariables {
    //Globals Instance
    private static final GlobalVariables globalInstance = new GlobalVariables();

    public static GlobalVariables getGlobalInstance() {
        return globalInstance;
    }

    public static GlobalVariables setGlobalInstance(GlobalVariables gv) {
        GlobalVariables globalInstance = new GlobalVariables();
        return globalInstance;
    }


    public Double CashAvailable;
    public Double AccountAvailable;
    public String DateOfExpense;
    public String CurrentDate;
    public String Memo;
    public boolean IsNewUser;
    public Double TotalMoneyAdded;
    public Double TotalExpense;
    public String ReasonAccount;
    public Integer ExpenseType;
    public Double ExpenseCapacity;
    public Double MilkRate = 0.0;
    public boolean FirstTime = true;

    public boolean isFirstTime() {
        return FirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        FirstTime = firstTime;
    }

    public Double getMilkRate() {
        return MilkRate;
    }

    public void setMilkRate(Double milkRate) {
        MilkRate = milkRate;
    }

    public Double getExpenseCapacity() {
        return ExpenseCapacity;
    }

    public void setExpenseCapacity(Double expenseCapacity) {
        ExpenseCapacity = expenseCapacity;
    }

    public Integer getExpenseType() {
        return ExpenseType;
    }

    public void setExpenseType(Integer expenseType) {
        ExpenseType = expenseType;
    }

    public String getReasonAccount() {
        return ReasonAccount;
    }

    public void setReasonAccount(String reasonAccount) {
        ReasonAccount = reasonAccount;
    }

    public String getReasoncash() {
        return Reasoncash;
    }

    public void setReasoncash(String reasoncash) {
        Reasoncash = reasoncash;
    }

    public String Reasoncash;

    public Double getTotalMoneyAdded() {
        return TotalMoneyAdded;
    }

    public void setTotalMoneyAdded(Double totalMoneyAdded) {
        TotalMoneyAdded = totalMoneyAdded;
    }

    public Double getTotalExpense() {
        return TotalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        TotalExpense = totalExpense;
    }


    public Double getCashAvailable() {
        return CashAvailable;
    }

    public void setCashAvailable(Double cashAvailable) {
        CashAvailable = cashAvailable;
    }

    public Double getAccountAvailable() {
        return AccountAvailable;
    }

    public void setAccountAvailable(Double accountAvailable) {
        AccountAvailable = accountAvailable;
    }

    public String getDateOfExpense() {
        return DateOfExpense;
    }

    public void setDateOfExpense(String dateOfExpense) {
        DateOfExpense = dateOfExpense;
    }

    public String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new Date();
        CurrentDate = formatter.format(date);
        return CurrentDate;
    }

    public  String GetUIDate(String Date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = new Date();
        Date = formatter.format(date);
        return Date;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public boolean isNewUser() {
        return IsNewUser;
    }

    public void setNewUser(boolean newUser) {
        if (newUser) {
            setCashAvailable(0.0);
            setAccountAvailable(0.0);
            setMemo("");
        }
        IsNewUser = newUser;
    }
}
