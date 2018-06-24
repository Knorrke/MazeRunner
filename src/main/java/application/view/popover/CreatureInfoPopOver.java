package application.view.popover;

import org.controlsfx.control.PopOver;
import application.model.creature.Creature;
import application.util.Util;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import jfxtras.scene.layout.VBox;

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
            new Label("Velocity: " + creature.getVelocity()));

    this.setContentNode(box);
    this.show(view);
  }
}
