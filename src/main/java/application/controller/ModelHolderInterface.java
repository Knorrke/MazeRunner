package application.controller;

import application.model.ModelInterface;

public interface ModelHolderInterface<T extends ModelInterface> {

	public void initModel(T model);
}
