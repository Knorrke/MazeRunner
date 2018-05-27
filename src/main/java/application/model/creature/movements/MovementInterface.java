package application.model.creature.movements;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import application.model.creature.VisitedMap;
import application.model.creature.vision.Vision;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = NoSightMovement.class, name = "NoSight"),
})
public interface MovementInterface {
  public double[] getNextGoal(Vision vision, VisitedMap visited, double currentX, double currentY);
}
