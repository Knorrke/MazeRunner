package view.maze;

import application.ImageLoader;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.maze.Wall;

public class WallView extends StackPane {
	public WallView(Wall wall, DoubleBinding scaleX, DoubleBinding scaleY) {
		this.getStyleClass().add("wall");
		
		Image im = ImageLoader.wall;
		ImageView img = new ImageView(im);
		img.fitWidthProperty().bind(scaleX);
		img.fitHeightProperty().bind(scaleY);
		
		this.getChildren().add(img);
		this.layoutXProperty().bind(wall.xProperty().multiply(scaleX));
		this.layoutYProperty().bind(wall.yProperty().multiply(scaleY));
	}
}
