package model.creature;

import static org.junit.Assert.*;

import org.junit.Test;

import application.model.creature.VisitedMap;

public class VisitedMapTest {
	
	@Test
	public void mergeOnlyUselessTest() {
		int maxX = 5, maxY=5;
		int[][] useless1 = {{0,4}, {0,3}, {0,2}};
		int[][] useless2 = {{4,4}, {4,3}, {4,2}};
		VisitedMap visitedMap1 = new VisitedMap(maxX, maxY);
		for(int[] uselessPos : useless1) {
			visitedMap1.markUseless(uselessPos[0], uselessPos[1]);
		}
		VisitedMap visitedMap2 = new VisitedMap(maxX, maxY);
		for(int[] uselessPos : useless2) {
			visitedMap2.markUseless(uselessPos[0], uselessPos[1]);
		}
		assertNotEquals("Visited maps shouln't be equal before merge", visitedMap1, visitedMap2);
		VisitedMap.mergeUseless(visitedMap1, visitedMap2);
		assertNotSame("Visited maps containing only USELESS markings shouln't be the same object...", visitedMap1, visitedMap2);
		assertEquals("... but should be equal after merge", visitedMap1, visitedMap2);
	}
}
