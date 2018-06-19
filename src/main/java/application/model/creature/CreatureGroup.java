package application.model.creature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatureGroup {
  private final CreatureType type;
  private final int number;

  @JsonProperty("toughnessFactor")
  private double thoughnessFactor = 1.0;

  @JsonCreator
  public CreatureGroup(
      @JsonProperty("type") CreatureType type, @JsonProperty("number") int number) {
    this(type, number, 1);
  }

  public CreatureGroup(CreatureType type, int number, double toughnessFactor) {
    this.type = type;
    this.number = number;
    this.thoughnessFactor = toughnessFactor;
  }

  /** @return the number */
  public int getNumber() {
    return number;
  }

  /** @return the type */
  public CreatureType getType() {
    return type;
  }

  /** @return the toughnessFactor */
  public double getToughnessFactor() {
    return thoughnessFactor;
  }
}
