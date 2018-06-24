package application.view.level;

import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureGroup;
import application.util.ImageLoader;
import application.view.FloatingLabel;
import application.view.popover.CreatureInfoPopOver;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

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
