package org.mazerunner.view.level;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureFactory;
import org.mazerunner.model.creature.CreatureGroup;
import org.mazerunner.util.ImageLoader;
import org.mazerunner.view.FloatingLabel;
import org.mazerunner.view.popover.CreatureInfoPopOver;

public class CreatureTimelineImage extends StackPane {
  public CreatureTimelineImage(CreatureGroup group) {
    this.getStyleClass().add("wave");
    this.setMaxWidth(40);

    Image im = ImageLoader.getCreatureImage(group.getType());
    ImageView img = new ImageView(im);
    img.setPreserveRatio(true);
    img.setFitWidth(40);

    Label label = new FloatingLabel(Integer.toString(group.getNumber()));
    this.getChildren().addAll(img, label);

    setOnMouseClicked(
        event -> {
          Creature creature = CreatureFactory.createExampleOfGroup(group);
          new CreatureInfoPopOver(creature, this);
        });
  }
}
