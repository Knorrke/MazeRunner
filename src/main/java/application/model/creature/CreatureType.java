package application.model.creature;

public enum CreatureType {
  NORMAL(10),
  TOUGH(20);

  private int defaultLifes;

  private CreatureType(int defaultLifes) {
    this.defaultLifes = defaultLifes;
  }

  public int getDefaultLifes() {
    return defaultLifes;
  }
}
