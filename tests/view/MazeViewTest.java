package view;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

public class MazeViewTest extends AbstractViewTest {
    
    @Test
    public void buildWallOnClick() {
    	clickOn("#maze");
    	assertEquals("There should be a wall now", 1, maze.getWalls().size());
    	verifyThat("#maze", NodeMatchers.hasChildren(1, ".wall"));
    	verifyThat("#maze .wall", NodeMatchers.hasChildren(1, ".image-view"));
    	//second click:
    	clickOn();

    	assertEquals("There should still be only one wall", 1, maze.getWalls().size());
    	verifyThat("#maze", NodeMatchers.hasChildren(1, ".wall"));
    	
    	//third click:
    	moveBy(20, 50);
    	clickOn();

    	assertEquals("There should be two walls now", 2, maze.getWalls().size());
    	verifyThat("#maze", NodeMatchers.hasChildren(2, ".wall"));
    	    	
    }
}
