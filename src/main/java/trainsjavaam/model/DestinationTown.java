package trainsjavaam.model;

import java.util.Objects;

public class DestinationTown {

	private String town;
	private int distanceFromOriginTown;

	public DestinationTown(String town, int distanceFromOriginTown) {
		this.town = town;
		this.distanceFromOriginTown = distanceFromOriginTown;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public int getDistanceFromOriginTown() {
		return distanceFromOriginTown;
	}

	public void setDistanceFromOriginTown(int distanceFromOriginTown) {
		this.distanceFromOriginTown = distanceFromOriginTown;
	}

	/*
	 * We override equals() and hashCode() to be able when testing, 
	 * to assert equality for two lists of DestinationTown objects 
	 * with the same values.
	 */
	@Override 
	public boolean equals(Object object) {
		if (!(object instanceof DestinationTown)) {
			return false; 
		} 
		DestinationTown that = (DestinationTown) object;
		return this.town.equals(that.town)
				&& this.distanceFromOriginTown == that.distanceFromOriginTown;
	} 
	@Override
	public int hashCode() {
		return Objects.hash(town, distanceFromOriginTown);
	}
}
