package application.view.maze;

import application.ImageLoader;
import application.model.maze.tower.Bullet;
import application.util.Calc;
import javafx.animation.RotateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

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
        .addListener(
            (obj, oldPos, newPos) -> {
              RotateTransition rotate = new RotateTransition(new Duration(50), this);
              rotate.setToAngle(Calc.calculateRotation(oldPos, newPos));
              rotate.play();
            });
  }

  public Object getBullet() {
    return bullet;
  }
}
