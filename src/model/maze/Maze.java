package model.maze;

import javafx.collections.ObservableList;
import model.creature.Creature;

public class Maze implements MazeModelInterface {

	private ObservableList<Wall> walls;
	private ObservableList<Creature> creatures;
	
	@Override
	public ObservableList<Wall> getWalls() {
		return walls;
	}
	
	@Override
	public void addWall(Wall wall) {
		this.walls.add(wall);
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
}
