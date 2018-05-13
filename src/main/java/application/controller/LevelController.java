package application.controller;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import application.model.creature.CreatureGroup;
import application.model.level.LevelModelInterface;
import application.view.CreatureTimelineImage;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.VBox;

public class LevelController implements ModelHolderInterface<LevelModelInterface> {
	private static Logger LOG = Logger.getLogger(LevelController.class.getName());
	private LevelModelInterface level;
	private VBox view;
	
	@Override
	public void initModel(LevelModelInterface level) {
		LOG.finer("initializing LevelModel");
		this.level = level;
		createCreatureTimeline();
		level.getCreatureTimeline().addListener((ListChangeListener<CreatureGroup>) c -> {
			this.createCreatureTimeline();
		});
	}
	
	public void createCreatureTimeline() {
		List<CreatureGroup> timeline = level.getCreatureTimeline();
		view.getChildren().clear();
		view.getChildren().addAll(
			timeline.stream().map(CreatureTimelineImage::new).collect(Collectors.toList())
		);
	}

	/**
	 * @return the level
	 */
	public LevelModelInterface getLevel() {
		return level;
	}
	
	/**
	 * @return the application.view
	 */
	public VBox getView() {
		return view;
	}

	/**
	 * @param application.view the application.view to set
	 */
	public void setView(VBox view) {
		this.view = view;
	}
}
