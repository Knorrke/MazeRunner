package org.mazerunner.model.level;

import java.util.List;
import java.util.Random;
import org.mazerunner.model.baseactions.CountdownAction;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureFactory;
import org.mazerunner.model.creature.CreatureGroup;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.util.ObservableCreatureGroupListDeserializer;
import org.mazerunner.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

public class Level implements LevelModelInterface {
  public static final double WAVE_DURATION = 15;

  @JsonDeserialize(using = ObservableCreatureGroupListDeserializer.class)
  private ObservableList<CreatureGroup> creatureTimeline;

  private IntegerProperty waveNumber;

  @JsonIgnore private MazeModelInterface maze;

  @JsonIgnore private CountdownAction countdownAction;

  @JsonIgnore private NumberBinding passedTimePercentageBinding;

  public Level() {
    creatureTimeline = FXCollections.observableArrayList();
    waveNumber = new SimpleIntegerProperty(0);
    this.countdownAction = new CreatureWaveAction(this, WAVE_DURATION, 1);
    passedTimePercentageBinding =
        Bindings.createFloatBinding(
            this::calculatePassedTimePercentage,
            waveNumber,
            countdownAction.countdownProperty(),
            creatureTimeline);
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
    countdownAction.act(dt);
  }

  @Override
  public void sendNextCreatureWave() {
    assert maze != null : "Models Level and Maze not connected!";
    if (getWaveNumber() < creatureTimeline.size()) {
      CreatureGroup nextCreatureGroup = creatureTimeline.get(getWaveNumber());
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
      incrementWaveNumber();
      countdownAction.resetCountdown();
    }
  }

  @Override
  public int getWaveNumber() {
    return waveNumber.get();
  }

  @Override
  public ReadOnlyIntegerProperty waveNumberProperty() {
    return waveNumber;
  }

  private void setWaveNumber(int value) {
    waveNumber.set(value);
  }

  private void incrementWaveNumber() {
    setWaveNumber(getWaveNumber() + 1);
  }

  @Override
  public double calculateGameDuration() {
    return creatureTimeline.size() * WAVE_DURATION;
  }

  @Override
  public NumberBinding passedTimePercentageBinding() {
    return passedTimePercentageBinding;
  }

  @Override
  public float calculatePassedTimePercentage() {
    return (float) Util.round(1 - calculateRemainingTimePercentage(), 5);
  }

  private float calculateRemainingTimePercentage() {
    double remaining = 0;
    remaining += (creatureTimeline.size() - getWaveNumber()) * WAVE_DURATION;
    if (getWaveNumber() > 0) remaining += countdownAction.getCountdown();
    double gameDuration = calculateGameDuration();
    return (float) (gameDuration != 0 ? Util.round(remaining / gameDuration, 5) : 1);
  }

  @Override
  public void setMazeModel(MazeModelInterface mazeUpdater) {
    this.maze = mazeUpdater;
    maze.getCreatures()
        .addListener(
            (Change<? extends Creature> c) -> {
              while (c.next()) {
                if (c.wasRemoved() && maze.getCreatures().isEmpty() && countdownAction.getCountdown() > 1) {
                  countdownAction.act(countdownAction.getCountdown() - 1);
                }
              }
            });
  }
}
