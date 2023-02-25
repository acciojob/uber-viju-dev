package com.driver.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int driverId;

    private String mobile;

    private String password;

    //mapping Driver -> Cab
    @OneToOne
    @JoinColumn
    private Cab cab;

    //mapping Driver -> TripBooking
    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private List<TripBooking> bookingList;

    public Driver() {
    }

    public Driver(int driverId, String mobile, String password, Cab cab, List<TripBooking> bookingList) {
        this.driverId = driverId;
        this.mobile = mobile;
        this.password = password;
        this.cab = cab;
        this.bookingList = bookingList;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
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

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public List<TripBooking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<TripBooking> bookingList) {
        this.bookingList = bookingList;
    }
}