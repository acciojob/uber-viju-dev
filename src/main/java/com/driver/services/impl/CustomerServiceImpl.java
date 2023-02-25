package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
		return;
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer= customerRepository2.getOne(customerId);
		customerRepository2.delete(customer);
		return;
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		boolean status = false;
		TripStatus tripStatus ;
		int driverId ;

		//trip booking
		TripBooking tripBooking = new TripBooking();
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
//		tripBooking.setStatus(TripStatus.CONFIRMED);
		tripBooking.setCustomer(customerRepository2.getOne(customerId));
//		tripBooking.setDriver();  // maybe not needed to set

		for (Driver driver:driverRepository2.findAll()){
			if (driver.getCab().getAvailable()){
				status = true;
				tripStatus = TripStatus.CONFIRMED;
				driverId = driver.getDriverId();
				//used this to attributes here coz maight be not initialised
				tripBooking.setDriver(driverRepository2.getOne(driverId));
				tripBooking.setStatus(TripStatus.CONFIRMED);
				break;
			}
		}

	Customer customer = customerRepository2.getOne(customerId);
		customer.getBookingList().add(tripBooking);
		customerRepository2.save(customer);
		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.getOne(tripId);
		tripBooking.setStatus(TripStatus.CANCELED);
		//confirm ki personally bhi krna pdega ya automatically ho jayenge changes if main mein kiye
		for (TripBooking tripBooking1:tripBooking.getCustomer().getBookingList()){
			if(tripBooking1.getTripBookingId() == tripId){
				tripBookingRepository2.getOne(tripBooking1.getTripBookingId()).setStatus(TripStatus.CANCELED);
			}
		}
		for (TripBooking tripBooking1:tripBooking.getDriver().getBookingList()){
			if(tripBooking1.getTripBookingId() == tripId){
				tripBookingRepository2.getOne(tripBooking1.getTripBookingId()).setStatus(TripStatus.CANCELED);
			}
		}
		return;
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.getOne(tripId);
		tripBooking.setStatus(TripStatus.COMPLETED);
		//confirm ki personally bhi krna pdega ya automatically ho jayenge changes if main mein kiye
		for (TripBooking tripBooking1:tripBooking.getCustomer().getBookingList()){
			if(tripBooking1.getTripBookingId() == tripId){
				tripBookingRepository2.getOne(tripBooking1.getTripBookingId()).setStatus(TripStatus.COMPLETED);
			}
		}
		for (TripBooking tripBooking1:tripBooking.getDriver().getBookingList()){
			if(tripBooking1.getTripBookingId() == tripId){
				tripBookingRepository2.getOne(tripBooking1.getTripBookingId()).setStatus(TripStatus.COMPLETED);
			}
		}
		return;
	}
}
