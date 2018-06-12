package application.view.maze;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import application.ImageLoader;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.Bullet;
import application.model.maze.tower.TowerType;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class TowerView extends StackPane {
  DoubleBinding scaleX, scaleY;
  ListChangeListener<Bullet> listener =
      (c) -> {
        while (c.next()) {
          if (c.wasAdded()) {
            createBullets(c.getAddedSubList());
          } else if (c.wasRemoved()) {
            removeBullets(c.getRemoved());
          }
        }
      };

  public TowerView(AbstractTower tower, DoubleBinding scaleX, DoubleBinding scaleY) {
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    TowerType towerType = tower.getType();
    ImageView towerImage = new ImageView(ImageLoader.getTowerImage(towerType));
    towerImage.fitWidthProperty().bind(scaleX);
    towerImage.fitHeightProperty().bind(scaleY);
    towerImage.getStyleClass().addAll("tower-image", towerType.toString());
    
    tower.getBullets().addListener(listener);
    this.getChildren().add(towerImage);
  }

  public void createBullets(List<? extends Bullet> list) {
    ObservableList<Node> children = this.getChildren();
    children.addAll(
        list.stream()
            .map(bullet -> new BulletView(bullet, scaleX, scaleY))
            .collect(Collectors.toList()));
  }

  private void removeBullets(List<? extends Bullet> removed) {
    ObservableList<Node> children = this.getChildren();
    for (Iterator<Node> iterator = children.iterator(); iterator.hasNext(); ) {
      Node node = iterator.next();
      if (node instanceof BulletView && removed.contains(((BulletView) node).getBullet())) {
        iterator.remove();
      }
    }
  }
}
