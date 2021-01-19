package com.example.petmarket2020.Models;

import com.example.petmarket2020.HelperClass.Utils;

public class TransactionHistoryModel {
    private long amount;
    private String payments;
    private String time;
    private String status;

    public TransactionHistoryModel() {
    }

    public TransactionHistoryModel(long amount, String payments, String status) {
        this.amount = amount;
        this.payments = payments;
        this.time = Utils.getCurrentDate(true);
        this.status = status;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
