package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int customerId;

    private String mobile;
    private String password;

    //mapping Customer -> TripBooking
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    List<TripBooking> bookingList = new ArrayList<>();

    public Customer() {
    }

    public Customer(int customerId, String mobile, String password, List<TripBooking> bookingList) {
        this.customerId = customerId;
        this.mobile = mobile;
        this.password = password;
        this.bookingList = bookingList;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TripBooking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<TripBooking> bookingList) {
        this.bookingList = bookingList;
    }
}