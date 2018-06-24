package application.view;

import application.util.FXMLLoaderUtil;
import javafx.scene.control.Label;

public class FloatingLabel {
  private Label label;

  public FloatingLabel(String text) {
    label = (Label) FXMLLoaderUtil.load("ImageLabel.fxml");
    label.setText(text);
  }

  public Label getView() {
    return label;
  }
}
