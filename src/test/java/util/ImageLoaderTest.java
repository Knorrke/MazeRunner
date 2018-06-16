package util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import application.util.ImageLoader;

public class ImageLoaderTest {

  @Test
  public void imageDefined() {
    ImageLoader.loadAll();
    assertNotNull(ImageLoader.lifes);
  }
}
