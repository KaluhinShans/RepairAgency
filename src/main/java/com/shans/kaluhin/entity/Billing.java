package com.shans.kaluhin.entity;

import java.util.Date;

public class Billing {
    private int id;
    private int amount;
    private int userId;
    private int orderId;
    private int reminder;
    private String card;
    private Date date = new Date(System.currentTimeMillis());

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCard() {
        return card;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    @Override
    public String toString() {
        return "Billing{" +
                "id=" + id +
                ", amount=" + amount +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", reminder=" + reminder +
                ", card='" + card + '\'' +
                ", date=" + date +
                '}';
    }
}
