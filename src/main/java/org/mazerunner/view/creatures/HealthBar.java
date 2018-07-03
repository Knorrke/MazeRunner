package org.mazerunner.view.creatures;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.ProgressBar;

public class HealthBar extends ProgressBar {

  public HealthBar(DoubleProperty width) {
    setVisible(false);
    getStyleClass().add("health-bar");
    maxWidthProperty().bind(width);
  }
}
