package application.model.level;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import application.model.GameModelInterface;
import application.model.actions.Action;
import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureGroup;
import application.util.ObservableCreatureGroupListDeserializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Level implements LevelModelInterface {
  @JsonDeserialize(using = ObservableCreatureGroupListDeserializer.class)
  private ObservableList<CreatureGroup> creatureTimeline = FXCollections.observableArrayList();

  @JsonBackReference private GameModelInterface game;
  private AtomicInteger waveNumber = new AtomicInteger(0);

  @JsonIgnore private Action action;

  /**
   * Empty constructor mainly used for json deserialization. When using this, you need to set {@link
   * Level#game} by calling {@link Level#setGame(GameModelInterface)}
   */
  public Level() {
    this(null);
  }

  public Level(GameModelInterface game) {
    this.game = game;
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
    if (waveNumber.get() < creatureTimeline.size()) {
      CreatureGroup nextCreatureGroup = creatureTimeline.get(waveNumber.getAndIncrement());
      double streuung = 0.4;
      List<Creature> creatures =
          CreatureFactory.createAll(
              game.getMaze(),
              nextCreatureGroup,
              () -> new Random().nextDouble() * streuung + 0.5 * (1 - streuung),
              () ->
                  new Random().nextInt(game.getMaze().getMaxWallY())
                      + Math.random() * streuung
                      + 0.5 * (1 - streuung));
      game.nextWave(creatures);
    }
  }

  @Override
  public void setGame(GameModelInterface game) {
    this.game = game;
  }
}
