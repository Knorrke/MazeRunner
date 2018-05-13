package application.model.level;

import application.model.creature.CreatureGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Level implements LevelModelInterface {

  private ObservableList<CreatureGroup> creatureTimeline = FXCollections.observableArrayList();

  @Override
  public void addCreatureToTimeline(CreatureGroup group) {
    creatureTimeline.add(group);
  }

  @Override
  public ObservableList<CreatureGroup> getCreatureTimeline() {
    return creatureTimeline;
  }

  @Override
  public void update(double dt) {
    // Start new creature waves?
  }
}