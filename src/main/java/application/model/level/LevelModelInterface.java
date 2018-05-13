package application.model.level;

import application.model.ModelInterface;
import application.model.creature.CreatureGroup;
import application.model.gameloop.Updateable;
import javafx.collections.ObservableList;

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
