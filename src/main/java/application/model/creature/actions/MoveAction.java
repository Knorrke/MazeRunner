package application.model.creature.actions;

import java.awt.Point;

import application.model.actions.Action;
import application.model.creature.Creature;

public class MoveAction extends Action {

  private Creature creature;
  private double[] goal;
  private double[] dirNormalized;
  private double remainingDist;

  public MoveAction(Creature creature, double[] goal) {
    super(
        Point.distance(creature.getX(), creature.getY(), goal[0], goal[1])
            / creature.getVelocity());
    this.goal = goal;
    remainingDist = Point.distance(creature.getX(), creature.getY(), goal[0], goal[1]);
    this.dirNormalized =
        new double[] {
          (goal[0] - creature.getX()) / remainingDist, (goal[1] - creature.getY()) / remainingDist
        };
    this.creature = creature;
  }

  @Override
  public void updateHook(double dt) {
    double ds = creature.getVelocity() * dt;
    if (remainingDist >= ds) {
      creature.moveBy(dirNormalized[0] * ds, dirNormalized[1] * ds);
      remainingDist -= ds;
    }
  }

  @Override
  protected void execute() {
    creature.moveTo(goal[0], goal[1]);
    creature.chooseNewAction();
  }
}
