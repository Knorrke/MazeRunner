package application.model.maze.tower;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import application.controller.gameloop.ActorInterface;
import application.model.actions.Action;
import application.model.creature.Creature;
import application.model.maze.Wall;
import application.util.ObservableBulletsListDeserializer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class AbstractTower implements ActorInterface {

  @JsonProperty protected final TowerType type;
  @JsonIgnore protected DoubleProperty fireRate;
  @JsonIgnore protected IntegerProperty damage;
  @JsonIgnore protected IntegerProperty costs;
  @JsonIgnore protected DoubleProperty visualRange;

  @JsonIgnore private Action shootingAction;
  @JsonIgnore protected int x, y;

  @JsonBackReference("tower")
  protected Wall wall;

  @JsonDeserialize(using = ObservableBulletsListDeserializer.class)
  protected ObservableList<Bullet> bullets;

  protected AbstractTower(
      double fireRate, int damage, int costs, double visualRange, TowerType type, Wall wall) {
    this(
        new SimpleDoubleProperty(fireRate),
        new SimpleIntegerProperty(damage),
        new SimpleIntegerProperty(costs),
        new SimpleDoubleProperty(visualRange),
        type,
        wall);
  }

  protected AbstractTower(
      DoubleProperty fireRate,
      IntegerProperty damage,
      IntegerProperty costs,
      DoubleProperty visualRange,
      TowerType type,
      Wall wall) {
    this.fireRate = fireRate;
    this.damage = damage;
    this.costs = costs;
    this.visualRange = visualRange;
    this.type = type;
    setWall(wall);
    shootingAction =
        new Action(1 / getFireRate()) { // Infinity if fireRate==0.0
          @Override
          protected void execute() {
            shoot();
            resetCountdown();
          }
        };
    bullets = FXCollections.observableArrayList();
  }

  @JsonCreator
  public static AbstractTower create(@JsonProperty("type") TowerType type) {
    return create(null, type);
  }

  public static AbstractTower create(Wall wall, TowerType type) {
    switch (type) {
      case NORMAL:
        return new NormalTower(wall);
      case NO:
      default:
        return new NoTower(wall);
    }
  }

  public abstract void shoot();
  
  protected void addBullet(Bullet bullet) {
    bullets.add(bullet);
  }

  @Override
  public void act(double dt) {
    shootingAction.run(dt);
    for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext(); ) {
      Bullet bullet = iterator.next();
      if (bullet.hasHitTarget()) {
        iterator.remove();
        continue;
      }
      bullet.act(dt);
    }
  }

  public List<Creature> findCreaturesInRange() {
    return wall.getCreaturesMatchingCondition(
        c -> Point.distance(c.getX(), c.getY(), x + 0.5, y + 0.5) <= getVisualRange());
  }

  /** @return the fireRate value */
  public double getFireRate() {
    return fireRate.get();
  }

  /** @param fireRate the fireRate to set */
  public void setFireRate(double fireRate) {
    this.fireRate.set(fireRate);
  }

  /** @return the fireRate property */
  public DoubleProperty fireRateProperty() {
    return fireRate;
  }

  /** @return the damage value */
  public int getDamage() {
    return damage.get();
  }

  /** @param damage the damage to set */
  public void setDamage(int damage) {
    this.damage.set(damage);
  }

  /** @return the damage property */
  public IntegerProperty damageProperty() {
    return damage;
  }

  /** @return the costs value */
  public int getCosts() {
    return costs.get();
  }

  /** @param costs the costs to set */
  public void setCosts(int costs) {
    this.costs.set(costs);
  }

  /** @return the costs property */
  public IntegerProperty costsProperty() {
    return costs;
  }

  /** @return the visualRange value */
  public double getVisualRange() {
    return visualRange.get();
  }

  /** @param visualRange the visualRange to set */
  public void setVisualRange(double visualRange) {
    this.visualRange.set(visualRange);
  }

  /** @return the visualRange property */
  public DoubleProperty visualRangeProperty() {
    return visualRange;
  }

  /** @return the type of the tower */
  public TowerType getType() {
    return type;
  }

  public void setWall(Wall wall) {
    this.wall = wall;
    if (wall != null) {
      setPosition(wall.getX(), wall.getY());
    }
  }

  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }
  protected double getX() {
    return x;
  }
  protected double getY() {
    return y;
  }


  public ObservableList<Bullet> getBullets() {
    return bullets;
  }
}
