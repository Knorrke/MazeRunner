package model;

import model.level.LevelModelInterface;
import model.maze.MazeModelInterface;
import model.player.PlayerModelInterface;

public interface GameModelInterface {
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
	 * Builds a new wall. If the wall already exists, this is a NoOp
	 * @param x
	 * @param y
	 */
	public void buildWall(int x, int y);
}
