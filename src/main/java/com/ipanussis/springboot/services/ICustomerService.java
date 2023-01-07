package com.ipanussis.springboot.services;

import java.util.List;

import com.ipanussis.springboot.model.Customer;

public interface ICustomerService {

	public List<Customer> findAll();
	
	public Customer findById(Long id);
	
	public Customer save(Customer customer);
	
	public void delete(Long id);
	
}
