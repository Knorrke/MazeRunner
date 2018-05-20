package application.model.creature;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import application.model.Position;
import application.model.creature.movements.MovementInterface;
import application.model.creature.vision.Vision;
import application.model.gameloop.ActorInterface;
import application.model.maze.MazeModelInterface;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Creature implements ActorInterface {
  private double countdown = 0.5;
  private transient ObjectProperty<Position> position;
  private DoubleProperty x, y;
  private DoubleProperty velocity;
  private IntegerProperty lifes;
  private MovementInterface movementStrategy;
  private final CreatureType type;
  private transient MazeModelInterface maze;
  private Vision vision;
  private VisitedMap map;
  private transient Stack<double[]> lastMovements;

  public Creature(
      double x,
      double y,
      double velocity,
      int lifes,
      MovementInterface movementStrategy,
      Vision vision,
      CreatureType type,
      MazeModelInterface maze) {
    this(
        new SimpleObjectProperty<Position>(new Position(x, y)),
        new SimpleDoubleProperty(velocity),
        new SimpleIntegerProperty(lifes),
        movementStrategy,
        vision,
        type,
        maze);
  }

  public Creature(
      ObjectProperty<Position> position,
      DoubleProperty velocity,
      IntegerProperty lifes,
      MovementInterface movementStrategy,
      Vision vision,
      CreatureType type,
      MazeModelInterface maze) {
    this.position = position;
    this.x = new SimpleDoubleProperty();
    this.y = new SimpleDoubleProperty();
    x.bind(Bindings.createDoubleBinding(() -> position.get().getX(), position));
    y.bind(Bindings.createDoubleBinding(() -> position.get().getY(), position));

    this.velocity = velocity;
    this.lifes = lifes;
    this.movementStrategy = movementStrategy;
    this.type = type;
    this.vision = vision;
    this.maze = maze;
    this.map = new VisitedMap(maze.getMaxWallX(), maze.getMaxWallY());
    markCurrentFieldVisited();
    lastMovements = new Stack<>();
  }

  public void move() {
    move(1);
  }

  public void move(double dt) {
    double[] dir = movementStrategy.getMoveDirection(maze, vision, map, getX(), getY());
    move(dir, dt);
  }

  public void move(double dirX, double dirY) {
    move(new double[] {dirX, dirY});
  }

  public void move(double[] dir) {
    move(dir, 1);
  }

  private void move(double[] dir, double dt) {
    boolean backtracking = false;
    if (dir == null) {
      if (lastMovements.isEmpty()) {
        dir = new double[] {1, 0};
      } else { // backtracking
        double[] last = lastMovements.pop();
        dir = new double[] {last[0] * -1, last[1] * -1};
        markCurrentFieldUseless();
        backtracking = true;
      }
    }
    double newX = getX() + getVelocity() * dir[0] * dt;
    double newY = getY() + getVelocity() * dir[1] * dt;
    setPosition(newX, newY);
    markCurrentFieldVisited();
    if (!backtracking) {
      lastMovements.push(dir);
    }

    communicateWithOthers();
  }

  private void communicateWithOthers() {
    List<Creature> allCreatures = maze.getCreatures();
    List<Creature> creaturesInRange =
        allCreatures
            .stream()
            .filter(
                creature ->
                    creature != this
                        && Math.abs(creature.getX() - getX()) < 0.3
                        && Math.abs(creature.getY() - getY()) < 0.3)
            .collect(Collectors.toList());
    creaturesInRange.forEach(this::synchronizeMaps);
  }

  private void markCurrentFieldVisited() {
    map.markVisited((int) getX(), (int) getY());
  }

  private void markCurrentFieldUseless() {
    map.markUseless((int) getX(), (int) getY());
  }

  public void synchronizeMaps(Creature creature2) {
    VisitedMap.mergeUseless(map, creature2.getVisitedMap());
  }

  /** @return the x value */
  public double getX() {
    return this.x.get();
  }

  /** @param x the x to set */
  public void setX(double x) {
    setPosition(x, getY());
  }

  /** @return the x property */
  public DoubleProperty xProperty() {
    return x;
  }

  /** @return the y value */
  public double getY() {
    return y.get();
  }

  /** @param y the y to set */
  public void setY(double y) {
    setPosition(getX(), y);
  }

  /** @return the y property */
  public DoubleProperty yProperty() {
    return y;
  }

  /** @return the velocity value */
  public double getVelocity() {
    return velocity.get();
  }

  /** @param velocity the velocity to set */
  public void setVelocity(double velocity) {
    this.velocity.set(velocity);
  }

  /** @return the velocity property */
  public DoubleProperty velocityProperty() {
    return velocity;
  }

  /** @return the lifes value */
  public int getLifes() {
    return lifes.get();
  }

  /** @param lifes the lifes to set */
  public void setLifes(int lifes) {
    this.lifes.set(lifes);
  }

  /** @return the lifes property */
  public IntegerProperty lifesProperty() {
    return lifes;
  }

  /** @param movementStrategy the movementStrategy to set */
  public void setMovementStrategy(MovementInterface movementStrategy) {
    this.movementStrategy = movementStrategy;
  }

  /** @return the type */
  public CreatureType getType() {
    return type;
  }

  public VisitedMap getVisitedMap() {
    return map;
  }

  @Override
  public void act(double dt) {
    if (countdown < dt) {
      move();
      countdown = 0.5;
    } else {
      countdown -= dt;
    }
  }

  public ObjectProperty<Position> positionProperty() {
    return position;
  }

  public void setPosition(double x, double y) {
    position.set(new Position(x, y));
  }
}
