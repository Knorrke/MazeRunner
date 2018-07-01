package application.view.creatures;

import application.util.ImageLoader;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.ImageView;

public class SpeechBubble extends ImageView {

  public SpeechBubble(DoubleBinding width) {
    super(ImageLoader.talking);
    this.getStyleClass().add("speech-bubble");
    setPreserveRatio(true);
    fitWidthProperty().bind(width);
    setVisible(false);
  }
}
