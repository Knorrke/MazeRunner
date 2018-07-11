package org.mazerunner.controller;

import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.TowerType;
import org.mazerunner.view.creatures.CreatureView;
import org.mazerunner.view.maze.MazeView;
import org.mazerunner.view.maze.WallView;
import org.mazerunner.view.popover.CreatureInfoPopOver;
import org.mazerunner.view.popover.WallInfoPopOver;

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
              Wall wall = maze.getWallOn(gameXInt, gameYInt);
              WallView wallView = view.getWalls().getWallView(wall);
              new WallInfoPopOver(wall, wallView);
            } else if (maze.hasCreatureNear(gameX, gameY)) {
              Creature creature = maze.getCreatureNear(gameX, gameY);
              CreatureView creatureView = view.getCreatures().getCreatureView(creature);
              new CreatureInfoPopOver(creature, creatureView);
            }
          }
        });
    view.bind(maze);
  }

  public void sell(Wall wall) {
    if (wall.hasTower()) {
      maze.sellTower(wall);
    } else {
      maze.sell(wall);
    }
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
