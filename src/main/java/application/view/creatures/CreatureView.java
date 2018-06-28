package application.view.creatures;

import application.model.Position;
import application.model.creature.Creature;
import application.model.creature.CreatureType;
import application.util.ImageLoader;
import application.util.Util;
import javafx.animation.RotateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class CreatureView extends StackPane {
  private double imageSize = 0.7;
  private final Creature creature;

  public CreatureView(Creature creature, DoubleBinding scaleX, DoubleBinding scaleY) {
    this.creature = creature;
    this.getStyleClass().add("creature");

    ImageView img = createImageView(creature.getType(), scaleX, scaleY);
    ImageView clip = createImageView(creature.getType(), scaleX, scaleY);

    double startVelocity = creature.getVelocity();
    Rectangle slowdownEffect = new Rectangle();
    slowdownEffect.widthProperty().bind(scaleX.multiply(imageSize));
    slowdownEffect.heightProperty().bind(scaleY.multiply(imageSize));
    slowdownEffect.setClip(clip);
    slowdownEffect.setFill(Color.rgb(25, 25, 100));
    slowdownEffect
        .opacityProperty()
        .bind(Bindings.subtract(1, creature.velocityProperty().divide(startVelocity)));

    this.getChildren().add(img);
    this.getChildren().add(slowdownEffect);
    this.layoutXProperty().bind(creature.xProperty().subtract(0.5 * imageSize).multiply(scaleX));
    this.layoutYProperty().bind(creature.yProperty().subtract(0.5 * imageSize).multiply(scaleY));
    creature
        .positionProperty()
        .addListener(
            new ChangeListener<Position>() {
              private double lastChange;
              private double signumX, signumY;

              @Override
              public void changed(
                  ObservableValue<? extends Position> obj, Position oldPos, Position newPos) {
                double dx = oldPos.getX() - newPos.getX();
                double newSignumX = Math.signum(dx);
                double dy = oldPos.getY() - newPos.getY();
                double newSignumY = Math.signum(dy);
                double change = dx / dy;
                if (signumX != newSignumX
                    || signumY != newSignumY
                    || Math.abs(lastChange - change) > 0.01) {
                  lastChange = change;
                  RotateTransition rotate = new RotateTransition(new Duration(50), img);
                  rotate.setToAngle(Util.calculateRotation(oldPos, newPos));
                  rotate.play();
                }
              }
            });
    ProgressBar healthBar = new ProgressBar();
    this.getChildren().add(healthBar);
    healthBar.setVisible(false);
    healthBar.getStyleClass().add("health-bar");
    healthBar.maxWidthProperty().bind(img.fitWidthProperty());
    creature
        .lifesProperty()
        .addListener(
            (obj, oldLifes, newLifes) -> {
              healthBar.setProgress(newLifes.doubleValue() / creature.getStartLifes());
              healthBar.setVisible(true);
              this.toFront();
              healthBar.toFront();
            });
  }

  private ImageView createImageView(CreatureType type, DoubleBinding scaleX, DoubleBinding scaleY) {
    Image im = ImageLoader.getCreatureImage(creature.getType());
    ImageView img = new ImageView(im);
    img.setPreserveRatio(true);
    img.fitWidthProperty().bind(scaleX.multiply(imageSize));
    img.fitHeightProperty().bind(scaleY.multiply(imageSize));
    return img;
  }

  public Creature getCreature() {
    return creature;
  }

  public boolean belongsToCreature(Creature creature) {
    return this.creature == creature;
  }
}
