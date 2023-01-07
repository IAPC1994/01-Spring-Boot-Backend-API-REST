package com.ipanussis.springboot.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ipanussis.springboot.model.Customer;
import com.ipanussis.springboot.services.ICustomerService;

import jakarta.validation.Valid;

@CrossOrigin(origins = {"http://localhost:4200"}) //Limita el acceso a la API a un origen especificado
@RestController
@RequestMapping("/api") //Endpoint
public class CustomerRestController {

	@Autowired
	private ICustomerService customerService;
	
	@GetMapping("/customers")
	public List<Customer> index(){
		return customerService.findAll();
	}
	
	@GetMapping("/customers/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Customer customer = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			customer = customerService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(customer==null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
	@PostMapping("/customers")
	public ResponseEntity<?> create(@Valid @RequestBody Customer customer, BindingResult result) {
		
		Customer newCustomer = null;
		Map<String, Object> response = new HashMap<>();
		
		if( result.hasErrors() ) {
			/*List<String> errors = new ArrayList<>();
			
			for(FieldError err : result.getFieldErrors()) {
				errors.add("Field: '" + err.getField() + "' "+err.getDefaultMessage());
			}*/
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "Field: '" + err.getField() + "' "+err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			newCustomer = customerService.save(customer);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la inserción en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("", "El cliente ha sido creado con exito");
		response.put("customer:", newCustomer);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/customers/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@Valid @RequestBody Customer customer, BindingResult result, @PathVariable Long id) {
		Customer currentCustomer = customerService.findById(id);
		Customer updatedCustomer = null;
		Map<String, Object> response = new HashMap<>();
		
		if( result.hasErrors() ) {
			List<String> errors = new ArrayList<>();
			
			for(FieldError err : result.getFieldErrors()) {
				errors.add("Field: '" + err.getField() + "' "+err.getDefaultMessage());
			}

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(currentCustomer == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")) );
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			currentCustomer.setName(customer.getName());
			currentCustomer.setLastName(customer.getLastName());
			currentCustomer.setEmail(customer.getEmail());
			currentCustomer.setCreateAt(customer.getCreateAt());
			updatedCustomer = customerService.save(currentCustomer);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la actualizacion en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido actualizado con exito");
		response.put("customer:", updatedCustomer);
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/customers/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			customerService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la eliminacion en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido eliminado con exito");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
}
