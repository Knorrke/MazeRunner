package org.mazerunner.model.baseactions;

import org.mazerunner.model.Moveable;
import org.mazerunner.model.PositionAware;
import org.mazerunner.util.Util;

public class MoveAction extends Action {

  private Moveable moveable;
  private PositionAware target;
  private double remainingDist;
  private double oldTargetX = -1, oldTargetY = -1;

  public MoveAction(Moveable moveable, PositionAware positionAware) {
    this.target = positionAware;
    this.moveable = moveable;
    remainingDist =
        Util.distance(moveable.getX(), moveable.getY(), positionAware.getX(), positionAware.getY());
  }

  @Override
  public void update(double dt) {
    if (target.getX() != oldTargetX || target.getY() != oldTargetY) {
      remainingDist = Util.distance(moveable.getX(), moveable.getY(), target.getX(), target.getY());
      oldTargetX = target.getX();
      oldTargetY = target.getY();
    }
    double dirXNormalized = (target.getX() - moveable.getX()) / remainingDist;
    double dirYNormalized = (target.getY() - moveable.getY()) / remainingDist;
    double ds = moveable.getVelocity() * dt;
    if (remainingDist < ds) {
      moveable.moveBy(dirXNormalized * remainingDist, dirYNormalized * remainingDist);
    } else {
      moveable.moveBy(dirXNormalized * ds, dirYNormalized * ds);
    }
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
