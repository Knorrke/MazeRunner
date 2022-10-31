package org.mazerunner.view.creatures;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.ImageView;
import org.mazerunner.util.ImageLoader;

public class Walkietalkie extends ImageView {

  public Walkietalkie(DoubleBinding width) {
    super(ImageLoader.walkietalkie);
    this.getStyleClass().add("speech-bubble");
    setPreserveRatio(true);
    fitWidthProperty().bind(width.multiply(0.4));
    setRotate(-135);
    setVisible(false);
  }
}
