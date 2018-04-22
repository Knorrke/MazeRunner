package model;

import javafx.collections.ObservableList;
import model.creature.CreatureGroup;

public interface LevelModelInterface {
	
	/**
	 * Adds a new group of creatures to the timeline
	 * @param group
	 */
	public void addCreatureToTimeline(CreatureGroup group);
	
	/**
	 * @return the timeline of creature groups
	 */
	public ObservableList<CreatureGroup> getCreatureTimeline();
}
