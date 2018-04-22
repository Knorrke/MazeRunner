package model;

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
}
