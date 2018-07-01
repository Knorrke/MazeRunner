package application.model.creature.actions;

import application.model.PositionAware;
import application.model.baseactions.MoveAction;
import application.model.creature.Creature;

public class CreatureMoveAction extends MoveAction {
  private Creature creature;

  public CreatureMoveAction(Creature creature, double[] goal) {
    super(
        creature,
        new PositionAware() {
          @Override
          public double getX() {
            return goal[0];
          }

          @Override
          public double getY() {
            return goal[1];
          }
        });
    this.creature = creature;
  }

  @Override
  protected void onFinish() {
    super.onFinish();
    creature.chooseNewAction();
  }
}
