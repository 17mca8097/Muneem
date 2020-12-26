package com.futureservices.muneem.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.Models.ModelAccountRecord;
import com.futureservices.muneem.Models.ModelCashRecord;
import com.futureservices.muneem.Models.ModelItemsRecord;
import com.futureservices.muneem.Models.ModelMilkRecord;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String CashTransaction = "CashTransaction";
    public static final String TransactionID = "TransactionID";
    public static final String Date = "Date";
    public static final String Mode = "Mode";
    public static final String Direction = "Direction";
    public static final String Amount = "Amount";
    public static final String AvailableCash = "AvailableCash";
    public static final String Subject = "Subject";
    public static final String AccountTransaction = "AccountTransaction";
    public static final String AvailableAccount = "AvailableAccount";
    public static final String Items = "Items";
    public static final String ItemName = "ItemName";
    public static final String ItemPrice = "ItemPrice";
    public static final String Expense_Type = "ExpenseType";
    public static final String MilkRecord = "MilkRecord";
    public static final String MilkQuantity = "MilkQuantity";
    public static final String MilkRate = "MilkRate";
    public static final String MilkMode = "MilkMode";
    GlobalVariables globals = GlobalVariables.getGlobalInstance();

    public DatabaseHelper(@Nullable Context context) {
        super(context, "MuneemDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //region createCashTransTable
        String createCashTransTable = "CREATE TABLE " + CashTransaction + "(" +
                TransactionID + " INTEGER NOT NULL UNIQUE," +
                Date + " TEXT NOT NULL," +
                Mode + " INTEGER NOT NULL," +
                Direction + " INTEGER NOT NULL," +
                Amount + " REAL NOT NULL," +
                AvailableCash + " REAL NOT NULL," +
                Subject + " TEXT NOT NULL," + "PRIMARY KEY (" + TransactionID + " AUTOINCREMENT) " + ");";
        //endregion

        //region createAccountTransTable
        String createAccountTransTable = "CREATE TABLE " + AccountTransaction + "(" +
                TransactionID + " INTEGER NOT NULL UNIQUE," +
                Date + " TEXT NOT NULL," +
                Mode + " INTEGER NOT NULL," +
                Direction + " INTEGER NOT NULL," +
                Amount + " REAL NOT NULL," +
                AvailableAccount + " REAL NOT NULL," +
                Subject + " TEXT NOT NULL," +
                "PRIMARY KEY (" + TransactionID + " AUTOINCREMENT));";
        //endregion

        //region createItemTable
        String createItemTable = "CREATE TABLE " + Items + "(" +
                TransactionID + " INTEGER NOT NULL UNIQUE," +
                Date + " TEXT NOT NULL," +
                ItemName + " TEXT NOT NULL," +
                ItemPrice + " REAL NOT NULL," +
                Expense_Type + " Text NOT NULL," +
                "PRIMARY KEY (" + TransactionID + " AUTOINCREMENT));";
        //endregion

        //region createMilkRecordTable
        String createMilkTable = "CREATE TABLE " + MilkRecord + "(" +
                TransactionID + " INTEGER NOT NULL UNIQUE," +
                Date + " TEXT NOT NULL," +
                MilkMode + " INTEGER NOT NULL," +
                MilkQuantity + " REAL NOT NULL," +
                MilkRate + " REAL NOT NULL," +
                "PRIMARY KEY (" + TransactionID + " AUTOINCREMENT));";

        //endregion
        try {
            db.execSQL(createCashTransTable);
            db.execSQL(createAccountTransTable);
            db.execSQL(createItemTable);
            db.execSQL(createMilkTable);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //region Checking NewUser
    public boolean CheckIfNewUser() {
        boolean isNewUser = true;
        Double TotalMoneyAdded = 0.0;
        String queryString = "SELECT SUM(`Amount`) AS `Total` FROM `CashTransaction` where Direction = 1 GROUP BY `Direction`;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            {
                TotalMoneyAdded += cursor.getDouble(0);
                isNewUser = false;
            }
        }
        cursor.close();

        queryString = "SELECT SUM(`Amount`) AS `Total` FROM `AccountTransaction` where Direction = 1 GROUP BY `Direction`;";
        cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            {
                TotalMoneyAdded += cursor.getDouble(0);
                isNewUser = false;
            }
        }
        cursor.close();

        queryString = "SELECT MilkRate FROM MilkRecord WHERE TRANSACTIONID = (SELECT MAX(TransactionID) FROM MilkRecord);";
        cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            {
                globals.setMilkRate(cursor.getDouble(0));
                isNewUser = false;
            }
        }
        cursor.close();

        globals.setTotalMoneyAdded(TotalMoneyAdded);

        cursor.close();
        db.close();
        return isNewUser;
    }

    public void GetTotalExpense() {
        Double TotalExpense = 0.0;
        String queryString = "SELECT SUM(`ItemPrice`) AS `Total` FROM `Items`;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            {
                TotalExpense += cursor.getDouble(0);
            }
        }
        globals.setTotalExpense(TotalExpense);
    }

    public void GetAccountAvailable() {
        Double AccountAvailable = 0.0;
        String ReasonAccount = "";
        String queryString = "SELECT AvailableAccount,Subject FROM AccountTransaction WHERE TRANSACTIONID = (SELECT MAX(TransactionID) FROM AccountTransaction);";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            {
                AccountAvailable += cursor.getDouble(0);
                ReasonAccount = cursor.getString(1);
            }
        }
        globals.setAccountAvailable(AccountAvailable);
        globals.setReasonAccount(ReasonAccount);
    }

    public void GetCashAvailable() {
        Double CashAvailable = 0.0;
        String ReasonCash = "";
        String queryString = "SELECT AvailableCash,Subject FROM CashTransaction WHERE TRANSACTIONID = (SELECT MAX(TransactionID) FROM CashTransaction);";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            {
                CashAvailable += cursor.getDouble(0);
                ReasonCash = cursor.getString(1);
            }
        }
        globals.setCashAvailable(CashAvailable);
        globals.setReasoncash(ReasonCash);
    }

    public boolean SaveCashTransaction(ModelCashRecord mcr) {
        Boolean Success = false;
        Double CashAvailable = 0.0;
        String ReasonCash = "";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Date, mcr.getDate());
        cv.put(Mode, mcr.getMode());
        cv.put(Direction, mcr.getDirection());
        cv.put(Amount, mcr.getAmount());
        cv.put(AvailableCash, mcr.getAvailableCash());
        cv.put(Subject, mcr.getSubject());

        long insert = db.insert(CashTransaction, null, cv);

        if (insert == -1) {
            Success = false;
        } else {
            globals.setCashAvailable(mcr.getAvailableCash());
            globals.setReasoncash(mcr.getSubject());
            Success = true;
        }
        CheckIfNewUser();
        GetTotalExpense();
        return Success;
    }

    public boolean SaveAccountTransaction(ModelAccountRecord mar) {
        Boolean Success = false;
        Double CashAvailable = 0.0;
        String ReasonCash = "";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Date, mar.getDate());
        cv.put(Mode, mar.getMode());
        cv.put(Direction, mar.getDirection());
        cv.put(Amount, mar.getAmount());
        cv.put(AvailableAccount, mar.getAvailableAccount());
        cv.put(Subject, mar.getSubject());

        long insert = db.insert(AccountTransaction, null, cv);

        if (insert == -1) {
            Success = false;
        } else {
            globals.setAccountAvailable(mar.getAvailableAccount());
            globals.setReasonAccount(mar.getSubject());
            Success = true;
        }
        CheckIfNewUser();
        GetTotalExpense();
        return Success;
    }

    public ArrayList<ModelItemsRecord> GetSavedItemList(String date) {
        ArrayList<ModelItemsRecord> listMIR = new ArrayList<>();

        String querryString = "SELECT * FROM Items " + " WHERE strftime('%Y',Date) = strftime('%Y'," + "'" + date + "'" + ") AND  strftime('%m',Date) = strftime('%m'," + "'" + date + "'" + ");";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querryString, null);

        if (cursor.moveToFirst()) {
            do {
                ModelItemsRecord mir = new ModelItemsRecord();
                mir.setTransactionID(cursor.getInt(0));
                mir.setDate(cursor.getString(1));
                mir.setItemName(cursor.getString(2));
                mir.setItemPrice(cursor.getDouble(3));
                mir.setExpenseType(cursor.getInt(4));
                listMIR.add(mir);

            } while (cursor.moveToNext());
        }
        return listMIR;
    }

    public long SaveItems(List<ModelItemsRecord> itemList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long insert = 0;
        for (ModelItemsRecord item : itemList) {
            cv.put(ItemName, item.getItemName());
            cv.put(ItemPrice, item.getItemPrice());
            cv.put(Date, item.getDate());
            cv.put(Expense_Type, item.getExpenseType());
            insert = db.insert(Items, null, cv);
        }
        return insert;
    }

    public ArrayList<ModelCashRecord> GetCashTransactions(String date) {
        ArrayList<ModelCashRecord> listMCR = new ArrayList<>();

        String queryString = "SELECT * FROM CashTransaction  " + " WHERE strftime('%Y',Date) = strftime('%Y'," + "'" + date + "'" + ") AND  strftime('%m',Date) = strftime('%m'," + "'" + date + "'" + ");";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                ModelCashRecord mcr = new ModelCashRecord();
                mcr.setDate(cursor.getString(1));
                mcr.setMode(cursor.getInt(2));
                mcr.setDirection(cursor.getInt(3));
                mcr.setAmount(cursor.getDouble(4));
                mcr.setAvailableCash(cursor.getDouble(5));
                mcr.setSubject(cursor.getString(6));
                listMCR.add(mcr);

            } while (cursor.moveToNext());
        }
        return listMCR;
    }

    public ArrayList<ModelAccountRecord> GetAccountTransaction(String date) {
        ArrayList<ModelAccountRecord> listMAR = new ArrayList<>();

        String queryString = "SELECT * FROM AccountTransaction  " + " WHERE strftime('%Y',Date) = strftime('%Y'," + "'" + date + "'" + ") AND  strftime('%m',Date) = strftime('%m'," + "'" + date + "'" + ");";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                ModelAccountRecord mar = new ModelAccountRecord();
                mar.setDate(cursor.getString(1));
                mar.setMode(cursor.getInt(2));
                mar.setDirection(cursor.getInt(3));
                mar.setAmount(cursor.getDouble(4));
                mar.setAvailableAccount(cursor.getDouble(5));
                mar.setSubject(cursor.getString(6));
                listMAR.add(mar);

            } while (cursor.moveToNext());
        }
        return listMAR;
    }

    public boolean SaveMilkRecords(ModelMilkRecord mmr) {
        boolean Success;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Date, mmr.getDate());
        cv.put(MilkQuantity, mmr.getMilkQuantity());
        cv.put(MilkRate, mmr.getMilkRate());
        cv.put(MilkMode,mmr.getMilkMode());

        long insert = db.insert(MilkRecord, null, cv);

        if (insert == -1) {
            Success = false;
        } else {
            Success = true;
        }
        return Success;
    }

