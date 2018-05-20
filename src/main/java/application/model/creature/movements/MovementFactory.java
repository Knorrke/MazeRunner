package application.model.creature.movements;

public class MovementFactory {
  public enum MovementType {
    NO_SIGHT
  }

  public static MovementInterface get(MovementType type) {
    switch(type) {
    case NO_SIGHT:
    default:
      return new NoSightMovement();
    }
  }
}
