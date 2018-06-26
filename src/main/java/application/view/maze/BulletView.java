package application.view.maze;

import application.model.maze.tower.bullet.Bullet;
import application.util.ImageLoader;
import application.util.Util;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.ImageView;

public class BulletView extends ImageView {
  private static final double bulletWidth = 0.2, bulletHeight = 0.1;
  private Bullet bullet;

  public BulletView(Bullet bullet, DoubleBinding scaleX, DoubleBinding scaleY) {
    super(ImageLoader.normalBullet);
    this.bullet = bullet;

    this.fitWidthProperty().bind(scaleX.multiply(bulletWidth));
    this.fitHeightProperty().bind(scaleY.multiply(bulletHeight));

    this.translateXProperty()
        .bind(bullet.dxProperty().subtract(0.5 * bulletWidth).multiply(scaleX));
    this.translateYProperty()
        .bind(bullet.dyProperty().subtract(0.5 * bulletHeight).multiply(scaleY));
    this.getStyleClass().add("bullet");
    bullet
        .relativePositionProperty()
        .addListener((obj, oldPos, newPos) -> setRotate(Util.calculateRotation(oldPos, newPos)));

    bullet
        .hasHitTargetProperty()
        .addListener(
            (obj, oldVal, newVal) -> {
              if (newVal) {
                this.setVisible(false);
              }
            });
  }

  public Bullet getBullet() {
    return bullet;
  }
}
