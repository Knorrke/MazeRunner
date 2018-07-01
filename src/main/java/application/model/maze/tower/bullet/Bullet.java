package application.model.maze.tower.bullet;

import application.controller.gameloop.ActorInterface;
import application.model.Moveable;
import application.model.Position;
import application.model.baseactions.Action;
import application.model.creature.Creature;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class Bullet implements Moveable, ActorInterface {
  private final double sourceX, sourceY;
  private ObjectProperty<Position> relativePosition;
  private DoubleProperty dx, dy;
  private double vel;
  private BooleanProperty hasHitTarget;
  private Action moveAction;
  private Creature target;

  public Bullet(double sourceX, double sourceY, Creature target,
      double vel) {
    this.sourceX = sourceX;
    this.sourceY = sourceY;
    relativePosition = new SimpleObjectProperty<>(new Position(0, 0));
    this.dx = new SimpleDoubleProperty();
    this.dy = new SimpleDoubleProperty();
    dx.bind(Bindings.createDoubleBinding(() -> relativePosition.get().getX(), relativePosition));
    dy.bind(Bindings.createDoubleBinding(() -> relativePosition.get().getY(), relativePosition));

    this.vel = vel;
    this.hasHitTarget = new SimpleBooleanProperty(false);
    moveAction = new BulletMoveAction(this, target);
    this.target = target;
  }

  @Override
  public void act(double dt) {
    moveAction.act(dt);
  }

  public void hitTarget() {
    setHasHitTarget(true);
  }
  
  public Creature getTarget() {
    return target;
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

  /**
   * @return true if bullet is completely finished and can be removed from updates
   */
  public boolean isOver() {
    return hasHitTarget();
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

  @Override
  public double getVelocity() {
    return vel;
  }

  @Override
  public void moveBy(double dx, double dy) {
    setRelativePosition(new Position(getDx() + dx, getDy() + dy));
  }

  @Override
  public void moveTo(double x, double y) {
    setRelativePosition(new Position(x,y));
  }
}
