package application.controller;

import java.util.logging.Logger;
import application.model.level.LevelModelInterface;
import application.view.level.CreatureTimelineView;

public class LevelController implements ModelHolderInterface<LevelModelInterface> {
  private static Logger LOG = Logger.getLogger(LevelController.class.getName());
  private CreatureTimelineView view;

  @Override
  public void initModel(LevelModelInterface level) {
    LOG.finer("binding LevelModel");
    view.bind(level);
  }

  /** @param view the view to set */
  public void setView(CreatureTimelineView view) {
    this.view = view;
  }
}
