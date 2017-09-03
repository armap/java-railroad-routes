package trainsjavaam;

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import trainsjavaam.model.EdgeRoute;


public class TrainsManagerTest extends TestCase{

	private static TrainsManager trainsManager; 
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		trainsManager = TrainsManager.getInstance();
	}
	
	@Test
	public void testEdgeRoutesGraph(){
		List<EdgeRoute> edgeRoutesGraph = TrainsManager.getEdgeRoutesGraph();
		EdgeRoute lastEdgeRoute = edgeRoutesGraph.get(8);
		
		assertEquals("A", lastEdgeRoute.getStartingTown());
		assertEquals("E", lastEdgeRoute.getEndingTown());
		assertEquals(7, lastEdgeRoute.getDistance());
	}

//	@Test
//	public void testDistanceAlongRoute() {
//		
//		trainsManager.distanceAlongRoute(route);
//		
//		fail("Not yet implemented");
//	}

}
