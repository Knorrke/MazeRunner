package org.mazerunner.model.creature;

import java.util.function.Supplier;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.movements.NoSightMovement;
import org.mazerunner.model.creature.movements.RandomMovement;

public enum CreatureType {
  DUMB(3, 10, 1, () -> new RandomMovement(), false),
  NORMAL(1, 10, 1, () -> new NoSightMovement(), true),
  TOUGH(0.8, 30, 3, () -> new NoSightMovement(), true);

  private double defaultVelocity;
  private int defaultLifes;
  private int defaultValue;
  private Supplier<MovementInterface> defaultMovementSupplier;
  private boolean canCommunicate;

  private CreatureType(
      double defaultVelocity,
      int defaultLifes,
      int defaultValue,
      Supplier<MovementInterface> defaultMovementSupplier,
      boolean canCommunicate) {
    this.defaultVelocity = defaultVelocity;
    this.defaultLifes = defaultLifes;
    this.defaultValue = defaultValue;
    this.defaultMovementSupplier = defaultMovementSupplier;
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
    return defaultMovementSupplier.get();
  }

  public boolean canCommunicate() {
    return canCommunicate;
  }
}
