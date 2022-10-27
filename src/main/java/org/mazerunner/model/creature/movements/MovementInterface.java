package org.mazerunner.model.creature.movements;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.vision.Vision;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = NoSightMovement.class, name = "NoSight"),
  @JsonSubTypes.Type(value = SightMovement.class, name = "Sight"),
  @JsonSubTypes.Type(value = PerfectMovement.class, name = "Perfect"),
  @JsonSubTypes.Type(value = PerfectMovement.class, name = "Random"),
})
public interface MovementInterface {
  public double[] getNextGoal(Vision vision, VisitedMap visited, double currentX, double currentY);
}
