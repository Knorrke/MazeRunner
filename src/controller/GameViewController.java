package controller;

import application.ImageLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.GameModelInterface;
import model.LevelModelInterface;
import model.creature.CreatureGroup;
import model.player.PlayerModelInterface;
import view.CreatureTimelineView;
import view.MazeView;

public class GameViewController {
	
	private BooleanProperty paused = new SimpleBooleanProperty(false);
	private GameModelInterface game;
	private PlayerModelInterface player;
	private LevelModelInterface level;
	
	@FXML CreatureTimelineView creatureTimeline;
	@FXML StackPane maze;
	@FXML ImageView pauseButton;

	@FXML Label money;
	@FXML Label lifes;

	public void initialize() {
		pauseButton.imageProperty().bind(Bindings.when(paused).then(ImageLoader.play).otherwise(ImageLoader.pause));
		maze.getChildren().add(new MazeView());
	}

	@FXML
	public void togglePause(MouseEvent event) {
		paused.set(!paused.get());
	}

	public void initGameModel(GameModelInterface game) {
		this.game = game;
		initPlayer(game.getPlayer());
		initLevel(game.getLevel());
	}
	
	private void initPlayer(PlayerModelInterface player) {
		this.player = player;
		money.textProperty().bind(player.moneyProperty().asString());
		lifes.textProperty().bind(player.lifesProperty().asString());
	}

	private void initLevel(LevelModelInterface level) {
		this.level = level;
		creatureTimeline.createFromData(level.getCreatureTimeline());
		level.getCreatureTimeline().addListener((ListChangeListener<CreatureGroup>) c -> {
			creatureTimeline.createFromData(level.getCreatureTimeline());
		});
	}
}
