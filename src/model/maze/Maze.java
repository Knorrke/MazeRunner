package model.maze;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.creature.Creature;
import model.creature.CreatureFactory;
import model.creature.CreatureType;
import model.gameloop.ActorInterface;

public class Maze implements MazeModelInterface {

	private ObservableList<Wall> walls = FXCollections.observableArrayList();
	private ObservableList<Creature> creatures = FXCollections.observableArrayList();
	private final int maxWallX, maxWallY;
	
	public Maze() {
		this(20,10);
	}
	
	public Maze(int maxX, int maxY) {
		maxWallX = maxX;
		maxWallY = maxY;
		addCreature(CreatureFactory.create(this, CreatureType.NORMAL));
	}

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

	@Override
	public void update(double dt) {
		for (ActorInterface creature : creatures) {
			creature.act(dt);
		}
		for (ActorInterface wall : walls) {
			wall.act(dt);
		}
	}
}
