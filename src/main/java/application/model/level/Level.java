package application.model.level;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import application.model.GameModelInterface;
import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Level implements LevelModelInterface {

  private ObservableList<CreatureGroup> creatureTimeline = FXCollections.observableArrayList();
  private GameModelInterface game;
  private double countdown;
  private AtomicInteger waveNumber = new AtomicInteger(0);

  public Level(GameModelInterface game) {
    this.game = game;
    countdown = 1;
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
    if (countdown < dt) {
      sendNextCreatureWave();
    } else {
      countdown -= dt;
    }
  }

  @Override
  public void sendNextCreatureWave() {
    CreatureGroup nextCreatureGroup = creatureTimeline.get(waveNumber.getAndIncrement());
    List<Creature> creatures = CreatureFactory.createAll(game.getMaze(), nextCreatureGroup, () -> 0.0, () -> (double) new Random().nextInt());
    game.nextWave(creatures);
    resetCountdown();
  }

  private void resetCountdown() {
    countdown = 20;
  }
}
