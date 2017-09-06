package trainsjavaam.model;

import java.util.List;

public class OriginTown {
	
	private String town;
	private boolean inCurrentRoute;
	private List<DestinationTown> destinations;
	
	
	public OriginTown(String town,  List<DestinationTown> destinations) {
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

	public List<DestinationTown> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<DestinationTown> destinations) {
		this.destinations = destinations;
	}


}
