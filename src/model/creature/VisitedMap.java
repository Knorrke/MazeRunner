package model.creature;

public class VisitedMap {
	public enum VisitedState {		
		UNKNOWN, VISITED, USELESS
	}
	
	private VisitedState[][] map;
	
	public VisitedMap(int maxX, int maxY) {
		map = new VisitedState[maxX][maxY];
		for(int x=0; x < map.length; x++) {
			for(int y=0; y<map[x].length; y++){
				map[x][y] = VisitedState.UNKNOWN;
			}
		}
	}
	
	public void markVisited(int x, int y) {
		if (x < map.length && y < map[x].length) {			
			map[x][y] = VisitedState.VISITED;
		}
	}
	
	public void markUseless(int x, int y) {
		if (x < map.length && y < map[x].length) {
			map[x][y] = VisitedState.USELESS;
		}
	}
	
	public boolean isVisited(int x, int y) {
		if (x < map.length && y < map[x].length) {
			return map[x][y] == VisitedState.VISITED;
		} else {
			return true;
		}
	}
	
	public boolean isUseless(int x, int y) {
		if (x < map.length && y < map[x].length){
		return map[x][y] == VisitedState.USELESS;
		} else {
			return true;
		}
	}
	
	public boolean isUnknown(int x, int y) {
		if (x < map.length && y < map[x].length) {
			return map[x][y] == VisitedState.UNKNOWN;
		} else {
			return false;
		}
	}
}
