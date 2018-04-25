package model.maze;

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
	 * @param creature the creature to remove
	 */
	public void removeCreature(Creature creature);
}
