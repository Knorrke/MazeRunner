package org.mazerunner.model.maze;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.mazerunner.controller.gameloop.ActorInterface;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.tower.AbstractTower;
import org.mazerunner.model.maze.tower.TowerType;

public class Wall implements ActorInterface {

  private IntegerProperty x, y;

  @JsonManagedReference("tower")
  private ObjectProperty<AbstractTower> tower;

  @JsonBackReference("wall")
  private MazeModelInterface maze;

  @JsonCreator
  public Wall(@JsonProperty("x") int x, @JsonProperty("y") int y) {
    this(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
    setTower(AbstractTower.create(this, TowerType.NO));
  }

  public Wall(int x, int y, AbstractTower tower) {
    this(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
    setTower(tower);
  }

  public Wall(IntegerProperty x, IntegerProperty y) {
    this(x, y, new SimpleObjectProperty<>());
  }

  public Wall(IntegerProperty x, IntegerProperty y, ObjectProperty<AbstractTower> tower) {
    this.x = x;
    this.y = y;
    this.tower = tower;
  }

  /** @return the x position */
  public int getX() {
    return x.get();
  }

  /** @param y the x position to set */
  public void setX(int x) {
    this.x.set(x);
  }

  /** @return the x property */
  public IntegerProperty xProperty() {
    return x;
  }

  /** @return the y position */
  public int getY() {
    return y.get();
  }

  /** @param y the y position to set */
  public void setY(int y) {
    this.y.set(y);
  }

  /** @return the y property */
  public IntegerProperty yProperty() {
    return y;
  }

  /** @return the position as array */
  @JsonIgnore
  public int[] getPosition() {
    return new int[] {x.get(), y.get()};
  }

  /** @return the abstractTower */
  public AbstractTower getTower() {
    return tower.get();
  }

  /** @param abstractTower the abstractTower to set */
  public void setTower(AbstractTower abstractTower) {
    this.tower.set(abstractTower);
    getTower().setWall(this);
  }

  public ObjectProperty<AbstractTower> towerProperty() {
    return tower;
  }

  @Override
  public void act(double dt) {
    getTower().act(dt);
  }

  @JsonIgnore
  public int getCosts() {
    return 1 + getTower().getCosts();
  }

  public List<Creature> getCreaturesMatchingCondition(Predicate<Creature> pred) {
    List<Creature> filtered = new ArrayList<>();
    for (Creature creature : maze.getCreatures()) {
      if (pred.test(creature)) {
        filtered.add(creature);
      }
    }
    return filtered;
  }

  public void setMaze(MazeModelInterface maze) {
    this.maze = maze;
  }

  public void buildTower(TowerType type) {
    setTower(AbstractTower.create(this, type));
  }

  public boolean hasTower() {
    return !getTower().getType().equals(TowerType.NO);
  }

  public void upgradeTower() {
    setTower(getTower().upgrade());
  }
}
