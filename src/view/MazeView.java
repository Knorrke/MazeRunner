package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MazeView extends StackPane {
	private Pane walls;
	private Canvas creatures;

	public MazeView() {
		setWalls(new WallsView());
		creatures = new Canvas();
		creatures.widthProperty().bind(getWalls().widthProperty());
		creatures.heightProperty().bind(getWalls().heightProperty());
		this.getChildren().addAll(getWalls(), creatures);
	}

	/**
	 * @return the walls
	 */
	public Pane getWalls() {
		return walls;
	}

	/**
	 * @param walls the walls to set
	 */
	public void setWalls(Pane walls) {
		this.walls = walls;
	}
}
