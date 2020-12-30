package com.amitthakare.sanskarschool.Model;

public class ModelList {
    String Name,Sub,Amount,Date,TransactionNote;


    public ModelList() {
    }

    public ModelList(String name, String sub, String amount, String date, String transactionNote) {
        Name = name;
        Sub = sub;
        Amount = amount;
        Date = date;
        TransactionNote = transactionNote;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSub() {
        return Sub;
    }

    public void setSub(String sub) {
        Sub = sub;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTransactionNote() {
        return TransactionNote;
    }

    public void setTransactionNote(String transactionNote) {
        TransactionNote = transactionNote;
    }
}
