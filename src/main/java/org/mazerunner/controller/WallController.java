package org.mazerunner.controller;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.input.MouseEvent;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.TowerType;
import org.mazerunner.view.maze.WallMenuView;
import org.mazerunner.view.maze.WallView;

public class WallController {

  private MazeController mazeController;
  private Wall wall;
  private WallView view;
  private WallMenuView menu;

  public WallController(MazeController mazeController) {
    this.mazeController = mazeController;
    view = new WallView(this);
  }

  public void init(Wall wall, DoubleBinding scaleX, DoubleBinding scaleY) {
    this.wall = wall;
    view.bind(wall, scaleX, scaleY);
    menu = new WallMenuView(view, scaleX, scaleY);
    menu.shownProperty()
        .addListener(
            (obj, oldVal, newval) -> {
              if (newval) {
                view.showSelection();
              } else {
                view.hideSelection();
              }
            });
  }

  public void sell() {
    mazeController.sell(wall);
  }

  public void buildTower(TowerType type) {
    mazeController.buildTower(wall, type);
  }

  public void upgradeTower() {
    mazeController.upgradeTower(wall);
  }

  public WallView getView() {
    return view;
  }

  public WallMenuView getMenu() {
    return menu;
  }

  public void showMenu(MouseEvent event) {
    if (!menu.isShown()) menu.show(event, wall, this);
    else menu.hide();
  }
}
