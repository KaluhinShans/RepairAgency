package com.shans.kaluhin.entity;

import com.shans.kaluhin.entity.enums.OrderStatus;
import com.shans.kaluhin.service.UserService;

import java.util.Date;

public class Order {
    private int id;
    private String name;
    private String description;
    private int price;
    private String location;
    private int userId;
    private int masterId;
    private OrderStatus status = OrderStatus.VERIFICATION;
    private Date date = new Date(System.currentTimeMillis());

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public User getUser() {
        UserService userService = new UserService();
        return userService.getUserByID(userId);
    }

    public User getMaster() {
        UserService userService = new UserService();
        return userService.getUserByID(masterId);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public boolean isNew() {
        return status.equals(OrderStatus.VERIFICATION);
    }

    public boolean isPayment() {
        return status.equals(OrderStatus.PAYMENT);
    }

    public boolean isPending() {
        return status.equals(OrderStatus.PENDING);
    }

    public boolean isReject() {
        return status.equals(OrderStatus.REJECT);
    }

    public boolean isProcess() {
        return status.equals(OrderStatus.PROCESS);
    }

    public boolean isDone() {
        return status.equals(OrderStatus.DONE);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", location='" + location + '\'' +
                ", userId=" + userId +
                ", masterId=" + masterId +
                ", status=" + status +
                ", date=" + date +
                '}';
    }
}
