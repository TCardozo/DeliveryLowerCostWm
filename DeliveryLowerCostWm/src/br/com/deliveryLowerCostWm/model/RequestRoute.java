package br.com.deliveryLowerCostWm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestRoute {

	private String mapName;
	private double distancy;
	private double autonomy;
	private double valueOfFuel;
	private String route;
	private double cost;
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public double getDistancy() {
		return distancy;
	}
	public void setDistancy(double distancy) {
		this.distancy = distancy;
	}
	public double getAutonomy() {
		return autonomy;
	}
	public void setAutonomy(double autonomy) {
		this.autonomy = autonomy;
	}
	public double getValueOfFuel() {
		return valueOfFuel;
	}
	public void setValueOfFuel(double valueOfFuel) {
		this.valueOfFuel = valueOfFuel;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}

	
}
