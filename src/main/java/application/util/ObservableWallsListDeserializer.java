package application.util;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import application.model.maze.Wall;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableWallsListDeserializer extends JsonDeserializer<ObservableList<Wall>> {
  public ObservableList<Wall> deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    ObjectMapper mapper = Serializer.create();
      JsonNode node = mapper.readTree(jp);
      ArrayList<Wall> list = mapper.convertValue(node, new TypeReference<ArrayList<Wall>>() {});
      return FXCollections.observableArrayList(list);
  }
}
