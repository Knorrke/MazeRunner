package view;

import static org.junit.Assert.*;

import application.ImageLoader;

import org.junit.Test;

public class ImageLoaderTest {

	@Test
	public void imageDefined() {
		ImageLoader.loadAll();
		assertNotNull(ImageLoader.lifes);
	}
}
