package org.mazerunner.model.creature.actions;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.mazerunner.model.baseactions.Action;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.maze.MazeModelInterface;

public class CommandAction extends Action {
  private Creature commander;
  private MazeModelInterface maze;
  private ObservableList<Creature> creatures;
  private List<CommandedAction> actions = new ArrayList<>();
  private ListChangeListener<Creature> listener =
      change -> {
        while (change.next()) {
          if (change.wasAdded() && !this.isFinished()) {
            for (Creature c : change.getAddedSubList()) {
              updateCommandedCreature(c);
            }
          }
        }
      };

  public CommandAction(Creature commander, MazeModelInterface maze) {
    this.commander = commander;
    this.maze = maze;
    this.creatures = maze.getCreatures();

    for (Creature c : this.creatures) {
      updateCommandedCreature(c);
    }
    this.creatures.addListener(listener);
    commander
        .lifesProperty()
        .lessThanOrEqualTo(0)
        .addListener(
            (obj, oldValue, newValue) -> {
              if (newValue.booleanValue()) this.onFinish();
            });
  }

  private void updateCommandedCreature(Creature c) {
    if (c.getType() == CreatureType.COMMANDER) return;
    CommandedAction commandedAction = new CommandedAction(c, maze);
    actions.add(commandedAction);
    c.setAction(commandedAction);
  }

  /** No-Op, because Commander doesn't need to act. */
  @Override
  protected void update(double dt) {}

  @Override
  public boolean isFinished() {
    return (commander.getLifes() <= 0
        || maze.getWallOn((int) commander.getX(), (int) commander.getY()).hasTower());
  }

  @Override
  protected void onFinish() {
    this.creatures.removeListener(listener);
    for (CommandedAction action : actions) {
      action.setFinished();
    }
    // update
    for (Creature c : creatures) {
      c.act(0);
    }
    if (commander.getLifes() > 0) {
      commander.chooseNewAction();
    }
  }
}
