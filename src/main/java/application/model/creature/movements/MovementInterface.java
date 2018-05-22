package application.model.creature.movements;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import application.model.creature.VisitedMap;
import application.model.creature.vision.Vision;
import application.model.maze.MazeModelInterface;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = NoSightMovement.class, name = "NoSight"),
})
public interface MovementInterface {
  public double[] getMoveDirection(
      MazeModelInterface maze, Vision vision, VisitedMap visited, double currentX, double currentY);
}
