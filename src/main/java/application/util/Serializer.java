package application.util;

import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.model.creature.movements.MovementInterface;
import application.model.creature.movements.NoSightMovement;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.NoTower;

public class Serializer {
  public static Gson create() {
    GsonBuilder builder = FxGson.coreBuilder();
    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(MovementInterface.class)
            .registerSubtype(NoSightMovement.class));
    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(AbstractTower.class).registerSubtype(NoTower.class));
    return builder.create();
  }
}
