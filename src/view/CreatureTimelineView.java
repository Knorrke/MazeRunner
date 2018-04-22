package view;

import java.util.List;
import java.util.stream.Collectors;

import application.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.creature.CreatureGroup;

public class CreatureTimelineView extends VBox {
	public CreatureTimelineView() {
		super();
		this.setSpacing(10);
	}

	public void createFromData(List<CreatureGroup> timeline) {
		this.getChildren().clear();
		this.getChildren().addAll(timeline.stream().map((group)-> {
			ImageView el = new ImageView(ImageLoader.lifes);
			el.setPreserveRatio(true);
			el.setFitWidth(20);
			return el;
		}).collect(Collectors.toList()));
	}

}
