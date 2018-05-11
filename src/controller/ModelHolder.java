package controller;

import model.ModelInterface;

public interface ModelHolder<T extends ModelInterface> {

	public void initModel(T model);
}
