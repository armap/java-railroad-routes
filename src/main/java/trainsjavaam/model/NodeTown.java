package trainsjavaam.model;

import java.util.Map;

public class NodeTown {
	
	private String town;
	private boolean inCurrentRoute;
	private Map<String, Integer> destinations;
	
	
	public NodeTown(String town,  Map<String, Integer> destinations) {
		this.town = town;
		this.setInCurrentRoute(false);
		this.destinations = destinations;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public boolean isInCurrentRoute() {
		return inCurrentRoute;
	}

	public void setInCurrentRoute(boolean inCurrentRoute) {
		this.inCurrentRoute = inCurrentRoute;
	}

	public Map<String, Integer> getDestinations() {
		return destinations;
	}

	public void setDestinations(Map<String, Integer> destinations) {
		this.destinations = destinations;
	}


}
