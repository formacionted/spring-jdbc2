package com.telefonica.jee.examples;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.telefonica.jee.beans.Customer;
import com.telefonica.jee.mappers.CustomerRowMapper;

/**
 * Ejemplo de como hacer una query basica, recuperamos el numero de customers de
 * la base de datos
 * 
 * 
 */
@Component
public class CustomerCRUD implements CommandLineRunner {

	// Objeto logger que nos permite imprimir en un log con mas informacion de lo
	// que haria un system.out.print
	private static final Logger log = LoggerFactory.getLogger(CustomerCRUD.class);

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
		log.info("El numero de customers en base de datos es: " + count());

		this.updateExample();

		this.deleteExample();

		log.info("El numero de customers en base de datos es: " + count());
		log.info("Ejecutando metodo save...");
		Customer customer1 = new Customer();
		customer1.setFirstName("Rauli");
		customer1.setLastName("Rocatagliata");
		save(customer1);
		log.info("El numero de customers en base de datos es: " + count());
		
		
		log.info("Ejecutando metodo delete...");
		delete(1);
		log.info("El numero de customers en base de datos es: " + count());
		
		log.info("Ejecutando metodo findOne(2)...");
		Customer customer2 = findOne(2);
		log.info("El customer 2 tiene de nombre {}", customer2.getFirstName());
	}

	public int count() {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customers", Integer.class);
	}
	
	public boolean save(Customer customer) {
		String sqlQuery = "insert into customers(first_name, last_name) values (?, ?)";
		return jdbcTemplate.update(sqlQuery, customer.getFirstName(), customer.getLastName()) > 0;
	}

	public boolean update(Customer customer) {
		String sqlQuery = "update customers set first_name = ?, last_name = ? where id = ?";
		return jdbcTemplate.update(sqlQuery, customer.getFirstName(), customer.getLastName(), customer.getId()) > 0;
	}

	public Customer findOne(int id) {
		String sqlQuery = "select id, first_name, last_name from customers where id = ?";
		return jdbcTemplate.queryForObject(sqlQuery, new Object[] { id }, new CustomerRowMapper());
	}

	public List<Customer> findAll() {
		return jdbcTemplate.query("SELECT * FROM customers", new CustomerRowMapper());
	}

    public boolean delete(long id) {
        String sqlQuery = "delete from customers where id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
      }
    
	private void deleteExample() {

		String query = "SELECT * FROM customers WHERE ID = ?";
		int idCustomer = 4;
		Customer filteredCustomer = jdbcTemplate.queryForObject(query, new Object[] { idCustomer },
				new CustomerRowMapper());
		log.info("El customer filtrado por el ID {} tiene es nuestro amigo {} {}", idCustomer,
				filteredCustomer.getFirstName(), filteredCustomer.getLastName());

		jdbcTemplate.update("delete from customers where id = ?", 4);

		try {
			filteredCustomer = jdbcTemplate.queryForObject(query, new Object[] { idCustomer }, new CustomerRowMapper());

		} catch (EmptyResultDataAccessException e) {
			log.warn("No se ha encontrado ningun customer con el id: {}", idCustomer);
		}

	}

	private void updateExample() {

		String query = "SELECT * FROM customers WHERE ID = ?";
		int idCustomer = 4;
		Customer filteredCustomer = jdbcTemplate.queryForObject(query, new Object[] { idCustomer },
				new CustomerRowMapper());
		log.info("El customer filtrado por el ID {} tiene es nuestro amigo {} {}", idCustomer,
				filteredCustomer.getFirstName(), filteredCustomer.getLastName());

		jdbcTemplate.update("update customers set first_name = ?, last_name = ? where id = ?", "Manguito", "Maracaton",
				4);

		filteredCustomer = jdbcTemplate.queryForObject(query, new Object[] { idCustomer }, new CustomerRowMapper());
		log.info("El customer filtrado por el ID {} tiene es nuestro amigo {} {}", idCustomer,
				filteredCustomer.getFirstName(), filteredCustomer.getLastName());

	}

}
