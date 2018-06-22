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
  public static Image play, pause;
  public static Image normalCreature, toughCreature;

  public static Image wall;

  public static Image sell, upgrade;
  public static Image noTower, normalTower;
  public static Image level0, level1, level2, level3, level4;

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
    normalCreature = loadImage("creatures/normal.png");
    toughCreature = loadImage("creatures/tough.png");

    sell = loadImage("sell.png");
    upgrade = loadImage("upgrade.png");

    noTower = empty;
    normalTower = loadImage("towers/normal.png");
    level0 = empty;
    level1 = loadImage("towers/level1.png");
    level2 = loadImage("towers/level2.png");
    level3 = loadImage("towers/level3.png");
    level4 = loadImage("towers/level4.png");

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
      case NORMAL:
      default:
        return ImageLoader.normalCreature;
    }
  }

  public static Image getTowerImage(TowerType towerType) {
    switch (towerType) {
      case NORMAL:
        return ImageLoader.normalTower;
      case NO:
      default:
        return ImageLoader.noTower;
    }
  }

  public static Image getLevelImage(int level) {
    switch (level) {
      case 0:
        return ImageLoader.level0;
      case 1:
        return ImageLoader.level1;
      case 2:
        return ImageLoader.level2;
      case 3:
        return ImageLoader.level3;
      case 4:
      default:
        return ImageLoader.level4;
    }
  }
}
