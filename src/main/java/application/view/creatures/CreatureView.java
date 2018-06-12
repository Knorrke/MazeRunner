package application.view.creatures;

import application.ImageLoader;
import application.model.creature.Creature;
import application.util.Calc;
import javafx.animation.RotateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class CreatureView extends StackPane {
  private double imageSize = 0.7;
  private final Creature creature;

  public CreatureView(Creature creature, DoubleBinding scaleX, DoubleBinding scaleY) {
    this.creature = creature;
    this.getStyleClass().add("creature");

    Image im = ImageLoader.getCreatureImage(creature.getType());
    ImageView img = new ImageView(im);
    img.setPreserveRatio(true);
    img.fitWidthProperty().bind(scaleX.multiply(imageSize));
    img.fitHeightProperty().bind(scaleY.multiply(imageSize));

    this.getChildren().add(img);
    this.layoutXProperty().bind(creature.xProperty().subtract(0.5 * imageSize).multiply(scaleX));
    this.layoutYProperty().bind(creature.yProperty().subtract(0.5 * imageSize).multiply(scaleY));
    creature
        .positionProperty()
        .addListener(
            (obj, oldPos, newPos) -> {
              RotateTransition rotate = new RotateTransition(new Duration(50), this);
              rotate.setToAngle(Calc.calculateRotation(oldPos, newPos));
              rotate.play();
            });
  }  
  
  public Creature getCreature() {
    return creature;
  }
}
