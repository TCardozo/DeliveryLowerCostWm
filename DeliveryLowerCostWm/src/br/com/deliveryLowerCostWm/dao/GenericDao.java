package br.com.deliveryLowerCostWm.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.deliveryLowerCostWm.connection.Connection;
import br.com.deliveryLowerCostWm.model.Map;
import br.com.deliveryLowerCostWm.model.RequestRoute;

public class GenericDao<T> implements Dao<T> {

	@Override
	public void persist(T t) {
		EntityManager entityManager = Connection.entityManager;
		entityManager.getTransaction().begin();
		entityManager.persist(t);
		entityManager.getTransaction().commit();
	}

	@Override
	public void update(Long id) {
		EntityManager entityManager = Connection.entityManager;
		entityManager.getTransaction().begin();
		entityManager.merge(entityManager.find(Map.class, id));
		entityManager.getTransaction().commit();
	}

	@Override
	public void delete(T t) {
		EntityManager entityManager = Connection.entityManager;
		entityManager.getTransaction().begin();
		;
		entityManager.remove(t);
		entityManager.getTransaction().commit();
	}

	@Override
	public void find(T t) {
	}

	@Override
	public boolean existsMap(String mapName) {
		EntityManager entityManager = Connection.entityManager;
		Query query = entityManager.createQuery("FROM Map WHERE Name LIKE :name ");
		query.setParameter("name", mapName);
		if (query.getResultList() != null && !query.getResultList().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean exists(String mapName, String origin, String destination) {
		EntityManager entityManager = Connection.entityManager;
		Query query = entityManager.createQuery(
				"FROM Map WHERE Name LIKE :name " + "AND origin = :origin " + "AND destination = :destination");
		query.setParameter("name", "%" + mapName + "%");
		query.setParameter("origin", origin);
		query.setParameter("destination", destination);

		if (query.getResultList() != null && !query.getResultList().isEmpty()) {
			return true;
		} else {
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	public List<String> returnOrginFromDestination(List<String> origins, String destination) {
		EntityManager entityManager = Connection.entityManager;
		Query query = entityManager.createQuery("FROM Map WHERE destination = :destination)");
		query.setParameter("destination", destination);
		List<String> listOfOrigins = new ArrayList<>();
		if (query.getResultList() != null && !query.getResultList().isEmpty()) {
			for (Map m : (List<Map>) query.getResultList()) {
				m.getOrigin();
				listOfOrigins.add(m.getOrigin());
			}
			return listOfOrigins;
		} else {
			return new ArrayList<>();
		}

	}

	@SuppressWarnings("unchecked")
	public Map exists(String origin, String destination) {
		EntityManager entityManager = Connection.entityManager;
		Query query = entityManager.createQuery("FROM Map WHERE origin = :origin " + "AND destination = :destination");
		query.setParameter("origin", origin);
		query.setParameter("destination", destination);
		List<String> origens = new ArrayList<>();
		if (query.getResultList() != null && !query.getResultList().isEmpty()) {
			for (Map m : (List<Map>) query.getResultList()) {
				m.getOrigin();
				origens.add(m.getOrigin());
			}
			exists(origin, destination);
		} else {
			return null;
		}
		return null;
	}

	@Override
	public boolean existsDestination(String mapName, String destination) {
		EntityManager entityManager = Connection.entityManager;
		Query query = entityManager.createQuery("FROM Map WHERE destination = :destination");
		query.setParameter("destination", destination);
		if (query.getResultList() != null && !query.getResultList().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public RequestRoute requestRoute(String mapName, String origin, String destination, double autonomy,
			double valueOfFuel) {
		EntityManager entityManager = Connection.entityManager;
		RequestRoute route = new RequestRoute();
		Query query = entityManager.createQuery(
				"FROM Map WHERE Name LIKE :name " + "AND origin = :origin " + "AND destination = :destination");
		query.setParameter("name", mapName);
		query.setParameter("origin", origin);
		query.setParameter("destination", destination);
		if (query.getResultList() != null && !query.getResultList().isEmpty()) {
			Map m = (Map) query.getSingleResult();
			route.setRoute(origin + "" + destination);
			route.setAutonomy(autonomy);
			route.setValueOfFuel(valueOfFuel);
			route.setDistancy(m.getDistancy());
			route.setCost(calculatorCost(m.getDistancy(), autonomy, valueOfFuel));
			return route;
		} else {
			RequestRoute r = doesLogic(mapName, origin, destination);
			route = r;
			route.setAutonomy(autonomy);
			route.setValueOfFuel(valueOfFuel);
			route.setCost(calculatorCost(r.getDistancy(), autonomy, valueOfFuel));
		}

		return route;
	}

	public RequestRoute doesLogic(String mapName, String origin, String destination) {
		boolean success = false;
		String ori = origin;
		String rou = ori;
		double dist = 0;
		Map m = null;
		RequestRoute route = new RequestRoute();
		while (!success) {
			m = returnShortDestination(ori);
			if (m != null) {
				rou += m.getDestination();
				dist += m.getDistancy();
				if (findMapAndOriginAndDestination(mapName, m.getDestination(), destination) != null) {
					rou += destination;
					dist += findMapAndOriginAndDestination(mapName, m.getDestination(), destination).getDistancy();
					success = true;
				} else {
					ori = m.getDestination();
				}
			}
		}
		route.setDistancy(dist);
		route.setRoute(rou);
		return route;
	}

	public void insertRouteNotFound(String mapName, String origin, String destination, double distancy) {
		EntityManager entityManager = Connection.entityManager;
		Map map = new Map();
		map.setName(mapName);
		map.setOrigin(origin);
		map.setDestination(destination);
		map.setDistancy(distancy);
		entityManager.persist(map);
	}

	public int returnCountDestination(String destination) {
		EntityManager entityManager = Connection.entityManager;
		Query query = entityManager.createQuery("FROM Map WHERE destination = :destination");
		query.setParameter("destination", destination);
		return query.getResultList().size();
	}

	public Map findOriginAndDestination(String origin, String destination) {
		EntityManager entityManager = Connection.entityManager;
		Map map = new Map();
		Query query = entityManager.createQuery("FROM Map WHERE origin = :origin " + "AND destination = :destination");
		query.setParameter("origin", origin);
		query.setParameter("destination", destination);
		if (query.getSingleResult() != null) {
			map = (Map) query.getSingleResult();
			return map;
		} else {
			return null;
		}
	}

	public void findOrigin(String origin) {
		EntityManager entityManager = Connection.entityManager;
		Query query = entityManager.createQuery("FROM Map WHERE Name LIKE :name " + "AND origin = :origin ");
		query.setParameter("origin", origin);
		if (query.getSingleResult() != null) {

		} else {

		}
	}

	public Map returnShortDestination(String origin) {
		EntityManager entityManager = Connection.entityManager;
		Map map = new Map();
		TypedQuery<Object[]> query = entityManager
				.createQuery("SELECT min(distancy), destination FROM Map WHERE origin = :origin ", Object[].class);
		query.setParameter("origin", origin);
		List<Object[]> results = query.getResultList();
		if (results != null && !results.isEmpty()) {
			for (Object[] result : results) {
				if (result[0] != null && result[1] != null) {
					map.setDistancy((double) result[0]);
					map.setDestination((String) result[1]);
					return map;
				} else {
					return null;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	public void calculaDistancia(String nomeMapa, String origem) {
		EntityManager entityManager = Connection.entityManager;
		Query query = entityManager.createQuery("FROM Mapa WHERE Nome LIKE :nome " + "AND origem = :origem ");
		query.setParameter("nome", nomeMapa);
		query.setParameter("origem", origem);
	}

	public double calculatorCost(double distancia, double autonomia, double valorCombustivel) {
		double kmsTotal = distancia / autonomia;
		return kmsTotal * valorCombustivel;
	}

	@Override
	public Map findMapAndOriginAndDestination(String mapName, String origin, String destination) {
		EntityManager entityManager = Connection.entityManager;
		Query query = entityManager.createQuery(
				"FROM Map WHERE Name LIKE :name " + "AND origin = :origin " + "AND destination = :destionation");
		query.setParameter("name", mapName);
		query.setParameter("origin", origin);
		query.setParameter("destionation", destination);

		if (query.getResultList() != null && !query.getResultList().isEmpty()) {
			return (Map) query.getResultList().get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> listAll() {
		EntityManager entityManager = Connection.entityManager;
		Query query = entityManager.createQuery("FROM Map");
		if (query.getResultList() != null && !query.getResultList().isEmpty()) {
			return (List<T>) query.getResultList();
		} else {
			return new ArrayList<>();
		}
	}

}
