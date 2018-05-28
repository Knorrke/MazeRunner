package application.controller.gameloop;

/**
 * This interface is for individual objects that should act depending on game time. Actors aren't
 * called by GameLoop directly but instead should be registered in an {@link Updateable} object.
 */
public interface ActorInterface {
  public void act(double dt);
}
