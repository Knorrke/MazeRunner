package controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import application.ImageLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.GameModelInterface;
import model.creature.CreatureGroup;
import model.creature.CreatureType;
import model.level.LevelModelInterface;

public class GameController {
	private static Logger LOG = Logger.getLogger(GameController.class.getName());
	
	/**
	 * Model classes
	 */
	private BooleanProperty paused = new SimpleBooleanProperty(false);
	private GameModelInterface game;
	private LevelModelInterface level;

	/**
	 * Connected controllers
	 */
	@FXML PlayerController player;
	/**
	 * View classes
	 */
	private Parent view;
	@FXML VBox creatureTimelineView;
	@FXML StackPane maze;
	@FXML ImageView pauseButton;
	
	public GameController() {
		FXMLLoader gameViewLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/GameView.fxml"));
		gameViewLoader.setController(this);
        try {
        	LOG.fine("loading GameView from fxml");
            view = gameViewLoader.load();
        } catch (IOException exception) {
        	LOG.log(Level.SEVERE, "Loading GameView.fxml failed", exception);
            throw new RuntimeException(exception);
        }
	}
	
	public void initialize() {
		LOG.fine("initializing GameController");
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
		LOG.finer("initializing GameModel");
		this.game = game;
		player.initModel(game.getPlayer());
		initLevel(game.getLevel());
	}
		
	private void initLevel(LevelModelInterface level) {
		LOG.finer("initializing LevelModel");
		this.level = level;
		createCreatureTimeline();
		level.getCreatureTimeline().addListener((ListChangeListener<CreatureGroup>) c -> {
			this.createCreatureTimeline();
		});
	}
	
	public void createCreatureTimeline() {
		List<CreatureGroup> timeline = level.getCreatureTimeline();
		creatureTimelineView.getChildren().clear();
		creatureTimelineView.getChildren().addAll(
			timeline.stream().map(this::createCreatureTimelineImageView).collect(Collectors.toList())
		);
	}
	
	public Node createCreatureTimelineImageView(CreatureGroup group) {
		StackPane parent = new StackPane();
		Image im = createCreatureTimelineImage(group.getType());
		ImageView img = new ImageView(im);
		img.setPreserveRatio(true);
		img.setFitWidth(40);
		
		Label label = new Label(Integer.toString(group.getNumber()));
		
		parent.getChildren().addAll(img, label);
		return parent;
	}

	private Image createCreatureTimelineImage(CreatureType type) {
		switch (type) {
			case NORMAL:
			default: return ImageLoader.normalCreature;
		}
	}

}
