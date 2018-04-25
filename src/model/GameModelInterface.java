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
}
