package org.mazerunner.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class FXMLLoaderUtil {
  private FXMLLoaderUtil() {}

  private static Logger LOG = Logger.getLogger(FXMLLoaderUtil.class.getName());
  public static String basePath = "fxml/";

  public static Parent load(String res) {
    return load(res, null);
  }

  public static Parent load(String res, Object controller) {
    try {
      FXMLLoader loader =
          new FXMLLoader(FXMLLoaderUtil.class.getClassLoader().getResource(basePath + res));
      loader.setController(controller);
      LOG.fine("loading " + res + " from fxml");
      Parent result = loader.load();
      LOG.fine("loading " + res + " successfull");
      return result;
    } catch (Exception exception) {
      LOG.log(Level.SEVERE, "Loading " + res + " failed", exception);
      return new ErrorView(res);
    }
  }

  public static class ErrorView extends Label {
    public ErrorView(String res) {
      super("Loading " + res + " failed. Please check the filename.");
      setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
    }
  }
}
