package application.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import application.controller.gameloop.Updateable;
import application.model.level.LevelModelInterface;
import application.model.maze.MazeModelInterface;
import application.model.player.PlayerModelInterface;
import javafx.beans.property.ObjectProperty;

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

  ObjectProperty<GameState> stateProperty();
}
