package application.model.maze;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import application.model.creature.Creature;

public interface MazeUpdaterInterface {
  public void nextWave(List<Creature> nextCreatures);

  /** @return the maze model handled by this updater */
  @JsonIgnore
  public MazeModelInterface getMaze();
}
