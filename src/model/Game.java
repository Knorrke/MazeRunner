package model;

import model.creature.CreatureGroup;
import model.creature.CreatureType;
import model.player.Player;
import model.player.PlayerModelInterface;

public class Game implements GameModelInterface {
	private final PlayerModelInterface player = new Player(50,20);
	private final LevelModelInterface level = new Level();

	public Game() {
		for (int i = 0; i < 20; i++) {
			level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 20));
		}
	}
	
	@Override
	public PlayerModelInterface getPlayer() {
		return player;
	}

	@Override
	public LevelModelInterface getLevel() {
		return level;
	}
}
