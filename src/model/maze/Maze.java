package model.maze;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.creature.Creature;

public class Maze implements MazeModelInterface {

	private ObservableList<Wall> walls = FXCollections.observableArrayList();
	private ObservableList<Creature> creatures = FXCollections.observableArrayList();
	
	@Override
	public ObservableList<Wall> getWalls() {
		return walls;
	}
	
	@Override
	public void addWall(Wall wall) {
		if (!hasWallOn(wall.getX(), wall.getY()))
			this.walls.add(wall);
	}

	@Override
	public void buildWall(int x, int y) {
		this.addWall(new Wall(x, y));
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
}
