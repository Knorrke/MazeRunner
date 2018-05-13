package view;

import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

public class CreaturesTimelineTest extends AbstractViewTest {

  @Test
  public void shouldContainCorrectNumberOfImages() {
    int countGroups = level.getCreatureTimeline().size();
    verifyThat(
        "#creatureTimelineView",
        NodeMatchers.hasChildren(countGroups, ".image-view"),
        collectInfos());
  }
}
