package application.controller;

import application.model.maze.Wall;
import application.model.maze.tower.TowerType;
import application.view.maze.WallMenuView;
import application.view.maze.WallView;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.input.MouseEvent;

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
    view.addEventHandler(
        MouseEvent.MOUSE_CLICKED,
        event -> {
          if (!menu.isShown()) menu.show(event, wall, this);
        });
    menu.shownProperty()
        .addListener(
            (obj, oldVal, newval) -> {
              if (newval) {
                view.showSelection();
                System.out.println("show");
              } else {
                view.hideSelection();
                System.out.println("hide");
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
}
