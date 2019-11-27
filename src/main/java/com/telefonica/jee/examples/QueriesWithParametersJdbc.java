package com.telefonica.jee.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.telefonica.jee.beans.Customer;

/**
 * Ejemplos de como hacer queries utilizando parametros
 * 
 *
 */
@Component
public class QueriesWithParametersJdbc implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(QueriesWithParametersJdbc.class);
    
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	// To get support for named parameters we use this template
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
	@Override
	public void run(String... strings) throws Exception {

		/* 1 - Creamos una tabla en nuestra base de datos
		 La base de datos es H2, autoconfigurada por Spring Boot porque la tenemos añadida como una dependencia en los starters*/
		jdbcTemplate.execute("DROP TABLE IF EXISTS customers");
		jdbcTemplate.execute("CREATE TABLE customers(" + "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

		// 2 - insertar customers
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 1, "Bill", "Gates");
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 2, "Steve", "Jobs");
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 3, "Elon", "Musk");
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 4, "Salustiano", "El grande");
		
		
		// 3 - Ejemplo de como utilizar parametros en una consulta con MapSqlParameterSource
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", 1);
		
		String firstCustomerName = namedParameterJdbcTemplate.queryForObject(
		    "SELECT first_name FROM customers WHERE ID = :id", namedParameters, String.class);
		log.info("El nombre del primer customer es: " + firstCustomerName);
		
		
		//4 - Ejemplo de como utilizar parametros en una consulta con MapSqlParameterSource
		String nameToFilter = "Gandolfi";
		String nameToFilter2 = "Salustiano";
		Customer customer = new Customer();
		
		// Filtro 1: buscamos un nombre que no existe
		customer.setFirstName(nameToFilter);
		String sql = "SELECT COUNT(*) FROM customers WHERE first_name = :firstName";
		SqlParameterSource namedParametersBean = new BeanPropertySqlParameterSource(customer);
		Integer customersNumber = namedParameterJdbcTemplate.queryForObject(sql, namedParametersBean, Integer.class);
		log.info("El numero de customers con el nombre {} es {}", nameToFilter, customersNumber);
		
		
		// Filtro 2: buscamos un nombre que si existe
		customer.setFirstName(nameToFilter2);
		namedParametersBean = new BeanPropertySqlParameterSource(customer);
		customersNumber = namedParameterJdbcTemplate.queryForObject(sql, namedParametersBean, Integer.class);
		log.info("El numero de customers con el nombre {} es {}", nameToFilter2, customersNumber);
	}

}
