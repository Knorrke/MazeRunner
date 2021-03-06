package view.level;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import javafx.scene.layout.VBox;
import org.junit.Test;
import org.mazerunner.model.creature.CreatureGroup;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.view.level.CreatureTimelineView;
import org.testfx.matcher.base.NodeMatchers;
import util.TestFXHelper;
import view.AbstractViewTest;

public class CreaturesTimelineTest extends AbstractViewTest {

  @Test
  public void shouldContainCorrectNumberOfImages() {
    int countGroups = level.getCreatureTimeline().size();
    verifyThat(
        "#creatureTimelineView",
        NodeMatchers.hasChildren(countGroups, ".image-view"),
        collectInfos());
  }

  @Test
  public void shouldShowInfoOnClick() {
    interact(() -> level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 5)));
    verifyThat(TestFXHelper.carefulQuery(".popover"), NodeMatchers.isNull(), collectInfos());
    clickOn(".wave");
    verifyThat(TestFXHelper.carefulQuery(".popover"), NodeMatchers.isNotNull(), collectInfos());
  }

  @Test
  public void shouldScrollWhenTimePasses() {
    CreatureTimelineView creatureTimelineView = lookup("#creatureTimelineView").query();
    VBox creatureTimeline = creatureTimelineView.getTimeline();
    double startTranslation = creatureTimeline.getTranslateY();
    interact(() -> level.sendNextCreatureWave()); // simulates passed time
    interact(() -> level.sendNextCreatureWave()); // simulates passed time
    assertTrue(Math.abs(creatureTimeline.getTranslateY()) > startTranslation);
    verifyThat(
        creatureTimeline,
        (VBox ct) -> {
          double visibleQuotient = Math.abs(ct.getTranslateY() / ct.getHeight());
          return Math.abs(visibleQuotient - level.calculatePassedTimePercentage()) < 0.01;
        });
  }
}
