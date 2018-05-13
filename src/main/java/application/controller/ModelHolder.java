package application.controller;

import application.model.ModelInterface;

public interface ModelHolder<T extends ModelInterface> {

	public void initModel(T model);
}
