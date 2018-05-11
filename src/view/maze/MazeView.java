package view.maze;

import javafx.scene.layout.StackPane;
import model.maze.MazeModelInterface;
import view.creatures.CreaturesView;

public class MazeView extends StackPane {
	private WallsView walls;
	private CreaturesView creatures;

	public MazeView() {
		setWalls(new WallsView());
		setCreatures(new CreaturesView());
//		creatures.widthProperty().bind(getWalls().widthProperty());
//		creatures.heightProperty().bind(getWalls().heightProperty());
		this.getChildren().addAll(getWalls(), getCreatures());
	}

	/**
	 * @return the walls
	 */
	public WallsView getWalls() {
		return walls;
	}

	/**
	 * @param walls the walls to set
	 */
	public void setWalls(WallsView walls) {
		this.walls = walls;
	}

	/**
	 * @return the creatures
	 */
	public CreaturesView getCreatures() {
		return creatures;
	}

	/**
	 * @param creatures the creatures to set
	 */
	public void setCreatures(CreaturesView creatures) {
		this.creatures = creatures;
	}
	
	public void bind(MazeModelInterface maze) {
		walls.bind(maze);
		creatures.bind(maze);
	}
}