//    public ArrayList<ModelMilkRecord> GetSavedMorningMilkRecord(String Date) {
//        ArrayList<ModelMilkRecord> listMMR = new ArrayList<>();
//
//        String querryString = "SELECT * FROM MilkRecord " + " WHERE strftime('%Y',Date) = strftime('%Y'," + "'" + Date + "'" + ") AND  strftime('%m',Date) = strftime('%m'," + "'" + Date + "'" + ");";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(querryString, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                ModelMilkRecord mmr = new ModelMilkRecord();
//                mmr.setDate(cursor.getString(1));
//                mmr.setMilkMode(cursor.getInt(2));
//                mmr.setMilkQuantity(cursor.getDouble(3));
//                mmr.setMilkRate(cursor.getDouble(4));
//
//                listMMR.add(mmr);
//
//            } while (cursor.moveToNext());
//        }
//        return listMMR;
//    }

    public ArrayList<ModelMilkRecord> GetSavedMorningMilkRecord(String Date,int milkMode) {
        ArrayList<ModelMilkRecord> listMMR = new ArrayList<>();

        String querryString = "SELECT * FROM MilkRecord " + " WHERE strftime('%Y',Date) = strftime('%Y'," + "'" + Date + "'" + ") AND  strftime('%m',Date) = strftime('%m'," + "'" + Date + "'" + ") AND MilkMode = " + milkMode + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querryString, null);

        if (cursor.moveToFirst()) {
            do {
                ModelMilkRecord mmr = new ModelMilkRecord();
                mmr.setDate(cursor.getString(1));
                mmr.setMilkMode(cursor.getInt(2));
                mmr.setMilkQuantity(cursor.getDouble(3));
                mmr.setMilkRate(cursor.getDouble(4));

                listMMR.add(mmr);

            } while (cursor.moveToNext());
        }
        return listMMR;
    }

    public boolean UpdateSavedList(ModelItemsRecord mir, int direction, Double newPrice) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long insert = 0;
        long transaction;
        cv.put(ItemName, mir.getItemName());
        cv.put(ItemPrice, mir.getItemPrice());
        cv.put(Date, mir.getDate());
        cv.put(Expense_Type, mir.getExpenseType());

        insert = db.update(Items, cv, "TransactionID=" + mir.getTransactionID(), null);
        if (insert == -1) {
            return false;
        } else {
            if (direction == -1) {
                if (mir.getExpenseType() == 1) {
                    Double AvailableCash = globals.getCashAvailable() - newPrice;
                    try {
                        this.SaveCashTransaction(new ModelCashRecord(mir.getDate(), 1, direction, newPrice, AvailableCash, "Record Modified On " + globals.getCurrentDate()));
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    Double AvailableAccount = globals.getAccountAvailable() - newPrice;
                    try {
                        this.SaveAccountTransaction(new ModelAccountRecord(mir.getDate(), 1, direction, newPrice, AvailableAccount, "Record Modified On " + globals.getCurrentDate()));
                    } catch (Exception e) {
                        throw e;
                    }
                }
            } else {
                if (mir.getExpenseType() == 1) {
                    Double AvailableCash = globals.getCashAvailable() + newPrice;
                    try {
                        this.SaveCashTransaction(new ModelCashRecord(mir.getDate(), 1, direction, newPrice, AvailableCash, "Record Modified On " + globals.getCurrentDate()));
                    } catch (Exception e) {
                        throw e;
                    }

                } else {
                    Double AvailableAccount = globals.getAccountAvailable() + newPrice;
                    try {
                        this.SaveAccountTransaction(new ModelAccountRecord(mir.getDate(), 1, direction, newPrice, AvailableAccount, "Record Modified On " + globals.getCurrentDate()));
                    } catch (Exception e) {
                        throw e;
                    }

                }
            }
            return true;
        }
    }

    public boolean DeleteItem(ModelItemsRecord mir) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long insert = 0;


        insert = db.delete(Items, "TransactionID=" + mir.getTransactionID(), null);
        if (insert == -1) {
            return false;
        } else {
            if (mir.getExpenseType() == 1) {
                Double AvailableCash = globals.getCashAvailable() + mir.getItemPrice();
                try {
                    this.SaveCashTransaction(new ModelCashRecord(mir.getDate(), 1, -1, mir.getItemPrice(), AvailableCash, "Record Deleted On " + globals.getCurrentDate()));
                } catch (Exception e) {
                    throw e;
                }
            } else {
                Double AvailableAccount = globals.getAccountAvailable() + mir.getItemPrice();
                try {
                    this.SaveAccountTransaction(new ModelAccountRecord(mir.getDate(), 1, -1, mir.getItemPrice(), AvailableAccount, "Record Deleted On " + globals.getCurrentDate()));
                } catch (Exception e) {
                    throw e;
                }
            }
            return true;
        }
    }


}