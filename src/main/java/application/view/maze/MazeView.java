package application.view.maze;

import java.util.logging.Logger;

import application.controller.MazeController;
import application.model.maze.MazeModelInterface;
import application.view.creatures.CreaturesView;
import javafx.scene.layout.StackPane;

public class MazeView extends StackPane {
  private static Logger LOG = Logger.getLogger(MazeView.class.getName());
  
  private WallsView walls;
  private CreaturesView creatures;
  private MazeController controller;

  public MazeView() {
    LOG.fine("creating Maze view");
    setWalls(new WallsView());
    setCreatures(new CreaturesView());
    getCreatures().setPickOnBounds(false);
    //		creatures.widthProperty().bind(getWalls().widthProperty());
    //		creatures.heightProperty().bind(getWalls().heightProperty());
    this.getChildren().addAll(getWalls(), getCreatures());
  }

  /** @return the walls */
  public WallsView getWalls() {
    return walls;
  }

  /** @param walls the walls to set */
  public void setWalls(WallsView walls) {
    this.walls = walls;
  }

  /** @return the creatures */
  public CreaturesView getCreatures() {
    return creatures;
  }

  /** @param creatures the creatures to set */
  public void setCreatures(CreaturesView creatures) {
    this.creatures = creatures;
  }

  public void bind(MazeModelInterface maze) {
    walls.bind(maze);
    creatures.bind(maze);
  }

  public void setController(MazeController mazeController) {
    this.controller = mazeController;
    walls.setController(mazeController);
  }
}
