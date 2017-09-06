package trainsjavaam;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;

import trainsjavaam.model.DestinationTown;
import trainsjavaam.model.OriginTown;


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
	public void testNodeTownsGraph(){
		Map<String,OriginTown> nodeTownsGraph = TrainsManager.getNodeTownsGraph();
		List<DestinationTown> destinations = nodeTownsGraph.get("A").getDestinations();
		
		List<DestinationTown> expectedDestinations = new ArrayList<>();
		expectedDestinations.add(new DestinationTown("B", 5));
		expectedDestinations.add(new DestinationTown("D", 5));
		expectedDestinations.add(new DestinationTown("E", 7));
		
		assertTrue(destinations.containsAll(expectedDestinations));
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
