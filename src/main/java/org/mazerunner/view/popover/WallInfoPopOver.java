package org.mazerunner.view.popover;

import org.controlsfx.control.PopOver;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.AbstractTower;
import org.mazerunner.model.maze.tower.TowerUpgrade;
import org.mazerunner.util.Util;
import org.mazerunner.view.maze.WallView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import jfxtras.scene.layout.VBox;

public class WallInfoPopOver extends PopOver {

  public WallInfoPopOver(Wall wall, WallView view) {
    setAnimated(false);
    HBox box = new HBox(5);
    VBox towerInfos = new VBox(5);
    AbstractTower tower = wall.getTower();
    towerInfos
        .getChildren()
        .addAll(
            new Label("Tower: " + tower.getType().toString()),
            new Label("Damage: " + tower.getDamage()),
            new Label("Range: " + tower.getVisualRange()),
            new Label("Fire rate: " + tower.getFireRate()),
            new Label("Total costs: " + Util.moneyString(wall.getCosts())),
            new Label("Upgrade Level: " + tower.getLevel()));
    box.getChildren().add(towerInfos);

    TowerUpgrade upgrade = tower.getNextUpgrade();
    if (upgrade != null) {
      VBox upgradeInfos = new VBox(5);
      upgradeInfos
          .getChildren()
          .addAll(
              new Label("Next Upgrade:"),
              new Label("Upgraded damage: " + upgrade.getDamageUpgrader().apply(tower.getDamage())),
              new Label(
                  "Upgraded range: "
                      + upgrade.getVisualRangeUpgrader().apply(tower.getVisualRange())),
              new Label(
                  "Upgraded fire rate: "
                      + upgrade.getFireRateUpgrader().apply(tower.getFireRate())),
              new Label("Costs: " + Util.moneyString(upgrade.getCosts())),
              new Label("Level: " + (tower.getLevel() + 1)));
      box.getChildren().add(upgradeInfos);
    }
    this.setContentNode(box);
    this.show(view);
    view.showSelection();
    this.setOnHidden(event -> view.hideSelection());
  }
}
