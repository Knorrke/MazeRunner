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

import application.model.creature.CreatureGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableCreatureGroupListDeserializer
    extends JsonDeserializer<ObservableList<CreatureGroup>> {
  public ObservableList<CreatureGroup> deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    ObjectMapper mapper = Serializer.create();
    JsonNode node = mapper.readTree(jp);
    ArrayList<CreatureGroup> list =
        mapper.convertValue(node, new TypeReference<ArrayList<CreatureGroup>>() {});
    return FXCollections.observableArrayList(list);
  }
}
