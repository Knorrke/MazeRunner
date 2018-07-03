package application.model.creature;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import application.controller.gameloop.ActorInterface;
import application.model.Moveable;
import application.model.Position;
import application.model.baseactions.Action;
import application.model.creature.actions.CreatureMoveAction;
import application.model.creature.actions.TalkAction;
import application.model.creature.movements.MovementInterface;
import application.model.creature.movements.NoSightMovement;
import application.model.creature.vision.Vision;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Creature implements ActorInterface, Moveable {
  private static final Logger LOG = Logger.getLogger(Creature.class.getName());

  private transient ObjectProperty<Position> position;
  private DoubleProperty x, y;
  private DoubleProperty velocity;
  private IntegerProperty lifes;
  private int value;
  private int startLifes;

  @JsonProperty private MovementInterface movementStrategy;
  private CreatureType type;
  @JsonBackReference private MazeModelInterface maze;
  private Vision vision;
  private VisitedMap visitedMap;
  @JsonIgnore private ObjectProperty<Action> action;

  /** json entry */
  public Creature() {
    this(0, 0, 0, 0, 0, new NoSightMovement(), new Vision(), CreatureType.NORMAL, new Maze());
  }

  public Creature(
      double x,
      double y,
      double velocity,
      int lifes,
      int value,
      MovementInterface movementStrategy,
      Vision vision,
      CreatureType type,
      MazeModelInterface maze) {
    this(
        new SimpleObjectProperty<Position>(new Position(x, y)),
        new SimpleDoubleProperty(velocity),
        new SimpleIntegerProperty(lifes),
        value,
        movementStrategy,
        vision,
        type,
        maze);
  }

  public Creature(
      ObjectProperty<Position> position,
      DoubleProperty velocity,
      IntegerProperty lifes,
      int value,
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
    this.startLifes = getLifes();
    this.value = value;
    this.movementStrategy = movementStrategy;
    this.type = type;
    this.vision = vision;
    this.maze = maze;
    this.visitedMap = new VisitedMap(maze.getMaxWallX(), maze.getMaxWallY());
    visitedMap.visit((int) getX(), (int) getY());
    markWalls();
    action = new SimpleObjectProperty<>();
    chooseNewAction();
  }

  public void moveBy(double dirX, double dirY) {
    moveTo(getX() + dirX, getY() + dirY);
  }

  public void moveTo(double newX, double newY) {
    visitedMap.visit((int) newX, (int) newY);
    setPosition(newX, newY);
    communicateWithOthers();
  }

  private void communicateWithOthers() {
    if (getType().canCommunicate()) {
      List<Creature> allCreatures = maze.getCreatures();
      List<Creature> creaturesInRange =
          allCreatures
              .stream()
              .filter(
                  creature ->
                      creature != this
                          && creature.getType().canCommunicate()
                          && Math.abs(creature.getX() - getX()) < 0.5
                          && Math.abs(creature.getY() - getY()) < 0.5)
              .collect(Collectors.toList());
      for (Creature creature : creaturesInRange) {
        synchronizeMaps(creature);
      }
    }
  }

  public void synchronizeMaps(Creature creature) {
    int differences = VisitedMap.merge(visitedMap, creature.getVisitedMap());
    if (differences > 4) {
      int timeToCommunicate = (differences - 4) * 100;
      createTalkAction(this, timeToCommunicate);
      createTalkAction(creature, timeToCommunicate);
    }
  }

  private void createTalkAction(Creature creature, int timeToCommunicate) {
    if (creature.getAction() instanceof TalkAction) {
      ((TalkAction) creature.getAction()).adjustTalkTime(timeToCommunicate);
    } else {
      creature.setAction(new TalkAction(timeToCommunicate, creature));
    }
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
  private void setLifes(int lifes) {
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

  /** @return the movementStrategy */
  public MovementInterface getMovementStrategy() {
    return movementStrategy;
  }

  /** @return the type */
  public CreatureType getType() {
    return type;
  }

  public VisitedMap getVisitedMap() {
    return visitedMap;
  }

  @Override
  public void act(double dt) {
    getAction().act(dt);
  }

  public ObjectProperty<Position> positionProperty() {
    return position;
  }

  public void setPosition(double x, double y) {
    position.set(new Position(x, y));
    markWalls();
  }

  public void chooseNewAction() {
    double[] goal = movementStrategy.getNextGoal(vision, visitedMap, getX(), getY());
    setAction(new CreatureMoveAction(this, goal));
  }

  public void setAction(Action action) {
    this.action.set(action);
  }

  /** return the current action */
  public Action getAction() {
    return action.get();
  }

  public ObjectProperty<Action> actionProperty() {
    return action;
  }

  public void markWalls() {
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        int adjacentX = (int) getX() + i;
        int adjacentY = (int) getY() + j;
        if (!visitedMap.isWall(adjacentX, adjacentY) && maze.hasWallOn(adjacentX, adjacentY)) {
          LOG.finest(String.format("wall detected on %d,%d", adjacentX, adjacentY));
          visitedMap.markWall(adjacentX, adjacentY);
        }
      }
    }
  }

  public void damage(int damage) {
    setLifes(getLifes() - damage);
    if (getLifes() <= 0) {
      maze.creatureDied(this);
    }
  }

  public void slowdown(double percentage) {
    this.setVelocity(getVelocity() * (1 - percentage));
  }

  public int getValue() {
    return value;
  }

  public int getStartLifes() {
    return startLifes;
  }
}
