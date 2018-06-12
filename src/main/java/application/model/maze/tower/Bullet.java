package application.model.maze.tower;

import java.awt.Point;

import application.controller.gameloop.ActorInterface;
import application.model.Position;
import application.model.creature.Creature;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Bullet implements ActorInterface {
  private int damage;
  private final double sourceX, sourceY;
  private ObjectProperty<Position> relativePosition;
  private DoubleProperty dx, dy;
  private Creature target;
  private double vel;
  private boolean hasHitTarget;

  public Bullet(double sourceX, double sourceY, int damage, Creature target) {
    this(sourceX, sourceY, damage, target, 3);
  }

  public Bullet(double sourceX, double sourceY, int damage, Creature target, double vel) {
    this.sourceX = sourceX;
    this.sourceY = sourceY;
    relativePosition = new SimpleObjectProperty<>(new Position(0, 0));
    this.dx = new SimpleDoubleProperty();
    this.dy = new SimpleDoubleProperty();
    dx.bind(Bindings.createDoubleBinding(() -> relativePosition.get().getX(), relativePosition));
    dy.bind(Bindings.createDoubleBinding(() -> relativePosition.get().getY(), relativePosition));

    this.damage = damage;
    this.target = target;
    this.vel = vel;
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
    target.damage(damage);
    hasHitTarget = true;
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

  /** @return true if bullet allready hit the target */
  public boolean hasHitTarget() {
    return hasHitTarget;
  }

  /** @return the target */
  public Creature getTarget() {
    return target;
  }
}
