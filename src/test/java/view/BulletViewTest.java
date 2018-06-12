package view;

import static org.testfx.api.FxAssert.*;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.TowerType;

public class BulletViewTest extends AbstractViewTest {

  @Test
  public void bulletVisibleTest() {
    interact(() -> maze.buildWall(2, 3));
    Wall wall = maze.getWalls().get(0);
    AbstractTower tower = AbstractTower.create(TowerType.NORMAL);
    interact(() -> wall.setTower(tower));
    interact(() -> maze.addCreature(CreatureFactory.create(maze, CreatureType.NORMAL, 1, 3)));

    interact(() -> tower.shoot());
    verifyThat(".bullet", NodeMatchers.isNotNull());
  }
}
