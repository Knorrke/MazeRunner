package application.model.baseactions;

import java.awt.Point;
import application.model.Moveable;
import application.model.PositionAware;

public class MoveAction extends Action {

  private Moveable moveable;
  private PositionAware target;
  private double remainingDist;

  public MoveAction(Moveable moveable, PositionAware positionAware) {
    this.target = positionAware;
    this.moveable = moveable;
    remainingDist =
        Point.distance(
            moveable.getX(), moveable.getY(), positionAware.getX(), positionAware.getY());
  }

  @Override
  public void update(double dt) {
    remainingDist = Point.distance(moveable.getX(), moveable.getY(), target.getX(), target.getY());
    double dirXNormalized = (target.getX() - moveable.getX()) / remainingDist;
    double dirYNormalized = (target.getY() - moveable.getY()) / remainingDist;
    double ds = moveable.getVelocity() * dt;
    moveable.moveBy(dirXNormalized * ds, dirYNormalized * ds);
    remainingDist -= ds;
  }

  @Override
  public boolean isFinished() {
    return remainingDist <= 0;
  }

  @Override
  protected void onFinish() {
    moveable.moveTo(target.getX(), target.getY());
  }
}
