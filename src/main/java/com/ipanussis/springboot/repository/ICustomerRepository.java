package com.ipanussis.springboot.repository;

import org.springframework.data.repository.CrudRepository;

import com.ipanussis.springboot.model.Customer;

public interface ICustomerRepository extends CrudRepository<Customer, Long>{

}
