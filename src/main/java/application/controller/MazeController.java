package application.controller;

import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.model.maze.MazeModelInterface;
import application.view.maze.MazeView;
import javafx.scene.input.KeyCode;

public class MazeController implements ModelHolder<MazeModelInterface>{
	private MazeView view;
	private MazeModelInterface maze;

	@Override
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
