package model;

import static model.GameState.*;

import model.creature.CreatureGroup;
import model.creature.CreatureType;
import model.level.Level;
import model.level.LevelModelInterface;
import model.maze.Maze;
import model.maze.MazeModelInterface;
import model.maze.Wall;
import model.player.Player;
import model.player.PlayerModelInterface;

public class Game implements GameModelInterface {
	private GameState state;
	private final PlayerModelInterface player;
	private final LevelModelInterface level;
	private final MazeModelInterface maze;

	public Game() {
		player = new Player(50,20);
		
		level = new Level();
		for (int i = 0; i < 9; i++) {
			level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 20));
		}
		
		maze = new Maze();
		state = BUILDING;
	}
	
	@Override
	public PlayerModelInterface getPlayer() {
		return player;
	}

	@Override
	public LevelModelInterface getLevel() {
		return level;
	}
	
	@Override
	public MazeModelInterface getMaze() {
		return maze;
	}

	public GameState getState() {
		return state;
	}

	@Override
	public void buildWall(int x, int y) {
		maze.addWall(new Wall(x, y));
	}
}
