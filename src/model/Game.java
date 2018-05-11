package model;

import static model.GameState.BUILDING;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.creature.CreatureGroup;
import model.creature.CreatureType;
import model.gameloop.GameLoop;
import model.level.Level;
import model.level.LevelModelInterface;
import model.maze.Maze;
import model.maze.MazeModelInterface;
import model.player.Player;
import model.player.PlayerModelInterface;

public class Game implements GameModelInterface {
	private ObjectProperty<GameState> state = new SimpleObjectProperty<GameState>();
	private final PlayerModelInterface player;
	private final LevelModelInterface level;
	private final MazeModelInterface maze;
	private final GameLoop gameloop;

	public Game() {
		player = new Player(50,20);
		
		level = new Level();
		for (int i = 0; i < 9; i++) {
			level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 20));
		}
		
		maze = new Maze();
		setState(BUILDING);
		gameloop = new GameLoop(this);
	}
		
	public void update(double dt) {
		maze.update(dt);
		level.update(dt);
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
		return state.get();
	}
	
	@Override
	public ObjectProperty<GameState> stateProperty() {
		return state;
	}
	
	/**
	 * @param state the state to set
	 */
	public void setState(GameState state) {
		this.state.set(state);
	}

	@Override
	public void start() {
		setState(GameState.RUNNING);
		gameloop.start();
	}
	
	@Override
	public void pause() {
		setState(GameState.PAUSED);
		gameloop.stop();
	}
}
