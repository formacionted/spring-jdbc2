package com.telefonica.jee.examples;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

/**
 * La clase SimpleJdbc nos provee de una forma sencilla de configurar y ejecutar sentencias SQL
 * 
 *
 */
@Component
public class SimpleJdbcInsertAutoId implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SimpleJdbcInsertAutoId.class);
    
    private SimpleJdbcInsert simpleJdbcInsert;
    
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
    @Autowired
    public void setDataSource(final DataSource dataSource) {
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("customers").usingGeneratedKeyColumns("id");
    }
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
		
		// obtener el numero de customers en base de datos
		int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customers", Integer.class);
		log.info("El numero de customers en base de datos es: {}", count);
		
		// Insertar datos
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("first_name", "Licenciado");
        parameters.put("last_name", "Varela");
        Number idCustomer = simpleJdbcInsert.executeAndReturnKey(parameters);
        
		// obtener el numero de customers en base de datos
		count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customers", Integer.class);
		log.info("El numero de customers en base de datos es: {}", count);
		log.info("El id del nuevo customer es {} ", idCustomer);

		
		
		
		
		
		
	}

}
