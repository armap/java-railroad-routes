package trainsjavaam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import trainsjavaam.model.EdgeRoute;


public class TrainsManager {

	private static TrainsManager trainsManager = new TrainsManager( );
	private static List<EdgeRoute> edgeRoutesGraph;	

	private TrainsManager() {
		generateEdgeRoutesGraph();
	}

	public static TrainsManager getInstance( ) {
		return trainsManager;
	}

	private List<EdgeRoute> generateEdgeRoutesGraph(){
		edgeRoutesGraph = new ArrayList<>();

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
			});

		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	public static List<EdgeRoute> getEdgeRoutesGraph() {
		return edgeRoutesGraph;
	}


//	public int distanceAlongRoute(String[] route){
//		int distance = 0;
//
//		String startingTown = route[0];
//		String endingTown= route[1];
//
//		for(Sring)
//
//			return distance;
//	}
	/*
	 * 		Edges:
			AB5
			BC4
			CD8
	 */

	/*	Compute the distance along a certain route	

		Input: Route ABC = A-B + B-C

		get 1st, 2nd cities A-B

		search edge that contains A-B. AB5 Get distance.

		get 2nd, 3rd cities B-C

		search edge that contains B-C. BC4 Get distance.

		Sum distances
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

}
