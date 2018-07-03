package application.model.maze.tower;

public enum TowerType {
  NO("No Tower"),
  NORMAL("Normal Tower"),
  FAST("Fast Tower"),
  SLOWDOWN("Slowdown Tower"),
  AMNESIA("Amnesia Tower");

  private final String description;

  private TowerType(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return description;
  }
}
