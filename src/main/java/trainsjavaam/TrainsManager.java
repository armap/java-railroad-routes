package trainsjavaam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import trainsjavaam.model.EdgeRoute;
import trainsjavaam.model.NodeTown;


public class TrainsManager {

	private static final String NO_SUCH_ROUTE = "NO SUCH ROUTE";
	private static TrainsManager trainsManager = new TrainsManager( );
	private static List<EdgeRoute> edgeRoutesGraph;	
	private static Map<String,NodeTown> nodeTownsGraph;	

	private TrainsManager() {
		generateEdgeRoutesGraph();
	}

	public static TrainsManager getInstance( ) {
		return trainsManager;
	}

	private void generateEdgeRoutesGraph(){
		edgeRoutesGraph = new ArrayList<>();
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
					// Distance is not a numeric value
					e.printStackTrace();
				}
				edgeRoutesGraph.add(new EdgeRoute(startingTown, endingTown, distance));	
				if(nodeTownsGraph.containsKey(startingTown)){
					nodeTownsGraph.get(startingTown).getDestinations().put(endingTown, distance);
				}else{
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
	public static List<EdgeRoute> getEdgeRoutesGraph() {
		return edgeRoutesGraph;
	}
	public static Map<String,NodeTown> getNodeTownsGraph() {
		return nodeTownsGraph;
	}
	/*	Compute the distance along a certain route	

	Input: Route ABC = A-B + B-C

	get 1st, 2nd cities A-B

	search edge that contains A-B. AB5 Get distance.

	get 2nd, 3rd cities B-C

	search edge that contains B-C. BC4 Get distance.

	Sum distances
	 */	
	public String distanceAlongRoute(String routeStr){
		String[] route = routeStr.split("(?!^)");
		int distance = 0;
		int startTownIndex = 0;
		int endTownIndex = 1;
		String startTown = route[startTownIndex];
		String endTown = route[endTownIndex];

		List<EdgeRoute> remainingEdgeRoutesGraph = new ArrayList<>();
		remainingEdgeRoutesGraph.addAll(edgeRoutesGraph);
		Iterator<EdgeRoute> iterator = remainingEdgeRoutesGraph.iterator();
		while (iterator.hasNext()){
			EdgeRoute edgeRoute = iterator.next();

			if(startTown.equals(edgeRoute.getStartingTown())
					&& endTown.equals(edgeRoute.getEndingTown())){

				distance += edgeRoute.getDistance();
				startTownIndex += 1;
				endTownIndex += 1;
				if(endTownIndex > route.length-1) 
					return String.valueOf(distance);
				startTown = route[startTownIndex];
				endTown = route[endTownIndex];

				iterator.remove();
			}
			if( ! iterator.hasNext() && ! remainingEdgeRoutesGraph.isEmpty())
				iterator = remainingEdgeRoutesGraph.iterator();
		}
		return NO_SUCH_ROUTE;
	}

	public int numRoutesBetween2TownsWithMaxDistance(String startingTown, String endingTown, int maxDistance){
		int[] routes = {0};
		int distance = 0;
		recursiveNumRoutesBetween2TownsWithMaxDistance(startingTown, endingTown, maxDistance, distance, routes);
		return routes[0];	
	}
	private void recursiveNumRoutesBetween2TownsWithMaxDistance(
			String startingTown, String endingTown, int maxDistance, int distance, int[] routes) 
	{
		List<EdgeRoute> edgesWithStartTown = edgeRoutesGraph.stream()
				.filter(edgeRoute -> startingTown.equals(edgeRoute.getStartingTown()))
				.collect(Collectors.toList());

		for(EdgeRoute edge : edgesWithStartTown){
			int newDistance = distance + edge.getDistance();
			if(newDistance >= maxDistance) continue;

			if(endingTown.equals(edge.getEndingTown())){
				routes[0] +=1;	
			}
			String newStartingTown = edge.getEndingTown();
			recursiveNumRoutesBetween2TownsWithMaxDistance(newStartingTown, endingTown, maxDistance, newDistance, routes);
		}
		return;
	}

	public int numRoutesBetween2TownsWithMaxStops(String startingTown, String endingTown, int maxStops){
		int[] routes = {0};
		recursiveNumRoutesBetween2TownsWithMaxStops(startingTown, endingTown, maxStops, routes);	
		return routes[0];
	}
	private void recursiveNumRoutesBetween2TownsWithMaxStops(
			String startingTown, String endingTown, int maxStops, int[] routes)
	{
		if(maxStops==0) return;
		int newMaxStops = maxStops - 1;

		List<EdgeRoute> edgesWithStartTown = edgeRoutesGraph.stream()
				.filter(edgeRoute -> startingTown.equals(edgeRoute.getStartingTown()))
				.collect(Collectors.toList());
		for(EdgeRoute edge : edgesWithStartTown){
			if(endingTown.equals(edge.getEndingTown())){
				routes[0] +=1;
			}
			String newStartingTown = edge.getEndingTown();
			recursiveNumRoutesBetween2TownsWithMaxStops(newStartingTown, endingTown, newMaxStops, routes);		
		}
		return;
	}

	public int numRoutesBetween2TownsWithExactStops(String startingTown, String endingTown, int exactStops){
		// The number of trips starting at A and ending at C with exactly 4 stops.  
		//In the sample data below, there are three such trips: 
		//		A to C (via B,C,D); 
		//		A to C (via D,C,D);  
		//		A to C (via D,E,B).
		int[] routes = {0};
		int stops = 0;		
		recursiveNumRoutesBetween2TownsWithExactStops(startingTown, endingTown, exactStops, stops, routes);	
		return routes[0];
	}
	private void recursiveNumRoutesBetween2TownsWithExactStops(
			String startingTown, String endingTown, int exactStops, int stops, int[] routes)
	{
		int newStops = stops + 1;

		List<EdgeRoute> edgesWithStartTown = edgeRoutesGraph.stream()
				.filter(edgeRoute -> startingTown.equals(edgeRoute.getStartingTown()))
				.collect(Collectors.toList());

		if(newStops < exactStops){
			for(EdgeRoute edge : edgesWithStartTown){
				String newStartingTown = edge.getEndingTown();
				recursiveNumRoutesBetween2TownsWithExactStops(newStartingTown, endingTown, exactStops, newStops, routes);	
			}
		}else if (newStops == exactStops){
			for(EdgeRoute edge : edgesWithStartTown){
				if(endingTown.equals(edge.getEndingTown())){
					routes[0] +=1;
				}
			}
		}
		return;
	}

	public String NO_shortestDistanceRouteBetween2Towns(String startingTown, String endingTown){		

		//		Map<LinkedList<String>, Integer> allRoutes = new LinkedHashMap<>();
		//		LinkedList<String> route = new LinkedList<>();
		//		route.add(startingTown);

		int distance = 0;
		int[] shortestDistance = new int[1];

		NO_recursiveShortestDistanceRouteBetween2Towns(startingTown, endingTown, distance, shortestDistance);
		//		return Collections.min(allRoutes.values());

		if(shortestDistance[0] == 0) return NO_SUCH_ROUTE;
		return String.valueOf(shortestDistance[0]);
	}
	private void NO_recursiveShortestDistanceRouteBetween2Towns(
			String startingTown, String endingTown, int distance, int[] shortestDistance) 
	{
		List<EdgeRoute> edgesWithStartTown = edgeRoutesGraph.stream()
				.filter(edgeRoute -> startingTown.equals(edgeRoute.getStartingTown()))
				.collect(Collectors.toList());

		for(EdgeRoute edge : edgesWithStartTown){
			int newDistance = distance + edge.getDistance();
			//			if(newDistance >= 30) continue;
			//			LinkedList<String> newRoute = new LinkedList<>();
			//			newRoute.addAll(route);
			//			newRoute.add(edge.getEndingTown());

			if(shortestDistance[0] != 0 && newDistance >= shortestDistance[0]) 
				continue;

			if(endingTown.equals(edge.getEndingTown())){
				//				routes[0] +=1;
				//				allRoutes.put(newRoute, newDistance);
				if(shortestDistance[0] == 0 || newDistance < shortestDistance[0]){
					shortestDistance[0] = newDistance;
				}
				continue;
			}
			String newStartingTown = edge.getEndingTown();
			NO_recursiveShortestDistanceRouteBetween2Towns(newStartingTown, endingTown, newDistance, shortestDistance);
		}
		return;
	}
	/*	Compute the shortest route between two towns
	 * 
	 * 	Input: Starting town: A. Ending town: C. 
	 * 
	 * 		DistancesRoutes[]: 0

		search edges that starts with A. Get 2nd towns = X , L
		search edges that ends with C. Get 1st towns = Y , M

		if X=Y .  DistancesRoutes+= ACdist
		else:
		search edges that starts with X or L. Get 2nd towns = R...
		search edges that ends with Y or M. Get 1st towns = S...

		if R=S .  DistancesRoutes+= XYdist
		else: LOOP 

		Finally find shortest from DistanceRoutes.

	 */	


	/*	Compute the number of different routes between two towns
	 * 
	 * 		Input: Starting town: C. Ending town: C. 
	 * 
	 * 		1- Condition MaxStops: 3

		Routes: 0
		Stops: 0

		search edges that starts with C. Get 2nd town = X
		search edges that ends with C. Get 1st town = Y
		Stops += 1

		if X=Y . Routes += 1
		else:
		search edges that starts with X. Get 2nd town = X
		search edges that ends with Y. Get 1st town = Y
		Stops += 1

		if X=Y . Routes += 1
		else: LOOP while stops<3


	2- Condition Distance < 30

		Routes: 0
		Distance: 0

		search edges that starts with C. Get 2nd town = X
		search edges that ends with C. Get 1st town = Y
		Stops += 1

		if X=Y . Routes += 1  Distance+= CCdist
		else:
		search edges that starts with X. Get 2nd town = L
		search edges that ends with Y. Get 1st town = M
		Stops += 1

		if L=M . Routes += 1  Distance+= XYdist
		else: LOOP while Distance<30


	 * 
	 */

	public String shortestDistanceRouteBetween2Towns(String startingTown, String endingTown){		
		int distance = 0;
		int[] shortestDistance = new int[1];

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
