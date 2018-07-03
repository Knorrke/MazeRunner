package org.mazerunner.util;

import javafx.scene.transform.Scale;

public class ScaleUtil {

  private static Scale scale;

  public static Scale getScale() {
    if (scale == null) {
      scale = new Scale();
    }
    return scale;
  }
}
