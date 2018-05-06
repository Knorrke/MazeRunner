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
}
