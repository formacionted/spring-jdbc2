package com.telefonica.jee.examples;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Ejemplo de como hacer una query basica, recuperamos el numero de customers de
 * la base de datos
 * 
 * 
 */
@Component
public class BatchUpdateJdbc implements CommandLineRunner {

	// Objeto logger que nos permite imprimir en un log con mas informacion de lo
	// que haria un system.out.print
	private static final Logger log = LoggerFactory.getLogger(BatchUpdateJdbc.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... strings) throws Exception {

		/*
		 * Creamos una tabla en nuestra base de datos La base de datos es H2,
		 * autoconfigurada por Spring Boot porque la tenemos añadida como una
		 * dependencia en los starters
		 */
		jdbcTemplate.execute("DROP TABLE IF EXISTS customers");
		jdbcTemplate.execute("CREATE TABLE customers(" + "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

		// insertar customers
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 1, "Bill", "Gates");
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 2, "Steve", "Jobs");
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 3, "Elon", "Musk");
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?, ?)", 4, "Salustiano", "El grande");

		// obtener el numero de customers en base de datos
		int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customers", Integer.class);
		log.info("El numero de customers en base de datos es: " + count);
		
		
		List<Object[]> customers = new ArrayList<Object[]>();
		customers.add(new String[] { "John", "Woo" });
		customers.add(new String[] { "Josh", "Bloch" });
		customers.add(new String[] { "Wilson", "Long" });
		
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", customers);
		
        count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customers", Integer.class);
		log.info("El numero de customers en base de datos es: " + count);

	}

}
