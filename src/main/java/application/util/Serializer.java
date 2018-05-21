package application.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javafx.collections.ObservableList;

public class Serializer {
  public static ObjectMapper create() {
    ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);

//    SimpleModule module = new SimpleModule();
//    module.addDeserializer(ObservableList.class, new ObservableListDeserializer());
//    objectMapper.registerModule(module);
    //    objectMapper.
    return objectMapper;
  }
}
