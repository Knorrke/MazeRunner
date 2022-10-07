package org.mazerunner.view.maze;

import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.mazerunner.controller.MazeController;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.view.creatures.CreaturesView;

public class MazeView extends StackPane {
  private static Logger LOG = Logger.getLogger(MazeView.class.getName());

  private WallsView walls;
  private CreaturesView creatures;
  //  private MazeController controller;

  public MazeView() {
    LOG.fine("creating Maze view");
    setWalls(new WallsView());
    setCreatures(new CreaturesView());
    getCreatures().setPickOnBounds(false);
    this.getChildren().addAll(getWalls(), getCreatures());
  }

  /**
   * @return the walls
   */
  public WallsView getWalls() {
    return walls;
  }

  /**
   * @param walls the walls to set
   */
  public void setWalls(WallsView walls) {
    this.walls = walls;
  }

  /**
   * @return the creatures
   */
  public CreaturesView getCreatures() {
    return creatures;
  }

  /**
   * @param creatures the creatures to set
   */
  public void setCreatures(CreaturesView creatures) {
    this.creatures = creatures;
  }

  public void bind(MazeModelInterface maze) {
    maze.errorProperty()
        .addListener((obj, oldError, newError) -> showError(newError.getDescription()));
    walls.bind(maze);
    creatures.bind(maze);
  }

  public void setController(MazeController mazeController) {
    //    this.controller = mazeController;
    walls.setController(mazeController);
  }

  public void showError(String s) {
    Label label = new Label(s);
    label.getStyleClass().add("error");
    label.setWrapText(true);
    label.setTextAlignment(TextAlignment.CENTER);
    label.setTextFill(Color.rgb(200, 0, 0));
    label.setBackground(
        new Background(new BackgroundFill(Color.rgb(200, 230, 90, 0.8), null, null)));
    label.setFont(Font.font("Open Sans", FontWeight.EXTRA_BOLD, 32));
    this.getChildren().add(label);
    FadeTransition transition = new FadeTransition(Duration.millis(300), label);
    transition.setToValue(0);
    transition.setDelay(Duration.millis(1000));
    transition.setOnFinished(event -> this.getChildren().remove(label));
    transition.play();
  }
}
