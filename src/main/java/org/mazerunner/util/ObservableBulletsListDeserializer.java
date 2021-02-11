package org.mazerunner.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mazerunner.model.maze.tower.bullet.Bullet;

public class ObservableBulletsListDeserializer extends JsonDeserializer<ObservableList<Bullet>> {
  public ObservableList<Bullet> deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    ObjectMapper mapper = Serializer.create();
    JsonNode node = mapper.readTree(jp);
    ArrayList<Bullet> list = mapper.convertValue(node, new TypeReference<ArrayList<Bullet>>() {});
    return FXCollections.observableArrayList(list);
  }
}
