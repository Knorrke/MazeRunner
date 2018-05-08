package controller;

import javafx.scene.layout.StackPane;
import model.maze.MazeModelInterface;
import view.MazeView;

public class MazeController {
	private MazeView view;

	public void initModel(MazeModelInterface maze) {
		
	}

	public void setView(MazeView view) {
		this.view = view;
	}

}
