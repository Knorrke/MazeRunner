package application.view.creatures;

import application.ImageLoader;
import application.model.creature.Creature;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class CreatureView extends StackPane {
  public CreatureView(Creature creature, DoubleBinding scaleX, DoubleBinding scaleY) {
    this.getStyleClass().add("creature");

    Image im = ImageLoader.getCreatureImage(creature.getType());
    ImageView img = new ImageView(im);
    img.fitWidthProperty().bind(scaleX);
    img.fitHeightProperty().bind(scaleY);

    this.getChildren().add(img);
    this.layoutXProperty().bind(creature.xProperty().multiply(scaleX));
    this.layoutYProperty().bind(creature.yProperty().multiply(scaleY));
  }
}
