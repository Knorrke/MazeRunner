package org.mazerunner.model.creature.actions;

import org.mazerunner.model.PositionAware;
import org.mazerunner.model.baseactions.MoveAction;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureType;

public class CreatureMoveAction extends MoveAction {
  private Creature creature;

  public CreatureMoveAction(Creature creature, double[] goal) {
    this(creature, PositionAware.valueOf(goal));
  }

  public CreatureMoveAction(Creature creature, PositionAware position) {
    super(creature, position);
    this.creature = creature;
  }

  @Override
  protected void onFinish() {
    super.onFinish();
    double[] nextGoal = creature.findNextGoal();
    if (creature.getType() == CreatureType.COMMANDER
        && nextGoal[0] == creature.getX()
        && nextGoal[1] == creature.getY()) {
      creature.setAction(new CommandAction(creature, creature.getMaze()));
    } else {
      setTarget(PositionAware.valueOf(nextGoal));
    }
  }
}
