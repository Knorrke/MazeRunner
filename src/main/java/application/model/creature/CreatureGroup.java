package application.model.creature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatureGroup {
  private final CreatureType type;
  private final int number;

  @JsonCreator
  public CreatureGroup(@JsonProperty("type") CreatureType type,@JsonProperty("number") int number) {
    this.type = type;
    this.number = number;
  }

  /** @return the number */
  public int getNumber() {
    return number;
  }

  /** @return the type */
  public CreatureType getType() {
    return type;
  }
}
