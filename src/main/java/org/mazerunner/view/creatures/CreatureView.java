package org.mazerunner.view.creatures;

import com.sun.scenario.Settings;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.RotateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.mazerunner.model.Position;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.actions.TalkAction;
import org.mazerunner.util.ImageLoader;
import org.mazerunner.util.Util;
import org.mazerunner.view.maze.MazeView;

public class CreatureView extends StackPane {
  private double imageSize = 0.7;
  private final Creature creature;

  private DoubleBinding scaleX, scaleY;

  public CreatureView(Creature creature, DoubleBinding scaleX, DoubleBinding scaleY) {
    this.creature = creature;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
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
    HealthBar healthBar = new HealthBar(img.fitWidthProperty());
    this.getChildren().add(healthBar);

    creature
        .lifesProperty()
        .addListener(
            (obj, oldLifes, newLifes) -> {
              healthBar.setProgress(newLifes.doubleValue() / creature.getStartLifes());
              healthBar.setVisible(true);
              this.toFront();
              healthBar.toFront();
            });

    SpeechBubble speechBubble = new SpeechBubble(scaleX.multiply(imageSize));
    this.getChildren().add(speechBubble);
    StackPane.setMargin(
        speechBubble, new Insets(-0.8 * scaleX.doubleValue(), -0.8 * scaleY.doubleValue(), 0, 0));

    creature
        .actionProperty()
        .addListener(
            (obj, oldAction, newAction) -> {
              if (newAction instanceof TalkAction) {
                speechBubble.setVisible(true);
              } else {
                speechBubble.setVisible(false);
              }
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

  private Canvas createSelectionView(
      Creature creature, MazeView mazeView, DoubleBinding scaleX, DoubleBinding scaleY) {
    VisitedMap map = creature.getVisitedMap();
    Canvas visited = new Canvas(mazeView.getWidth(), mazeView.getHeight());
    AtomicInteger hash = new AtomicInteger(map.hashCode());
    creature
        .positionProperty()
        .addListener(
            c -> {
              if (map.hashCode() != hash.get()) {
                hash.set(map.hashCode());
                if (visited.isVisible()) {
                  drawVisited(map, visited.getGraphicsContext2D(), scaleX.get(), scaleY.get());
                }
              }
            });
    visited.setManaged(false);
    visited.setVisible(false);
    visited.setId("creature-information");
    return visited;
  }

  private void drawVisited(
      VisitedMap map, GraphicsContext graphicsContext2D, double scaleX, double scaleY) {
    int maxX = Settings.getInt("maxX", 100);
    int maxY = Settings.getInt("maxY", 100);
    graphicsContext2D.clearRect(0, 0, maxX * scaleX, maxY * scaleY);
    graphicsContext2D.setFill(Color.BLACK);
    for (int x = 0; x < 20; x++) {
      for (int y = 0; y < 10; y++) {
        if (map.isUnknown(x, y)) {
          graphicsContext2D.fillRect(x * scaleX, y * scaleY, scaleX, scaleY);
        }
      }
    }
  }

  public void showSelection() {
    MazeView mazeView = (MazeView) this.getScene().lookup("#maze");
    Node alreadySelected = mazeView.lookup("#creature-information");
    if (alreadySelected != null) {
      mazeView.getChildren().remove(alreadySelected);
    }
    Canvas selectionView = createSelectionView(creature, mazeView, scaleX, scaleY);
    mazeView.getChildren().add(selectionView);
    drawVisited(
        creature.getVisitedMap(), selectionView.getGraphicsContext2D(), scaleX.get(), scaleY.get());
    selectionView.setVisible(true);
  }

  public void hideSelection() {
    MazeView mazeView = (MazeView) this.getScene().lookup("#maze");
    Node alreadySelected = mazeView.lookup("#creature-information");
    if (alreadySelected != null) {
      mazeView.getChildren().remove(alreadySelected);
    }
  }

  public Creature getCreature() {
    return creature;
  }

  public boolean belongsToCreature(Creature creature) {
    return this.creature == creature;
  }
}
