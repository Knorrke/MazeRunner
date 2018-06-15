package application.view.level;

import application.ImageLoader;
import application.model.creature.CreatureGroup;
import application.view.FloatingLabel;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class CreatureTimelineImage extends StackPane {
  public CreatureTimelineImage(CreatureGroup group){
    this.setMaxWidth(40);
    
    Image im = ImageLoader.getCreatureImage(group.getType());
    ImageView img = new ImageView(im);
    img.setPreserveRatio(true);
    img.setFitWidth(40);

    Label label = new FloatingLabel(Integer.toString(group.getNumber())).getView();
    this.getChildren().addAll(img, label);
  }
}
