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
		Driver driver1 = null;



		for (Driver driver:driverRepository2.findAll()){
			if (driver.getCab().getAvailable()){
				if((driver1 == null) || (driver.getDriverId() < driver1.getDriverId())){
					driver1 = driver;
					status = true;
					tripStatus = TripStatus.CONFIRMED;
					driverId = driver.getDriverId();
				}

//				driver1 = driver;
			}
		}
		if (driver1 == null){
			throw new RuntimeException("No cab available!");
		}
//		if (status==false){
//			throw new RuntimeException("No cab available!");
//		}
		//trip booking
		TripBooking tripBooking = new TripBooking();
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
//		tripBooking.setStatus(TripStatus.CONFIRMED);
		tripBooking.setCustomer(customerRepository2.findById(customerId).get());
//		tripBooking.setDriver();  // maybe not needed to set
		//used this to attributes here coz maight be not initialised
		driver1.getCab().setAvailable(false); //trip booked so hes not available
		tripBooking.setDriver(driver1);
		tripBooking.setStatus(TripStatus.CONFIRMED);


	Customer customer = customerRepository2.findById(customerId).get();
		customer.getBookingList().add(tripBooking);


//		Driver driver = driverRepository2.getOne(driverId);
		driver1.getBookingList().add(tripBooking);
		driverRepository2.save(driver1);
//		tripBookingRepository2.save(tripBooking); // nullpointer

		customerRepository2.save(customer);
		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.CANCELED);
		tripBooking.setBill(0); // why 0 ??
		tripBooking.getDriver().getCab().setAvailable(true);
		tripBookingRepository2.save(tripBooking);
	}

	@Override
	public void completeTrip(Integer tripId){
//		//Complete the trip having given trip Id and update TripBooking attributes accordingly
//		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
//		tripBooking.setStatus(TripStatus.COMPLETED);
//		//confirm ki personally bhi krna pdega ya automatically ho jayenge changes if main mein kiye
//		Customer customer = tripBooking.getCustomer();
//		for (TripBooking tripBooking1:customer.getBookingList()){
//			if(tripBooking1.getTripBookingId() == tripId){
//				//tripBookingRepository2.findById(tripBooking1.getTripBookingId()).get().setStatus(TripStatus.COMPLETED);
//				//ig ye same repo mein save krra
//				tripBooking1.setStatus(TripStatus.COMPLETED);
//				//Customer customer = tripBooking1.getCustomer();
//				customerRepository2.save(customer);
//			}
//		}
//		Driver driver = tripBooking.getDriver();
//		for (TripBooking tripBooking1:driver.getBookingList()){
//			if(tripBooking1.getTripBookingId() == tripId){
////				tripBookingRepository2.findById(tripBooking1.getTripBookingId()).get().setStatus(TripStatus.COMPLETED);
////				Driver driver = tripBooking1.getDriver();
//				tripBooking1.setStatus(TripStatus.COMPLETED);
//				driverRepository2.save(driver);
//			}
//		}
//		tripBookingRepository2.save(tripBooking);
//		return;
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.COMPLETED);
		tripBooking.getDriver().getCab().setAvailable(true);
		tripBookingRepository2.save(tripBooking);
	}
}
