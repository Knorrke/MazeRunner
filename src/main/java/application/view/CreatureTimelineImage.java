package application.view;

import application.ImageLoader;
import application.model.creature.CreatureGroup;
import application.model.creature.CreatureType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class CreatureTimelineImage extends StackPane {
  public CreatureTimelineImage(CreatureGroup group) {
    Image im = createCreatureTimelineImage(group.getType());
    ImageView img = new ImageView(im);
    img.setPreserveRatio(true);
    img.setFitWidth(40);

    Label label = new Label(Integer.toString(group.getNumber()));

    this.getChildren().addAll(img, label);
  }

  private Image createCreatureTimelineImage(CreatureType type) {
    switch (type) {
      case NORMAL:
      default:
        return ImageLoader.normalCreature;
    }
  }
}
