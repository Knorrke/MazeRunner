package application;

import javafx.scene.image.Image;

public class ImageLoader {

	public static Image money, lifes;
	public static Image play, pause;

	public ImageLoader() {
		money = new Image(getClass().getClassLoader().getResourceAsStream("images/desert.png"));
		lifes = new Image(getClass().getClassLoader().getResourceAsStream("images/desert.png"));
		play = new Image(getClass().getClassLoader().getResourceAsStream("images/play.png"));
		pause = new Image(getClass().getClassLoader().getResourceAsStream("images/pause.png"));
	}
	
	
}
