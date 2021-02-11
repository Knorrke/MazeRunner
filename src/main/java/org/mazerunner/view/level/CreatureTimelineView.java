package org.mazerunner.view.level;

import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import org.mazerunner.model.creature.CreatureGroup;
import org.mazerunner.model.level.LevelModelInterface;
import org.mazerunner.view.Bindable;

public class CreatureTimelineView extends StackPane implements Bindable<LevelModelInterface> {
  private VBox creatureTimeline;
  private Rectangle currentWaveHighlight;

  public CreatureTimelineView() {
    this.setPadding(new Insets(10, 20, 0, 0));
    this.setAlignment(Pos.TOP_CENTER);

    creatureTimeline = new VBox();
    creatureTimeline.setSpacing(10);
    creatureTimeline.setPadding(new Insets(5, 0, 5, 0));
    creatureTimeline.setAlignment(Pos.TOP_CENTER);
    creatureTimeline.setMaxHeight(USE_PREF_SIZE);

    currentWaveHighlight = new Rectangle(100, 50);
    currentWaveHighlight.setFill(Color.rgb(200, 200, 200, 0.5));
    currentWaveHighlight.setStroke(Color.rgb(20, 100, 20));
    currentWaveHighlight.setStrokeLineJoin(StrokeLineJoin.ROUND);
    currentWaveHighlight.setStrokeType(StrokeType.INSIDE);
    currentWaveHighlight.setStrokeWidth(5);

    this.getChildren().addAll(currentWaveHighlight, creatureTimeline);
  }

  @Override
  public void bind(LevelModelInterface level) {
    creatureTimeline
        .translateYProperty()
        .bind(
            level
                .passedTimePercentageBinding()
                .multiply(creatureTimeline.heightProperty())
                .multiply(-1));
    createCreatureTimeline(level.getCreatureTimeline());
    level
        .getCreatureTimeline()
        .addListener(
            (ListChangeListener<CreatureGroup>)
                c -> {
                  this.createCreatureTimeline(level.getCreatureTimeline());
                });
  }

  public void createCreatureTimeline(List<? extends CreatureGroup> timeline) {
    creatureTimeline.getChildren().clear();
    for (CreatureGroup group : timeline) {
      creatureTimeline.getChildren().add(new CreatureTimelineImage(group));
    }
  }

  public VBox getTimeline() {
    return creatureTimeline;
  }
}
