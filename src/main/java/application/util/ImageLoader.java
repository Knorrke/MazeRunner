package application.util;

import java.io.InputStream;
import java.util.logging.Logger;
import application.model.creature.CreatureType;
import application.model.maze.tower.TowerType;
import javafx.scene.image.Image;

public class ImageLoader {
  private ImageLoader() {}

  private static Logger LOG = Logger.getLogger(ImageLoader.class.getName());

  private static final String basePath = "images/";
  private static Image placeholder = loadImage("placeholder.png");
  private static Image empty;

  public static Image money, lifes;
  public static Image play, pause, buildSelected, buildNotSelected, infoSelected, infoNotSelected;
  public static Image normalCreature, toughCreature, dumbCreature;
  public static Image talking;

  public static Image wall;

  public static Image sell, upgrade;
  public static Image noTower, normalTower, slowdownTower, fastTower;
  public static Image[] levels;
  public static Image normalBullet;

  public static Image endModalBackground;

  public static void loadAll() {
    loadGameImages();
    loadMenuImages();
  }

  public static void loadGameImages() {
    LOG.fine("Loading game images");
    empty = loadImage("empty.png");
    money = loadImage("money.png");
    lifes = loadImage("lifes.png");

    wall = loadImage("wall.png");

    play = loadImage("play.png");
    pause = loadImage("pause.png");
    buildSelected = loadImage("build-selected.png");
    buildNotSelected = loadImage("build.png");
    infoSelected = loadImage("info-selected.png");
    infoNotSelected = loadImage("info.png");

    normalCreature = loadImage("creatures/normal.png");
    toughCreature = loadImage("creatures/tough.png");
    dumbCreature = loadImage("creatures/dumb.png");

    talking = loadImage("creatures/talking.png");

    sell = loadImage("sell.png");
    upgrade = loadImage("upgrade.png");

    noTower = empty;
    normalTower = loadImage("towers/normal.png");
    slowdownTower = loadImage("towers/slowdown.png");
    fastTower = loadImage("towers/fast.png");
    levels = new Image[5];
    levels[0] = empty;
    for (int i = 1; i < levels.length; i++) {
      levels[i] = loadImage("towers/level" + i + ".png");
    }

    normalBullet = loadImage("towers/normalBullet.png");

    LOG.fine("Finished loading game images");
  }

  public static void loadMenuImages() {
    endModalBackground = loadImage("end-modal-background.png");
  }

  private static Image loadImage(String src) {
    InputStream resourceStream =
        ImageLoader.class.getClassLoader().getResourceAsStream(basePath + src);
    if (resourceStream != null) {
      return new Image(resourceStream);
    } else if (placeholder != null) {
      return placeholder;
    }

    LOG.severe("Image loading unsuccessfull!");
    throw new Error("Failed loading Image " + src);
  }

  public static Image getCreatureImage(CreatureType type) {
    switch (type) {
      case TOUGH:
        return ImageLoader.toughCreature;
      case DUMB:
        return ImageLoader.dumbCreature;
      case NORMAL:
      default:
        return ImageLoader.normalCreature;
    }
  }

  public static Image getTowerImage(TowerType towerType) {
    switch (towerType) {
      case FAST:
        return ImageLoader.fastTower;
      case SLOWDOWN:
        return ImageLoader.slowdownTower;
      case NORMAL:
        return ImageLoader.normalTower;
      case NO:
      default:
        return ImageLoader.noTower;
    }
  }

  public static Image getLevelImage(int level) {
    if (level < levels.length) {
      return levels[level];
    }
    return levels[levels.length - 1];
  }
}
