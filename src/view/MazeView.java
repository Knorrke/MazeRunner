package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MazeView extends StackPane {
	private Pane walls;
	private Canvas creatures;

	public MazeView() {
		walls = new WallsView();
		creatures = new Canvas();
		creatures.widthProperty().bind(walls.widthProperty());
		creatures.heightProperty().bind(walls.heightProperty());
		this.getChildren().addAll(walls, creatures);
	}
}
