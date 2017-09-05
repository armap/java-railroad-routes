package trainsjavaam;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import trainsjavaam.model.EdgeRoute;
import trainsjavaam.model.NodeTown;


public class TrainsManagerTest {

	private static TrainsManager trainsMgr; 
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		trainsMgr = TrainsManager.getInstance();
	}
	
	@Test
	public void testEdgeRoutesGraph(){
		List<EdgeRoute> edgeRoutesGraph = TrainsManager.getEdgeRoutesGraph();
		EdgeRoute lastEdgeRoute = edgeRoutesGraph.get(8);
		
		assertEquals("A", lastEdgeRoute.getStartingTown());
		assertEquals("E", lastEdgeRoute.getEndingTown());
		assertEquals(7, lastEdgeRoute.getDistance());
	}
	@Test
	public void testNodeTownsGraph(){
		Map<String,NodeTown> nodeTownsGraph = TrainsManager.getNodeTownsGraph();
		Map<String,Integer> destinations = nodeTownsGraph.get("A").getDestinations();
		
		Map<String,Integer> expectedDestinations = new HashMap<>();
		expectedDestinations.put("B", 5);
		expectedDestinations.put("D", 5);
		expectedDestinations.put("E", 7);

		assertEquals(expectedDestinations, destinations);

	}

	@Test
	public void testDistanceAlongRoute() {		
		assertEquals("9", trainsMgr.distanceAlongRoute("ABC"));
		assertEquals("5", trainsMgr.distanceAlongRoute("AD"));
		assertEquals("13", trainsMgr.distanceAlongRoute("ADC"));
		assertEquals("22", trainsMgr.distanceAlongRoute("AEBCD"));
		assertEquals("NO SUCH ROUTE", trainsMgr.distanceAlongRoute("AED"));
	}	
	@Test
	public void testNumRoutesBetween2TownsWithMaxStops() {		
		assertEquals(2, trainsMgr.numRoutesBetween2TownsWithMaxStops("C","C", 3));
	}
	@Test
	public void testNumRoutesBetween2TownsWithExactStops() {		
		assertEquals(3, trainsMgr.numRoutesBetween2TownsWithExactStops("A","C", 4));
	}
	@Test
	public void testNumRoutesBetween2TownsWithMaxDistance() {			
		assertEquals(7, trainsMgr.numRoutesBetween2TownsWithMaxDistance("C","C", 30));
	}	
	@Test
	public void testShortestDistanceRouteBetween2Towns() {	
		assertEquals("9", trainsMgr.shortestDistanceRouteBetween2Towns("A","C"));
		assertEquals("9", trainsMgr.shortestDistanceRouteBetween2Towns("B","B"));
	}
	

}
