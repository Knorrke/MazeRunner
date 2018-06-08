package application.model.level;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import application.model.actions.Action;
import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureGroup;
import application.model.maze.MazeUpdaterInterface;
import application.util.ObservableCreatureGroupListDeserializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Level implements LevelModelInterface {
  @JsonDeserialize(using = ObservableCreatureGroupListDeserializer.class)
  private ObservableList<CreatureGroup> creatureTimeline = FXCollections.observableArrayList();

  @JsonIgnore private MazeUpdaterInterface mazeUpdater;
  private AtomicInteger waveNumber = new AtomicInteger(0);

  @JsonIgnore private Action action;

  public Level() {
    this.action = new CreatureWaveAction(this);
  }

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
    action.run(dt);
  }

  @Override
  public void sendNextCreatureWave() {
    assert mazeUpdater != null : "Model Level and Maze not connected!";
    if (waveNumber.get() < creatureTimeline.size()) {
      CreatureGroup nextCreatureGroup = creatureTimeline.get(waveNumber.getAndIncrement());
      double streuung = 0.4;
      List<Creature> creatures =
          CreatureFactory.createAll(
              mazeUpdater.getMaze(),
              nextCreatureGroup,
              () -> new Random().nextDouble() * streuung + 0.5 * (1 - streuung),
              () ->
                  new Random().nextInt(mazeUpdater.getMaze().getMaxWallY())
                      + Math.random() * streuung
                      + 0.5 * (1 - streuung));
      mazeUpdater.nextWave(creatures);
    }
  }

  @Override
  public void setMazeUpdater(MazeUpdaterInterface mazeUpdater) {
    this.mazeUpdater = mazeUpdater;
  }
}
