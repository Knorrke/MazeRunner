package model;

import javafx.beans.property.ObjectProperty;
import model.gameloop.Updateable;
import model.level.LevelModelInterface;
import model.maze.MazeModelInterface;
import model.player.PlayerModelInterface;

public interface GameModelInterface extends Updateable, ModelInterface {
	/**
	 * @return the player
	 */
	public PlayerModelInterface getPlayer();
	
	/**
	 * @return the level
	 */
	public LevelModelInterface getLevel();

	/**
	 * @return the maze
	 */
	public MazeModelInterface getMaze();
	
	/**
	 * @return the state
	 */
	public GameState getState();

	/**
	 * start the game
	 */
	public void start();

	/**
	 * pause the game
	 */
	void pause();

	ObjectProperty<GameState> stateProperty();
	
}
