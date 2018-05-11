package controller;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.VBox;
import model.creature.CreatureGroup;
import model.level.LevelModelInterface;
import view.CreatureTimelineImage;

public class LevelController implements ModelHolder<LevelModelInterface> {
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
	 * @return the view
	 */
	public VBox getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(VBox view) {
		this.view = view;
	}
}
