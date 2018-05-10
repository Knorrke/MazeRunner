package view;

import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.DebugUtils;

public class CreaturesTimelineTest extends AbstractViewTest {
    
	@Test
    public void shouldContainCorrectNumberOfImages() {
    	int countGroups = level.getCreatureTimeline().size();
    	verifyThat("#creatureTimelineView", NodeMatchers.hasChildren(countGroups, ".image-view"), DebugUtils.informedErrorMessage(this));
    }
}
