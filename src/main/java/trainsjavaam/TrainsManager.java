package trainsjavaam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import trainsjavaam.model.NodeTown;


public class TrainsManager {

	private static final String NO_SUCH_ROUTE = "NO SUCH ROUTE";
	
	private static TrainsManager trainsManager = new TrainsManager( );
	private static Map<String,NodeTown> nodeTownsGraph;	

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
					// Add destination into an existing NodeTown
					nodeTownsGraph.get(startingTown).getDestinations().put(endingTown, distance);
				}else{
					// Create new NodeTown with its destination
					Map<String,Integer> destinations = new HashMap<>();
					destinations.put(endingTown, distance);
					NodeTown nodeTown = new NodeTown(startingTown, destinations);
					nodeTownsGraph.put(startingTown, nodeTown);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public static Map<String,NodeTown> getNodeTownsGraph() {
		return nodeTownsGraph;
	}

	/**
	 * Computes the distance along a given route.
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

			if( ! nodeTownsGraph.containsKey(originTownInRoute))
				// This town doesn't exist in the Graph
				return NO_SUCH_ROUTE;
			NodeTown originNodeTown = nodeTownsGraph.get(originTownInRoute);

			Map<String, Integer> destinations = originNodeTown.getDestinations();
			if(destinations==null || ! destinations.containsKey(destinationTownInRoute))
				// The edgeRoute between the origin town and 
				// the destination town doesn't exist in the Graph.
				return NO_SUCH_ROUTE;
			
			// Add distance from origin town to destination town.
			distance += destinations.get(destinationTownInRoute);	
		}
		return String.valueOf(distance);
	}

	/**
	 * Computes the number of different routes between 2 given towns, with a maximum of X stops.
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

		if(nodeTownsGraph.containsKey(startingTown)){
			NodeTown originNodeTown = nodeTownsGraph.get(startingTown);

			Map<String, Integer> destinations = originNodeTown.getDestinations();
			if(destinations!=null){				
				for(Entry<String,Integer> destination : destinations.entrySet()){

					String destinationTown = destination.getKey();
					if(endingTown.equals(destinationTown)){
						// New route found
						routes[0] +=1;	
					}
					// Let's move to destinationTown to see which routes to other towns does it have.
					recursiveNumRoutesBetween2TownsWithMaxStops(destinationTown, endingTown, newMaxStops, routes);		
				}
			}
		}
		return;
	}

	/**
	 * Computes the number of different routes between 2 given towns, with exactly X stops.
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

		if(nodeTownsGraph.containsKey(startingTown)){
			NodeTown originNodeTown = nodeTownsGraph.get(startingTown);

			Map<String, Integer> destinations = originNodeTown.getDestinations();
			if(destinations!=null){	

				if(newStops < exactStops){
					for(Entry<String,Integer> destination : destinations.entrySet()){	
						// Let's move to destinationTown to see which routes to other towns does it have.
						recursiveNumRoutesBetween2TownsWithExactStops(destination.getKey(), endingTown, exactStops, newStops, routes);	
					}
				}else if (newStops == exactStops){
					for(Entry<String,Integer> destination : destinations.entrySet()){		
						if(endingTown.equals(destination.getKey())){
							// New route found
							routes[0] +=1;	
						}
					}
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
		if(nodeTownsGraph.containsKey(startingTown)){
			NodeTown originNodeTown = nodeTownsGraph.get(startingTown);

			Map<String, Integer> destinations = originNodeTown.getDestinations();
			if(destinations!=null){
				for(Entry<String,Integer> destination : destinations.entrySet()){
					// Add distance to the last destination into the route's distance.
					int newDistance = distance + destination.getValue();
					if(newDistance >= maxDistance) continue;

					String destinationTown = destination.getKey();
					if(endingTown.equals(destinationTown)){
						// New route found
						routes[0] +=1;	
					}
					// Let's move to destinationTown to see which routes to other towns does it have.
					recursiveNumRoutesBetween2TownsWithMaxDistance(destinationTown, endingTown, maxDistance, newDistance, routes);	
				}
			}
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
		NodeTown originNodeTown = nodeTownsGraph.get(startingTown);
		// Set that origin NodeTown is in current route to avoid on a future iteration passing again through this town.
		// The goal is to find the shortest route, so doesn't make sense a route that passes through the same town many times.
		originNodeTown.setInCurrentRoute(true);

		Map<String, Integer> destinations = originNodeTown.getDestinations();
		if(destinations==null) return;

		for(Entry<String,Integer> destination : destinations.entrySet()){
			String destinationTown = destination.getKey();
			NodeTown destinationNodeTown = nodeTownsGraph.get(destinationTown);

			int newDistance = distance + destination.getValue();
			if(shortestDistance[0] != 0 && newDistance >= shortestDistance[0]){ 
				// The route cannot include Destination town because
				// its distance is longer than the shortest distance found so far.
				continue;
			}else if(endingTown.equals(destinationTown)){
				shortestDistance[0] = newDistance;
				// Route finished: Destination town matches Ending town with the shortest distance so far.
				// Starting town and Ending town CAN be the same.
				continue;
			}else if(destinationNodeTown.isInCurrentRoute()){
				// Destination town doesn't match Ending town but it can't be included
				// in the route because it's already in it.
				continue;
			}else{
				// Destination town doesn't match Ending town, so we include it into the 
				// route and keep searching.
				recursiveShortestDistanceRouteBetween2Towns(destinationTown, endingTown, newDistance, shortestDistance);
			}
		}
		// This town has no more destinations, so we go backwards 
		// to the previous town and remove this town from the route.
		originNodeTown.setInCurrentRoute(false);
		return;
	}
}
