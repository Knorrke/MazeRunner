package application.model.maze;

import java.util.List;

import application.model.creature.Creature;

public class MazeUpdater implements MazeUpdaterInterface {
  private MazeModelInterface maze;

  public MazeUpdater(MazeModelInterface maze) {
    this.maze = maze;
  }

  @Override
  public void nextWave(List<Creature> nextCreatures) {
    maze.addAllCreatures(nextCreatures);
  }

  @Override
  public MazeModelInterface getMaze() {
    return maze;
  }
}
