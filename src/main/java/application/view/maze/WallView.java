package application.view.maze;

import application.controller.WallController;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.util.ImageLoader;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class WallView extends StackPane {
  private ImageView imgView;
  private TowerView towerView;
  private WallController controller;
  private Wall wall;
  private Node selectionView;

  public WallView(WallController controller) {
    this.controller = controller;
  }

  public void bind(Wall wall, DoubleBinding scaleX, DoubleBinding scaleY) {
    this.wall = wall;
    this.getStyleClass().add("wall");

    imgView = new ImageView(ImageLoader.wall);
    imgView.fitWidthProperty().bind(scaleX);
    imgView.fitHeightProperty().bind(scaleY);
    imgView.getStyleClass().add("wall-image");

    this.getChildren().addAll(imgView);

    setTowerView(new TowerView(wall.getTower(), scaleX, scaleY));
    setSelectionView(createSelectionView(wall.getTower(), scaleX, scaleY));

    wall.towerProperty()
        .addListener(
            (obj, oldTower, newTower) -> {
              setSelectionView(createSelectionView(newTower, scaleX, scaleY));
              setTowerView(new TowerView(newTower, scaleX, scaleY));
            });
    this.layoutXProperty().bind(wall.xProperty().multiply(scaleX));
    this.layoutYProperty().bind(wall.yProperty().multiply(scaleY));
  }

  private Node createSelectionView(
      AbstractTower tower, DoubleBinding scaleX, DoubleBinding scaleY) {
    Ellipse range = new Ellipse();
    range.setFill(Color.rgb(200, 255, 200, 0.5));
    range.radiusXProperty().bind(scaleX.multiply(tower.getVisualRange()));
    range.radiusYProperty().bind(scaleY.multiply(tower.getVisualRange()));
    range.translateXProperty().bind(widthProperty().divide(2));
    range.translateYProperty().bind(heightProperty().divide(2));
    range.setManaged(false);
    range.setVisible(false);
    return range;
  }

  private void setSelectionView(Node selectionView) {
    if (this.selectionView != null) {
      this.getChildren().remove(this.selectionView);
    }
    this.selectionView = selectionView;
    this.getChildren().add(this.selectionView);
  }

  private void setTowerView(TowerView towerView) {
    if (this.towerView != null) {
      this.getChildren().remove(this.towerView);
    }
    this.towerView = towerView;
    this.getChildren().add(this.towerView);
  }

  public WallController getController() {
    return controller;
  }

  public void showSelection() {
    selectionView.setVisible(true);
  }

  public void hideSelection() {
    selectionView.setVisible(false);
  }

  public boolean belongsToWall(Wall wall) {
    return this.wall == wall;
  }
}
