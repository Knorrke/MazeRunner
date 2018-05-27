package application.controller.gameloop;

/**
 * This interface is for model parts that should be updated by GameLoop. Typically the update method
 * calls {@link ActorInterface#act(double)} of registered {@link ActorInterface Actors}
 */
public interface Updateable {
  public void update(double dt);
}
