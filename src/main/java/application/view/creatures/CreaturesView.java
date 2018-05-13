package application.view.creatures;

import java.util.stream.Collectors;

import application.model.creature.Creature;
import application.model.maze.MazeModelInterface;
import application.view.Bindable;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class CreaturesView extends Pane implements Bindable<MazeModelInterface> {

  ObservableList<Creature> creatures;
  DoubleBinding scaleX, scaleY;
  ListChangeListener<Creature> listener =
      (c) -> {
        createCreatures();
      };

  @Override
  public void bind(MazeModelInterface maze) {
    if (creatures != null) {
      creatures.removeListener(listener);
    }
    this.creatures = maze.getCreatures();
    this.scaleX = widthProperty().divide(maze.getMaxWallX());
    this.scaleY = heightProperty().divide(maze.getMaxWallY());
    creatures.addListener(listener);
    createCreatures();
  }

  public void createCreatures() {
    ObservableList<Node> children = getChildren();
    children.clear();
    children.addAll(
        creatures
            .stream()
            .map(creature -> new CreatureView(creature, scaleX, scaleY))
            .collect(Collectors.toList()));
  }
}
