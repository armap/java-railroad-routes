package trainsjavaam.model;

public class EdgeRoute {
	
	private String startingTown;
	private String endingTown;
	private int distance;
	
	
	public EdgeRoute(String startingTown, String endingTown, int distance) {
		super();
		this.startingTown = startingTown;
		this.endingTown = endingTown;
		this.distance = distance;
	}	
	
	public String getStartingTown() {
		return startingTown;
	}
	public void setStartingTown(String startingTown) {
		this.startingTown = startingTown;
	}
	public String getEndingTown() {
		return endingTown;
	}
	public void setEndingTown(String endingTown) {
		this.endingTown = endingTown;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}

	
}
