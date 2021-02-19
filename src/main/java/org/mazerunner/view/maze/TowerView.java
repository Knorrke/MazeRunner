package org.mazerunner.view.maze;

import java.util.Iterator;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.mazerunner.model.Position;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.tower.AbstractTower;
import org.mazerunner.model.maze.tower.TowerType;
import org.mazerunner.model.maze.tower.bullet.Bullet;
import org.mazerunner.util.ImageLoader;
import org.mazerunner.util.Util;

public class TowerView extends StackPane {
  private AbstractTower tower;
  private DoubleBinding scaleX, scaleY;
  private ListChangeListener<Bullet> listener =
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
    this.tower = tower;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    this.getStyleClass().add("tower");
    TowerType towerType = tower.getType();
    ImageView towerImage = new ImageView(ImageLoader.getTowerImage(towerType));
    towerImage.fitWidthProperty().bind(scaleX);
    towerImage.fitHeightProperty().bind(scaleY);
    towerImage.getStyleClass().addAll("tower-image", towerType.name());

    ObjectProperty<Creature> targetedCreature = tower.targetedCreatureProperty();

    targetedCreature.addListener(
        (obj, oldValue, newValue) -> {
          towerImage.rotateProperty().unbind();
          if (newValue != null) {
            var position = newValue.positionProperty();
            Position towerPos = new Position(tower.getX() + 0.5, tower.getY() + 0.5);

            towerImage
                .rotateProperty()
                .bind(
                    Bindings.createDoubleBinding(
                        () -> Util.calculateRotation(position.get(), towerPos), position));
          }
        });

    ImageView upgradeView = new ImageView(ImageLoader.getLevelImage(tower.getLevel()));
    upgradeView.fitWidthProperty().bind(scaleX);
    upgradeView.fitHeightProperty().bind(scaleY);
    upgradeView.getStyleClass().add("level" + tower.getLevel());

    tower.getBullets().addListener(listener);
    this.getChildren().addAll(towerImage, upgradeView);
  }

  public void createBullets(List<? extends Bullet> list) {
    ObservableList<Node> children = this.getChildren();
    for (Bullet bullet : list) {
      children.add(new BulletView(bullet, scaleX, scaleY));
    }
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

  public AbstractTower getTower() {
    return tower;
  }
}
