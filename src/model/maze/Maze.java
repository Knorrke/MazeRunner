package model.maze;

import java.awt.Rectangle;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.creature.Creature;

public class Maze implements MazeModelInterface {

	private ObservableList<Wall> walls = FXCollections.observableArrayList();
	private ObservableList<Creature> creatures = FXCollections.observableArrayList();
	private final int maxWallX = 20, maxWallY = 10;
	
	@Override
	public ObservableList<Wall> getWalls() {
		return walls;
	}
	
	@Override
	public void addWall(Wall wall) {
		int x = wall.getX();
		int y = wall.getY();
		if (checkBounds(x,y) && !hasWallOn(x, y)) {			
			this.walls.add(wall);
		}
	}

	@Override
	public void buildWall(int x, int y) {
		this.addWall(new Wall(x, y));
	}
	
	private boolean checkBounds(int x, int y) {
		//return new Rectangle(maxWallX, maxWallY).contains(x, y);
		return x >= 0 && x < maxWallX && y >= 0 && y < maxWallY;
	}

	
	@Override
	public void removeWall(Wall wall) {
		this.walls.remove(wall);
	}
	
	@Override
	public ObservableList<Creature> getCreatures() {
		return creatures;
	}
	
	@Override
	public void addCreature(Creature creature) {
		this.creatures.add(creature);
	}
	
	@Override
	public void removeCreature(Creature creature) {
		this.creatures.remove(creature);
	}

	@Override
	public void addAllCreatures(List<Creature> creatures) {
		this.creatures.addAll(creatures);
	}

	public boolean hasWallOn(int x, int y) {
		return !getWalls().filtered(wall -> wall.getX() == x && wall.getY() == y).isEmpty();
	}
	

	@Override
	public int getMaxWallX() {
		return maxWallX;
	}

	@Override
	public int getMaxWallY() {
		return maxWallY;
	}
}
