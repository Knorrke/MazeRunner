package application.model.level;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import application.controller.gameloop.Updateable;
import application.model.ModelInterface;
import application.model.creature.CreatureGroup;
import application.model.maze.MazeModelInterface;
import javafx.collections.ObservableList;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Level.class, name = "Level"),
})
public interface LevelModelInterface extends Updateable, ModelInterface {

  /**
   * Adds a new group of creatures to the timeline
   *
   * @param group
   */
  public void addCreatureToTimeline(CreatureGroup group);

  /** @return the timeline of creature groups */
  public ObservableList<CreatureGroup> getCreatureTimeline();

  /**
   * GameLoop update
   *
   * @param dt
   */
  @Override
  public void update(double dt);

  /** Creates the next wave of creatures according to Timeline and resets the countdown */
  public void sendNextCreatureWave();

  public void setMazeModel(MazeModelInterface maze);
}
