package br.com.deliveryLowerCostWm.connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Connection {

public static EntityManager entityManager;
	
	static {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("deliveryWmDB");
		entityManager = entityManagerFactory.createEntityManager();
	}
}
