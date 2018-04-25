package controller;

import java.io.IOException;

import application.ImageLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.GameModelInterface;
import model.creature.CreatureGroup;
import model.level.LevelModelInterface;
import view.CreatureTimelineView;

public class GameController {
	private BooleanProperty paused = new SimpleBooleanProperty(false);
	private GameModelInterface game;
	private LevelModelInterface level;
	private Parent view;
	
	@FXML PlayerController player;
	@FXML CreatureTimelineView creatureTimeline;
	@FXML StackPane maze;
	@FXML ImageView pauseButton;
	
	public GameController() {
		FXMLLoader gameViewLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/GameView.fxml"));
		gameViewLoader.setController(this);
        try {
        	System.out.println("loading gameController view");
            view = gameViewLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	public void initialize() {
		System.out.println("initializing");
		pauseButton.imageProperty().bind(Bindings.when(paused).then(ImageLoader.play).otherwise(ImageLoader.pause));
		//maze.getChildren().add(new MazeView());
	}

	@FXML
	public void togglePause(MouseEvent event) {
		paused.set(!paused.get());
	}

	public Parent getView() {
		return view;
	}
	
	public void initModel(GameModelInterface game) {
		this.game = game;
		player.initModel(game.getPlayer());
		initLevel(game.getLevel());
	}
		
	private void initLevel(LevelModelInterface level) {
		this.level = level;
		creatureTimeline.createFromData(level.getCreatureTimeline());
		level.getCreatureTimeline().addListener((ListChangeListener<CreatureGroup>) c -> {
			creatureTimeline.createFromData(level.getCreatureTimeline());
		});
	}

}
