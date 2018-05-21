package application.util;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import application.model.creature.Creature;
import application.model.creature.CreatureGroup;
import application.model.maze.Wall;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableListDeserializer {
  private static class For<T> extends JsonDeserializer<ObservableList<T>> {
    public ObservableList<T> deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      ArrayList<T> list = (ArrayList<T>) ctxt.readValue(jp, ArrayList.class);
      return FXCollections.observableArrayList(list);
    }
  }

  public static class forWalls extends For<Wall> {}

  public static class forCreatures extends For<Creature> {}

  public static class forCreatureGroups extends For<CreatureGroup> {}
}
