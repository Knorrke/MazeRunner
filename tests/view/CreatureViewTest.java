package view;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.*;

import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

public class CreatureViewTest extends AbstractViewTest {
	@Test
	public void checkCreatureVisible() {
		game.start();
		assertEquals("There should be a creature", false, maze.getCreatures().isEmpty());
		verifyThat("#maze", NodeMatchers.hasChild(".creature"));
		game.pause();
	}
}
