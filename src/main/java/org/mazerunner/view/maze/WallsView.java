package org.mazerunner.view.maze;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.mazerunner.controller.MazeController;
import org.mazerunner.controller.WallController;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.view.Bindable;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class WallsView extends Pane implements Bindable<MazeModelInterface> {
  private DoubleBinding scaleX, scaleY;
  ListChangeListener<Wall> listener =
      (c) -> {
        while (c.next()) {
          if (c.wasAdded()) {
            createWalls(c.getAddedSubList());
          } else if (c.wasRemoved()) {
            removeWalls(c.getRemoved());
          }
        }
      };

  private MazeController controller;

  @Override
  public void bind(MazeModelInterface maze) {
    ObservableList<Wall> walls = maze.getWalls();
    this.scaleX = widthProperty().divide(maze.getMaxWallX());
    this.scaleY = heightProperty().divide(maze.getMaxWallY());
    walls.addListener(listener);
    createWalls(walls);
  }

  public void createWalls(List<? extends Wall> walls) {
    ObservableList<Node> children = getChildren();
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

  private void removeWalls(List<? extends Wall> removed) {
    ObservableList<Node> children = getChildren();
    for (Iterator<Node> iterator = children.iterator(); iterator.hasNext(); ) {
      Node node = iterator.next();
      if (node instanceof WallView && removed.contains(((WallView) node).getWall())) {
        iterator.remove();
      }
    }
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
