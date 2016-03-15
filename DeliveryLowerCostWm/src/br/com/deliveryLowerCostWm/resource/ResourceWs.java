package br.com.deliveryLowerCostWm.resource;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.thoughtworks.xstream.XStream;

import br.com.deliveryLowerCostWm.dao.Dao;
import br.com.deliveryLowerCostWm.dao.GenericDao;
import br.com.deliveryLowerCostWm.model.Map;
import br.com.deliveryLowerCostWm.model.RequestRoute;

@Path("/maps")
public class ResourceWs {

	private static final String SUCCESS_RESULT = "<result>success</result>";
	private static final String DESTINATION_NOT_FOUND = "<result>Destination not found</result>";
	private static final String ROUTE_NOT_FOUND = "<result>Route not found</result>";
	private static final String MAPS_NOT_FOUND = "<result>Maps not found</result>";
	private static final String ROUTE_ALREADY_EXISTS = "<result>Route already exists</result>";

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getRoutes() {
		Dao<Map> mapDao = new GenericDao<Map>();
		List<Map> listMaps = mapDao.listAll();
		if(listMaps != null && !listMaps.isEmpty()) {
			XStream xStream = new XStream();
			xStream.alias("Route", Map.class);
			xStream.aliasField("name", Map.class, "map_name");
			return xStream.toXML(listMaps);	
		} else {
			return MAPS_NOT_FOUND;
		}
		
	}
	
	@Path("{mapName}/{origin}/{destination}/{autonomy}/{valueOfFuel}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String requestRoute(@PathParam("mapName") String mapName, @PathParam("origin") String origin,
			@PathParam("destination") String destination, @PathParam("autonomy") double autonomy,
			@PathParam("valueOfFuel") double valueOfFuel) {
		Dao<Map> mapaDao = new GenericDao<Map>();
			RequestRoute route = null;
			if (mapaDao.existsDestination(mapName, destination)) {
				route = mapaDao.requestRoute(mapName, origin, destination, autonomy, valueOfFuel);
			} else {
				return DESTINATION_NOT_FOUND;
			}
			if (route != null) {
				XStream xStream = new XStream();
				xStream.alias("route", RequestRoute.class);
				return xStream.toXML(route);
			} else {
				return DESTINATION_NOT_FOUND;
			}
	}

	@POST
	@Produces(MediaType.APPLICATION_XML)
	public String createRoute(String content) {
		Map map = (Map) new XStream().fromXML(content);
		Dao<Map> mapDao = new GenericDao<Map>();
		if (!mapDao.exists(map.getName(), map.getOrigin(), map.getDestination())) {
			mapDao.persist(map);
			return SUCCESS_RESULT;

		} else {
			return ROUTE_ALREADY_EXISTS;
		}
	}
	
	@Path("{distancy}")
	@PUT
	@Produces(MediaType.APPLICATION_XML)
	public String updateRouteDistance(String content, @PathParam("distancy") double distancy) {
		Map map = (Map) new XStream().fromXML(content);
		Dao<Map> mapDao = new GenericDao<Map>();
		if (mapDao.findMapAndOriginAndDestination(map.getName(), map.getOrigin(), map.getDestination()) != null) {
			Map m = mapDao.findMapAndOriginAndDestination(map.getName(), map.getOrigin(), map.getDestination());
			m.setDistancy(distancy);
			mapDao.update(m.getId());
			return SUCCESS_RESULT;

		} else {
			return null;
		}
	}

	@Path("{mapName}/{origin}/{destination}")
	@DELETE
	@Produces(MediaType.APPLICATION_XML)
	public String deleteRoute(@PathParam("mapName") String mapName, @PathParam("origin") String origin,
			@PathParam("destination") String destination) {
		Dao<Map> mapDao = new GenericDao<Map>();
		if (mapDao.findMapAndOriginAndDestination(mapName, origin, destination) != null) {
			mapDao.delete(mapDao.findMapAndOriginAndDestination(mapName, origin, destination));
			return SUCCESS_RESULT; 
			} else {
				return ROUTE_NOT_FOUND;
			}
	}

}
