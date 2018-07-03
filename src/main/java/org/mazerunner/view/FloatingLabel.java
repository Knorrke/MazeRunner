package org.mazerunner.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FloatingLabel extends Label {
  public FloatingLabel() {
    setAlignment(Pos.BOTTOM_RIGHT);
    setBackground(new Background(new BackgroundFill(Color.web("#6c6"), new CornerRadii(30), null)));
    StackPane.setAlignment(this, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(this, new Insets(0, -10, -4, 0));
    setFont(new Font("Open Sans", 14));
    setPadding(new Insets(1, 5, 1, 5));
    DropShadow dropshadow = new DropShadow(12, -2, 0, Color.BLACK);
    dropshadow.setHeight(30);
    dropshadow.setWidth(20);
    setEffect(dropshadow);
  }

  public FloatingLabel(String text) {
    this();
    setText(text);
  }
}
