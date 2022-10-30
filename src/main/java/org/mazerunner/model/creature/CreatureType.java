package org.mazerunner.model.creature;

import java.util.function.Supplier;
import org.mazerunner.model.creature.movements.ClimbWallMovement;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.movements.NoSightMovement;
import org.mazerunner.model.creature.movements.RandomMovement;
import org.mazerunner.model.creature.movements.SightMovement;

public enum CreatureType {
  DUMB(3, 10, 1, RandomMovement::new, 0, false),
  NORMAL(1, 10, 1, NoSightMovement::new, 0, true),
  TOUGH(0.8, 30, 3, NoSightMovement::new, 0, true),
  SIGHT(1, 10, 1, SightMovement::new, Integer.MAX_VALUE, true),
  COMMANDER(1, 15, 3, ClimbWallMovement::new, Integer.MAX_VALUE, false);

  private double defaultVelocity;
  private int defaultLifes;
  private int defaultValue;
  private Supplier<MovementInterface> defaultMovementSupplier;
  private int defaultVisionRadius;
  private boolean canCommunicate;

  private CreatureType(
      double defaultVelocity,
      int defaultLifes,
      int defaultValue,
      Supplier<MovementInterface> defaultMovementSupplier,
      int defaultVisionRadius,
      boolean canCommunicate) {
    this.defaultVelocity = defaultVelocity;
    this.defaultLifes = defaultLifes;
    this.defaultValue = defaultValue;
    this.defaultMovementSupplier = defaultMovementSupplier;
    this.defaultVisionRadius = defaultVisionRadius;
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

  double getDefaultVisionRadius() {
    return defaultVisionRadius;
  }
}
