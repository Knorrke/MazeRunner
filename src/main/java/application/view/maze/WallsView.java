package application.view.maze;

import java.util.stream.Collectors;
import application.controller.MazeController;
import application.controller.WallController;
import application.model.maze.MazeModelInterface;
import application.model.maze.Wall;
import application.view.Bindable;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class WallsView extends Pane implements Bindable<MazeModelInterface> {
  private ObservableList<Wall> walls;
  private DoubleBinding scaleX, scaleY;
  private ListChangeListener<Wall> listener =
      (c) -> {
        createWalls();
      };

  private MazeController controller;

  @Override
  public void bind(MazeModelInterface maze) {
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
                  WallController wallController = new WallController(controller);
                  wallController.init(wall, scaleX, scaleY);
                  return wallController.getView();
                })
            .collect(Collectors.toList()));
  }

  public void setController(MazeController mazeController) {
    this.controller = mazeController;
  }

  public WallView getWallView(Wall wall) {
    for (Node node : this.getChildren()) {
      if (node instanceof WallView && ((WallView) node).belongsToWall(wall)) {
        return (WallView) node;
      }
    }
    return null;
  }

  public void showMenu(MouseEvent event, Wall wall) {
    WallView view = getWallView(wall);
    if (view != null) {
      view.getController().showMenu(event);
    }
  }
}
