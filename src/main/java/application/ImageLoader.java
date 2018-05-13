package application;

import java.io.InputStream;
import java.util.logging.Logger;

import application.model.creature.CreatureType;
import javafx.scene.image.Image;

public class ImageLoader {
  private static Logger LOG = Logger.getLogger(ImageLoader.class.getName());

  private static final String basePath = "images";
  private static Image placeholder = loadImage("placeholder.png");

  public static Image money, lifes;
  public static Image play, pause;
  public static Image normalCreature, toughCreature;

  public static Image wall;

  public static void loadAll() {
    loadGameImages();
    loadMenuImages();
  }

  public static void loadGameImages() {
    LOG.fine("Loading game images");
    money = loadImage("money.png");
    lifes = loadImage("lifes.png");

    wall = loadImage("wall.png");

    play = loadImage("play.png");
    pause = loadImage("pause.png");
    normalCreature = loadImage("creatures/normal.png");
    toughCreature = loadImage("creatures/tough.png");
    LOG.fine("Finished loading game images");
  }

  public static void loadMenuImages() {}

  private static Image loadImage(String src) {
    InputStream resourceStream =
        ImageLoader.class.getClassLoader().getResourceAsStream(createLookupPath(src));
    if (resourceStream != null) {
      return new Image(resourceStream);
    } else if (placeholder != null) {
      return placeholder;
    }

    LOG.severe("Image loading unsuccessfull!");
    throw new Error("Failed loading Image " + src);
  }

  private static String createLookupPath(String src) {
    if (basePath.endsWith("/")) {
      return basePath + src;
    } else {
      return basePath + "/" + src;
    }
  }

  public static Image getCreatureImage(CreatureType type) {
    switch (type) {
      case TOUGH:
        return ImageLoader.toughCreature;
      case NORMAL:
      default:
        return ImageLoader.normalCreature;
    }
  }
}
