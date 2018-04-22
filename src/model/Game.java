package model;

import model.player.Player;
import model.player.PlayerModelInterface;

public class Game implements GameModelInterface {
	private final PlayerModelInterface player = new Player(50,20);
	private final LevelModelInterface level = new Level();

	@Override
	public PlayerModelInterface getPlayer() {
		return player;
	}

	@Override
	public LevelModelInterface getLevel() {
		return level;
	}
}
