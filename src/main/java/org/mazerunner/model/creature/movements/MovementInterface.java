package org.mazerunner.model.creature.movements;

import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.vision.Vision;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = NoSightMovement.class, name = "NoSight"),
})
public interface MovementInterface {
  public double[] getNextGoal(Vision vision, VisitedMap visited, double currentX, double currentY);
}
