package org.mazerunner.model.creature.actions;

import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.movements.PerfectMovement;
import org.mazerunner.model.maze.MazeModelInterface;

public class CommandedAction extends CreatureMoveAction {

  private boolean finished;
  private Creature creature;
  private MovementInterface previousMovement;

  public CommandedAction(Creature creature, MazeModelInterface maze) {
    super(creature, creature.findNextGoal());
    this.creature = creature;
    previousMovement = creature.getMovementStrategy();
    creature.setMovementStrategy(new PerfectMovement(maze));
    target = toPosition(creature.findNextGoal());
  }

  public void setFinished() {
    finished = true;
  }

  @Override
  public boolean isFinished() {
    //    System.out.println("Commanded is finished? " + finished);
    if (super.isFinished()) {
      super.onFinish();
      target = toPosition(creature.findNextGoal());
    }
    return finished;
  }

  @Override
  protected void onFinish() {
    System.out.println("Commanded on finish");
    creature.setMovementStrategy(previousMovement);
  }
}
