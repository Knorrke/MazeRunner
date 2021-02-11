package org.mazerunner.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import javafx.beans.property.ReadOnlyObjectProperty;
import org.mazerunner.controller.gameloop.Updateable;
import org.mazerunner.model.level.LevelModelInterface;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.model.player.PlayerModelInterface;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Game.class, name = "Game"),
})
public interface GameModelInterface extends Updateable, ModelInterface {
  /** @return the player */
  public PlayerModelInterface getPlayer();

  /** @return the level */
  public LevelModelInterface getLevel();

  /** @return the maze */
  public MazeModelInterface getMaze();

  /** @return the state */
  public GameState getState();

  ReadOnlyObjectProperty<GameState> stateProperty();
}
