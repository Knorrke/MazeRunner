package application.view.creatures;

import java.util.Iterator;
import java.util.List;
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
        while (c.next()) {
          if (c.wasAdded()) {
            createCreatures(c.getAddedSubList());
          } else if (c.wasRemoved()) {
            removeCreatures(c.getRemoved());
          }
        }
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
    createCreatures(this.creatures);
  }

  public void createCreatures(List<? extends Creature> list) {
    ObservableList<Node> children = getChildren();
    children.addAll(
        list.stream()
            .map(creature -> new CreatureView(creature, scaleX, scaleY))
            .collect(Collectors.toList()));
  }

  private void removeCreatures(List<? extends Creature> removed) {
    ObservableList<Node> children = getChildren();
    for (Iterator<Node> iterator = children.iterator(); iterator.hasNext(); ) {
      Node node = iterator.next();
      if (node instanceof CreatureView && removed.contains(((CreatureView) node).getCreature())) {
        iterator.remove();
      }
    }
  }
}
