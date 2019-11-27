package com.telefonica.jee.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telefonica.jee.beans.Customer;

/**
 * Clase encargada de mapear el objeto Customer con la tabla customers de base de datos
 * 
 *
 */
public class CustomerRowMapper implements RowMapper<Customer> {

	   @Override
	    public Customer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		   Customer customer = new Customer();
	        customer.setId(resultSet.getInt("id"));
	        customer.setFirstName(resultSet.getString("first_name"));
	        customer.setLastName(resultSet.getString("last_name"));
	        return customer;
	    }
	   
}
