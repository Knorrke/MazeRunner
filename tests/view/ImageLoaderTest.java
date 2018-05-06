package view;

import static org.junit.Assert.*;

import org.junit.Test;

import application.ImageLoader;

public class ImageLoaderTest {

	@Test
	public void imageDefined() {
		ImageLoader.loadAll();
		assertNotNull(ImageLoader.lifes);
	}
}
