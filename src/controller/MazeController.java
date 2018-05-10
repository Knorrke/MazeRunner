package controller;

import javafx.scene.input.KeyCode;
import model.creature.CreatureFactory;
import model.creature.CreatureType;
import model.maze.MazeModelInterface;
import view.MazeView;

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
		
		view.setOnKeyTyped(event -> {
			if (event.getCode() == KeyCode.SPACE) {
				maze.addCreature(CreatureFactory.create(maze, CreatureType.NORMAL));
			}
		});
		
		view.bind(maze);
	}

}
