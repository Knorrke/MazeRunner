package org.mazerunner.model.maze.tower;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mazerunner.controller.gameloop.ActorInterface;
import org.mazerunner.model.baseactions.CountdownAction;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.bullet.Bullet;
import org.mazerunner.util.ObservableBulletsListDeserializer;
import org.mazerunner.util.Util;

public abstract class AbstractTower implements ActorInterface, Cloneable {
  private static final Logger LOG = Logger.getLogger(AbstractTower.class.getName());
  @JsonProperty protected final TowerType type;
  @JsonIgnore protected double fireRate;
  @JsonIgnore protected int damage;
  @JsonIgnore protected int costs;
  @JsonIgnore protected double visualRange;

  @JsonIgnore private CountdownAction shootingAction;
  @JsonIgnore private ObjectProperty<Creature> targetedCreatureProperty;
  @JsonIgnore protected int x, y;

  @JsonIgnore private List<TowerUpgrade> upgrades;

  @JsonBackReference("tower")
  protected Wall wall;

  @JsonDeserialize(using = ObservableBulletsListDeserializer.class)
  protected ObservableList<Bullet> bullets;

  private int level = 0;

  protected AbstractTower(
      double fireRate, int damage, int costs, double visualRange, TowerType type, Wall wall) {
    this.fireRate = fireRate;
    this.damage = damage;
    this.costs = costs;
    this.visualRange = visualRange;
    this.type = type;
    setWall(wall);
    shootingAction =
        new CountdownAction(1 / getFireRate()) { // Infinity if fireRate==0.0
          @Override
          protected void onFinish() {
            shoot();
            resetCountdown();
          }
        };
    bullets = FXCollections.observableArrayList();
    this.targetedCreatureProperty = new SimpleObjectProperty<Creature>(null);
    this.upgrades = new ArrayList<>();
  }

  @JsonCreator
  private static AbstractTower create(
      @JsonProperty("type") TowerType type, @JsonProperty("level") int level) {
    AbstractTower base = create(null, type);
    AbstractTower upgraded = base;
    for (int i = 0; i < level; i++) {
      upgraded = upgraded.upgrade();
    }
    return upgraded;
  }

  public static AbstractTower create(TowerType type) {
    return create(null, type);
  }

  public static AbstractTower create(Wall wall, TowerType type) {
    switch (type) {
      case AMNESIA:
        return new AmnesiaTower(wall);
      case FAST:
        return new FastTower(wall);
      case SLOWDOWN:
        return new SlowdownTower(wall);
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
    setTargetedCreature(target());
    shootingAction.act(dt);
    for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext(); ) {
      Bullet bullet = iterator.next();
      if (bullet.isOver()) {
        iterator.remove();
        continue;
      }
      bullet.act(dt);
    }
  }

  protected Creature target() {
    List<Creature> all = findCreaturesInRange();
    return all.isEmpty() ? null : all.get(0);
  }

  public List<Creature> findCreaturesInRange() {
    return wall.getCreaturesMatchingCondition(
        c -> Util.distance(c.getX(), c.getY(), getX() + 0.5, getY() + 0.5) <= getVisualRange());
  }

  public AbstractTower upgrade() {
    if (upgrades.size() > getLevel()) {
      TowerUpgrade upgrade = upgrades.get(getLevel());
      try {
        AbstractTower clone = (AbstractTower) this.clone();
        clone.fireRate = upgrade.getFireRateUpgrader().apply(getFireRate());
        clone.damage = upgrade.getDamageUpgrader().apply(getDamage());
        clone.visualRange = upgrade.getVisualRangeUpgrader().apply(getVisualRange());
        clone.costs = this.costs + upgrade.getCosts();
        clone.level = level + 1;
        clone.shootingAction =
            new CountdownAction(1 / clone.getFireRate()) { // Infinity if fireRate==0.0
              @Override
              protected void onFinish() {
                clone.shoot();
                resetCountdown();
              }
            };
        return clone;
      } catch (CloneNotSupportedException e) {
        LOG.log(Level.SEVERE, "couldn't clone tower", e);
      }
    }

    return this; // if something went wrong
  }

  /** @return the fireRate */
  public double getFireRate() {
    return fireRate;
  }

  /** @return the damage */
  public int getDamage() {
    return damage;
  }

  /** @return the costs */
  public int getCosts() {
    return costs;
  }

  /** @return the visualRange */
  public double getVisualRange() {
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

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public ObservableList<Bullet> getBullets() {
    return bullets;
  }

  @JsonIgnore
  public TowerUpgrade getNextUpgrade() {
    if (upgrades.size() > getLevel()) {
      return upgrades.get(getLevel());
    }
    return null;
  }

  protected List<TowerUpgrade> getUpgrades() {
    return upgrades;
  }

  public int getLevel() {
    return level;
  }

  public ObjectProperty<Creature> targetedCreatureProperty() {
    return targetedCreatureProperty;
  }

  private void setTargetedCreature(Creature targetedCreature) {
    targetedCreatureProperty().set(targetedCreature);
    ;
  }
}
