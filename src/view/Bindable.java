package view;

import model.ModelInterface;

public interface Bindable<T extends ModelInterface> {

	/**
	 * Bind the view to a model
	 * @param model
	 */
	public void bind(T model);
}
