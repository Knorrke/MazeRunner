package org.mazerunner.controller;

import org.mazerunner.model.ModelInterface;

public interface ModelHolderInterface<T extends ModelInterface> {

  public void initModel(T model);
}
