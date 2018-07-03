package application.model.creature;

import application.model.creature.movements.MovementInterface;
import application.model.creature.movements.NoSightMovement;
import application.model.creature.movements.RandomMovement;

public enum CreatureType {
  DUMB(3, 10, 1, new RandomMovement(), false),
  NORMAL(1, 10, 1, new NoSightMovement(), true),
  TOUGH(0.8, 30, 3, new NoSightMovement(), true);

  private double defaultVelocity;
  private int defaultLifes, defaultValue;
  private MovementInterface defaultMovement;
  private boolean canCommunicate;

  private CreatureType(
      double defaultVelocity,
      int defaultLifes,
      int defaultValue,
      MovementInterface defaultMovement,
      boolean canCommunicate) {
    this.defaultVelocity = defaultVelocity;
    this.defaultLifes = defaultLifes;
    this.defaultValue = defaultValue;
    this.defaultMovement = defaultMovement;
    this.canCommunicate = canCommunicate;
  }

  public int getDefaultLifes() {
    return defaultLifes;
  }

  public int getDefaultValue() {
    return defaultValue;
  }

  public double getDefaultVelocity() {
    return defaultVelocity;
  }

  public MovementInterface getMovementStrategy() {
    return defaultMovement;
  }

  public boolean canCommunicate() {
    return canCommunicate;
  }
}
