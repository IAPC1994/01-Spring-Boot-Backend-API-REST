package com.ipanussis.springboot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ipanussis.springboot.model.Customer;
import com.ipanussis.springboot.repository.ICustomerRepository;

@Service //Marca la clase como un componente de servicio en Spring
public class CustomerServiceImpl implements ICustomerService {

	@Autowired //Crea una instancia de la interfaz.
	private ICustomerRepository customerRepository;

	@Override
	@Transactional(readOnly = true) //Manejar la manera de transaccion en el metodo
	public List<Customer> findAll() {
		return (List<Customer>) customerRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Customer findById(Long id) {
		return customerRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		customerRepository.deleteById(id);
	}

}
