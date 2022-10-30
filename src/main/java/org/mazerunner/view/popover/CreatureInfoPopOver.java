package org.mazerunner.view.popover;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import jfxtras.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.util.Util;
import org.mazerunner.view.creatures.CreatureView;

public class CreatureInfoPopOver extends PopOver {

  public CreatureInfoPopOver(Creature creature, Node view) {
    setAnimated(false);
    VBox box = new VBox(5);
    box.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    box.getChildren()
        .addAll(
            new Label("Type: " + creature.getType().toString()),
            new Label("Value: " + Util.moneyString(creature.getValue())),
            new Label("Lifes: " + creature.getLifes() + "/" + creature.getStartLifes()),
            new Label("Velocity: " + creature.getVelocity()),
            new Label("Action: " + creature.getAction().getClass().getName()),
            new Label("Movement: " + creature.getMovementStrategy().getClass().getName()));

    this.setContentNode(box);
    this.show(view);
    if (view instanceof CreatureView) {
      ((CreatureView) view).showSelection();
      this.setOnHidden(event -> ((CreatureView) view).hideSelection());
    }
  }
}
