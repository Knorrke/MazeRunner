package application.model.maze.tower.bullet;

import java.awt.Point;
import java.util.function.Consumer;
import application.controller.gameloop.ActorInterface;
import application.model.Position;
import application.model.creature.Creature;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Bullet implements ActorInterface {
  private Consumer<Creature> action;
  private final double sourceX, sourceY;
  private ObjectProperty<Position> relativePosition;
  private DoubleProperty dx, dy;
  private Creature target;
  private double vel;
  private BooleanProperty hasHitTarget;

  public Bullet(
      double sourceX, double sourceY, Consumer<Creature> action, Creature target, double vel) {
    this.sourceX = sourceX;
    this.sourceY = sourceY;
    relativePosition = new SimpleObjectProperty<>(new Position(0, 0));
    this.dx = new SimpleDoubleProperty();
    this.dy = new SimpleDoubleProperty();
    dx.bind(Bindings.createDoubleBinding(() -> relativePosition.get().getX(), relativePosition));
    dy.bind(Bindings.createDoubleBinding(() -> relativePosition.get().getY(), relativePosition));

    this.action = action;
    this.target = target;
    this.vel = vel;
    this.hasHitTarget = new SimpleBooleanProperty(false);
  }

  @Override
  public void act(double dt) {
    if (!hasHitTarget()) {
      double remainingDist = Point.distance(getX(), getY(), target.getX(), target.getY());
      double dirXNormalized = (target.getX() - getX()) / remainingDist;
      double dirYNormalized = (target.getY() - getY()) / remainingDist;
      setRelativePosition(
          new Position(getDx() + dirXNormalized * dt * vel, getDy() + dirYNormalized * dt * vel));

      if (remainingDist < dt * vel) {
        hitTarget();
      }
    }
  }

  public void hitTarget() {
    action.accept(target);
    setHasHitTarget(true);
  }

  /** @return the relative position property */
  public ObjectProperty<Position> relativePositionProperty() {
    return relativePosition;
  }

  private void setRelativePosition(Position relativePosition) {
    this.relativePosition.set(relativePosition);
  }

  /** @return the relative x property */
  public DoubleProperty dxProperty() {
    return dx;
  }
  /** @return the relative x value */
  private double getDx() {
    return dx.get();
  }

  /** @return the absolute x value */
  public double getX() {
    return sourceX + getDx();
  }

  /** @return the relative y property */
  public DoubleProperty dyProperty() {
    return dy;
  }

  /** @return the relative y value */
  private double getDy() {
    return dy.get();
  }

  /** @return the absolute y value */
  public double getY() {
    return sourceY + getDy();
  }

  /** @return true if bullet already hit the target */
  public boolean hasHitTarget() {
    return hasHitTarget.get();
  }

  /** @return hasHitTarget property if bullet has hit the target */
  public BooleanProperty hasHitTargetProperty() {
    return hasHitTarget;
  }

  private void setHasHitTarget(boolean newVal) {
    hasHitTarget.set(newVal);
  }

  /** @return the target */
  public Creature getTarget() {
    return target;
  }

  /** @return true if the action has finished and bullet can be removed from updates */
  public boolean isOver() {
    return hasHitTarget();
  }
}
