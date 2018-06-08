package application.model.player;

import application.model.creature.Creature;
import application.model.maze.Wall;

public class PlayerUpdater implements PlayerUpdaterInterface {
  private PlayerModelInterface player;

  public PlayerUpdater(PlayerModelInterface player) {
    this.player = player;
  }

  @Override
  public void leavingCreature(Creature creature) {
    player.looseLife();
  }

  @Override
  public boolean newWallBuilt(Wall wall) {
    return player.spendMoney(1);
  }

  @Override
  public void soldWall(Wall wall) {
    player.earnMoney(1);
  }
}
