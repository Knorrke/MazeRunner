package application.view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

public class FloatingLabel {
  private static Logger LOG = Logger.getLogger(Label.class.getName());
  private Label label;
  
  public FloatingLabel(String text) {
    try {
      LOG.fine("loading ImageLabel from fxml");
      label = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ImageLabel.fxml"));
      LOG.fine("loading ImageLabel successfull");
      label.setText(text);
    } catch (IOException exception) {
      LOG.log(Level.SEVERE, "Loading ImageLabel.fxml failed", exception);
      throw new RuntimeException(exception);
    }
  }
  
  public Label getView() {
    return label;
  }
}
