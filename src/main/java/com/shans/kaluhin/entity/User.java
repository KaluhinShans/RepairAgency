package com.shans.kaluhin.entity;

import com.shans.kaluhin.entity.enums.Locales;
import com.shans.kaluhin.entity.enums.Role;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private int balance;
    private int phone;
    private String email;
    private String activationCode;
    private String password;
    private String name;
    private String lastName;
    private String photo;
    private List<Role> roles = new ArrayList<>();
    private Locales locale;

    public User(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public void hashPassword() {
        password = String.valueOf(password.hashCode());
    }

    public boolean isManager() {
        return roles.contains(Role.MANAGER);
    }

    public boolean isMaster() {
        return roles.contains(Role.MASTER);
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getLocale() {
        return locale.name();
    }

    public void setLocale(Locales locale) {
        this.locale = locale;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", balance=" + balance +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", photo='" + photo + '\'' +
                ", roles=" + roles +
                ", locale=" + locale +
                '}';
    }
}
