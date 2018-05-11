package view.maze;

import java.util.stream.Collectors;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.maze.MazeModelInterface;
import model.maze.Wall;
import view.Bindable;

public class WallsView extends Pane implements Bindable<MazeModelInterface>{
	ObservableList<Wall> walls;
	DoubleBinding scaleX, scaleY;
	ListChangeListener<Wall> listener = (c) -> {
		createWalls();
	};
	
	@Override
	public void bind(MazeModelInterface maze) {
		if (walls != null) {
			walls.removeListener(listener);
		}
		this.walls = maze.getWalls();
		this.scaleX = widthProperty().divide(maze.getMaxWallX());
		this.scaleY = heightProperty().divide(maze.getMaxWallY());
		walls.addListener(listener);
		createWalls();
	}
	
	public void createWalls() {
		ObservableList<Node> children = getChildren();
		children.clear();
		children.addAll(walls.stream().map(wall -> new WallView(wall,scaleX, scaleY)).collect(Collectors.toList()));
	}
}
