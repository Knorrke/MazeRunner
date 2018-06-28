package application.view.maze;

import java.util.ArrayList;
import java.util.List;
import application.controller.WallController;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.TowerType;
import application.model.maze.tower.TowerUpgrade;
import application.util.ImageLoader;
import application.util.Util;
import application.view.FloatingLabel;
import javafx.beans.binding.DoubleBinding;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import jfxtras.scene.menu.CirclePopupMenu;

public class WallMenuView extends CirclePopupMenu {
  private MenuItem sellItem;
  private Label sellItemLabel;
  private MenuItem upgradeItem;
  private Label upgradeItemLabel;
  private List<MenuItem> towerItems;

  private DoubleBinding scaleX, scaleY;

  public WallMenuView(WallView view, DoubleBinding scaleX, DoubleBinding scaleY) {
    super(view, null);
    super.setAnimationDuration(Duration.millis(200));
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    WallController wallController = view.getController();

    sellItemLabel = new FloatingLabel();
    sellItem = createMenuItem("Sell", ImageLoader.sell, sellItemLabel, e -> wallController.sell());

    upgradeItemLabel = new FloatingLabel();
    upgradeItem =
        createMenuItem(
            "Upgrade Tower",
            ImageLoader.upgrade,
            upgradeItemLabel,
            e -> wallController.upgradeTower());

    towerItems = new ArrayList<>();
    for (TowerType type : TowerType.values()) {
      if (type.equals(TowerType.NO)) continue;

      Label towerLabel =
          new FloatingLabel(Util.moneyString(-1 * AbstractTower.create(type).getCosts()));
      MenuItem towerItem =
          createMenuItem(
              type.toString(),
              ImageLoader.getTowerImage(type),
              towerLabel,
              e -> wallController.buildTower(type));
      towerItems.add(towerItem);
    }
    this.getItems().addAll(sellItem, upgradeItem);
    this.getItems().addAll(towerItems);
  }

  private MenuItem createMenuItem(
      String hoverText, Image image, Label label, EventHandler<? super MouseEvent> handler) {
    ImageView view = new ImageView(image);
    String id = hoverText.toLowerCase().replace(' ', '-');
    view.setId(id);
    view.setPreserveRatio(true);
    view.fitWidthProperty().bind(scaleX.add(10));
    view.fitHeightProperty().bind(scaleY.add(10));
    view.setOnMouseClicked(handler);
    label.setId(id + "-label");
    label.setMouseTransparent(true);
    return new MenuItem(hoverText, new StackPane(view, label));
  }

  public void show(MouseEvent event, Wall wall, WallController wallController) {
    getItems().clear();
    sellItemLabel.setText(Util.moneyString(wall.getCosts()));
    getItems().add(sellItem);
    if (wall.getTower().getType() == TowerType.NO) {
      getItems().addAll(towerItems);
    } else {
      TowerUpgrade upgrade = wall.getTower().getNextUpgrade();
      if (upgrade != null) {
        upgradeItemLabel.setText(Util.moneyString(-1 * upgrade.getCosts()));
        getItems().add(upgradeItem);
      }
    }
    super.show(event);
  }
}
