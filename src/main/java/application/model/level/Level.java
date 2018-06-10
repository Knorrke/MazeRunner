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
import application.model.maze.MazeModelInterface;
import application.util.ObservableCreatureGroupListDeserializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Level implements LevelModelInterface {
  @JsonDeserialize(using = ObservableCreatureGroupListDeserializer.class)
  private ObservableList<CreatureGroup> creatureTimeline = FXCollections.observableArrayList();

  @JsonIgnore private MazeModelInterface maze;
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
    assert maze != null : "Models Level and Maze not connected!";
    if (waveNumber.get() < creatureTimeline.size()) {
      CreatureGroup nextCreatureGroup = creatureTimeline.get(waveNumber.getAndIncrement());
      double streuung = 0.4;
      List<Creature> creatures =
          CreatureFactory.createAll(
              maze,
              nextCreatureGroup,
              () -> new Random().nextDouble() * streuung + 0.5 * (1 - streuung),
              () ->
                  new Random().nextInt(maze.getMaxWallY())
                      + Math.random() * streuung
                      + 0.5 * (1 - streuung));
      maze.addAllCreatures(creatures);
    }
  }

  @Override
  public void setMazeModel(MazeModelInterface mazeUpdater) {
    this.maze = mazeUpdater;
  }
}
