package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.maze.tower.TowerType;
import org.mazerunner.util.ImageLoader;

public class ImageLoaderTest {

  @Test
  public void imageDefined() {
    ImageLoader.loadAll();
    assertNotNull(ImageLoader.lifes);
  }

  @Test
  public void getCreatureImageTest() {
    ImageLoader.loadGameImages();
    assertEquals(ImageLoader.normalCreature, ImageLoader.getCreatureImage(CreatureType.NORMAL));
    assertEquals(ImageLoader.toughCreature, ImageLoader.getCreatureImage(CreatureType.TOUGH));
  }

  @Test
  public void getTowerImageTest() {
    ImageLoader.loadGameImages();
    assertEquals(ImageLoader.normalTower, ImageLoader.getTowerImage(TowerType.NORMAL));
    assertEquals(ImageLoader.noTower, ImageLoader.getTowerImage(TowerType.NO));
  }

  @Test
  public void getLevelImageTest() {
    ImageLoader.loadGameImages();
    for (int i = 0; i < ImageLoader.levels.length; i++) {
      assertEquals(ImageLoader.levels[i], ImageLoader.getLevelImage(i));
    }
    assertEquals(
        "Should be error prone for too high indices",
        ImageLoader.levels[ImageLoader.levels.length - 1],
        ImageLoader.getLevelImage(ImageLoader.levels.length + 1));
  }
}
