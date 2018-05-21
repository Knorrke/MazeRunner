package application.util;

import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.model.Game;
import application.model.GameModelInterface;
import application.model.creature.movements.MovementInterface;
import application.model.creature.movements.NoSightMovement;
import application.model.level.Level;
import application.model.level.LevelModelInterface;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.NoTower;
import application.model.player.Player;
import application.model.player.PlayerModelInterface;

public class Serializer {
  public static Gson create() {
    GsonBuilder builder = FxGson.coreBuilder();
    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(MovementInterface.class)
            .registerSubtype(NoSightMovement.class));
    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(AbstractTower.class).registerSubtype(NoTower.class));

    builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(PlayerModelInterface.class).registerSubtype(Player.class));
    builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(LevelModelInterface.class).registerSubtype(Level.class));
    builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(MazeModelInterface.class).registerSubtype(Maze.class));
    builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(GameModelInterface.class).registerSubtype(Game.class));
    
    new GraphAdapterBuilder().addType(LevelModelInterface.class).addType(GameModelInterface.class).registerOn(builder);
    return builder.create();
  }
}
