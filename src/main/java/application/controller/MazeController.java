package application.controller;

import application.model.maze.MazeModelInterface;
import application.model.maze.Wall;
import application.model.maze.tower.TowerType;
import application.view.maze.MazeView;
import application.view.popover.CreatureInfoPopOver;
import application.view.popover.WallInfoPopOver;

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
          double gameX = maze.getMaxWallX() * event.getX() / view.getWidth();
          double gameY = maze.getMaxWallY() * event.getY() / view.getHeight();
          int gameXInt = (int) gameX;
          int gameYInt = (int) gameY;
          if (gameController.getUserActionState().equals(UserActionState.BUILD)) {
            if (maze.hasWallOn(gameXInt, gameYInt)) {
              view.getWalls().showMenu(event, maze.getWallOn(gameXInt, gameYInt));
            } else {
              maze.buildWall(gameXInt, gameYInt);
            }
          } else if (gameController.getUserActionState().equals(UserActionState.INFO)) {
            if (maze.hasWallOn(gameXInt, gameYInt)) {
              new WallInfoPopOver(maze.getWallOn(gameXInt, gameYInt), view.getWalls());
            } else if (maze.hasCreatureNear(gameX, gameY)) {
              new CreatureInfoPopOver(maze.getCreatureNear(gameX, gameY), view.getCreatures());
            }
          }
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
