package controller;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import model.maze.MazeModelInterface;
import model.maze.Wall;
import view.MazeView;
import view.WallView;

public class MazeController {
	private MazeView view;
	private MazeModelInterface maze;

	public void initModel(MazeModelInterface maze) {
		this.maze = maze;
		if (view != null) {
			setListeners();
		}
	}

	public void setView(MazeView view) {
		this.view = view;
		if (maze != null) {
			setListeners();
		}
	}

	private void setListeners() {
		view.setOnMouseClicked(event -> {
			maze.buildWall((int) (maze.getMaxWallX() * event.getX() / view.getWidth()),
					(int) (maze.getMaxWallY() * event.getY() / view.getHeight()));
		});

		maze.getWalls().addListener((ListChangeListener<Wall>) c -> {
			this.createCreatureTimeline();
		});
	}

	public void createCreatureTimeline() {
		List<Wall> walls = maze.getWalls();
		ObservableList<Node> children = view.getWalls().getChildren();
		children.clear();
		children.addAll(walls.stream().map(new Function<Wall, WallView>() {

			@Override
			public WallView apply(Wall wall) {
				DoubleBinding scaleX = view.widthProperty().divide(maze.getMaxWallX());
				DoubleBinding scaleY = view.heightProperty().divide(maze.getMaxWallY());
				return new WallView(wall,scaleX, scaleY);
			}
		}).collect(Collectors.toList()));
	}

}
