package trainsjavaam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import trainsjavaam.model.DestinationTown;
import trainsjavaam.model.OriginTown;


public class TrainsManager {

	private static final String NO_SUCH_ROUTE = "NO SUCH ROUTE";

	private static TrainsManager trainsManager = new TrainsManager( );
	private static Map<String,OriginTown> nodeTownsGraph;	

	private TrainsManager() {
		generateNodeTownsGraph();
	}
	public static TrainsManager getInstance( ) {
		return trainsManager;
	}

	private void generateNodeTownsGraph(){
		nodeTownsGraph = new HashMap<>();

		String fileName = "EdgeRoutesGraph.txt";
		Path filePath = Paths.get(getClass().getClassLoader().getResource(fileName).getPath());
		try (Stream<String> stream = Files.lines(filePath)) {

			stream.forEach(line -> {
				String startingTown = String.valueOf(line.charAt(0));
				String endingTown = String.valueOf(line.charAt(1));
				int distance = 0;
				try {
					distance = Integer.parseInt(String.valueOf(line.charAt(2)));
				} catch (NumberFormatException e) {
					// Error: Distance is not a numeric value
					e.printStackTrace();
				}
				if(nodeTownsGraph.containsKey(startingTown)){
					// Add destinationTown into an existing OriginTown
					nodeTownsGraph.get(startingTown).getDestinations().add(new DestinationTown(endingTown, distance));
				}else{
					// Create new OriginTown with its new destinationTown
					List<DestinationTown> destinations = new ArrayList<>();
					destinations.add(new DestinationTown(endingTown, distance));
					OriginTown originTown = new OriginTown(startingTown, destinations);
					nodeTownsGraph.put(startingTown, originTown);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public static Map<String,OriginTown> getNodeTownsGraph() {
		return nodeTownsGraph;
	}

	/**
	 * Computes the distance along a given route.
	 * Time complexity: (T = towns in given route) (N = nodeTowns in Graph) (E = edgeRoutes in Graph)
	 * - Average Case: O(T*1+E)
	 * - Worst Case: O(T*N+E)  --very low probability with Hashmap.get()--
	 * @param routeStr
	 * @return distance or "NO SUCH ROUTE"
	 */
	public String distanceAlongRoute(String routeStr){
		// Each letter from the routeString is town of the route.
		String[] route = routeStr.split("(?!^)");
		int distance = 0;

		for(int i=0; i < route.length - 1; i++){
			String originTownInRoute = route[i];
			String destinationTownInRoute = route[i+1];

			OriginTown originTown = nodeTownsGraph.get(originTownInRoute);
			if( originTown == null)
				// This town doesn't exist in the Graph
				return NO_SUCH_ROUTE;

			List<DestinationTown> destinations = originTown.getDestinations();
			if(destinations==null)
				return NO_SUCH_ROUTE;
			
			boolean routeToDestinationFound = false;
			for(DestinationTown destination : destinations){
				if(destinationTownInRoute.equals(destination.getTown())){
					// Add distance from origin town to destination town.
					distance += destination.getDistanceFromOriginTown();
					routeToDestinationFound = true;
					break;
				}
			}
			if(routeToDestinationFound == false) 
				return NO_SUCH_ROUTE;
		}
		return String.valueOf(distance);
	}

	/**
	 * Computes the number of different routes between 2 given towns, with a maximum of X stops.
	 * Time complexity: (S = max stops) (E = edgeRoutes in Graph)
	 * - Worst Case: O(1+E^S)  --HashMap.get() is considered with its Average Case: O(1)--
	 * @param startingTown
	 * @param endingTown
	 * @param maxStops
	 * @return number of routes
	 */
	public int numRoutesBetween2TownsWithMaxStops(String startingTown, String endingTown, int maxStops){
		// routes is a single element array to be passed as a parameter by reference,
		// because it has to me modified inside the recursive method.
		int[] routes = {0};
		recursiveNumRoutesBetween2TownsWithMaxStops(startingTown, endingTown, maxStops, routes);	
		return routes[0];
	}
	private void recursiveNumRoutesBetween2TownsWithMaxStops(
			String startingTown, String endingTown, int maxStops, int[] routes)
	{
		if(maxStops==0) return;
		// Each new town included in the route, decreases maxStops by 1.
		int newMaxStops = maxStops - 1;

		OriginTown originTown = nodeTownsGraph.get(startingTown);
		if(originTown == null) return;
		List<DestinationTown> destinations = originTown.getDestinations();
		if(destinations == null) return;

		for(DestinationTown destination : destinations){

			if(endingTown.equals(destination.getTown())){
				// New route found
				routes[0] +=1;	
			}
			// Let's move to destinationTown to see which routes to other towns does it have.
			recursiveNumRoutesBetween2TownsWithMaxStops(destination.getTown(), endingTown, newMaxStops, routes);		
		}
		return;
	}

	/**
	 * Computes the number of different routes between 2 given towns, with exactly X stops.
	 * Time complexity: (S = exact stops) (E = edgeRoutes in Graph)
	 * - Worst Case: O(1+E^S)	 --HashMap.get() is considered with its Average Case: O(1)--
	 * @param startingTown
	 * @param endingTown
	 * @param exactStops
	 * @return number of routes
	 */
	public int numRoutesBetween2TownsWithExactStops(String startingTown, String endingTown, int exactStops){
		// routes is a single element array to be passed as a parameter by reference,
		// because it has to me modified inside the recursive method.
		int[] routes = {0};
		int stops = 0;		
		recursiveNumRoutesBetween2TownsWithExactStops(startingTown, endingTown, exactStops, stops, routes);	
		return routes[0];
	}
	private void recursiveNumRoutesBetween2TownsWithExactStops(
			String startingTown, String endingTown, int exactStops, int stops, int[] routes)
	{
		// Each new town included in the route, increases stops by 1
		int newStops = stops + 1;

		OriginTown originTown = nodeTownsGraph.get(startingTown);
		if(originTown == null) return;
		List<DestinationTown> destinations = originTown.getDestinations();
		if(destinations == null) return;	

		if(newStops < exactStops){
			for(DestinationTown destination : destinations){
				// Let's move to destinationTown to see which routes to other towns does it have.
				recursiveNumRoutesBetween2TownsWithExactStops(destination.getTown(), endingTown, exactStops, newStops, routes);	
			}
		}else if (newStops == exactStops){	
			for(DestinationTown destination : destinations){
				if(endingTown.equals(destination.getTown())){
					// New route found
					routes[0] +=1;	
				}
			}
		}
		return;
	}

	/**
	 * Computes the number of different routes between 2 given towns, with a maximum of X distance.	
	 * @param startingTown
	 * @param endingTown
	 * @param maxDistance
	 * @return number of routes
	 */
	public int numRoutesBetween2TownsWithMaxDistance(String startingTown, String endingTown, int maxDistance){
		// routes is a single element array to be passed as a parameter by reference,
		// because it has to me modified inside the recursive method.
		int[] routes = {0};
		int distance = 0;
		recursiveNumRoutesBetween2TownsWithMaxDistance(startingTown, endingTown, maxDistance, distance, routes);
		return routes[0];	
	}
	private void recursiveNumRoutesBetween2TownsWithMaxDistance(
			String startingTown, String endingTown, int maxDistance, int distance, int[] routes) 
	{
		OriginTown originTown = nodeTownsGraph.get(startingTown);
		if(originTown == null) return;
		List<DestinationTown> destinations = originTown.getDestinations();
		if(destinations == null) return;

		for(DestinationTown destination : destinations){
			// Add distance to the last destination into the route's distance.
			int newDistance = distance + destination.getDistanceFromOriginTown();
			if(newDistance >= maxDistance) continue;

			if(endingTown.equals(destination.getTown())){
				// New route found
				routes[0] +=1;	
			}
			// Let's move to destinationTown to see which routes to other towns does it have.
			recursiveNumRoutesBetween2TownsWithMaxDistance(destination.getTown(), endingTown, maxDistance, newDistance, routes);	
		}
		return;
	}

	/**
	 * Finds shortest distance route between 2 given towns.
	 * @param startingTown
	 * @param endingTown
	 * @return shortest distance or "NO SUCH ROUTE"
	 */
	public String shortestDistanceRouteBetween2Towns(String startingTown, String endingTown){
		// shortestDistance is a single element array to be passed as a parameter by reference,
		// because it has to me modified inside the recursive method.
		int[] shortestDistance = new int[1];
		int distance = 0;
		recursiveShortestDistanceRouteBetween2Towns(startingTown, endingTown, distance, shortestDistance);
		if(shortestDistance[0] == 0) return NO_SUCH_ROUTE;
		return String.valueOf(shortestDistance[0]);
	}
	private void recursiveShortestDistanceRouteBetween2Towns(
			String startingTown, String endingTown, int distance, int[] shortestDistance) 
	{
		OriginTown originTown = nodeTownsGraph.get(startingTown);
		if(originTown == null) return;
		// Set that origin OriginTown is in current route to avoid on a future iteration passing again through this town.
		// The goal is to find the shortest route, so doesn't make sense a route that passes through the same town many times.
		originTown.setInCurrentRoute(true);

		List<DestinationTown> destinations = originTown.getDestinations();
		if(destinations == null) return;

		for(DestinationTown destination : destinations){

			int newDistance = distance + destination.getDistanceFromOriginTown();
			if(shortestDistance[0] != 0 && newDistance >= shortestDistance[0]){ 
				// The route cannot include Destination town because
				// its distance is longer than the shortest distance found so far.
				continue;
			}else if(endingTown.equals(destination.getTown())){
				shortestDistance[0] = newDistance;
				// Route finished: Destination town matches Ending town with the shortest distance so far.
				// Starting town and Ending town CAN be the same.
				continue;
			}else if(nodeTownsGraph.get(destination.getTown()).isInCurrentRoute()){
				// Destination town doesn't match Ending town but it can't be included
				// in the route because it's already in it.
				continue;
			}else{
				// Destination town doesn't match Ending town, so we include it into the 
				// route and keep searching.
				recursiveShortestDistanceRouteBetween2Towns(destination.getTown(), endingTown, newDistance, shortestDistance);
			}
		}
		// This town has no more destinations, so we go backwards 
		// to the previous town and remove this town from the route.
		originTown.setInCurrentRoute(false);
		return;
	}
}
