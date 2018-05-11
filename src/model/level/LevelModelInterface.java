package model.level;

import javafx.collections.ObservableList;
import model.ModelInterface;
import model.creature.CreatureGroup;
import model.gameloop.Updateable;

public interface LevelModelInterface extends Updateable, ModelInterface {
	
	/**
	 * Adds a new group of creatures to the timeline
	 * @param group
	 */
	public void addCreatureToTimeline(CreatureGroup group);
	
	/**
	 * @return the timeline of creature groups
	 */
	public ObservableList<CreatureGroup> getCreatureTimeline();

	/**
	 * GameLoop update
	 * @param dt
	 */
	public void update(double dt);
}
