package application.model.creature.actions;

import java.awt.Point;

import application.model.actions.Action;
import application.model.creature.Creature;

public class MoveAction extends Action {

  private Creature creature;
  private double[] goal;
  private double remainingDist;

  public MoveAction(Creature creature, double[] goal) {
    super(
        Point.distance(creature.getX(), creature.getY(), goal[0], goal[1])
            / creature.getVelocity());
    this.remainingDist = Point.distance(creature.getX(), creature.getY(), goal[0], goal[1]);
    this.goal = goal;
    this.creature = creature;
  }

  @Override
  public void updateHook(double dt) {
    double ds = creature.getVelocity() * dt;
    if (remainingDist >= creature.getVelocity() * dt) {
      double newX = creature.getX() + (goal[0] - creature.getX()) / remainingDist * ds;
      double newY = creature.getY() + (goal[1] - creature.getY()) / remainingDist * ds;
      creature.moveTo(newX, newY);
      remainingDist -= ds;
    }
  }

  @Override
  public void execute() {
    creature.moveTo(goal[0], goal[1]);
    creature.chooseNewAction();
  }
}
