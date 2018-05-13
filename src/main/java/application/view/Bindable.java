package application.view;

import application.model.ModelInterface;

public interface Bindable<T extends ModelInterface> {

  /**
   * Bind the application.view to a application.model
   *
   * @param application.model
   */
  public void bind(T model);
}
