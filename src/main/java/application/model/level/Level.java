package application.model.level;

import java.util.List;
import java.util.Random;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import application.model.actions.Action;
import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureGroup;
import application.model.maze.MazeModelInterface;
import application.util.ObservableCreatureGroupListDeserializer;
import application.util.Util;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Level implements LevelModelInterface {
  public static final double WAVE_DURATION = 20;

  @JsonDeserialize(using = ObservableCreatureGroupListDeserializer.class)
  private ObservableList<CreatureGroup> creatureTimeline;

  private IntegerProperty waveNumber;

  @JsonIgnore private MazeModelInterface maze;

  @JsonIgnore private Action action;

  @JsonIgnore private NumberBinding passedTimePercentageBinding;

  public Level() {
    creatureTimeline = FXCollections.observableArrayList();
    waveNumber = new SimpleIntegerProperty(0);
    this.action = new CreatureWaveAction(this, WAVE_DURATION, 1);
    passedTimePercentageBinding =
        Bindings.createFloatBinding(
            this::calculatePassedTimePercentage,
            waveNumber,
            action.countdownProperty(),
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
    action.run(dt);
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
      action.resetCountdown();
    }
  }

  private int getWaveNumber() {
    return waveNumber.get();
  }

  private void incrementWaveNumber() {
    waveNumber.set(waveNumber.get() + 1);
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
    if (getWaveNumber() > 0) remaining += action.getCountdown();
    double gameDuration = calculateGameDuration();
    return (float) (gameDuration != 0 ? Util.round(remaining / calculateGameDuration(), 5) : 1);
  }

  @Override
  public void setMazeModel(MazeModelInterface mazeUpdater) {
    this.maze = mazeUpdater;
  }
}
