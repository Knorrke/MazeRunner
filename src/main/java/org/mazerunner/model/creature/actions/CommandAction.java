package org.mazerunner.model.creature.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.mazerunner.model.baseactions.Action;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.maze.MazeModelInterface;

public class CommandAction extends Action {
  private static final Logger LOG = Logger.getLogger(CommandAction.class.getName());

  private Creature commander;
  private MazeModelInterface maze;
  private ObservableList<Creature> creatures;
  private List<CommandedAction> actions = new ArrayList<>();
  private ListChangeListener<Creature> listener =
      new ListChangeListener<>() {

        @Override
        public void onChanged(Change<? extends Creature> change) {
          if (commander.getLifes() <= 0) {
            LOG.log(Level.INFO, "listener called although killed");
            creatures.removeListener(this);
          }
          while (change.next()) {
            if (change.wasAdded() && !isFinished()) {
              for (Creature c : change.getAddedSubList()) {
                updateCommandedCreature(c);
              }
            }
          }
        }
      };

  public CommandAction(Creature commander, MazeModelInterface maze) {
    this.commander = commander;
    this.maze = maze;
    commander
        .lifesProperty()
        .addListener(
            (obj, oldValue, newValue) -> {
              LOG.log(Level.INFO, "lives: {0},{1}", new Object[] {oldValue, newValue});
              if (newValue.intValue() <= 0) {
                LOG.log(Level.INFO, "Commander killed");
                onFinish();
              }
            });
  }

  private void updateCommandedCreature(Creature c) {
    if (c.getType() == CreatureType.COMMANDER) return;
    CommandedAction commandedAction = new CommandedAction(c, maze);
    actions.add(commandedAction);
    c.setAction(commandedAction);
  }

  @Override
  protected void update(double dt) {
    if (this.creatures == null) {
      // do this here, because otherwise the commander might not be in game yet.
      this.creatures = maze.getCreatures();

      for (Creature c : this.creatures) {
        updateCommandedCreature(c);
      }
      this.creatures.addListener(listener);
    }
  }

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
      if (c.getType() == CreatureType.COMMANDER) continue;
      c.act(0);
    }
    if (commander.getLifes() > 0) {
      commander.chooseNewAction();
    }
  }
}
