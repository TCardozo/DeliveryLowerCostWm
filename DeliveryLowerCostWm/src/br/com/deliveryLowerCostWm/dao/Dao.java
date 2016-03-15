package br.com.deliveryLowerCostWm.dao;

import java.util.List;

import br.com.deliveryLowerCostWm.model.Map;
import br.com.deliveryLowerCostWm.model.RequestRoute;

public interface Dao<T> {

	public void persist(T t);
	
	public void update(Long id);
	
	public void delete(T t);
	
	public void find(T t);
	
	public List<T> listAll();
	
	public boolean exists(String mapName, String origin, String destination);
	
	public Map findMapAndOriginAndDestination(String mapName, String origin, String destination);
	
	public boolean existsMap(String mapName);
	
	public boolean existsDestination(String mapName, String destination);
	
	public RequestRoute requestRoute(String mapName, String origin, String destination, double autonomy, double valueOfFuel);
}
