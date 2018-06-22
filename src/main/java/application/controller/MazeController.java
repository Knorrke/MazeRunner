package application.controller;

import application.model.maze.MazeModelInterface;
import application.model.maze.Wall;
import application.model.maze.tower.TowerType;
import application.view.maze.MazeView;

public class MazeController implements ModelHolderInterface<MazeModelInterface> {
  private MazeView view;
  private MazeModelInterface maze;
  private GameController gameController;

  public MazeController(GameController gameController) {
    this.gameController = gameController;
  }

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
    view.setController(this);
  }

  private void setListeners() {
    view.setOnMouseClicked(
        event -> {
          int gameX = (int) (maze.getMaxWallX() * event.getX() / view.getWidth());
          int gameY = (int) (maze.getMaxWallY() * event.getY() / view.getHeight());
          maze.buildWall(gameX, gameY);
        });
    view.bind(maze);
  }

  public void sell(Wall wall) {
    maze.sell(wall);
  }

  public void buildTower(Wall wall, TowerType type) {
    maze.buildTower(wall, type);
  }

  public void upgradeTower(Wall wall) {
    maze.upgradeTower(wall);
  }

  public MazeView getView() {
    return view;
  }
}
