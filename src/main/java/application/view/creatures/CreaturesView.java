package application.view.creatures;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import application.model.creature.Creature;
import application.model.maze.MazeModelInterface;
import application.util.Util;
import application.view.Bindable;
import application.view.FloatingLabel;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class CreaturesView extends Pane implements Bindable<MazeModelInterface> {
  private static final Object monitor = new Object();

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
    synchronized (monitor) {
      for (Iterator<Node> iterator = children.iterator(); iterator.hasNext(); ) {
        Node node = iterator.next();
        if (node instanceof CreatureView && removed.contains(((CreatureView) node).getCreature())) {
          iterator.remove();
        }
      }
    }
    for (Creature creature : removed) {
      if (creature.getLifes() <= 0) {
        Label label = new FloatingLabel(Util.moneyString(creature.getValue())).getView();
        label.setLayoutX(creature.getX() * scaleX.doubleValue());
        label.setLayoutY(creature.getY() * scaleY.doubleValue());
        label.getStyleClass().add("earning-label");
        label.setMouseTransparent(true);        
        
        FadeTransition fade = new FadeTransition(new Duration(200), label);
        fade.setToValue(0.2);
        fade.setDelay(new Duration(600));
        TranslateTransition translate = new TranslateTransition(new Duration(800), label);
        translate.setToY(-1*scaleY.doubleValue());
        ParallelTransition transition = new ParallelTransition(fade, translate);
        transition.setOnFinished(
            event -> {
              synchronized (monitor) {
                children.remove(label);
              }
            });
        transition.playFromStart();

        synchronized (monitor) {
          children.add(label);
        }
      }
    }
  }
}
