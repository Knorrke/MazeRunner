package org.mazerunner.view;

import org.mazerunner.model.ModelInterface;

public interface Bindable<T extends ModelInterface> {

  /**
   * Bind the org.mazerunner.view to a org.mazerunner.model
   *
   * @param org.mazerunner.model
   */
  public void bind(T model);
}
