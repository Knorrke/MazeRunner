package org.mazerunner.model.creature.actions;

import org.mazerunner.model.baseactions.Action;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.movements.PerfectMovement;
import org.mazerunner.model.maze.MazeModelInterface;

public class CommandedAction extends CreatureMoveAction {
  private boolean finished;
  private Creature creature;
  private MovementInterface previousMovement;
  private Action previousAction;

  public CommandedAction(Creature creature, MazeModelInterface maze) {
    super(creature, creature.findNextGoal());
    this.creature = creature;
    previousMovement = creature.getMovementStrategy();
    previousAction = creature.getAction();
    creature.setMovementStrategy(new PerfectMovement(maze));
    target = toPosition(creature.findNextGoal());
  }

  public void setFinished() {
    finished = true;
  }

  @Override
  public boolean isFinished() {
    if (super.isFinished()) {
      // super.onFinish();
      target = toPosition(creature.findNextGoal());
    }
    return finished;
  }

  @Override
  protected void onFinish() {
    creature.setMovementStrategy(previousMovement);
    creature.setAction(previousAction);
    creature.act(0);
  }
}
