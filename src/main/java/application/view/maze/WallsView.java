package application.view.maze;

import java.util.stream.Collectors;

import application.controller.MazeController;
import application.model.maze.MazeModelInterface;
import application.model.maze.Wall;
import application.view.Bindable;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class WallsView extends Pane implements Bindable<MazeModelInterface> {
  private ObservableList<Wall> walls;
  private DoubleBinding scaleX, scaleY;
  private ListChangeListener<Wall> listener =
      (c) -> {
        createWalls();
      };

  private WallMenuView wallMenu = new WallMenuView(this);
  private MazeController controller;

  @Override
  public void bind(MazeModelInterface maze) {
    if (walls != null) {
      walls.removeListener(listener);
    }
    this.walls = maze.getWalls();
    this.scaleX = widthProperty().divide(maze.getMaxWallX());
    this.scaleY = heightProperty().divide(maze.getMaxWallY());
    walls.addListener(listener);
    createWalls();
  }

  public void createWalls() {
    ObservableList<Node> children = getChildren();
    children.clear();
    children.addAll(
        walls
            .stream()
            .map(
                wall -> {
                  WallView view = new WallView(wall, scaleX, scaleY);
                  view.setMenu(wallMenu, controller);
                  return view;
                })
            .collect(Collectors.toList()));
  }

  public void setController(MazeController mazeController) {
    this.controller = mazeController;
  }
}
