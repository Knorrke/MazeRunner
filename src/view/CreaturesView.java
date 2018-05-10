package view;

import java.util.stream.Collectors;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.creature.Creature;
import model.maze.MazeModelInterface;

public class CreaturesView extends Pane {
	
	ObservableList<Creature> creatures;
	DoubleBinding scaleX, scaleY;
	ListChangeListener<Creature> listener = (c) -> {
		createCreatures();
	};
	
	public void bind(MazeModelInterface maze) {
		if (creatures != null) {
			creatures.removeListener(listener);
		}
		this.creatures = maze.getCreatures();
		this.scaleX = widthProperty().divide(maze.getMaxWallX());
		this.scaleY = heightProperty().divide(maze.getMaxWallY());
		creatures.addListener(listener);
		createCreatures();
	}
	
	public void createCreatures() {
		ObservableList<Node> children = getChildren();
		children.clear();
		children.addAll(creatures.stream().map(creature -> new CreatureView(creature,scaleX, scaleY)).collect(Collectors.toList()));
	}
}
