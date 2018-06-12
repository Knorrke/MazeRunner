package application.model.maze.tower;

import java.awt.Point;

import application.controller.gameloop.ActorInterface;
import application.model.creature.Creature;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Bullet implements ActorInterface {
  private int damage;
  private DoubleProperty x, y;
  private Creature target;
  private double vel;
  private boolean hasHitTarget;

  public Bullet(double sourceX, double sourceY, int damage, Creature target) {
    this(sourceX, sourceY, damage, target, 3);
  }

  public Bullet(double sourceX, double sourceY, int damage, Creature target, double vel) {
    x = new SimpleDoubleProperty(sourceX);
    y = new SimpleDoubleProperty(sourceY);
    this.damage = damage;
    this.target = target;
    this.vel = vel;
  }


  @Override
  public void act(double dt) {
    double remainingDist = Point.distance(getX(), getY(), target.getX(), target.getY());
    double dirXNormalized = (target.getX() - getX())/remainingDist;
    double dirYNormalized = (target.getY() - getY())/remainingDist;
    setX(getX() + dirXNormalized * dt * vel);
    setY(getY() + dirYNormalized * dt * vel);
    if (remainingDist < dt*vel) {
      hitTarget();
    }
  }

  public void hitTarget() {
    target.damage(damage);
    hasHitTarget = true;
  }

  /** @return the x property */
  public DoubleProperty xProperty() {
    return x;
  }

  /** @param x the x value to set */
  private void setX(double x) {
    this.x.set(x);
  }

  /** @return the x value */
  public double getX() {
    return x.get();
  }
  /** @return the y property */
  public DoubleProperty yProperty() {
    return y;
  }
  /** @param y the y value to set */
  private void setY(double y) {
    this.y.set(y);
  }

  /** @return the y value */
  public double getY() {
    return y.get();
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
