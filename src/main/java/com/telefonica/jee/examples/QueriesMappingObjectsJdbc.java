package com.telefonica.jee.examples;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.telefonica.jee.beans.Customer;
import com.telefonica.jee.mappers.CustomerRowMapper;

@Component
public class QueriesMappingObjectsJdbc implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(QueriesMappingObjectsJdbc.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	// To get support for named parameters we use this template
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void run(String... strings) throws Exception {

		/*
		 * 1 - Creamos una tabla en nuestra base de datos La base de datos es H2,
		 * autoconfigurada por Spring Boot porque la tenemos añadida como una
		 * dependencia en los starters
		 */
		jdbcTemplate.execute("DROP TABLE IF EXISTS customers");
		jdbcTemplate.execute("CREATE TABLE customers(" + "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

		// 2 - insertar customers
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 1, "Bill", "Gates");
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 2, "Steve", "Jobs");
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 3, "Elon", "Musk");
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 4, "Salustiano", "El grande");

		// 3 - Ejemplo de como obtener un empleado filtrando por ID
		String query = "SELECT * FROM customers WHERE ID = ?";
		int idCustomer = 4;
		Customer filteredCustomer = jdbcTemplate.queryForObject(query, new Object[] { idCustomer }, new CustomerRowMapper());
		log.info("El customer filtrado por el ID {} es nuestro amigo {} {}", idCustomer, filteredCustomer.getFirstName(), filteredCustomer.getLastName());

		
		//4 - Ejemplo de como obtener todos los empleados
		List<Customer> customers = jdbcTemplate.query("SELECT * FROM customers", new CustomerRowMapper());
		log.info("El total de customers en base de datos es {}", customers.size());
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
