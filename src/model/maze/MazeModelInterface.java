package model.maze;

import java.util.List;

import javafx.collections.ObservableList;
import model.creature.Creature;

public interface MazeModelInterface {
	/**
	 * @return the walls
	 */
	public ObservableList<Wall> getWalls();
	/**
	 * @param wall the wall to add
	 */
	public void addWall(Wall wall);
	
	/**
	 * Builds a new wall. If the wall already exists, this is a no-op
	 * @param x
	 * @param y
	 */
	public void buildWall(int x, int y);
	
	/**
	 * @param wall the wall to remove
	 */
	public void removeWall(Wall wall);
	
	/**
	 * @return the creatures
	 */
	public ObservableList<Creature> getCreatures();
	/**
	 * @param creature the creature to add
	 */
	public void addCreature(Creature creature);
	

	/**
	 * @param creatures the list of creatures to add
	 */
	public void addAllCreatures(List<Creature> creatures);
	
	/**
	 * @param creature the creature to remove
	 */
	public void removeCreature(Creature creature);
	
	
	/**
	 * Checks if there is a wall on a certain position
	 * @param int x
	 * @param int y
	 */
	public boolean hasWallOn(int x, int y);
	
	/**
	 * @return the maximal X value for walls
	 */
	public int getMaxWallX();
	
	/**
	 * @return the maximal Y value for walls
	 */
	public int getMaxWallY();
}
