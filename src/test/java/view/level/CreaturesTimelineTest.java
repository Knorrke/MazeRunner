package view.level;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;
import application.view.level.CreatureTimelineView;
import javafx.scene.layout.VBox;
import view.AbstractViewTest;

public class CreaturesTimelineTest extends AbstractViewTest {

  @Test
  public void shouldContainCorrectNumberOfImages() {
    int countGroups = level.getCreatureTimeline().size();
    verifyThat("#creatureTimelineView", NodeMatchers.hasChildren(countGroups, ".image-view"),
        collectInfos());
  }

  @Test
  public void shouldScrollWhenTimePasses() {
    CreatureTimelineView creatureTimelineView = lookup("#creatureTimelineView").query();
    VBox creatureTimeline = creatureTimelineView.getTimeline();
    double startTranslation = creatureTimeline.getTranslateY();
    interact(() -> level.sendNextCreatureWave()); //simulates passed time
    interact(() -> level.sendNextCreatureWave()); //simulates passed time
    assertTrue(Math.abs(creatureTimeline.getTranslateY()) > startTranslation);
	verifyThat(creatureTimeline, (VBox ct) -> {
	  double visibleQuotient = Math.abs(ct.getTranslateY() / ct.getHeight());
	  return Math.abs(visibleQuotient - level.calculatePassedTimePercentage()) < 0.01;
	});
  }
}
